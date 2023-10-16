package me.ranzeplay.messagechain.models;


import java.util.function.Function;

public abstract class AbstractRouteExecutor<TPayload extends AbstractNBTSerializable, TSuccess extends AbstractNBTSerializable> implements Function<TPayload, TSuccess> {
}
