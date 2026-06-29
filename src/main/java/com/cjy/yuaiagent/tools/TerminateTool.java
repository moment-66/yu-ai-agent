package com.cjy.yuaiagent.tools;

import org.springframework.ai.tool.annotation.Tool;

/**
 * 终止工具（作用是让自主规划智能体能够合理地中断）
 */
public class TerminateTool {

    @Tool(description = """
            当请求完成或者助手无法继续完成任务时终止交互。
            当你完成所有任务时，调用此工具结束工作。
            """)
    public String doTerminate() {
        return "任务结束";
    }
}