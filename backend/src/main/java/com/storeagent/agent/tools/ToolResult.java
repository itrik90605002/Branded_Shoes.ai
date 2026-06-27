package com.storeagent.agent.tools;

public class ToolResult {
    private boolean success;
    private String output;
    private Object data;

    public ToolResult() {}

    public ToolResult(boolean success, String output, Object data) {
        this.success = success;
        this.output = output;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    // Custom Builder Pattern to support ToolResult.builder() style
    public static ToolResultBuilder builder() {
        return new ToolResultBuilder();
    }

    public static class ToolResultBuilder {
        private boolean success;
        private String output;
        private Object data;

        public ToolResultBuilder success(boolean success) {
            this.success = success;
            return this;
        }

        public ToolResultBuilder output(String output) {
            this.output = output;
            return this;
        }

        public ToolResultBuilder data(Object data) {
            this.data = data;
            return this;
        }

        public ToolResult build() {
            return new ToolResult(success, output, data);
        }
    }
}
