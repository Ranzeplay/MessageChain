package me.ranzeplay.messagechain.form.components;

import lombok.Getter;
import lombok.Setter;
import me.ranzeplay.messagechain.form.AbstractFormComponent;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.text.Text;

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
}
