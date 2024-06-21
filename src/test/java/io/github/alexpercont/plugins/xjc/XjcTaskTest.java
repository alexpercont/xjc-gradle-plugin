package io.github.alexpercont.plugins.xjc;

import org.gradle.api.Project;
import org.gradle.api.internal.provider.MissingValueException;
import org.junit.Test;

import java.io.*;
import java.util.Objects;

public class XjcTaskTest extends AbstractXjcPluginTest {

    @Test
    public void shouldGenerateCodeWhenSourceSchemaIsSpecified() {
        Project testProject = getTestProject();
        String schemaPath = Objects.requireNonNull(getClass().getResource("schema_01.xsd"))
                .getPath();
        testProject.getExtensions().getByType(XjcPluginExtension.class)
                .getSchema().set(schemaPath);

        ((XjcTask) testProject.getTasks().getByName("xjc")).exec();
        validateGeneratedCodeInDefaultFolder();
    }

    @Test
    public void shouldGenerateCodeWhenSourceDirectoryIsSpecified() {
        Project testProject = getTestProject();
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
        Project testProject = getTestProject();
        String url = "https://jakarta.ee/xml/ns/jakartaee/web-common_6_0.xsd";
        testProject.getExtensions().getByType(XjcPluginExtension.class)
                .getSchema().set(url);
        ((XjcTask) testProject.getTasks().getByName("xjc")).exec();
        validateGeneratedCodeInDefaultFolder();
    }

    @Test(expected = MissingValueException.class)
    public void shouldFailWhenSourceIsNotSpecified() {
        Project testProject = getTestProject();
        ((XjcTask) testProject.getTasks().getByName("xjc")).exec();
    }
}
