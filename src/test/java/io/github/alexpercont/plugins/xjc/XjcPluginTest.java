package io.github.alexpercont.plugins.xjc;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Assert;
import org.junit.Test;

public class XjcPluginTest {

    @Test
    public void testApplyingPlugin() {
        Project project = ProjectBuilder.builder().build();
        project.getPluginManager().apply("io.github.alexpercont.xjc-plugin");
        Assert.assertTrue(project.getPlugins().hasPlugin("java"));
        Assert.assertNotNull(project.getTasks().findByName("xjc"));
    }
}
