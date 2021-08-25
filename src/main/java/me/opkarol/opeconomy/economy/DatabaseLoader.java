package me.opkarol.opeconomy.economy;

import me.opkarol.opeconomy.Economy;

import java.io.*;
import java.util.HashMap;
import java.util.UUID;

public class DatabaseLoader extends Database {

    public static void onStart(){
        try {
            setMoneyMap(readFile());
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void onDisable(){
        try {
            saveFile(getMoneyMap());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final String PATH = Economy.getEconomy().getDataFolder().getPath() + "/moneyDatabase.db";

    private static void saveFile(HashMap<UUID, Integer> map)
            throws IOException
    {
        try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(PATH))) {
            os.writeObject(map);
        }
    }

    private static HashMap<UUID, Integer> readFile()
            throws ClassNotFoundException, IOException
    {
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(PATH))) {
            return (HashMap<UUID, Integer>) is.readObject();
        }
    }
}
