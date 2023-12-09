package me.ranzeplay.messagechain.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.Widget;
import org.apache.commons.lang3.NotImplementedException;

@Getter
@AllArgsConstructor
public abstract class AbstractFormComponent {
    String name;
    String caption;
    String description;
    Object defaultValue;
    boolean isEnabled;

    public Widget draw(int width, TextRenderer renderer) {
        throw new NotImplementedException();
    }
}
