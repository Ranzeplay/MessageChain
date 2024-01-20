package me.ranzeplay.messagechain.form;

import jdk.jfr.Experimental;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;

@Experimental
@Getter
@AllArgsConstructor
public class SimpleForm {
    String title;
    String description;
    FormLayout layout;
    boolean showCancelButton;
    ArrayList<AbstractFormComponent> components;

    @Environment(EnvType.CLIENT)
    public void show() {
        var client = MinecraftClient.getInstance();
        client.send(() -> client.setScreen(new AdvancedFormScreen(this)));
    }
}
