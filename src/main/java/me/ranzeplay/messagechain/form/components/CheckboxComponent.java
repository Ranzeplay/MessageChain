package me.ranzeplay.messagechain.form.components;

import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.core.Component;
import io.wispforest.owo.ui.core.Insets;
import jdk.jfr.Experimental;
import me.ranzeplay.messagechain.form.AbstractFormComponent;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.Collections;

@Experimental
public class CheckboxComponent extends AbstractFormComponent {
    boolean isChecked;

    public CheckboxComponent(String name, String caption, String description) {
        super(name, caption, description, false, true);
        isChecked = false;
    }

    @Override
    public Widget draw(int width, TextRenderer renderer) {
        return Components.checkbox(Text.of(getCaption()));
    }

    @Override
    public Collection<Component> getUIComponent() {
        return Collections.singleton(
                Components.checkbox(Text.literal(this.getCaption()))
                        .margins(Insets.bottom(6))
        );
    }
}
