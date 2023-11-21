package com.cbfacademy.apiassessment.OpenAI;

import com.cbfacademy.apiassessment.Identifier;

import java.util.List;

public class ChatGPTResponse implements Identifier {

    private String id;
    private String object;
    private long created;
    private String model;
    private List<Choice> choices;

    private Usage usage;

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public Usage getUsage() {
        return usage;
    }

    public void setUsage(Usage usage) {
        this.usage = usage;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}

class Usage {
    private int promptTokens;
    private int completionTokens;

    private int totalToken;

    public int getPromptTokens() {
        return promptTokens;
    }

    public void setPromptTokens(int promptTokens) {
        this.promptTokens = promptTokens;
    }

    public int getCompletionTokens() {
        return completionTokens;
    }

    public void setCompletionTokens(int completionTokens) {
        this.completionTokens = completionTokens;
    }

    public int getTotalToken() {
        return totalToken;
    }

    public void setTotalToken(int totalToken) {
        this.totalToken = totalToken;
    }
}