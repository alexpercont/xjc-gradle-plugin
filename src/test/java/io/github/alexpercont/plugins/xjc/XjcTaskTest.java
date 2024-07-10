package io.github.alexpercont.plugins.xjc;

import org.gradle.api.Project;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.Collections;
import java.util.Objects;

public class XjcTaskTest extends AbstractXjcPluginTest {

    @Test
    public void shouldGenerateCodeWhenSourceSchemaIsSpecified() {
        Project testProject = getTestProject();
        String schemaPath = Objects.requireNonNull(getClass().getResource("schema_01.xsd"))
                .getPath();
        testProject.getExtensions().getByType(XjcPluginExtension.class)
                .getSchema().set(Collections.singletonList(schemaPath));

        ((XjcTask) testProject.getTasks().getByName("xjc")).exec();
        validateGeneratedCodeInDefaultFolder();
    }

    @Test
    public void shouldGenerateCodeWhenSourceDirectoryIsSpecified() {
        Project testProject = getTestProject();
        Assert.assertTrue(testProject.getLayout().getBuildDirectory()
                .dir("generated/sources/xjc")
                .get().getAsFile().mkdirs());
        String sampleSchemaPath = Objects.requireNonNull(getClass().getResource("schema_01.xsd")).getPath();
        File f = new File(sampleSchemaPath);
        String sampleSchemaDir = f.getParent();
        testProject.getExtensions().getByType(XjcPluginExtension.class)
                .getSchema().set(Collections.singletonList(sampleSchemaDir));
        ((XjcTask) testProject.getTasks().getByName("xjc")).exec();
        validateGeneratedCodeInDefaultFolder();
    }

    @Test
    public void shouldGenerateCodeWhenSourceUrlIsSpecified() {
        Project testProject = getTestProject();
        String url = "https://jakarta.ee/xml/ns/jakartaee/web-common_6_0.xsd";
        testProject.getExtensions().getByType(XjcPluginExtension.class)
                .getSchema().set(Collections.singletonList(url));
        ((XjcTask) testProject.getTasks().getByName("xjc")).exec();
        validateGeneratedCodeInDefaultFolder();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenSourceIsNotSpecified() {
        Project testProject = getTestProject();
        ((XjcTask) testProject.getTasks().getByName("xjc")).exec();
    }
}
