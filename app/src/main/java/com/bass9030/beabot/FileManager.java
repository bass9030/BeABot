package com.bass9030.beabot;

import android.os.Environment;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileManager {
    private final static String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
    private final static String workingDirectory = Paths.get(sdcard, "BeABot").toString();

    public static ArrayList<Bot> getAllBots() {
        ArrayList<Bot> bots = new ArrayList<>();
        File folders = Paths.get(workingDirectory, "Bots").toFile();
        try {
            for(File file : folders.listFiles()) {
                if(file.isDirectory()) {
                    String config = Paths.get(file.getAbsolutePath(), "bot.json").toString();
                    try {
                        JSONObject obj = (JSONObject)new JSONParser().parse(readString(config));
                        String botName = obj.get("name").toString();
                        boolean botPower = (boolean)obj.get("isPower");
                        String codePath = Paths.get(file.getAbsolutePath(), botName + ".js").toString();
                        Bot bot = new Bot(botName, botPower, codePath);
                        bots.add(bot);
                    }catch(Exception ignored) { }
                }
            }
        }catch(NullPointerException ignored) { }

        return bots;
    }

    public static Bot getBot(String name) {
        if(!validateFileName(name)) return null;
        String path = Paths.get(workingDirectory, "Bots", name).toString();
        try {
            String config = Paths.get(path, "bot.json").toString();
            JSONObject obj = (JSONObject) new JSONParser().parse(readString(config));
            String botName = obj.get("name").toString();
            boolean botPower = (boolean) obj.get("isPower");
            String codePath = Paths.get(path, botName + ".js").toString();

            return new Bot(botName, botPower, codePath);
        }catch(NullPointerException ignored) { } catch (JSONException | ParseException e) {
            return null;
        }
        return null;
    }

    public static boolean addBot(Bot bot) {
        if(validateFileName(bot.getBotName()))
            return false;

        File folder = Paths.get(workingDirectory, "Bots", bot.getBotName()).toFile();
        if(!folder.mkdirs())
            return false;
        File configFile = Paths.get(workingDirectory, "Bots", "bot.json").toFile();
        File scriptFile = Paths.get(workingDirectory, "Bots", bot.getBotName() + ".js").toFile();
        try {
            JSONObject obj = new JSONObject();
            obj.put("name", bot.getBotName());
            obj.put("isPower", bot.getIsPower());
            writeString(configFile.getAbsolutePath(), obj.toString());
            scriptFile.createNewFile();
            return true;
        }catch(IOException | JSONException ignored) {
            return false;
        }
    }

    public static boolean removeBot(String name) {
        if(validateFileName(name))
            return false;

        File folder = Paths.get(workingDirectory, "Bots", name).toFile();
        if(!folder.exists()) return false;
        return folder.delete();
    }

    public static boolean validateFileName(String fileName) {
        return fileName.matches("^[^.\\\\/:*?\"<>|]?[^\\\\/:*?\"<>|]*")
                && getValidFileName(fileName).length()>0;
    }

    private static String getValidFileName(String fileName) {
        String newFileName = fileName.replace("^\\.+", "").replaceAll("[\\\\/:*?\"<>|]", "");
        if(newFileName.length()==0)
            throw new IllegalStateException(
                    "File Name " + fileName + " results in a empty fileName!");
        return newFileName;
    }

    public static class addBotResult {
        public boolean success;
        public String reason;
        public addBotResult(boolean success, @Nullable String reason) {
            this.success = success;
            this.reason = reason;
        }
    }

    private static boolean writeString(String path, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(content);
            return true;
        } catch (IOException ignored) { return false; }
    }

    private static String readString(String path) {
        try(BufferedReader br = new BufferedReader(new FileReader(path))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            return sb.toString();
        } catch (IOException e) {
            return "";
        }
    }
}
