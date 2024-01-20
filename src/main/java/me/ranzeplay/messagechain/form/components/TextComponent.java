package me.ranzeplay.messagechain.form.components;

import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.core.Component;
import io.wispforest.owo.ui.core.Insets;
import jdk.jfr.Experimental;
import lombok.Getter;
import lombok.Setter;
import me.ranzeplay.messagechain.form.AbstractFormComponent;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.Collections;

@Experimental
@Getter
@Setter
public class TextComponent extends AbstractFormComponent {
    Text text;

    public TextComponent(String name, String caption, String description, Text text) {
        super(name, caption, description, null, true);
        this.text = text;
    }

    @Override
    public Widget draw(int width, TextRenderer renderer) {
        return new TextWidget(width, 20, text, renderer);
    }

    @Override
    public Collection<Component> getUIComponent() {
        return Collections.singleton(
                Components.label(text)
                .margins(Insets.bottom(6))
        );
    }
}
