package me.ranzeplay.messagechain.models.routing;

import lombok.*;
import me.ranzeplay.messagechain.models.AbstractNBTSerializable;
import net.minecraft.nbt.NbtCompound;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RouteFailResponse<T extends AbstractNBTSerializable> extends AbstractNBTSerializable {
    FailType reason;
    T data;
    Class<T> failClass;

    @Override
    public NbtCompound toNbt() {
        var nbt = new NbtCompound();
        nbt.putString("reason", reason.name());
        nbt.put("data", data.toNbt());

        return nbt;
    }

    @SneakyThrows
    @Override
    public void fromNbt(NbtCompound nbt) {
        this.data = (T) failClass.getConstructor().newInstance().loadFromNbt(nbt.getCompound("data"));
        this.reason = FailType.valueOf(nbt.getString("reason"));

    }

    @Override
    public Class<RouteFailResponse> getGenericClass() {
        return RouteFailResponse.class;
    }

    public enum FailType {
        ROUTE_NOT_FOUND,
        FAILED_TO_PROCESS
    }
}
