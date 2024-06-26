package io.github.alexpercont.plugins.xjc;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestName;

import java.io.File;

public abstract class AbstractXjcPluginTest {

    @Rule
    public TemporaryFolder projectDirectory = new TemporaryFolder();

    @Rule
    public TestName testName = new TestName();

    private Project testProject;

    @Before
    public void defineBaseProject() {
        testProject = ProjectBuilder.builder()
                .withProjectDir(projectDirectory.getRoot())
                .withName(testName.getMethodName())
                .build();
        testProject.getPluginManager().apply("io.github.alexpercont.xjc-plugin");
        testProject.getRepositories().add(testProject.getRepositories().mavenCentral());
    }

    protected Project getTestProject() {
        return testProject;
    }

    protected void validateGeneratedCodeInDefaultFolder() {
        File projectFolder = testProject.getProjectDir();
        Assert.assertNotNull("Project directory is null", projectFolder);
        File xjcFolder = new File(projectFolder, "build/generated/sources/xjc");
        Assert.assertNotNull("XJC output folder is null", xjcFolder);
        Assert.assertTrue("XJC output folder does not exist.", xjcFolder.exists());
        Assert.assertTrue("XJC output folder is not a directory.", xjcFolder.isDirectory());
        File[] outputFiles = xjcFolder.listFiles();
        Assert.assertNotNull("XJC output folder is empty.", outputFiles);
        Assert.assertTrue("XJC output folder is empty.", outputFiles.length > 0);
    }
}
