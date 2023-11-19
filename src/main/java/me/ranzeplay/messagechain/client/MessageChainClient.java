package me.ranzeplay.messagechain.client;

import me.ranzeplay.messagechain.MessageChain;
import me.ranzeplay.messagechain.managers.notification.NotificationManager;
import me.ranzeplay.messagechain.managers.routing.LocalRequestManager;
import me.ranzeplay.messagechain.testing.ExampleNotificationTest;
import me.ranzeplay.messagechain.testing.ExampleRouteTest;
import net.fabricmc.api.ClientModInitializer;

public class MessageChainClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        new LocalRequestManager();
        new NotificationManager().registerClientEvents();

        if (MessageChain.CONFIG.enableNotificationTest()) {
            ExampleNotificationTest.setupClientSide();
        }
        if (MessageChain.CONFIG.enableRoutingTest()) {
            ExampleRouteTest.setupClientSide();
        }
    }
}
