package io.github.alexpercont.plugins.xjc;

import org.gradle.api.Project;
import org.gradle.api.internal.provider.MissingValueException;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestName;

import java.io.*;
import java.util.Objects;

public class XjcTaskTest {

    @Rule
    public TestName testName = new TestName();

    @Rule
    public TemporaryFolder projectDir = new TemporaryFolder();

    private Project testProject;

    @Before
    public void defineBaseProject() {
        testProject = ProjectBuilder.builder()
                .withProjectDir(projectDir.getRoot())
                .withName(testName.getMethodName())
                .build();
        testProject.getPluginManager().apply("io.github.alexpercont.xjc-plugin");
        testProject.getRepositories().add(testProject.getRepositories().mavenCentral());
    }

    @Test
    public void shouldGenerateCodeWhenSourceSchemaIsSpecified() {
        String schemaPath = Objects.requireNonNull(getClass().getResource("schema_01.xsd"))
                .getPath();
        testProject.getExtensions().getByType(XjcPluginExtension.class)
                .getSchema().set(schemaPath);

        ((XjcTask) testProject.getTasks().getByName("xjc")).exec();
        validateGeneratedCodeInDefaultFolder();
    }

    private void validateGeneratedCodeInDefaultFolder() {
        validateGeneratedCodeInDefaultFolder("/build/generated-sources/xjc");
    }

    private void validateGeneratedCodeInDefaultFolder(String generatedFolder) {
        File projectRoot = projectDir.getRoot();
        Assert.assertNotNull(projectRoot);
        File xjcFolder = new File(projectRoot.getAbsolutePath().concat(generatedFolder));
        Assert.assertNotNull(xjcFolder);
        Assert.assertTrue(xjcFolder.exists());
        Assert.assertTrue(xjcFolder.isDirectory());
        Assert.assertNotNull(xjcFolder.listFiles());
        Assert.assertTrue(Objects.requireNonNull(xjcFolder.listFiles()).length > 0);
    }

    @Test
    public void shouldGenerateCodeWhenSourceDirectoryIsSpecified() {
        String sampleSchemaPath = Objects.requireNonNull(getClass().getResource("schema_01.xsd")).getPath();
        File f = new File(sampleSchemaPath);
        String sampleSchemaDir = f.getParent();
        testProject.getExtensions().getByType(XjcPluginExtension.class)
                .getSchema().set(sampleSchemaDir);
        ((XjcTask) testProject.getTasks().getByName("xjc")).exec();
        validateGeneratedCodeInDefaultFolder();
    }

    @Test
    public void shouldGenerateCodeWhenSourceUrlIsSpecified() {
        String url = "https://jakarta.ee/xml/ns/jakartaee/web-common_6_0.xsd";
        testProject.getExtensions().getByType(XjcPluginExtension.class)
                .getSchema().set(url);
        ((XjcTask) testProject.getTasks().getByName("xjc")).exec();
        validateGeneratedCodeInDefaultFolder();
    }

    @Test(expected = MissingValueException.class)
    public void shouldFailWhenSourceIsNotSpecified() {
        ((XjcTask) testProject.getTasks().getByName("xjc")).exec();
    }
}
