package me.ranzeplay.messagechain.form;

import me.ranzeplay.messagechain.init.MessageChainInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@Environment(EnvType.CLIENT)
public class FormScreen extends Screen {
    final int LIST_SPACING = 6;
    int reservedHeight = 0;

    SimpleForm form;
    int lastHeight = 0;
    GridWidget grid;

    ArrayList<ArrayList<Widget>> pagedComponents;
    int showingPage = 0;

    protected FormScreen(Text title) {
        super(title);
    }

    public FormScreen(SimpleForm form) {
        super(Text.literal(form.title));
        this.form = form;
        this.pagedComponents = new ArrayList<>();
    }

    @Override
    protected void init() {
        grid = new GridWidget()
                .setSpacing(10);
        grid.getMainPositioner()
                .margin(8, 8, 8, 0);
        var adder = grid.createAdder(1);

        var headerGrid = addHeader();
        var menuGrid = addMenu();

        adder.add(headerGrid);
        reservedHeight = headerGrid.getHeight() + menuGrid.getHeight();
        updatePages(height - reservedHeight);

        var listGrid = setShowingPage();
        adder.add(listGrid);
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
    public void resize(MinecraftClient client, int width, int height) {
        super.resize(client, width, height);

        MessageChainInitializer.LOGGER.info("Height updated: {} -> {}", lastHeight, height);
        lastHeight = height;
        // updatePages(height - reservedHeight);
        MessageChainInitializer.LOGGER.info("Page count updated to: {}", pagedComponents.size());

        this.clearAndInit();
    }

    private void updatePages(final int availableHeight) {
        pagedComponents.clear();
        int usedHeight = 0;
        var currentPageElements = new ArrayList<Widget>();
        for (var component : form.components) {
            var widget = component.draw(width - 20, this.textRenderer);

            var sectionGrid = new GridWidget()
                    .setSpacing(4);
            var sectionAdder = sectionGrid.createAdder(1);
            var caption = new TextWidget(Text.literal(component.name), this.textRenderer);
            sectionAdder.add(caption);
            sectionAdder.add(widget);
            sectionGrid.refreshPositions();

            if (usedHeight + sectionGrid.getHeight() > availableHeight) {
                pagedComponents.add(currentPageElements);
                currentPageElements = new ArrayList<>();
            } else {
                currentPageElements.add(sectionGrid);
                usedHeight += sectionGrid.getHeight() + LIST_SPACING;
            }
        }
        pagedComponents.add(currentPageElements);
    }

    private @NotNull GridWidget addHeader() {
        var headerGrid = new GridWidget()
                .setSpacing(4);
        var headerAdder = headerGrid.createAdder(1);
        headerAdder.add(new TextWidget(Text.literal(form.title).formatted(Formatting.BOLD), this.textRenderer).alignCenter());
        headerAdder.add(new TextWidget(Text.literal(form.description).formatted(Formatting.GRAY), this.textRenderer).alignLeft());
        headerAdder.add(addPageTurn());
        headerGrid.refreshPositions();

        return headerGrid;
    }

    private @NotNull GridWidget addMenu() {
        var menuGrid = new GridWidget()
                .setSpacing(4);
        menuGrid.getMainPositioner()
                .alignRight();
        var menuAdder = menuGrid.createAdder(2);
        menuAdder.add(ButtonWidget.builder(Text.literal("Submit"), this::submit).build());
        if (form.isShowCancelButton()) {
            menuAdder.add(ButtonWidget.builder(Text.literal("Cancel"), this::cancel).build());
        }
        menuGrid.refreshPositions();

        return menuGrid;
    }

    private @NotNull GridWidget setShowingPage() {
        var listGrid = new GridWidget()
                .setSpacing(LIST_SPACING);
        var listAdder = listGrid.createAdder(1);
        pagedComponents.get(showingPage).forEach(listAdder::add);
        listGrid.refreshPositions();

        return listGrid;
    }

    private GridWidget addPageTurn() {
        var pageGrid = new GridWidget()
                .setColumnSpacing(10);
        var adder = pageGrid.createAdder(2);

        var prevButton = ButtonWidget.builder(Text.literal("Prev"), this::prevPage).build();
        if(showingPage == 0) {
            prevButton.active = false;
        }
        adder.add(prevButton);

        var nextButton = ButtonWidget.builder(Text.literal("Next"), this::nextPage).build();
        if(showingPage == pagedComponents.size() - 1) {
            nextButton.active = false;
        }
        adder.add(nextButton);

        pageGrid.refreshPositions();
        return pageGrid;
    }

    private void prevPage(ButtonWidget buttonWidget) {
        showingPage--;
        clearAndInit();
    }

    private void nextPage(ButtonWidget buttonWidget) {
        showingPage++;
        clearAndInit();
    }
}
