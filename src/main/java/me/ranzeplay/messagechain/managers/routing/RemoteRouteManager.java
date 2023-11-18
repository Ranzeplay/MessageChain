package me.ranzeplay.messagechain.managers.routing;

import me.ranzeplay.messagechain.MessageChain;
import me.ranzeplay.messagechain.models.AbstractNBTSerializable;
import me.ranzeplay.messagechain.models.routing.*;
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

    HashMap<Identifier, RouteHandler<?, ?, ?>> routeRegistry;

    public RemoteRouteManager() {
        INSTANCE = this;
        routeRegistry = new HashMap<>();
        registerEvents();
    }

    public static RemoteRouteManager getInstance() {
        return INSTANCE;
    }

    /**
     * Register a new route handler
     *
     * @param handler The route handler to be registered.
     */
    public void registerRoute(RouteHandler<?, ?, ?> handler) {
        routeRegistry.put(handler.getRoute(), handler);
    }

    /**
     * Unregister an existing route handler
     */
    public void unregisterRoute(Identifier route) {
        routeRegistry.remove(route);
    }

    private void registerEvents() {
        ServerPlayNetworking.registerGlobalReceiver(MessageChain.COMM_IDENTIFIER, this::handleNetworking);
    }

    // @SneakyThrows
    private void handleNetworking(MinecraftServer server, ServerPlayerEntity playerSender, ServerPlayNetworkHandler networkHandler, PacketByteBuf buf, PacketSender packetSender) {
        var packetId = buf.readUuid();
        var param = buf.readNbt();
        assert param != null;
        var routeIdRaw = param.getString("path").split("-");
        var routeId = new Identifier(routeIdRaw[0], routeIdRaw[1]);

        var route = routeRegistry.get(routeId);

        RouteResponse response;
        if (route == null) {
            response = RouteResponse.fail(new RouteFailResponse<>(RouteFailResponse.FailType.ROUTE_NOT_FOUND, null, AbstractNBTSerializable.class), AbstractNBTSerializable.class);
        } else {
            AbstractNBTSerializable payloadObjectSuper;
            try {
                payloadObjectSuper = route.getPayloadClazz()
                        .getConstructor()
                        .newInstance()
                        .loadFromNbt(param.getCompound("payload"));
                var payloadObject = route.getPayloadClazz()
                        .cast(payloadObjectSuper);

                var context = new RouteRequestContext<>(packetId, routeId, payloadObject, server, playerSender, networkHandler, packetSender);
                response = (RouteResponse) route.getAction().apply(context);
            } catch (Exception e) {
                response = RouteResponse.fail(new RouteFailResponse<>(RouteFailResponse.FailType.INTERNAL_ERROR, null, AbstractNBTSerializable.class), AbstractNBTSerializable.class);
            }
        }

        var packet = new RoutingCommPacket<>(packetId, response);
        ServerPlayNetworking.send(playerSender, MessageChain.COMM_IDENTIFIER, packet.toPacketByteBuf());
    }
}
