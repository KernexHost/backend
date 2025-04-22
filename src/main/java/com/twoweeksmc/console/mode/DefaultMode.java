package com.twoweeksmc.console.mode;

import com.twoweeksmc.console.ConsoleColor;
import com.twoweeksmc.console.JLineConsole;

public class DefaultMode extends Mode {
    private final JLineConsole console;

    public DefaultMode(JLineConsole console) {
        this.console = console;
    }

    @Override
    public String getName() {
        return "default";
    }

    @Override
    public String getPrefix() {
        return ConsoleColor.apply("[00FFFF-00BFFF]2weeksmc&7@&b%hostname &7» ");
    }

    @Override
    public void handleCommand(String command, String[] args) {
        switch (command.toLowerCase()) {
            case "help" -> {
                this.console.print("&7-------------------------------&bHelp&7-------------------------------");
                this.console.print(" &bexit &7- &fShutdown the cloud");
                this.console.print(" &bhelp &7- &fShow this help menu.");
                this.console.print("&7-------------------------------&bHelp&7-------------------------------");
            }
            case "clear" -> {
                this.console.clear();
            }
            default ->
                    this.console.print("[FF3333]The command &b" + command + " [FF3333]can not be executed by the console.");
        }
    }
}