package io.github.alexpercont.plugins.xjc;

import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.JavaExec;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.OutputDirectory;

import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class XjcTask extends JavaExec {

    private static final String OUTPUT_DIR = "generated/sources/xjc";
    private static final String EPISODE_FILE = "META-INF/sun-jaxb.episode";

    private final Property<String> schema;
    private final DirectoryProperty outputDir;
    private final ListProperty<File> bindings;

    /**
     * @param objectFactory object factory
     */
    @Inject
    public XjcTask(ObjectFactory objectFactory) {
        this.schema = objectFactory.property(String.class);
        this.bindings = objectFactory.listProperty(File.class);
        this.outputDir = objectFactory.directoryProperty()
                .convention(getProject().getLayout().getBuildDirectory().dir(OUTPUT_DIR));
    }

    /**
     * @return schema
     */
    @Input
    public Property<String> getSchema() {
        return schema;
    }

    /**
     * @return bindingPaths
     */
    @Input
    @Optional
    public ListProperty<File> getBindings() {
        return bindings;
    }

    /**
     * @return outputDir
     */
    @OutputDirectory
    public DirectoryProperty getOutputDir() {
        return outputDir;
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
        addEpisodeFile(args);
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

    private void addEpisodeFile(final List<String> args) {
        args.add("-episode");
        args.add(new File(outputDir.get().getAsFile(), EPISODE_FILE).getPath());
    }

    private boolean createOutputDirectory() {
        File outputDirectory = outputDir.get().getAsFile(),
                metaInfDirectory = new File(outputDirectory, "META-INF");
        if (!outputDirectory.exists()) {
            return outputDirectory.mkdirs() && metaInfDirectory.mkdirs();
        }
        return true;
    }
}
