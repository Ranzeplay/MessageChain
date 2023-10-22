package me.ranzeplay.messagechain.models.routing;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import me.ranzeplay.messagechain.models.AbstractNBTSerializable;

import java.util.UUID;

@Getter
public class RouteRequestCache<TPayload extends AbstractNBTSerializable, TSuccess extends AbstractNBTSerializable> {
    Class<TPayload> payloadClass;
    Class<TSuccess> responseClass;

    UUID id;
    RouteRequest<TPayload> requestData;
    @Setter
    RouteResponse<TSuccess> responseData;

    @SneakyThrows
    public RouteRequestCache(UUID id, RouteRequest<TPayload> requestData, Class<TSuccess> responseClass) {
        this.id = id;

        payloadClass = (Class<TPayload>) requestData.getPayload().getClass();
        this.responseClass = responseClass;

        this.requestData = requestData;
    }
}
