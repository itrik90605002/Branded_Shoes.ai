package com.storeagent.agent.tools;

import java.util.Map;

public interface Tool {
    String getName();
    String getDescription();
    ToolResult execute(Map<String, Object> parameters);
}
