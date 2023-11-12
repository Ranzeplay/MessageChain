package me.ranzeplay.messagechain.models.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.ranzeplay.messagechain.models.AbstractNBTSerializable;

@AllArgsConstructor
@Getter
public class NotificationHandlerWrapper<T extends AbstractNBTSerializable> {
    AbstractNotificationHandler<T> handler;
    boolean subscribeOnce;
}
