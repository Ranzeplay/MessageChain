package me.ranzeplay.messagechain.models.routing;


import me.ranzeplay.messagechain.nbtutils.AbstractNBTSerializable;

import java.util.function.Function;

/**
 *
 * @param <TPayload> Payload type that matches the type of `RouteHandler`.
 * @param <TSuccess> Returning data type when succeeded in processing data, should match the type of `RouteHandler`.
 */
public abstract class AbstractRouteExecutor<TPayload extends AbstractNBTSerializable, TSuccess extends AbstractNBTSerializable, TFail extends AbstractNBTSerializable> implements Function<RouteRequestContext<TPayload>, RouteResponse<TSuccess, TFail>> {
}
