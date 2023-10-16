package me.ranzeplay.messagechain.managers;

import lombok.SneakyThrows;
import me.ranzeplay.messagechain.MessageChain;
import me.ranzeplay.messagechain.models.CommPacket;
import me.ranzeplay.messagechain.models.RouteHandler;
import me.ranzeplay.messagechain.models.RouteResponse;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.HashMap;

public class RemoteRouteManager {
    private static RemoteRouteManager INSTANCE;

    HashMap<Identifier, RouteHandler<?, ?>> routeRegistry;

    public RemoteRouteManager() {
        INSTANCE = this;
        routeRegistry = new HashMap<>();
        registerEvents();
    }

    public static RemoteRouteManager getInstance() {
        return INSTANCE;
    }

    public void registerRoute(RouteHandler<?, ?> handler) {
        routeRegistry.put(handler.getRoute(), handler);
    }

    public void unregisterRoute(Identifier route) {
        routeRegistry.remove(route);
    }

    private void registerEvents() {
        ServerPlayNetworking.registerGlobalReceiver(MessageChain.COMM_IDENTIFIER, this::handleNetworking);
    }

    @SneakyThrows
    private void handleNetworking(MinecraftServer server, ServerPlayerEntity playerSender, ServerPlayNetworkHandler networkHandler, PacketByteBuf buf, PacketSender packetSender) {
        var packetId = buf.readUuid();
        var param = buf.readNbt();
        var routeIdRaw = param.getString("path").split("-");
        var routeId = new Identifier(routeIdRaw[0], routeIdRaw[1]);

        var route = routeRegistry.get(routeId);
        var payloadObjectSuper = route.getPayloadClazz()
                .newInstance()
                .loadFromNbt(param.getCompound("payload"));
        var payloadObject = route.getPayloadClazz()
                .cast(payloadObjectSuper);

        var executionResult = route.getSuccessClazz().cast(route.getAction().apply(payloadObject));

        var response = new RouteResponse<>(executionResult);
        var packet = new CommPacket<>(packetId, response);

        ServerPlayNetworking.send(playerSender, MessageChain.COMM_IDENTIFIER, packet.toPacketByteBuf());
    }
}
