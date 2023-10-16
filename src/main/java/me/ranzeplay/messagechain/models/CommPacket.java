package me.ranzeplay.messagechain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;

import java.lang.reflect.ParameterizedType;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class CommPacket<T extends AbstractNBTSerializable> {
    UUID id;
    T payload;

    public PacketByteBuf toPacketByteBuf() {
        return PacketByteBufs.create()
                .writeUuid(id)
                .writeNbt(payload.toNbt());
    }

    @SneakyThrows
    public CommPacket(PacketByteBuf buf) {
        this.id = buf.readUuid();

        var superClass = (ParameterizedType) getClass().getGenericSuperclass();
        var clazz = (Class<T>) superClass.getActualTypeArguments()[0];
        var payload = clazz.newInstance();
        payload.fromNbt(buf.readNbt());
        this.payload = payload;
    }
}
