package me.ranzeplay.messagechain.client;

import me.ranzeplay.messagechain.managers.routing.LocalRequestManager;
import me.ranzeplay.messagechain.testing.ExampleRouteTest;
import net.fabricmc.api.ClientModInitializer;

public class MessageChainClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        new LocalRequestManager();
        ExampleRouteTest.configureClientSide();
    }
}
