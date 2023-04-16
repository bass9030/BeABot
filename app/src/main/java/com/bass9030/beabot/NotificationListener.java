package com.bass9030.beabot;

import android.app.Notification;
import android.app.Person;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationListener extends NotificationListenerService {
    // <room, HashMap<packageName, readAction>>
    private static final HashMap<String, HashMap<String, Notification.Action>> readSessions = new HashMap<>();

    // <room, replyAction>
    private static final HashMap<String, Notification.Action> replyRooms = new HashMap<>();

    // <room, HashMap<packageName, replyAction>>
    private static final HashMap<String, HashMap<String, Notification.Action>> replySessions = new HashMap<>();


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("NotificationListener", "알림 접근 시작");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("NotificationListener", "알림 접근 정지");
    }

//    private Pair<Bitmap, Bitmap> loadLargeIcon(StatusBarNotification paramStatusBarNotification) {
//        Bitmap bitmap;
//        bitmap = (paramStatusBarNotification.getNotification()).;
//        Parcelable[] arrayOfParcelable = (paramStatusBarNotification.getNotification()).extras.getParcelableArray("android.messages");
//        return (arrayOfParcelable.length > 0) ? new Pair(icon2Bitmap(paramContext, ((Person)((Bundle)arrayOfParcelable[0]).getParcelable("sender_person")).getIcon()), bitmap) : new Pair(bitmap, null);
//    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        String packageName = sbn.getPackageName();
        if(!packageName.startsWith("com.kakao.tal")) return;
        Notification notification = sbn.getNotification();
        Bundle extras = notification.extras;
        Notification.Action[] actions = notification.actions;
        if(actions == null) return;
        String sender = extras.getString(Notification.EXTRA_TITLE);
        CharSequence msg = extras.getCharSequence(Notification.EXTRA_TEXT);
        CharSequence room = extras.getCharSequence(Notification.EXTRA_SUB_TEXT);
        if(room == null) room = extras.getString(Notification.EXTRA_SUMMARY_TEXT);
        boolean isGroupChat = room != null;
        if(room == null) room = sender;
        boolean isMultiChat = sbn.getUser().hashCode() != 0;
        Icon icon = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P)
            icon = ((Person)((Bundle)extras.getParcelableArray(Notification.EXTRA_MESSAGES)[0]).get("sender_person")).getIcon();
        Bitmap profileImage = null;
        if(icon != null)
            profileImage = ((BitmapDrawable)icon.loadDrawable(MainActivity.getContext())).getBitmap();
        else
            profileImage = notification.largeIcon;
//        Bitmap bitmap = (Bitmap) extras.get(Notification.EXTRA_LARGE_ICON_BIG);

//        Log.d("isBitmapIsNull", bitmap != null ? "true" : "false");

        Notification.Action session = null;

        for(Notification.Action action : actions) {
            String title = action.title.toString().toLowerCase();
            if(title.contains("읽음") || title.contains("mark as read"))
                Utils.setMarkAsRead(room.toString(), packageName, action);
            else if(title.contains("답장") || title.contains("reply")) {
                Utils.setSession(room.toString(), packageName, action);
                session = action;
            }
        }

//        readSessions.put(room.toString(), readHashMap);
//        replySessions.put(room.toString(), replyHashMap);

//        Log.d("PushLog", "onNotificationPosted ~ " +
//                " packageName: " + sbn.getPackageName() +
//                " postTime: " + sbn.getPostTime() +
//                " room: " + room +
//                " sender: " + sender +
//                " msg : " + msg);
        ImageDB imageDB = new ImageDB(
                profileImage,
                null
        );
        Replier replier = new Replier(
                packageName,
                room.toString(),
                session,
                false
        );
//        runTestCode(room.toString(), msg.toString().trim(), sender, isGroupChat, replier, imageDB, packageName, isMultiChat);
    }

    private void runTestCode(String room, String msg, String sender, boolean isGroupChat,
                             Replier replier, ImageDB imageDB, String packageName, boolean isMultiChat) {
//        Log.d("room", !room.equals("베스봇") ? "true" : "false");
//        Log.d("room", !room.equals("bass9030") ? "true" : "false");
        Log.d("msg", msg);
        if(!room.equals("bass9030") || !room.equals("베스봇")) return;
        try {
            if(msg.equals("!프로필사진")) {
                replier.reply(imageDB.getProfileHash());
            }
            if(msg.contains("사진")) {
                replier.reply(imageDB.getImage());
            }
        }catch(Exception ex) {
            replier.reply(ex.toString());
        }
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {

        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.NO_WRAP);
    }

    public static class Utils {

        public static void setMarkAsRead(String packageName, String room, Notification.Action readAction) {
            if(readSessions.get(room) == null)
                readSessions.put(room, new HashMap<String, Notification.Action>());
            readSessions.get(room).put(packageName, readAction);
        }

        public static void setSession(String room, String packageName, Notification.Action replyAction) {
            if(replySessions.get(room) == null)
                replySessions.put(room, new HashMap<String, Notification.Action>());
            replySessions.get(room).put(packageName, replyAction);
            replyRooms.put(room, replyAction);
        }

        public static HashMap<String, Notification.Action> getMarkAsReadInAllPackage(String room) {
            return readSessions.get(room);
        }

        public static Notification.Action getMarkAsRead(String room, String packageName) {
            // packageName, Action[]
            HashMap<String, Notification.Action> hashmap = readSessions.get(room);
            return hashmap != null ? hashmap.get(packageName) : null;
        }

        public static List<String> getRooms(@Nullable String packageName) {
            if(packageName == null) {
                return new ArrayList<String>(replySessions.keySet());
            }else{
                List<String> list = new ArrayList<>();
                for(Map.Entry<String, HashMap<String, Notification.Action>> entry : replySessions.entrySet()) {
                    if(entry.getValue().get(packageName) != null) list.add(entry.getKey());
                }
                return list;
            }
        }

        public static Notification.Action getSession(String room, String packageName) {
            HashMap<String, Notification.Action> hashmap = replySessions.get(room);
            if(hashmap != null) return hashmap.get(packageName);
            return null;
        }

        public static Notification.Action getSessionByRoom(String room) {
            return replyRooms.get(room);
        }
    }
}