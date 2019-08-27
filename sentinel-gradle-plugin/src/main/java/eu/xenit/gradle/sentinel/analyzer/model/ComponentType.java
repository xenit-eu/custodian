package eu.xenit.gradle.sentinel.analyzer.model;

public enum ComponentType {

    MODULE("module"),
    LIBRARY("library"),
    PROJECT("project"),
    UNDEFINED("undefined");

    private final String type;
    ComponentType(String type)
    {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
