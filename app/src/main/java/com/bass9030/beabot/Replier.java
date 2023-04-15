package com.bass9030.beabot;

import android.app.Notification;

public class Replier {
    private String packageName;
    private String room;
    private Notification.Action session;
    private boolean isDebugChat;
    private messageSender msgSender;

    public Replier(String packageName, String room, Notification.Action session, boolean isDebugChat) {
        this.packageName = packageName;
        this.room = room;
        this.session = session;
        this.isDebugChat = isDebugChat;
        this.msgSender = new messageSender();
    }

    public boolean reply(String msg) {
        return msgSender.send(this.session, msg, this.isDebugChat);
    }

    public boolean reply(String room, String msg) {
        return msgSender.send(room, this.packageName, msg, this.isDebugChat);
    }

    public boolean reply(String room, String msg, String packageName) {
        return msgSender.send(room, packageName, msg, this.isDebugChat);
    }

    public boolean replyDelayed(String msg, long ms) {
        return this.replyDelayed(this.room, msg, ms);
    }

    public boolean replyDelayed(String room, String msg, long ms) {
        try {
            Thread.sleep(ms);
            return msgSender.send(room, msg, this.isDebugChat);
        }catch(InterruptedException ex){ return false; }
    }

    public boolean markAsRead() {
        return this.markAsRead(this.room, this.packageName);
    }

    public boolean markAsRead(String room) {
        return msgSender.markAsRead(room, this.isDebugChat);
    }

    public boolean markAsRead(String room, String packageName) {
        return msgSender.markAsRead(room, packageName, this.isDebugChat);
    }
}

