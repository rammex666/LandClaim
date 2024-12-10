package fr.rammex.landClaim;

import fr.rammex.landClaim.commands.LandCommand;
import fr.rammex.landClaim.data.DataManager;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class LandClaim extends JavaPlugin {

    public static LandClaim instance;
    private File messageFile;

    @Getter
    public FileConfiguration messageConfig;

    private final Map<String, DataManager> databases = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.getCommand("land").setExecutor(new LandCommand());

        initializeDatabases();
        saveMessageConfig();
    }

    @Override
    public void onDisable() {
    }

    private void initializeDatabases() {

        initializeDatabase("lands");
    }

    private void initializeDatabase(String databaseName) {
        DataManager db = new DataManager(databaseName, this.getDataFolder());
        db.load();
        databases.put(databaseName, db);
    }

    public Map<String, DataManager> getDatabases() {
        return databases;
    }


    public DataManager getDatabase(String databaseName) {
        return getDatabases().get(databaseName);
    }

    private void saveMessageConfig() {
        File file = new File(getDataFolder(), "messages.yml");
        if (!file.exists()) {
            saveResource("messages.yml", false);
        }
    }

    public String getMessage(String path){
        if (messageConfig.getString(path) !=null){
            return messageConfig.getString(path);
        } else {
            return "Message not found";
        }
    }
}
