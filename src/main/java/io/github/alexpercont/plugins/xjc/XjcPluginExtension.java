package io.github.alexpercont.plugins.xjc;

import org.gradle.api.provider.Property;

import java.io.File;

public interface XjcPluginExtension {

    Property<String> getSource();

    Property<File> getTarget();

}
