package com.bass9030.beabot;

public class Bot {
    private String botName;
    private Boolean isPower;
    private String codePath;

    public Bot(String botName, Boolean isPower, String codePath) {
        this.botName = botName;
        this.isPower = isPower;
        this.codePath = codePath;
    }

    public void setBotName(String botName) {
        this.botName = botName;
    }

    public String getBotName() {
        return botName;
    }

    public void setIsPower(boolean isPower) {
        this.isPower = isPower;
    }

    public boolean getIsPower() {
        return this.isPower;
    }

    public void setCodePath(String codePath) {
        this.codePath = codePath;
    }

    public String getCodePath() {
        return codePath;
    }
}
