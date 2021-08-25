package me.opkarol.opeconomy.redeem;

import me.opkarol.opeconomy.Economy;

import java.io.*;
import java.util.HashMap;

public class DatabaseLoader extends Database {

    public static void onStart(){
        try {
            setMap(readFile());
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void onDisable(){
        try {
            saveFile(getMap());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final String PATH = Economy.getEconomy().getDataFolder().getPath() + "/redeemDatabase.db";

    private static void saveFile(HashMap<String, Redeem> map)
            throws IOException
    {
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(PATH))) {
            os.writeObject(map);
        }
    }

    private static HashMap<String, Redeem> readFile()
            throws ClassNotFoundException, IOException
    {
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(PATH))) {
            return (HashMap<String, Redeem>) is.readObject();
        }
    }
}
