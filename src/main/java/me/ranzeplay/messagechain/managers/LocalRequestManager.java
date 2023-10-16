package me.ranzeplay.messagechain.managers;

import lombok.SneakyThrows;
import me.ranzeplay.messagechain.MessageChain;
import me.ranzeplay.messagechain.models.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LocalRequestManager {
    private static LocalRequestManager INSTANCE;

    public Map<UUID, RouteRequestCache<?, ?>> requestMap;

    public LocalRequestManager() {
        requestMap = new HashMap<>();
        registerEvents();
        INSTANCE = this;
    }

    public static LocalRequestManager getInstance() {
        return INSTANCE;
    }

    @SneakyThrows
    public <TPayload extends AbstractNBTSerializable, TSuccess extends AbstractNBTSerializable> RouteResponse<TSuccess> sendRequest(Identifier route, TPayload payload, Class<TSuccess> successClass) {
        var id = UUID.randomUUID();
        var reqParam = new RouteRequest<>(route, payload);

        requestMap.put(id, new RouteRequestCache<>(id, reqParam, successClass));

        var packet = new CommPacket<>(id, reqParam);
        ClientPlayNetworking.send(MessageChain.COMM_IDENTIFIER, packet.toPacketByteBuf());

        // Wait for response
        var obj = requestMap.get(id);
        synchronized (obj) {
            obj.wait();
        }

        var response = requestMap.get(id);
        requestMap.remove(id);

        return (RouteResponse<TSuccess>) response.getResponseData();
    }

    private void registerEvents() {
        ClientPlayNetworking.registerGlobalReceiver(MessageChain.COMM_IDENTIFIER, this::handleNetworkingResponse);
    }

    @SneakyThrows
    private void handleNetworkingResponse(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        var commPacketId = buf.readUuid();
        var obj = requestMap.get(commPacketId);

        var nbt = buf.readNbt();
        var response = new RouteResponse(nbt, obj.getResponseClass());
        obj.setResponseData(response);

        synchronized (obj) {
            obj.notify();
        }
    }
}
