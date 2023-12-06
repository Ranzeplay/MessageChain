package me.ranzeplay.messagechain.routing;

import lombok.*;
import me.ranzeplay.messagechain.nbtutils.AbstractNBTSerializable;
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
        ROUTE_TIME_OUT,
        FAILED_TO_PROCESS,
        INTERNAL_ERROR,
    }

    public static <TFail extends AbstractNBTSerializable> RouteFailResponse<TFail> notFound(Class<TFail> failClass) {
        return new RouteFailResponse<>(FailType.ROUTE_NOT_FOUND, null, failClass);
    }

    public static <TFail extends AbstractNBTSerializable> RouteFailResponse<TFail> timedOut(Class<TFail> failClass) {
        return new RouteFailResponse<>(FailType.ROUTE_TIME_OUT, null, failClass);
    }

    public static <TFail extends AbstractNBTSerializable> RouteFailResponse<TFail> internalError(Class<TFail> failClass) {
        return new RouteFailResponse<>(FailType.INTERNAL_ERROR, null, failClass);
    }

    public static <TFail extends AbstractNBTSerializable> RouteFailResponse<TFail> failedToProcess(Class<TFail> failClass) {
        return new RouteFailResponse<>(FailType.INTERNAL_ERROR, null, failClass);
    }
}
