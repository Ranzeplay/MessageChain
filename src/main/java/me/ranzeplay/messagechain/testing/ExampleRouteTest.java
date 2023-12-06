package me.ranzeplay.messagechain.testing;

import com.mojang.brigadier.Command;
import me.ranzeplay.messagechain.routing.LocalRequestManager;
import me.ranzeplay.messagechain.routing.RemoteRouteManager;
import me.ranzeplay.messagechain.routing.*;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class ExampleRouteTest extends AbstractRouteExecutor<ExampleData, ExampleData, ExampleData> {
    private static final Identifier ROUTE_IDENTIFIER = new Identifier("message_chain.networking.route", "test");

    public static void configureServerSide() {
        RemoteRouteManager.getInstance().registerRoute(new RouteHandler<>(ExampleData.class, ExampleData.class, ExampleData.class, ROUTE_IDENTIFIER, new ExampleRouteTest(), false));
    }

    public static void setupClientSide() {
        registerCommands();
    }

    public static void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess)
                -> dispatcher.register(ClientCommandManager.literal("msgc_test_routing")
                .executes(context -> {
                    LocalRequestManager.getInstance().sendThreadedRequest(ROUTE_IDENTIFIER, new ExampleData("expected to fail", true), ExampleData.class, ExampleData.class,
                            response -> Objects.requireNonNull(MinecraftClient.getInstance().player)
                                    .sendMessage(Text.literal(response.getSuccessResponse().getMessage()))
                    );
                    LocalRequestManager.getInstance().sendThreadedRequest(ROUTE_IDENTIFIER, new ExampleData("expected to success", false), ExampleData.class, ExampleData.class,
                            response -> Objects.requireNonNull(MinecraftClient.getInstance().player)
                                    .sendMessage(Text.literal(response.getFailResponse().getData().getMessage()))
                    );
                    return Command.SINGLE_SUCCESS;
                })
        ));
    }

    @Override
    public RouteResponse<ExampleData, ExampleData> apply(RouteRequestContext<ExampleData> context) {
        var exampleData = context.getPayload();
        if(exampleData.isIssueError()) {
            exampleData.setMessage(String.format("Testing failing routing: %s", exampleData.getMessage()));
            return RouteResponse.fail(new RouteFailResponse<>(RouteFailResponse.FailType.FAILED_TO_PROCESS, exampleData, ExampleData.class), ExampleData.class);
        } else {
            exampleData.setMessage(String.format("Testing success routing: %s", exampleData.getMessage()));
            return RouteResponse.success(exampleData, ExampleData.class);
        }
    }
}
