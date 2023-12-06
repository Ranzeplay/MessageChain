package me.ranzeplay.messagechain.routing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.ranzeplay.messagechain.nbtutils.AbstractNBTSerializable;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class RouteRequestContext<TPayload extends AbstractNBTSerializable> {
    UUID id;
    Identifier route;
    TPayload payload;

    MinecraftServer server;
    ServerPlayerEntity playerSender;
    ServerPlayNetworkHandler networkHandler;
    PacketSender packetSender;
}
