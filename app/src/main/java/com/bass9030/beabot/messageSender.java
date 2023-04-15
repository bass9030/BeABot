package com.bass9030.beabot;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class messageSender {

    public boolean send(Notification.Action replyAction, String message, boolean isDebugChat) {
        if(isDebugChat) {
            //TODO: 디버그채팅 구현후 연결
        }
        Intent sendIntent = new Intent();
        Bundle msg = new Bundle();
        for(RemoteInput inputable: replyAction.getRemoteInputs()) msg.putCharSequence(inputable.getResultKey(), message);
        RemoteInput.addResultsToIntent(replyAction.getRemoteInputs(), sendIntent, msg);
        try {
            replyAction.actionIntent.send(MainActivity.getContext(), 0, sendIntent, null, null);
            return true;
        }catch(PendingIntent.CanceledException e) { return false; }
    }

    public boolean send(String room, String packageName, String message, boolean isDebugChat) {
        if(isDebugChat) {
            //TODO: 디버그 채팅 구현후 연결
        }
        Notification.Action session = NotificationListener.Utils.getSession(room, packageName);
        if(session != null)
            return send(session, message, isDebugChat);
        return false;
    }

    public boolean send(String room, String message, boolean isDebugChat) {
        if(isDebugChat) {
            //TODO: 디버그 채팅 구현후 연결
        }
        Notification.Action session = NotificationListener.Utils.getSessionByRoom(room);
        if(session != null)
            return send(session, message, isDebugChat);
        return false;
    }

    public boolean markAsRead(String room, String packageName, boolean isDebugChat) {
        if(isDebugChat) {
            Toast.makeText(MainActivity.getContext(), "디버그룸에서 채팅 읽음 처리를 요청했습니다.", Toast.LENGTH_LONG)
                    .show();
            return true;
        }
        Notification.Action action = NotificationListener.Utils.getMarkAsRead(room, packageName);
        if(action != null) {
            return sendMarkAsRead(action, isDebugChat);
        }
        return false;
    }

    public boolean markAsRead(String room, boolean isDebugChat) {
        if(isDebugChat) {
            Toast.makeText(MainActivity.getContext(), "디버그룸에서 채팅 읽음 처리를 요청했습니다.", Toast.LENGTH_LONG)
                    .show();
            return true;
        }
        HashMap<String, Notification.Action> action = NotificationListener.Utils.getMarkAsReadInAllPackage(room);
        boolean success = true;
        for(Map.Entry<String, Notification.Action> entry : action.entrySet()) {
            boolean result = sendMarkAsRead(entry.getValue(), isDebugChat);
            if(!result) success = false;
        }
        return success;
    }

    private boolean sendMarkAsRead(Notification.Action action, boolean isDebugChat) {
        if(isDebugChat) {
            Toast.makeText(MainActivity.getContext(), "디버그룸에서 채팅 읽음 처리를 요청했습니다.", Toast.LENGTH_LONG)
                    .show();
            return true;
        }
        try {
            action.actionIntent.send(MainActivity.getContext(), 1, null);
            return true;
        }catch(PendingIntent.CanceledException ex) {
            return false;
        }
    }
}
