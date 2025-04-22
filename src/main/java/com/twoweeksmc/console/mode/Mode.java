package com.twoweeksmc.console.mode;

public abstract class Mode {

    public abstract String getName();

    public abstract String getPrefix();

    public abstract void handleCommand(String command, String[] args);
}
