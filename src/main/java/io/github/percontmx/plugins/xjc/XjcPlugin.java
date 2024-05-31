package io.github.percontmx.plugins.xjc;

import io.github.percontmx.plugins.xjc.impl.DefaultXjcPluginExtension;
import io.github.percontmx.plugins.xjc.impl.XjcTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

@SuppressWarnings("unused")
public class XjcPlugin implements Plugin<Project> {

    @Override
    public void apply(Project target) {
        target.getPluginManager().apply("java");

        target.getConfigurations().create("xjc", conf -> {
            conf.setVisible(false);
            conf.setTransitive(true);
            conf.setDescription("XJC configuration");
        });

        target.getDependencies().add("xjc",
                "com.sun.xml.bind:jaxb-xjc:4.0.5");

        target.getExtensions().create(XjcPluginExtension.class, "xjc",
                DefaultXjcPluginExtension.class);

        target.getTasks().register("xjc", XjcTask.class, task -> {
            task.setGroup("build");
            task.setDescription("Generates Java classes from XML schema");
            task.setClasspath(target.getConfigurations().getByName("xjc"));
            task.getMainClass().set("com.sun.tools.xjc.XJCFacade");
        });
    }
}
