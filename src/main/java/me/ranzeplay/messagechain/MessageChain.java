package me.ranzeplay.messagechain;

import me.ranzeplay.messagechain.managers.notification.NotificationManager;
import me.ranzeplay.messagechain.managers.routing.RemoteRouteManager;
import me.ranzeplay.messagechain.managers.subscription.DataSubscriptionManager;
import me.ranzeplay.messagechain.testing.ExampleNotificationTest;
import me.ranzeplay.messagechain.testing.ExampleRouteTest;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class MessageChain implements ModInitializer {
    public static Identifier COMM_IDENTIFIER = new Identifier("message_chain.networking", "comm");
    public static Identifier DATA_SUBSCRIPTION_IDENTIFIER = new Identifier("message_chain.networking", "data_subscription");

    @Override
    public void onInitialize() {
        new RemoteRouteManager();
        new NotificationManager();
        new DataSubscriptionManager();

        ExampleRouteTest.configureServerSide();
        ExampleNotificationTest.configureServerSide();
    }
}
