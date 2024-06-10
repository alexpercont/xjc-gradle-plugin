package io.github.alexpercont.plugins.xjc.impl;

import io.github.alexpercont.plugins.xjc.XjcPluginExtension;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;

import javax.inject.Inject;
import java.io.File;

public class DefaultXjcPluginExtension implements XjcPluginExtension {

    private final Property<String> source;

    private final Property<File> target;

    @Inject
    public DefaultXjcPluginExtension(ObjectFactory objects) {
        this.source = objects.property(String.class);
        this.target = objects.property(File.class);
    }

    public Property<String> getSource() {
        return source;
    }

    public Property<File> getTarget() {
        return target;
    }
}
