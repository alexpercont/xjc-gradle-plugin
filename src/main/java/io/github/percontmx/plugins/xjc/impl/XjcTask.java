package io.github.percontmx.plugins.xjc.impl;

import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.JavaExec;
import org.gradle.api.tasks.Optional;

import java.util.ArrayList;
import java.util.List;

public abstract class XjcTask extends JavaExec {

    private String source;

    @Input
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public void exec() {
        List<String> args = new ArrayList<>();

        args.add(source);

        setArgs(args);

        super.exec();
    }
}
