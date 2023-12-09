package me.ranzeplay.messagechain.form.components;

import me.ranzeplay.messagechain.form.AbstractFormComponent;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.EditBoxWidget;
import net.minecraft.client.gui.widget.MultilineTextWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.text.Text;

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
}
