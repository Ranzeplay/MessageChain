package me.ranzeplay.messagechain.managers.routing;

import lombok.SneakyThrows;
import me.ranzeplay.messagechain.MessageChain;
import me.ranzeplay.messagechain.models.*;
import me.ranzeplay.messagechain.models.routing.RouteRequest;
import me.ranzeplay.messagechain.models.routing.RouteRequestCache;
import me.ranzeplay.messagechain.models.routing.RouteResponse;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public class LocalRequestManager {
    private static LocalRequestManager INSTANCE;

    private final Map<UUID, RouteRequestCache<?, ?>> requestMap;

    public LocalRequestManager() {
        requestMap = new HashMap<>();
        registerEvents();
        INSTANCE = this;
    }

    public static LocalRequestManager getInstance() {
        return INSTANCE;
    }

    /**
     * Send a request to the server with custom data provided
     * @param route Target route to server
     * @param payload Payload that sends to server
     * @param successClass Returning data type when successfully processed data
     * @param <TPayload> Payload type
     * @param <TSuccess> Success type
     * @return Response data
     */
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

    /**
     * Send a request to the server with custom data provided using a new thread.
     * @param route Target route to server.
     * @param payload Payload that sends to server.
     * @param successClass Returning data type when successfully processed data.
     * @param <TPayload> Payload type.
     * @param <TSuccess> Success type.
     * @param callback Actions when receiving response.
     */
    public <TPayload extends AbstractNBTSerializable, TSuccess extends AbstractNBTSerializable> void sendThreadedRequest(Identifier route, TPayload payload, Class<TSuccess> successClass, Consumer<RouteResponse<TSuccess>> callback) {
        new Thread(() -> {
            var response = sendRequest(route, payload, successClass);
            callback.accept(response);
        }).start();
    }

    private void registerEvents() {
        ClientPlayNetworking.registerGlobalReceiver(MessageChain.COMM_IDENTIFIER, this::handleNetworkingResponse);
    }

    @SneakyThrows
    private void handleNetworkingResponse(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        var commPacketId = buf.readUuid();
        var obj = requestMap.get(commPacketId);

        var nbt = Objects.requireNonNull(buf.readNbt());
        var response = new RouteResponse(nbt, obj.getResponseClass());
        obj.setResponseData(response);

        synchronized (obj) {
            obj.notify();
        }
    }
}
