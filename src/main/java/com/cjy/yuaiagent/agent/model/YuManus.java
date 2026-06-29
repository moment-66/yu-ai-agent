package com.cjy.yuaiagent.agent.model;

import com.cjy.yuaiagent.advisor.MyLoggerAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;

/**
 * 鱼皮的 AI 超级智能体（拥有自主规划能力，可以直接使用）
 * 注意：此 Agent 每次请求都需新建实例（状态不共享），因此不使用 Spring 单例注入
 */
public class YuManus extends ToolCallAgent {

    public YuManus(ToolCallback[] allTools, ChatModel dashscopeChatModel) {
        super(allTools);
        this.setName("yuManus");
        String SYSTEM_PROMPT = """
                你是 YuManus，一个全能的 AI 助手，旨在解决用户提出的任何任务。
                你可以调用多种工具来高效完成复杂的请求。
                请用中文回复用户。
                """;
        this.setSystemPrompt(SYSTEM_PROMPT);
        String NEXT_STEP_PROMPT = """
                根据用户需求，主动选择最合适的工具或工具组合。
                对于复杂任务，可以分解问题，逐步使用不同的工具来解决。
                每次使用工具后，清晰解释执行结果并建议下一步行动。
                当所有任务完成时，调用 terminate 工具来结束工作。
                """;
        this.setNextStepPrompt(NEXT_STEP_PROMPT);
        this.setMaxSteps(20);
        ChatClient chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultAdvisors(new MyLoggerAdvisor())
                .build();
        this.setChatClient(chatClient);
    }
}
