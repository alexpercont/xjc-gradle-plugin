package io.github.alexpercont.plugins.xjc;

import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.JavaExec;
import org.gradle.api.tasks.Optional;

import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class XjcTask extends JavaExec {

    private final Property<String> schema;
    private final DirectoryProperty outputDir;
    private final ListProperty<File> bindings;

    /**
     * @param objectFactory object factory
     */
    @Inject
    public XjcTask(ObjectFactory objectFactory) {
        this.schema = objectFactory.property(String.class);
        this.outputDir = objectFactory.directoryProperty();
        this.bindings = objectFactory.listProperty(File.class);
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
    public ListProperty<File> getBindings() {
        return bindings;
    }

    @Override
    public void exec() {
        if (createOutputDirectory()) {
            getLogger().info("Output directory created");
        }

        setArgs(constructArgs());
        super.exec();
    }

    private List<String> constructArgs() {
        List<String> args = new ArrayList<>();
        addOutputDirectory(args);
        addBindingsDirectories(args);
        addExtension(args);
        addSchemaDirectory(args);
        return args;
    }

    private void addOutputDirectory(final List<String> args) {
        args.add("-d");
        args.add(outputDir.get().getAsFile().getPath());
    }

    private void addBindingsDirectories(final List<String> args) {
        bindings.get().forEach(file -> {
            args.add("-b");
            args.add(file.getPath());
        });
    }

    private void addSchemaDirectory(final List<String> args) {
        args.add(schema.get());
    }

    private void addExtension(final List<String> args) {
        args.add("-extension");
    }

    private boolean createOutputDirectory() {
        File targetFile = outputDir.get().getAsFile();
        if (!targetFile.exists()) {
            return targetFile.mkdirs();
        }
        return false;
    }
}
