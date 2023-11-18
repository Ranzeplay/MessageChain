package me.ranzeplay.messagechain.models.routing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import me.ranzeplay.messagechain.models.AbstractNBTSerializable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.lang.reflect.ParameterizedType;

/**
 * Wrap the request with RouteRequest
 * @param <TPayload> Type of payload that sends to server, should match the payload type that registered on server-side.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RouteRequest<TPayload extends AbstractNBTSerializable> extends AbstractNBTSerializable {
    Identifier route;
    TPayload payload;

    @Override
    public NbtCompound toNbt() {
        var nbt = new NbtCompound();
        nbt.putString("path", route.getNamespace() + "-" + route.getPath());
        nbt.put("payload", payload.toNbt());
        return nbt;
    }

    @SneakyThrows
    @Override
    public void fromNbt(NbtCompound nbt) {
        // Decode path
        var path = nbt.getString("path").split("-");
        this.route = new Identifier(path[0], path[1]);

        var superClass = (ParameterizedType) getClass().getGenericSuperclass();
        var clazz = (Class<TPayload>) superClass.getActualTypeArguments()[0];
        var payload = clazz.getConstructor().newInstance();
        payload.fromNbt(nbt.getCompound("payload"));
        this.payload = payload;
    }

    @Override
    public Class<RouteRequest> getGenericClass() {
        return RouteRequest.class;
    }
}
