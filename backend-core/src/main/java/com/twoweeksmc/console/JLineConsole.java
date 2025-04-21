package com.twoweeksmc.console;

/*
 * Copyright (c) 2024-2025 2WeeksMc team & contributors
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.twoweeksmc.console.mode.DefaultMode;
import com.twoweeksmc.console.mode.Mode;
import com.twoweeksmc.util.FileUtils;
import com.twoweeksmc.web.WebServer;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.LineReaderImpl;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedString;
import org.jline.utils.InfoCmp;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class JLineConsole {
    private final Terminal terminal;
    private final LineReaderImpl reader;
    private WebServer webServer;

    private boolean isRunning;
    private boolean isPaused;

    private Mode currentMode;

    private final HashMap<Mode, List<String>> logs;

    @SneakyThrows
    public JLineConsole(WebServer webServer) {
        this.webServer = webServer;
        this.terminal = TerminalBuilder.builder()
                .system(true)
                .encoding(StandardCharsets.UTF_8)
                .dumb(true)
                .jansi(true)
                .build();
        this.terminal.enterRawMode();
        this.reader = (LineReaderImpl) LineReaderBuilder.builder()
                .terminal(this.terminal)
                .option(LineReader.Option.DISABLE_EVENT_EXPANSION, true)
                .option(LineReader.Option.AUTO_PARAM_SLASH, false)
                .build();
        AttributedString coloredPrefix = new AttributedString(this.prefix());
        this.reader.setPrompt(coloredPrefix.toAnsi());
        this.isRunning = true;
        this.isPaused = false;
        this.currentMode = new DefaultMode(this);
        this.logs = new HashMap<>();
        this.clear();
        this.sendWelcomeMessage();
    }

    public void start() {
        while (this.isRunning) {
            try {
                AttributedString coloredPrefix = new AttributedString(this.prefix());
                String input = this.reader.readLine(coloredPrefix.toAnsi()).trim();

                if (input.isEmpty()) {
                    this.print("[FF3333]The input field can not be empty.");
                    continue;
                }

                if (this.isPaused) {
                    if (input.equalsIgnoreCase("resume")) {
                        this.isPaused = false;
                        this.print("Resumed console.");
                    }
                    continue;
                }
                String[] inputParts = input.split(" ");
                String command = inputParts[0];
                String[] args = Arrays.copyOfRange(inputParts, 1, inputParts.length);

                switch (command.toLowerCase()) {
                    case "exit" -> {
                        this.isRunning = false;
                        this.print("Exiting console...");
                    }
                    case "pause" -> {
                        this.isPaused = true;
                        this.print("Paused console.");
                    }
                    default -> this.currentMode.handleCommand(command, args);
                }
            } catch (EndOfFileException e) {
                throw new RuntimeException(e);
            }
        }
        this.print("Exited console.");
        this.webServer.stop();
        System.exit(0);
    }

    public void switchMode(String modeName) {
        switch (modeName.toLowerCase()) {
            case "default" -> this.currentMode = new DefaultMode(this);
            default -> this.print("Unknown mode: " + modeName);
        }
        this.clear();
        if (this.logs.containsKey(this.currentMode)) {
            this.logs.forEach((mode, strings) -> strings.forEach(this::print));
        }
    }

    public String prefix() {
        try {
            String hostname = InetAddress.getLocalHost().getHostName();
            String prefix = this.currentMode != null
                    ? this.currentMode.getPrefix().replace("%hostname", hostname)
                    : "NoMode";
            return ConsoleColor.apply("\r" + prefix);

        } catch (UnknownHostException e) {
            return ConsoleColor.apply("\r" + this.currentMode.getPrefix().replace("%hostname", "unknown"));
        }
    }

    public void print(String message) {
        if (this.currentMode == null) {
            throw new IllegalStateException("Current mode is not initialized");
        }
        this.print(message, true);
    }

    public void print(String message, boolean newLine) {
        String coloredMessage = ConsoleColor.apply(this.prefix() + message);
        this.logs.computeIfAbsent(this.currentMode, mode -> new ArrayList<>()).add(coloredMessage);
        if (newLine) {
            System.out.println(coloredMessage);
            return;
        }
        System.out.print(coloredMessage);
    }

    public void sendWelcomeMessage() {
        sendFiglet();
        System.out.print("\n");
        System.out.println(ConsoleColor.apply("       [00FFFF-00BFFF]2weeksmc &7- &b1.0.0&7@&bdevelopment"));
        System.out.println(ConsoleColor.apply("       &fby &bezTxmMC&7, &bDragonRex &fand contributors."));
        System.out.print("\n");
        System.out.println(ConsoleColor.apply("       &fType &bhelp &fto list all commands."));
        System.out.print("\n");
        System.out.print("\n");
    }

    public void sendFiglet() {
        System.out.println(FileUtils.fileContent("header.txt"));
    }

    public void clear() {
        this.terminal.puts(InfoCmp.Capability.clear_screen);
        this.terminal.flush();
    }
}
