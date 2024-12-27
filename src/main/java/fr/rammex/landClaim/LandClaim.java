package fr.rammex.landClaim;

import fr.rammex.landClaim.commands.LandCommand;
import fr.rammex.landClaim.data.DataManager;
import fr.rammex.landClaim.listener.lands.BlockBreakListener;
import fr.rammex.landClaim.listener.lands.BlockPlaceListener;
import fr.rammex.landClaim.listener.player.LandListener;
import lombok.Getter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class LandClaim extends JavaPlugin {

    public static LandClaim instance;

    private final Map<String, DataManager> databases = new HashMap<>();

    @Getter
    private FileConfiguration messagesConf;
    private File file;

    public static LandClaim getInstance() {
        return instance;
    }




    @Override
    public void onEnable() {
        instance = this;
        loadFiles();
        saveDefaultConfig();

        this.getServer().getPluginManager().registerEvents(new LandListener(), this);

        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);

        this.getCommand("land").setExecutor(new LandCommand());

        initializeDatabases();
    }

    @Override
    public void onDisable() {
    }

    private void initializeDatabases() {

        initializeDatabase("lands");
    }

    public void initializeDatabase(String databaseName) {
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


    private void loadFiles() {
        loadFile("messages", null);
    }

    private void loadFile(String fileName, String folder) {
        File file;
        if (folder == null) {
            file = new File(getDataFolder(), fileName + ".yml");
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                saveResource(fileName + ".yml", false);
            }
        } else {
            file = new File(getDataFolder() + "/" + folder, fileName + ".yml");
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                saveResource(folder + "/" + fileName + ".yml", false);
            }
        }

        FileConfiguration fileConf = new YamlConfiguration();
        try {
            fileConf.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        switch (fileName) {
            case "messages":
                messagesConf = fileConf;
                break;
        }
    }

}
