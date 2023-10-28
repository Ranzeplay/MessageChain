package me.ranzeplay.messagechain.testing;

import com.mojang.brigadier.Command;
import me.ranzeplay.messagechain.models.notification.AbstractNotificationHandler;
import me.ranzeplay.messagechain.managers.notification.NotificationManager;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ExampleNotificationTest extends AbstractNotificationHandler<ExampleData> {
    private static final Identifier NOTIFICATION_IDENTIFIER = new Identifier("message_chain.networking.notification", "test");

    public static void configureServerSide() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, registrationEnvironment)
                -> dispatcher.register(CommandManager.literal("msgc_test_notification")
                .executes(context -> {
                    NotificationManager.getInstance().sendNotification(NOTIFICATION_IDENTIFIER, new ExampleData("Message pushed by notification"), context.getSource().getPlayer());
                    return Command.SINGLE_SUCCESS;
                })
        ));
    }

    public static void configureClientSide() {
        NotificationManager.getInstance().registerHandler(NOTIFICATION_IDENTIFIER, new ExampleNotificationTest());
    }

    public ExampleNotificationTest() {
        super(ExampleData.class);
    }

    @Override
    public void accept(ExampleData exampleData) {
        assert MinecraftClient.getInstance().player != null;
        MinecraftClient.getInstance()
                .player
                .sendMessage(Text.of(String.format("Testing notifications: %s", exampleData.getMessage())));
    }
}
