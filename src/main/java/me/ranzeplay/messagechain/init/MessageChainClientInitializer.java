package me.ranzeplay.messagechain.init;

import me.ranzeplay.messagechain.notification.NotificationManager;
import me.ranzeplay.messagechain.routing.LocalRequestManager;
import me.ranzeplay.messagechain.testing.ExampleNotificationTest;
import me.ranzeplay.messagechain.testing.ExampleRouteTest;
import me.ranzeplay.messagechain.testing.ExampleFormTest;
import net.fabricmc.api.ClientModInitializer;

public class MessageChainClientInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        new LocalRequestManager();
        new NotificationManager().registerClientEvents();

        if (MessageChainInitializer.CONFIG.enableNotificationTest()) {
            ExampleNotificationTest.setupClientSide();
        }
        if (MessageChainInitializer.CONFIG.enableRoutingTest()) {
            ExampleRouteTest.setupClientSide();
        }
        if (MessageChainInitializer.CONFIG.enableFormTest()) {
            ExampleFormTest.setupClientSide();
        }
    }
}
