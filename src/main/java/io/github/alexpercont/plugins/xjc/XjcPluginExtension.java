package io.github.alexpercont.plugins.xjc;

import org.gradle.api.Project;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.Optional;

import javax.inject.Inject;
import java.io.File;

/**
 *
 */
public class XjcPluginExtension {

    private static final String DEFAULT_OUTPUT_PATH = "generated-sources/xjc";

    private final Property<String> schema;
    private final DirectoryProperty outputDir;
    private final ListProperty<File> bindingPaths;

    /**
     * @param project project
     */
    @Inject
    public XjcPluginExtension(final Project project) {
        ObjectFactory objectFactory = project.getObjects();
        this.schema = objectFactory.property(String.class);
        this.outputDir = objectFactory.directoryProperty()
                .convention(project.getLayout().getBuildDirectory().dir(DEFAULT_OUTPUT_PATH));
        this.bindingPaths = objectFactory.listProperty(File.class);
    }

    /**
     * @return schema
     */
    @Input
    public Property<String> getSchema() {
        return schema;
    }

    /**
     * @return outputDir
     */
    @InputDirectory
    @Optional
    public DirectoryProperty getOutputDir() {
        return outputDir;
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
