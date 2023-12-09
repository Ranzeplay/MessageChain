package me.ranzeplay.messagechain.form;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public class FormBuilder {
    String title;
    String description;
    FormLayout layout;
    boolean showCancelButton;

    ArrayList<AbstractFormComponent> components = new ArrayList<>();

    public FormBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public FormBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public FormBuilder setLayout(FormLayout layout) {
        this.layout = layout;
        return this;
    }

    public FormBuilder setShowCancelButton(boolean showCancelButton) {
        this.showCancelButton = showCancelButton;
        return this;
    }

    public FormBuilder appendElement(AbstractFormComponent component) {
        this.components.add(component);
        return this;
    }

    public SimpleForm build() {
        return new SimpleForm(title, description, layout, showCancelButton, components);
    }
}
