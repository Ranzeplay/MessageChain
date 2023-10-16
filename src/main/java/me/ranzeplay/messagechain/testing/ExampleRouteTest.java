package me.ranzeplay.messagechain.testing;

import com.mojang.brigadier.Command;
import me.ranzeplay.messagechain.managers.LocalRequestManager;
import me.ranzeplay.messagechain.managers.RemoteRouteManager;
import me.ranzeplay.messagechain.models.AbstractRouteExecutor;
import me.ranzeplay.messagechain.models.RouteHandler;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.util.Identifier;

public class ExampleRouteTest extends AbstractRouteExecutor<ExampleData, ExampleData> {
    private static final Identifier ROUTE_IDENTIFIER = new Identifier("message_chain.networking.route", "test");

    public static void configureServerSide() {
        RemoteRouteManager.getInstance().registerRoute(new RouteHandler<>(ExampleData.class, ExampleData.class, ROUTE_IDENTIFIER, new ExampleRouteTest()));
    }

    public static void configureClientSide() {
        registerCommands();
    }

    public static void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("msg_chain_test")
                    .executes(context -> {
                        new Thread(() -> LocalRequestManager.getInstance().sendRequest(ROUTE_IDENTIFIER, new ExampleData("hello"), ExampleData.class)).start();
                        return Command.SINGLE_SUCCESS;
                    })
            );
        });
    }

    @Override
    public ExampleData apply(ExampleData exampleData) {
        exampleData.setMessage(String.format("You are right, but %s", exampleData.getMessage()));
        return exampleData;
    }
}
