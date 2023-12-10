package me.ranzeplay.messagechain.form;

import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.ScrollContainer;
import io.wispforest.owo.ui.core.*;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AdvancedFormScreen extends BaseOwoScreen<FlowLayout> {
    SimpleForm form;

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent.surface(Surface.VANILLA_TRANSLUCENT)
                .verticalAlignment(VerticalAlignment.TOP)
                .horizontalAlignment(HorizontalAlignment.LEFT)
                .padding(Insets.of(8));

        // Add heading
        rootComponent.child(Components.label(Text.literal(form.title).formatted(Formatting.BOLD)).margins(Insets.bottom(4)));
        rootComponent.child(Components.label(Text.literal(form.description).formatted(Formatting.GRAY)).margins(Insets.bottom(10)));

        // var container = Containers.verticalScroll(Sizing.fill(100), Sizing.fill(100), Components.box(Sizing.content(), Sizing.content()));

        // Add form entries
        for(var component : form.components) {
            component.getUIComponent().forEach(c -> {
                rootComponent.child(c);
            });
        }

        // rootComponent.child(container);

        // Add menu
        var button = Components.button(Text.of("Submit"), this::submit);
        button.margins(Insets.top(4));
        button.setWidth(50);
        rootComponent.child(button);
    }

    private void submit(ButtonComponent buttonComponent) {
        this.close();
    }

    public AdvancedFormScreen(SimpleForm form) {
        this.form = form;
    }
}
