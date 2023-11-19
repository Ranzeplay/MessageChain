package me.ranzeplay.messagechain.models.routing;

import lombok.*;
import me.ranzeplay.messagechain.nbtutils.AbstractNBTSerializable;
import net.minecraft.nbt.NbtCompound;
import org.apache.commons.lang3.NotImplementedException;

/**
 * Data structure to wrap data returned from the server.
 * @param <TSuccess> Actual data returning from the server, if it failed to process, it should be String containing a reason, and `success` set to `false`.
 */
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Setter
@Getter
public class RouteResponse<TSuccess extends AbstractNBTSerializable, TFail extends AbstractNBTSerializable> extends AbstractNBTSerializable {
    boolean success;
    TSuccess successResponse;
    RouteFailResponse<TFail> failResponse;

    /**
     * Create the object with a successful response.
     * @param successResponse Returning data
     */
    public RouteResponse(TSuccess successResponse) {
        this.success = true;
        this.successResponse = successResponse;
    }

    public static <S extends AbstractNBTSerializable, F extends AbstractNBTSerializable> RouteResponse<S, F> success(S data, Class<F> failClass) {
        return new RouteResponse<>(true, data, new RouteFailResponse<>());
    }

    @SneakyThrows
    public static <S extends AbstractNBTSerializable, F extends AbstractNBTSerializable> RouteResponse<S, F> fail(RouteFailResponse<F> data, Class<S> successClass) {
        return new RouteResponse<>(false, successClass.getConstructor().newInstance(), data);
    }

    @SneakyThrows
    public RouteResponse(NbtCompound nbt, Class<TSuccess> successClass, Class<TFail> failClass) {
        success = nbt.getBoolean("success");
        if(success) {
            var successData = successClass.getConstructor().newInstance();
            successData.fromNbt(nbt.getCompound("payload"));
            successResponse = successData;
        } else {
            var failData = failClass.getConstructor().newInstance();
            failData.fromNbt(nbt.getCompound("payload").getCompound("data"));

            var response = RouteFailResponse.class.getConstructor().newInstance();
            response.failClass = failClass;
            response.data = failData;
            failResponse = response;
        }
    }

    @Override
    public NbtCompound toNbt() {
        var nbt = new NbtCompound();
        nbt.putBoolean("success", success);
        if(success) {
            nbt.put("payload", successResponse.toNbt());
        } else {
            nbt.put("payload", failResponse.toNbt());
        }

        return nbt;
    }

    @Override
    public void fromNbt(NbtCompound nbt) {
        throw new NotImplementedException();
    }

    @Override
    public Class<RouteResponse> getGenericClass() {
        return RouteResponse.class;
    }
}
