package me.ranzeplay.messagechain.form.components;

import me.ranzeplay.messagechain.form.AbstractFormComponent;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

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
}
