package me.ranzeplay.messagechain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.util.Identifier;

@AllArgsConstructor
@Getter
public class RouteHandler<TPayload extends AbstractNBTSerializable, TSuccess extends AbstractNBTSerializable> {
    Class<TPayload> payloadClazz;
    Class<TSuccess> successClazz;
    Identifier route;
    AbstractRouteExecutor action;
}
