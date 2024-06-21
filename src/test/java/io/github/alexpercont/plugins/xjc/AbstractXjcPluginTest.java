package io.github.alexpercont.plugins.xjc;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestName;

import java.io.File;
import java.util.Objects;

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
        validateGeneratedCodeInDefaultFolder("/build/generated-sources/xjc");
    }

    private void validateGeneratedCodeInDefaultFolder(String generatedFolder) {
        File projectRoot = projectDirectory.getRoot();
        Assert.assertNotNull(projectRoot);
        File xjcFolder = new File(projectRoot.getAbsolutePath().concat(generatedFolder));
        Assert.assertNotNull(xjcFolder);
        Assert.assertTrue(xjcFolder.exists());
        Assert.assertTrue(xjcFolder.isDirectory());
        Assert.assertNotNull(xjcFolder.listFiles());
        Assert.assertTrue(Objects.requireNonNull(xjcFolder.listFiles()).length > 0);
    }
}
