package io.github.percontmx.plugins.xjc.impl;

import io.github.percontmx.plugins.xjc.XjcPluginExtension;

public class DefaultXjcPluginExtension implements XjcPluginExtension {

    private String source;

    public String getSource(){
        return source;
    }

    public void setSource(String source){
        this.source = source;
    }
}
