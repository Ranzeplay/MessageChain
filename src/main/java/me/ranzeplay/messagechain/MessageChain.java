package me.ranzeplay.messagechain;

import me.ranzeplay.messagechain.managers.RemoteRouteManager;
import me.ranzeplay.messagechain.testing.ExampleRouteTest;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class MessageChain implements ModInitializer {
    public static Identifier COMM_IDENTIFIER = new Identifier("message_chain.networking", "comm");

    @Override
    public void onInitialize() {
        new RemoteRouteManager();
        ExampleRouteTest.configureServerSide();
    }
}
