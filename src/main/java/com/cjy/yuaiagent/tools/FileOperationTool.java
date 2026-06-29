package com.cjy.yuaiagent.tools;

import cn.hutool.core.io.FileUtil;
import com.cjy.yuaiagent.constant.FileConstant;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

/**
 * 文件操作工具类（提供文件读写功能）
 */
public class FileOperationTool {

    private final String FILE_DIR = FileConstant.FILE_SAVE_DIR + "/file";

    /**
     * 校验文件名是否安全（防止路径遍历攻击）
     */
    private void validateFileName(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("文件名不能为空");
        }
        if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
            throw new IllegalArgumentException("非法的文件名：" + fileName);
        }
    }

    @Tool(description = "从文件读取内容")
    public String readFile(@ToolParam(description = "要读取的文件名") String fileName) {
        try {
            validateFileName(fileName);
            String filePath = FILE_DIR + "/" + fileName;
            return FileUtil.readUtf8String(filePath);
        } catch (Exception e) {
            return "Error reading file: " + e.getMessage();
        }
    }

    @Tool(description = "写入内容到文件")
    public String writeFile(@ToolParam(description = "要写入的文件名") String fileName,
                            @ToolParam(description = "要写入的内容") String content
    ) {
        try {
            validateFileName(fileName);
            String filePath = FILE_DIR + "/" + fileName;
            // 创建目录
            FileUtil.mkdir(FILE_DIR);
            FileUtil.writeUtf8String(content, filePath);
            return "File written successfully to: " + filePath;
        } catch (Exception e) {
            return "Error writing to file: " + e.getMessage();
        }
    }
}