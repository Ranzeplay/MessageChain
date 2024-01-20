package me.ranzeplay.messagechain.form.components;

import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.core.Component;
import io.wispforest.owo.ui.core.Insets;
import io.wispforest.owo.ui.core.Sizing;
import jdk.jfr.Experimental;
import me.ranzeplay.messagechain.form.AbstractFormComponent;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.Collection;

@Experimental
public class TextBoxComponent extends AbstractFormComponent {
    String text;

    public TextBoxComponent(String name, String caption, String description, String text) {
        super(name, caption, description, text, true);
        this.text = text;
    }

    @Override
    public Widget draw(int width, TextRenderer renderer) {
        var widget = new TextFieldWidget(renderer, 0, 0, width, 20, Text.literal(text).formatted(Formatting.GRAY));
        return widget;
    }

    @Override
    public Collection<Component> getUIComponent() {
        var list = new ArrayList<Component>();
        list.add(Components.label(Text.of(this.getCaption())).margins(Insets.bottom(3)));
        list.add(
                Components.textBox(Sizing.fill(80), text)
                        .margins(Insets.bottom(6))
        );

        return list;
    }
}
