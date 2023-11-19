package me.ranzeplay.messagechain.models.routing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.ranzeplay.messagechain.nbtutils.AbstractNBTSerializable;
import net.minecraft.util.Identifier;

/**
 * The handler of a route.
 * @param <TPayload> The class of data that sends to server to process.
 * @param <TSuccess> Data returns when succeeded in processing data.
 */
@AllArgsConstructor
@Getter
public class RouteHandler<TPayload extends AbstractNBTSerializable, TSuccess extends AbstractNBTSerializable, TFail extends AbstractNBTSerializable> {
    Class<TPayload> payloadClazz;
    Class<TSuccess> successClazz;
    Class<TFail> failClazz;

    /**
     * The route of the handler. Just like a URL.
     */
    Identifier route;

    /**
     * Actions when receiving data from a connected client.
     */
    AbstractRouteExecutor action;

    /**
     * Create another thread to handle the route
     */
    boolean threadedExecution;
}
