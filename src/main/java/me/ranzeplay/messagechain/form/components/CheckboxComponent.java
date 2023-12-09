package me.ranzeplay.messagechain.form.components;

import me.ranzeplay.messagechain.form.AbstractFormComponent;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.text.Text;

public class CheckboxComponent extends AbstractFormComponent {
    boolean isChecked;

    public CheckboxComponent(String name, String caption, String description) {
        super(name, caption, description, false, true);
        isChecked = false;
    }

    @Override
    public Widget draw(int width, TextRenderer renderer) {
        return new CheckboxWidget(0, 0, width, 20, Text.literal(this.getCaption()), isChecked);
    }
}
