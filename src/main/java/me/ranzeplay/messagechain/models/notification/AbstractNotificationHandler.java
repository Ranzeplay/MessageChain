package me.ranzeplay.messagechain.models.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.ranzeplay.messagechain.models.AbstractNBTSerializable;

import java.util.function.Consumer;

@Getter
@AllArgsConstructor
public abstract class AbstractNotificationHandler<T extends AbstractNBTSerializable> implements Consumer<T> {
    Class<T> payloadClazz;
}
