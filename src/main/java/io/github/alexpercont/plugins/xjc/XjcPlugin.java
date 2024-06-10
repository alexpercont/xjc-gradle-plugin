package io.github.alexpercont.plugins.xjc;

import io.github.alexpercont.plugins.xjc.impl.DefaultXjcPluginExtension;
import io.github.alexpercont.plugins.xjc.impl.XjcTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

@SuppressWarnings("unused")
public class XjcPlugin implements Plugin<Project> {

    @Override
    public void apply(Project target) {
        target.getPluginManager().apply("java");

        // Create XJC plugin configuration.
        target.getConfigurations().create("xjc", conf -> {
            conf.setVisible(false);
            conf.setTransitive(true);
            conf.setDescription("XJC configuration");
        });

        // Adding JAXB-XJC dependency.
        target.getDependencies().add("xjc",
                "com.sun.xml.bind:jaxb-impl:4.0.5");
        target.getDependencies().add("xjc",
                "com.sun.xml.bind:jaxb-xjc:4.0.5");

        target.getExtensions().create(XjcPluginExtension.class, "xjc",
                DefaultXjcPluginExtension.class);

        target.getTasks().register("xjc", XjcTask.class, task -> {
            XjcPluginExtension pluginExtension = target.getExtensions().getByType(XjcPluginExtension.class);
            task.getSource().set(pluginExtension.getSource());
            task.getTarget().set(pluginExtension.getTarget().convention(target.file("build/generated-sources/xjc")));
            task.setGroup("build");
            task.setDescription("Generates Java classes from XML schema");
            task.setClasspath(target.getConfigurations().getByName("xjc"));
            task.getMainClass().set("com.sun.tools.xjc.XJCFacade");
        });
    }
}
