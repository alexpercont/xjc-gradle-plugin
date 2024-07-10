package io.github.alexpercont.plugins.xjc;

import org.gradle.api.Project;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Optional;

import javax.inject.Inject;
import java.io.File;

/**
 *
 */
public class XjcPluginExtension {

    private final ListProperty<String> schema;
    private final ListProperty<File> bindingPaths;

    /**
     * @param project project
     */
    @Inject
    public XjcPluginExtension(final Project project) {
        ObjectFactory objectFactory = project.getObjects();
        this.schema = objectFactory.listProperty(String.class);
        this.bindingPaths = objectFactory.listProperty(File.class);
    }

    /**
     * @return schema
     */
    @Input
    public ListProperty<String> getSchema() {
        return schema;
    }

    /**
     * @return bindingPaths
     */
    @Input
    @Optional
    public ListProperty<File> getBindingPaths() {
        return bindingPaths;
    }
}
