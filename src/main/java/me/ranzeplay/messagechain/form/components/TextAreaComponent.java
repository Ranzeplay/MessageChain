package me.ranzeplay.messagechain.form.components;

import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.core.Component;
import io.wispforest.owo.ui.core.Insets;
import io.wispforest.owo.ui.core.Sizing;
import jdk.jfr.Experimental;
import me.ranzeplay.messagechain.form.AbstractFormComponent;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.EditBoxWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collection;

@Experimental
public class TextAreaComponent extends AbstractFormComponent {
    Text placeholder;
    Text text;

    public TextAreaComponent(String name, String caption, String description, Text placeholder, Text text) {
        super(name, caption, description, text, true);
        this.placeholder = placeholder;
        this.text = text;
    }

    @Override
    public Widget draw(int width, TextRenderer renderer) {
        var widget = new EditBoxWidget(renderer, 0, 0, width, 20 * 8, placeholder, text);
        return widget;
    }

    @Override
    public Collection<Component> getUIComponent() {
        var list = new ArrayList<Component>();
        list.add(Components.label(Text.of(this.getCaption())).margins(Insets.bottom(3)));
        list.add(
                Components.textArea(Sizing.fill(80), Sizing.fixed(20 * 8), text.getString())
                        .margins(Insets.bottom(6))
        );

        return list;
    }
}
