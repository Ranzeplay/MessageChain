package me.ranzeplay.messagechain.init;

import me.ranzeplay.messagechain.notification.NotificationManager;
import me.ranzeplay.messagechain.routing.RemoteRouteManager;
import me.ranzeplay.messagechain.config.MessageChainDeveloperConfig;
import me.ranzeplay.messagechain.testing.ExampleNotificationTest;
import me.ranzeplay.messagechain.testing.ExampleRouteTest;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class MessageChainInitializer implements ModInitializer {
    public static Identifier COMM_IDENTIFIER = new Identifier("message_chain.networking", "comm");
    public static final MessageChainDeveloperConfig CONFIG = MessageChainDeveloperConfig.createAndLoad();

    @Override
    public void onInitialize() {
        new RemoteRouteManager();
        new NotificationManager();

        if (CONFIG.enableNotificationTest()) {
            ExampleNotificationTest.setupServerSide();
        }
        if (CONFIG.enableRoutingTest()) {
            ExampleRouteTest.configureServerSide();
        }
    }
}
