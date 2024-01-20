package me.ranzeplay.messagechain.form;

import io.wispforest.owo.ui.core.Component;
import jdk.jfr.Experimental;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.Widget;

import java.util.Collection;

@Getter
@Experimental
@AllArgsConstructor
public abstract class AbstractFormComponent {
    String name;
    String caption;
    String description;
    Object defaultValue;
    boolean isEnabled;

    public abstract Widget draw(int width, TextRenderer renderer);

    public abstract Collection<Component> getUIComponent();
}
