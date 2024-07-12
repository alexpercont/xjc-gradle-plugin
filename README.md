# io.github.alexpercont.plugins.xjc

![Sonar](https://github.com/alexpercont/xjc-gradle-plugin/actions/workflows/sonar.yml/badge.svg?branch=main)

Yet another Gradle plugin for generating Java code from XSD definition.

## Usage

To use the XJC Gradle plugin, you need to apply it in you

```groovy
plugins {
    id 'io.github.alexpercont.plugins.xjc' version '1.1'
}

xjc {
    schema = 'src/main/resources/schema.xsd'
}
```

Once adding the plugin, execute task `xjc` to generate Java classes.

Adding this plugin to your project will automatically add the necessary dependencies to your project, in addition to 
Java plugin.

## Configuration

The plugin provides the following configuration options:

```groovy
xjc {
    // The schema file to generate Java classes from. Mandatory field. Value given is the default.
    schema = 'src/main/resources/schema.xsd'
    
    // The directories or files to be used as binding files for XJC customization
    // Optional. No bindings are applied if not set.
    bindingPaths = file('src/main/resources/binding.xjb')
}
```
