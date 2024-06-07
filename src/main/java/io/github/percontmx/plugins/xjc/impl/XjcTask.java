package io.github.percontmx.plugins.xjc.impl;

import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.JavaExec;
import org.gradle.api.tasks.Optional;

import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class XjcTask extends JavaExec {

    private final Property<String> source;

    private final Property<File> target;

    @Input
    public Property<String> getSource() {
        return source;
    }

    @InputDirectory
    @Optional
    public Property<File> getTarget() {
        return target;
    }

    @Inject
    public XjcTask (ObjectFactory objectFactory){
        this.source = objectFactory.property(String.class);
        this.target = objectFactory.property(File.class);
    }

    @Override
    public void exec() {
        File targetFile = target.get();
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }

        List<String> args = new ArrayList<>();
        args.add("-d");
        args.add(target.get().getPath());
        args.add(source.get());
        setArgs(args);
        super.exec();
    }


}
