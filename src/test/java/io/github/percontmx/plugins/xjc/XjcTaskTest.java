package io.github.percontmx.plugins.xjc;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class XjcTaskTest {

    @Rule
    public TemporaryFolder projectDir = new TemporaryFolder();

    private final static String BUILD_FILE_CONTENTS = """
                plugins {
                    id 'io.github.percontmx.xjc-plugin'
                }
                
                repositories {
                    mavenCentral()
                }
                """;

    @Test
    public void shouldGenerateCodeWhenSourceSchemaIsSpecified() throws IOException {
        String sampleSchemaPath = Objects.requireNonNull(getClass().getResource("schema_01.xsd")).getPath();
        String testBuildFileWithSource = BUILD_FILE_CONTENTS.concat("""                
                xjc {
                    source = 'sampleSchemaPath'
                }
                """).replace("sampleSchemaPath", sampleSchemaPath);
        File file = projectDir.newFile("build.gradle");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(testBuildFileWithSource);
        }

        BuildResult build = GradleRunner.create()
                .withProjectDir(projectDir.getRoot()).withArguments("xjc")
                .withPluginClasspath()
                .build();
        System.out.println(build.getOutput());
        Assert.assertTrue(build.getOutput().contains("BUILD SUCCESSFUL"));
    }

    @Test
    public void shouldGenerateCodeWhenSourceDirectoryIsSpecified() throws IOException {
        String sampleSchemaPath = Objects.requireNonNull(getClass().getResource("schema_01.xsd")).getPath();
        File f = new File(sampleSchemaPath);
        String sampleSchemaDir = f.getParent();
        String testBuildFileWithSource = BUILD_FILE_CONTENTS.concat("""                
                xjc {
                    source = 'sampleSchemaDir'
                }
                """).replace("sampleSchemaDir", sampleSchemaDir);
        File file = projectDir.newFile("build.gradle");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(testBuildFileWithSource);
        }

        BuildResult build = GradleRunner.create()
                .withProjectDir(projectDir.getRoot()).withArguments("xjc")
                .withPluginClasspath()
                .build();
        System.out.println(build.getOutput());
        Assert.assertTrue(build.getOutput().contains("BUILD SUCCESSFUL"));
    }

    @Test
    public void shouldGenerateCodeWhenSourceUrlIsSpecified() throws IOException {
        String sampleSchemaUrl = "http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI/tdCFDI.xsd";
        String testBuildFileWithSource = BUILD_FILE_CONTENTS.concat("""                
                xjc {
                    source = 'sampleSchemaUrl'
                }
                """).replace("sampleSchemaUrl", sampleSchemaUrl);
        File file = projectDir.newFile("build.gradle");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(testBuildFileWithSource);
        }

        BuildResult build = GradleRunner.create()
                .withProjectDir(projectDir.getRoot()).withArguments("xjc")
                .withPluginClasspath()
                .build();
        System.out.println(build.getOutput());
        Assert.assertTrue(build.getOutput().contains("BUILD SUCCESSFUL"));
    }

    @Test
    public void shouldFailWhenSourceIsNotSpecified() throws IOException {
        File file = projectDir.newFile("build.gradle");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(BUILD_FILE_CONTENTS);
        }

        BuildResult build = GradleRunner.create()
                .withProjectDir(projectDir.getRoot()).withArguments("xjc")
                .withPluginClasspath()
                .buildAndFail();
        System.out.println(build.getOutput());
        Assert.assertTrue(build.getOutput().contains("property 'source' doesn't have a configured value"));
    }
}
