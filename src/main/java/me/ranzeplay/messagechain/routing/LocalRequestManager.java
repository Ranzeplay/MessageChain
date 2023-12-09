package me.ranzeplay.messagechain.routing;

import lombok.SneakyThrows;
import me.ranzeplay.messagechain.init.MessageChainInitializer;
import me.ranzeplay.messagechain.nbtutils.AbstractNBTSerializable;
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
import java.util.concurrent.Future;
import java.util.function.Consumer;

public class LocalRequestManager {
    private static LocalRequestManager INSTANCE;

    private final Map<UUID, RouteRequestCache<?, ?, ?>> requestMap;

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
     *
     * @param route        Target route to server
     * @param payload      Payload that sends to server
     * @param successClass Returning data type when successfully processed data
     * @param <TPayload>   Payload type
     * @param <TSuccess>   Success type
     * @return Response data
     */
    @SneakyThrows
    public <TPayload extends AbstractNBTSerializable, TSuccess extends AbstractNBTSerializable, TFail extends AbstractNBTSerializable>
    RouteResponse<TSuccess, TFail> sendRequest(Identifier route, TPayload payload, Class<TSuccess> successClass, Class<TFail> failClass) {
        var id = UUID.randomUUID();
        var reqParam = new RouteRequest<>(route, payload);

        requestMap.put(id, new RouteRequestCache<>(id, reqParam, successClass, failClass));

        var packet = new RoutingCommPacket<>(id, reqParam);
        ClientPlayNetworking.send(MessageChainInitializer.COMM_IDENTIFIER, packet.toPacketByteBuf());

        // Wait for response
        var obj = requestMap.get(id);
        synchronized (obj) {
            obj.wait(MessageChainInitializer.CONFIG.timeoutMilliseconds());
        }

        var response = requestMap.get(id);
        requestMap.remove(id);

        var responseData = (RouteResponse<TSuccess, TFail>) response.getResponseData();
        return Objects.requireNonNullElseGet(responseData, () -> RouteResponse.fail(RouteFailResponse.timedOut(failClass), successClass));
    }

    /**
     * Send a request to the server with custom data provided using a new thread.
     *
     * @param route        Target route to server.
     * @param payload      Payload that sends to server.
     * @param successClass Returning data type when successfully processed data.
     * @param <TPayload>   Payload type.
     * @param <TSuccess>   Success type.
     * @param callback     Actions when receiving response.
     */
    public <TPayload extends AbstractNBTSerializable, TSuccess extends AbstractNBTSerializable, TFail extends AbstractNBTSerializable>
    void sendThreadedRequest(Identifier route, TPayload payload, Class<TSuccess> successClass, Class<TFail> failClass, Consumer<RouteResponse<TSuccess, TFail>> callback) {
        new Thread(() -> {
            var response = sendRequest(route, payload, successClass, failClass);
            callback.accept(response);
        }).start();
    }

    private void registerEvents() {
        ClientPlayNetworking.registerGlobalReceiver(MessageChainInitializer.COMM_IDENTIFIER, this::handleNetworkingResponse);
    }

    @SneakyThrows
    private void handleNetworkingResponse(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        var commPacketId = buf.readUuid();
        var obj = requestMap.get(commPacketId);

        var nbt = Objects.requireNonNull(buf.readNbt());
        var response = new RouteResponse(nbt, obj.getSuccessClass(), obj.getFailClass());
        obj.setResponseData(response);

        synchronized (obj) {
            obj.notify();
        }
    }
}
