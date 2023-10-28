package me.ranzeplay.messagechain.managers.notification;

import lombok.SneakyThrows;
import me.ranzeplay.messagechain.models.AbstractNBTSerializable;
import me.ranzeplay.messagechain.models.notification.AbstractNotificationHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class NotificationManager {
    private static NotificationManager INSTANCE;

    public static NotificationManager getInstance() {
        return INSTANCE;
    }

    private static final Identifier NOTIFICATION_IDENTIFIER = new Identifier("message_chain.networking", "notification");

    private final Map<Identifier, AbstractNotificationHandler> notificationHandlers;

    public NotificationManager() {
        INSTANCE = this;
        notificationHandlers = new HashMap<>();
    }

    public void registerClientEvents() {
        ClientPlayNetworking.registerGlobalReceiver(NOTIFICATION_IDENTIFIER, this::handleClientNetworking);
    }

    @SneakyThrows
    private void handleClientNetworking(MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf buf, PacketSender responseSender) {
        var identifier = buf.readIdentifier();
        var handler = notificationHandlers.get(identifier);
        if (handler != null) {
            var payloadObject = ((AbstractNBTSerializable) handler.getPayloadClazz()
                    .getConstructor()
                    .newInstance())
                    .loadFromNbt(buf.readNbt());
            var payload = handler.getPayloadClazz().cast(payloadObject);

            handler.accept(payload);
        }
    }

    public <T extends AbstractNBTSerializable> void sendNotification(Identifier identifier, T payload, ServerPlayerEntity target) {
        ServerPlayNetworking.send(target, NOTIFICATION_IDENTIFIER, PacketByteBufs.create().writeIdentifier(identifier).writeNbt(payload.toNbt()));
    }

    public void registerHandler(Identifier identifier, AbstractNotificationHandler<? extends AbstractNBTSerializable> handler) {
        notificationHandlers.put(identifier, handler);
    }

    public void unregisterHandler(Identifier identifier) {
        notificationHandlers.remove(identifier);
    }
}
