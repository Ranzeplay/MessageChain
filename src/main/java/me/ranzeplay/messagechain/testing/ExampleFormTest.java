package me.ranzeplay.messagechain.testing;

import com.mojang.brigadier.Command;
import me.ranzeplay.messagechain.form.FormBuilder;
import me.ranzeplay.messagechain.form.FormLayout;
import me.ranzeplay.messagechain.form.components.CheckboxComponent;
import me.ranzeplay.messagechain.form.components.TextAreaComponent;
import me.ranzeplay.messagechain.form.components.TextBoxComponent;
import me.ranzeplay.messagechain.form.components.TextComponent;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

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
                .appendElement(new TextComponent("label", "label", "Example label", Text.empty().append("This is an example form, just use it as an example").formatted(Formatting.YELLOW)))
                .appendElement(new TextBoxComponent("text", "TextBox", "Example text box", "Initial text"))
                .appendElement(new TextAreaComponent("textarea", "TextArea", "Example text area", Text.literal("Placeholder"), Text.empty()))
                .build()
                .show();
    }
}
