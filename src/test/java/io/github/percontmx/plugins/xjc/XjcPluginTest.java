package io.github.percontmx.plugins.xjc;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class XjcPluginTest {

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    @Before
    public void setUp() throws IOException {
        File sampleBuild = temp.newFile("build.gradle");
        try (FileWriter f = new FileWriter(sampleBuild)) {
            f.write("plugins {\n");
            f.write("    id 'io.github.percontmx.xjc-plugin' version '1.0-SNAPSHOT'\n");
            f.write("}\n");
            f.write("repositories {\n");
            f.write("    mavenCentral()\n");
            f.write("}\n");
        }
    }

    @Test
    public void testApplyingPlugin() {
        BuildResult result = GradleRunner.create()
                .withProjectDir(temp.getRoot())
                .withPluginClasspath()
                .withArguments("tasks")
                .build();
        Assert.assertTrue(result.getOutput().contains("xjc"));
    }

    @Test
    public void testRunningTastXjc() {
        BuildResult result = GradleRunner.create()
                .withProjectDir(temp.getRoot())
                .withPluginClasspath()
                .withArguments("xjc")
                .buildAndFail();
        Assert.assertTrue(result.getOutput().contains("BUILD FAILED"));
    }
}
