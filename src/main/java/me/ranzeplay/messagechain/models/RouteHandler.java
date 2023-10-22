package me.ranzeplay.messagechain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.util.Identifier;

/**
 * The handler of a route.
 * @param <TPayload> The class of data that sends to server to process.
 * @param <TSuccess> Data returns when succeeded in processing data.
 */
@AllArgsConstructor
@Getter
public class RouteHandler<TPayload extends AbstractNBTSerializable, TSuccess extends AbstractNBTSerializable> {
    Class<TPayload> payloadClazz;
    Class<TSuccess> successClazz;
    /**
     * The route of the handler. Just like a URL.
     */
    Identifier route;
    /**
     * Actions when receiving data from a connected client.
     */
    AbstractRouteExecutor action;
}
