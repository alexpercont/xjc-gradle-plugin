package io.github.percontmx.plugins.xjc;

import io.github.percontmx.plugins.xjc.impl.XjcTask;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
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
        testProject.getPluginManager().apply("io.github.percontmx.xjc-plugin");
        testProject.getRepositories().add(testProject.getRepositories().mavenCentral());
    }

    @Test
    public void shouldGenerateCodeWhenSourceSchemaIsSpecified() {
       String schemaPath = Objects.requireNonNull(getClass().getResource("schema_01.xsd"))
                .getPath();

       XjcTask xjc = (XjcTask) testProject.getTasks().getByName("xjc");
       xjc.setSource(schemaPath);
       xjc.exec();

    }

    @Test
    public void shouldGenerateCodeWhenSourceDirectoryIsSpecified() {
        String sampleSchemaPath = Objects.requireNonNull(getClass().getResource("schema_01.xsd")).getPath();
        File f = new File(sampleSchemaPath);
        String sampleSchemaDir = f.getParent();

        XjcTask xjc = (XjcTask) testProject.getTasks().getByName("xjc");
        xjc.setSource(sampleSchemaDir);
        xjc.exec();
    }

    @Test
    public void shouldGenerateCodeWhenSourceUrlIsSpecified() {
        String url = "http://www.sat.gob.mx/sitio_internet/cfd/tipoDatos/tdCFDI/tdCFDI.xsd";
        XjcTask xjc = (XjcTask) testProject.getTasks().getByName("xjc");
        xjc.setSource(url);
        xjc.exec();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenSourceIsNotSpecified() {
        ((XjcTask) testProject.getTasks().getByName("xjc")).exec();
    }
}
