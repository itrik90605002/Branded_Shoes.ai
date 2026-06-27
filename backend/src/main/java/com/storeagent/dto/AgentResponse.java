package com.storeagent.dto;

import java.util.List;
import java.util.Map;

public class AgentResponse {
    private String reply;
    private List<String> logs;
    private String intent;
    private Map<String, Object> detectedParams;

    public AgentResponse() {}

    public AgentResponse(String reply, List<String> logs, String intent, Map<String, Object> detectedParams) {
        this.reply = reply;
        this.logs = logs;
        this.intent = intent;
        this.detectedParams = detectedParams;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public List<String> getLogs() {
        return logs;
    }

    public void setLogs(List<String> logs) {
        this.logs = logs;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public Map<String, Object> getDetectedParams() {
        return detectedParams;
    }

    public void setDetectedParams(Map<String, Object> detectedParams) {
        this.detectedParams = detectedParams;
    }

    public static AgentResponseBuilder builder() {
        return new AgentResponseBuilder();
    }

    public static class AgentResponseBuilder {
        private String reply;
        private List<String> logs;
        private String intent;
        private Map<String, Object> detectedParams;

        public AgentResponseBuilder reply(String reply) {
            this.reply = reply;
            return this;
        }

        public AgentResponseBuilder logs(List<String> logs) {
            this.logs = logs;
            return this;
        }

        public AgentResponseBuilder intent(String intent) {
            this.intent = intent;
            return this;
        }

        public AgentResponseBuilder detectedParams(Map<String, Object> detectedParams) {
            this.detectedParams = detectedParams;
            return this;
        }

        public AgentResponse build() {
            return new AgentResponse(reply, logs, intent, detectedParams);
        }
    }
}
