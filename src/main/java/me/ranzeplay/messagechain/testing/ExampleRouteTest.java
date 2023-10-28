package me.ranzeplay.messagechain.testing;

import com.mojang.brigadier.Command;
import me.ranzeplay.messagechain.managers.routing.LocalRequestManager;
import me.ranzeplay.messagechain.managers.routing.RemoteRouteManager;
import me.ranzeplay.messagechain.models.routing.AbstractRouteExecutor;
import me.ranzeplay.messagechain.models.routing.RouteHandler;
import me.ranzeplay.messagechain.models.routing.RouteRequestContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class ExampleRouteTest extends AbstractRouteExecutor<ExampleData, ExampleData> {
    private static final Identifier ROUTE_IDENTIFIER = new Identifier("message_chain.networking.route", "test");

    public static void configureServerSide() {
        RemoteRouteManager.getInstance().registerRoute(new RouteHandler<>(ExampleData.class, ExampleData.class, ROUTE_IDENTIFIER, new ExampleRouteTest()));
    }

    public static void configureClientSide() {
        registerCommands();
    }

    public static void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess)
                -> dispatcher.register(ClientCommandManager.literal("msgc_test_routing")
                .executes(context -> {
                    LocalRequestManager.getInstance().sendThreadedRequest(ROUTE_IDENTIFIER, new ExampleData("hello"), ExampleData.class,
                            response -> Objects.requireNonNull(MinecraftClient.getInstance().player)
                                    .sendMessage(Text.literal(response.getSuccessResponse().getMessage()))
                    );
                    return Command.SINGLE_SUCCESS;
                })
        ));
    }

    @Override
    public ExampleData apply(RouteRequestContext<ExampleData> context) {
        var exampleData = context.getPayload();
        exampleData.setMessage(String.format("Testing routing: %s", exampleData.getMessage()));
        return exampleData;
    }
}
