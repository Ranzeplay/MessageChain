package me.ranzeplay.messagechain.testing;

import com.mojang.brigadier.Command;
import me.ranzeplay.messagechain.form.FormBuilder;
import me.ranzeplay.messagechain.form.FormLayout;
import me.ranzeplay.messagechain.form.components.CheckboxComponent;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

public class ExampleFormTest {
    public static void setupClientSide() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess)
                -> dispatcher.register(ClientCommandManager.literal("msgc_form_test")
                .then(ClientCommandManager.literal("show")
                        .executes(context -> {
                            showForm();
                            return Command.SINGLE_SUCCESS;
                        })
                )
        ));
    }

    private static void showForm() {
        new FormBuilder()
                .setTitle("Form test")
                .setDescription("MessageChain form test")
                .setLayout(FormLayout.PAGING)
                .setShowCancelButton(true)
                .appendElement(new CheckboxComponent("checkbox", "Checkbox", "Example checkbox"))
                .build()
                .show();
    }
}
