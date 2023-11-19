package me.ranzeplay.messagechain.models.routing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import me.ranzeplay.messagechain.nbtutils.AbstractNBTSerializable;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;

import java.lang.reflect.ParameterizedType;
import java.util.UUID;

/**
 * MessageChain sends packets to server or client using CommPacket, it then serialized to PacketByteBuf to send to server and then deserialized to original.
 * @param <T> Data type loaded to packet
 */
@AllArgsConstructor
@Getter
public class RoutingCommPacket<T extends AbstractNBTSerializable> {
    /**
     * Packet ID, used to uniquely identify packets, which is used to trace response from the server.
     */
    UUID id;
    T payload;

    public PacketByteBuf toPacketByteBuf() {
        return PacketByteBufs.create()
                .writeUuid(id)
                .writeNbt(payload.toNbt());
    }

    @SneakyThrows
    public RoutingCommPacket(PacketByteBuf buf) {
        this.id = buf.readUuid();

        var superClass = (ParameterizedType) getClass().getGenericSuperclass();
        var clazz = (Class<T>) superClass.getActualTypeArguments()[0];
        var payload = clazz.getConstructor().newInstance();
        payload.fromNbt(buf.readNbt());
        this.payload = payload;
    }
}
