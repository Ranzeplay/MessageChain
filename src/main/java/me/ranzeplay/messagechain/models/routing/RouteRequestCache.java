package me.ranzeplay.messagechain.models.routing;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import me.ranzeplay.messagechain.nbtutils.AbstractNBTSerializable;

import java.util.UUID;

@Getter
public class RouteRequestCache<TPayload extends AbstractNBTSerializable, TSuccess extends AbstractNBTSerializable, TFail extends AbstractNBTSerializable> {
    Class<TPayload> payloadClass;
    Class<TSuccess> successClass;
    Class<TFail> failClass;

    UUID id;
    RouteRequest<TPayload> requestData;
    @Setter
    RouteResponse<TSuccess, TFail> responseData;

    @SneakyThrows
    public RouteRequestCache(UUID id, RouteRequest<TPayload> requestData, Class<TSuccess> responseClass, Class<TFail> failClass) {
        this.id = id;

        payloadClass = (Class<TPayload>) requestData.getPayload().getClass();
        this.successClass = responseClass;
        this.failClass = failClass;

        this.requestData = requestData;
    }
}
