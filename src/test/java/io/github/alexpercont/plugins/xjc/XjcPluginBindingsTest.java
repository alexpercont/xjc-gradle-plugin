package io.github.alexpercont.plugins.xjc;

import org.gradle.api.Project;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class XjcPluginBindingsTest extends AbstractXjcPluginTest {

    @Override
    @Before
    public void defineBaseProject() {
        super.defineBaseProject();
        Project testProject = getTestProject();
        URL schemaUrl = Objects.requireNonNull(getClass().getResource("schema_01.xsd"));
        String schemaPath = schemaUrl.getPath();

        testProject.getExtensions().getByType(XjcPluginExtension.class)
                .getSchema().set(schemaPath);
    }

    @Test
    public void shouldGenerateCodeWithBindingsSpecified() {
        Project testProject = getTestProject();
        URL bindingsFileUrl = Objects.requireNonNull(getClass().getResource("bindings/binding_01.xjb"));
        List<File> bindings = List.of(new File(bindingsFileUrl.getPath()));

        testProject.getExtensions().getByType(XjcPluginExtension.class)
                        .getBindingPaths().set(bindings);

        ((XjcTask) testProject.getTasks().getByName("xjc")).exec();
        validateGeneratedCodeInDefaultFolder();
    }

    @Test
    public void shouldGenerateCodeWithBindingsDirectorySpecified(){
        Project testProject = getTestProject();
        URL bindingsDirUrl = Objects.requireNonNull(getClass().getResource("bindings"));
        List<File> bindings = List.of(new File(bindingsDirUrl.getPath()));

        testProject.getExtensions().getByType(XjcPluginExtension.class)
                        .getBindingPaths().set(bindings);

        ((XjcTask) testProject.getTasks().getByName("xjc")).exec();
        validateGeneratedCodeInDefaultFolder();
    }
}
