package io.github.alexpercont.plugins.xjc;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.plugins.JavaPluginExtension;

/**
 * XJC plugin
 */
@SuppressWarnings("unused")
public class XjcPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        // Apply Java plugin.
        project.getPluginManager().apply("java");

        // Create XJC plugin configuration.
        project.getConfigurations().create("xjc", configuration-> {
            configuration.setVisible(false);
            configuration.setTransitive(true);
            configuration.setDescription("XJC configuration");
        });

        // Adding JAXB-XJC dependency.
        project.getDependencies().add("xjc", "com.sun.xml.bind:jaxb-impl:4.0.5");
        project.getDependencies().add("xjc", "com.sun.xml.bind:jaxb-xjc:4.0.5");

        // Define XJC plugin extension.
        project.getExtensions().create("xjc", XjcPluginExtension.class, project);

        // Define xjc task.
        project.getTasks().register("xjc", XjcTask.class, task -> {
            XjcPluginExtension pluginExtension = project.getExtensions().getByType(XjcPluginExtension.class);
            task.setGroup("build");
            task.setDescription("Generates Java classes from XML schema");
            task.setClasspath(project.getConfigurations().getByName("xjc"));
            task.getMainClass().set("com.sun.tools.xjc.XJCFacade");
            task.getSchema().set(pluginExtension.getSchema());
            task.getBindings().set(pluginExtension.getBindingPaths());

            DirectoryProperty outputDir = task.getOutputDir();
            project.getExtensions().getByType(JavaPluginExtension.class)
                    .getSourceSets().getByName("main").getJava()
                    .srcDir(outputDir.get().getAsFile().getPath());


        });
    }
}
