package com.cjy.yuaiagent.demo.invoke;

public interface TestApiKey {

    // 从环境变量读取 API Key，本地开发可在 IDE 运行配置中设置
    // 生产环境通过 Render / Docker 环境变量注入
    String API_KEY = System.getenv("DASHSCOPE_API_KEY") != null
            ? System.getenv("DASHSCOPE_API_KEY")
            : "";
}
