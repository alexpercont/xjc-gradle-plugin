package io.github.alexpercont.plugins.xjc;

import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ResolvedArtifact;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.JavaExec;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.OutputDirectory;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.zip.ZipFile;

/**
 *
 */
public class XjcTask extends JavaExec {

    private static final String OUTPUT_DIR = "generated/sources/xjc";
    private static final String EPISODE_FILE = "META-INF/sun-jaxb.episode";

    private final ListProperty<String> schema;
    private final DirectoryProperty outputDir;
    private final ListProperty<File> bindings;

    /**
     * @param objectFactory object factory
     */
    @Inject
    public XjcTask(ObjectFactory objectFactory) {
        this.schema = objectFactory.listProperty(String.class);
        this.bindings = objectFactory.listProperty(File.class);
        this.outputDir = objectFactory.directoryProperty()
                .convention(getProject().getLayout().getBuildDirectory().dir(OUTPUT_DIR));
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
        checkForEmptySchema();
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
        addClasspathDependencies(args);
        addSchemaDirectory(args);
        return args;
    }

    private void checkForEmptySchema(){
        if(schema.get().isEmpty()){
            throw new IllegalArgumentException("Schema is not specified");
        }
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
        args.addAll(schema.get());
    }

    private void addExtension(final List<String> args) {
        args.add("-extension");
    }

    private void addEpisodeFile(final List<String> args) {
        args.add("-episode");
        args.add(new File(outputDir.get().getAsFile(), EPISODE_FILE).getPath());
    }

    private boolean createOutputDirectory() {
        File outputDirectory = outputDir.get().getAsFile();
        File metaInfDirectory = new File(outputDirectory, "META-INF");
        boolean outputDirectoryCreated = false;
        if(!outputDirectory.exists()) {
            outputDirectoryCreated = outputDirectory.mkdirs();
        }

        if(!metaInfDirectory.exists()) {
            outputDirectoryCreated &= metaInfDirectory.mkdirs();
        }
        return outputDirectoryCreated;
    }

    private void addClasspathDependencies(final List<String> args) {
        getProject().getConfigurations().stream()
                .filter(Configuration::isCanBeResolved)
                .forEach(configuration -> {
                    System.out.println(configuration.getName());
                    List<String> candidates = getCandidatesFromConfig(configuration);
                    args.addAll(candidates);
                });
    }

    private static List<String> getCandidatesFromConfig(Configuration configuration) {
        return configuration.getResolvedConfiguration().getFiles().stream()
                .filter(XjcTask::isJarFile)
                .filter(XjcTask::containsEpisodeFile)
                .map(File::getAbsolutePath)
                .toList();

    }

    private static boolean isJarFile(File file) {
        return file.getName().endsWith(".jar");
    }

    private static boolean containsEpisodeFile(File file) {
        try {
            return containsEpisodeFile(new ZipFile(file));
        } catch (IOException e) {
            return false;
        }
    }

    private static boolean containsEpisodeFile(ZipFile file) {
        return Objects.nonNull(file.getEntry(EPISODE_FILE));
    }
}
