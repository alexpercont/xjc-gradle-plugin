package io.github.percontmx.plugins.xjc;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.BuildTask;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

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
        GradleRunner gradleRunner = GradleRunner.create()
                .withProjectDir(temp.getRoot())
                .withPluginClasspath()
                .withArguments("tasks");

        BuildResult result = gradleRunner.build();


        Assert.assertTrue(result.getOutput().contains("BUILD SUCCESSFUL"));
    }

    @Test
    public void testRunningTastXjc() {
        GradleRunner gradleRunner = GradleRunner.create()
                .withProjectDir(temp.getRoot())
                .withPluginClasspath()
                .withArguments("xjc");

        BuildResult result = gradleRunner.build();
        List<BuildTask> tasks = result.tasks(TaskOutcome.SUCCESS);
        Assert.assertTrue(tasks.stream().anyMatch(t -> t.getPath().contains("xjc")));
    }
}
