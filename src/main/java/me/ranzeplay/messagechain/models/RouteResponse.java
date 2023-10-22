package me.ranzeplay.messagechain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import net.minecraft.nbt.NbtCompound;
import org.apache.commons.lang3.NotImplementedException;

import java.lang.reflect.ParameterizedType;

/**
 * Data structure to wrap data returned from the server.
 * @param <TSuccess> Actual data returning from the server, if it failed to process, it should be String containing a reason, and `success` set to `false`.
 */
@NoArgsConstructor
@Setter
@Getter
public class RouteResponse<TSuccess extends AbstractNBTSerializable> extends AbstractNBTSerializable {
    boolean success;
    TSuccess successResponse;
    String failedReason;

    /**
     * Create the object with a successful response.
     * @param successResponse Returning data
     */
    public RouteResponse(TSuccess successResponse) {
        this.success = true;
        this.successResponse = successResponse;
    }

    /**
     * Create the object with a failed response
     * @param failedReason The reason of failing to process data.
     */
    public RouteResponse(String failedReason) {
        this.success = false;
        this.failedReason = failedReason;
    }

    @SneakyThrows
    public RouteResponse(NbtCompound nbt, Class<TSuccess> successClass) {
        success = nbt.getBoolean("success");
        if(success) {
            var successData = successClass.newInstance();
            successData.fromNbt(nbt.getCompound("payload"));
            successResponse = successData;
        } else {
            failedReason = nbt.getString("reason");
        }
    }

    @Override
    public NbtCompound toNbt() {
        var nbt = new NbtCompound();
        nbt.putBoolean("success", success);
        if(success) {
            nbt.put("payload", successResponse.toNbt());
        } else {
            nbt.putString("reason", failedReason);
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
