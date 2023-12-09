package me.ranzeplay.messagechain.form;

import me.ranzeplay.messagechain.init.MessageChainInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.BlankFont;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.network.message.MessageChain;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@Environment(EnvType.CLIENT)
public class FormScreen extends Screen {
    SimpleForm form;
    int lastWidth = 0;

    protected FormScreen(Text title) {
        super(title);
    }

    public FormScreen(SimpleForm form) {
        super(Text.literal(form.title));
        this.form = form;
    }

    @Override
    protected void init() {
        // this.textRenderer = MinecraftClient.getInstance().textRenderer;

        var grid = new GridWidget()
                .setSpacing(10);
        grid.getMainPositioner()
                .margin(8, 8, 8, 0);
        var adder = grid.createAdder(1);

        var headerGrid = new GridWidget()
                .setSpacing(4);
        var headerAdder = headerGrid.createAdder(1);
        headerAdder.add(new TextWidget(Text.literal(form.title).formatted(Formatting.BOLD), this.textRenderer).alignCenter());
        headerAdder.add(new TextWidget(Text.literal(form.description).formatted(Formatting.GRAY), this.textRenderer).alignLeft());
        headerGrid.refreshPositions();
        adder.add(headerGrid);

        var listGrid = new GridWidget()
                .setSpacing(6);
        var listAdder = listGrid.createAdder(1);
        for(var component: form.components) {
            var widget = component.draw(width - 20, this.textRenderer);

            var sectionGrid = new GridWidget()
                    .setSpacing(4);
            var sectionAdder = sectionGrid.createAdder(1);
            var caption = new TextWidget(Text.literal(component.name), this.textRenderer);
            sectionAdder.add(caption);
            sectionAdder.add(widget);
            sectionGrid.refreshPositions();

            listAdder.add(sectionGrid);
        }
        listGrid.refreshPositions();
        adder.add(listGrid);

        var menuGrid = new GridWidget()
                .setSpacing(4);
        menuGrid.getMainPositioner()
                .alignRight();
        var menuAdder = menuGrid.createAdder(2);
        menuAdder.add(ButtonWidget.builder(Text.literal("Submit"), this::submit).build());
        if(form.isShowCancelButton()) {
            menuAdder.add(ButtonWidget.builder(Text.literal("Cancel"), this::cancel).build());
        }
        menuGrid.refreshPositions();
        adder.add(menuGrid);

        grid.refreshPositions();
        grid.forEachChild(this::addDrawableChild);
        setFocused(true);
    }

    private void cancel(ButtonWidget buttonWidget) {
        this.close();
    }

    private void submit(ButtonWidget buttonWidget) {
        this.close();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(context);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void tick() {
        if(width != lastWidth) {
            MessageChainInitializer.LOGGER.info("Width updated: {} -> {}", lastWidth, width);
            lastWidth = width;
        }
    }
}
