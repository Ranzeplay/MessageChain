package me.ranzeplay.messagechain.form.components;

import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.SmallCheckboxComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Component;
import io.wispforest.owo.ui.core.Insets;
import me.ranzeplay.messagechain.form.AbstractFormComponent;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.text.Text;

import java.lang.ref.Reference;
import java.util.Collection;
import java.util.Collections;

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

    @Override
    public Collection<Component> getUIComponent() {
        return Collections.singleton(
                Components.checkbox(Text.literal(this.getCaption()))
                        .margins(Insets.bottom(6))
        );
    }
}
