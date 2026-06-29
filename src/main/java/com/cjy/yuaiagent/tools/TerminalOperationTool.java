package com.cjy.yuaiagent.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;

/**
 * 终端操作工具（仅允许执行安全的白名单命令）
 */
public class TerminalOperationTool {

    /**
     * 命令白名单 —— 只允许执行这些安全的命令
     */
    private static final Set<String> ALLOWED_COMMANDS = Set.of(
            "dir", "ls", "echo", "type", "cat", "find", "where",
            "date", "time", "whoami", "hostname", "ipconfig", "ping"
    );

    /**
     * 校验命令是否在白名单中
     */
    private boolean isCommandAllowed(String command) {
        String trimmed = command.trim().toLowerCase();
        // 提取命令名（去掉参数和路径）
        String commandName = trimmed.split("\\s+")[0];
        // 处理带路径的命令，例如 C:\Windows\System32\ping.exe → ping
        if (commandName.contains("\\") || commandName.contains("/")) {
            commandName = commandName.substring(commandName.lastIndexOf('\\') + 1);
            commandName = commandName.substring(commandName.lastIndexOf('/') + 1);
        }
        // 去掉 .exe 后缀
        if (commandName.endsWith(".exe")) {
            commandName = commandName.substring(0, commandName.length() - 4);
        }
        return ALLOWED_COMMANDS.contains(commandName);
    }

    @Tool(description = "在终端执行命令（仅限安全的系统命令：dir, ls, echo, type, cat, find, where, date, time, whoami, hostname, ipconfig, ping）")
    public String executeTerminalCommand(@ToolParam(description = "要执行的命令") String command) {
        if (!isCommandAllowed(command)) {
            return "错误：不允许执行此命令 —— 仅允许执行白名单中的安全命令。当前命令: " + command;
        }
        StringBuilder output = new StringBuilder();
        try {
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
//            Process process = Runtime.getRuntime().exec(command);
            Process process = builder.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                output.append("Command execution failed with exit code: ").append(exitCode);
            }
        } catch (IOException | InterruptedException e) {
            output.append("Error executing command: ").append(e.getMessage());
        }
        return output.toString();
    }
}