package me.ranzeplay.messagechain.form;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.BlankFont;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@Environment(EnvType.CLIENT)
public class FormScreen extends Screen {
    protected FormScreen(Text title) {
        super(title);
    }

    public FormScreen(SimpleForm form) {
        super(Text.literal(form.title));
        this.textRenderer = MinecraftClient.getInstance().textRenderer;

        var grid = new GridWidget()
                .setSpacing(6);
        grid.getMainPositioner()
                .margin(8, 8, 8, 0);
        var adder = grid.createAdder(1);

        var headerGrid = new GridWidget()
                .setSpacing(3);
        var headerAdder = headerGrid.createAdder(1);
        headerAdder.add(new TextWidget(Text.literal(form.title).formatted(Formatting.BOLD), this.textRenderer).alignCenter());
        headerAdder.add(new TextWidget(Text.literal(form.description).formatted(Formatting.GRAY), this.textRenderer).alignLeft());
        headerGrid.refreshPositions();
        adder.add(headerGrid);

        var listGrid = new GridWidget()
                .setSpacing(2);
        var listAdder = listGrid.createAdder(1);
        for(var component: form.components) {
            var widget = component.draw();
            listAdder.add(widget);
        }
        listGrid.refreshPositions();
        adder.add(listGrid);

        grid.refreshPositions();
        grid.forEachChild(this::addDrawableChild);
        setFocused(true);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(context);
        super.render(context, mouseX, mouseY, delta);
    }
}
