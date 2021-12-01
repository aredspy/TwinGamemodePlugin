package me.sabre.twingamemode;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;

/*
* lazily made by sabre3
*
* */

public final class TwinGamemode extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        //init dirs
        initDirs();

        //init config
        initConfig();

        //start new player listener
        getServer().getPluginManager().registerEvents(new NewPlayerListener(), this);

        //start gamemode listener
        getServer().getPluginManager().registerEvents(new GamemodeListener(this.getConfig()), this);

        //start location listener
        getServer().getPluginManager().registerEvents(new LocationListener(this.getConfig()), this);
        
        System.out.println("[TwinGamemode] Has successfully started!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        System.out.println("[TwinGamemode] Has successfully shutdown!");
    }

    public static void initDirs() {

        //create playerdata dirs if they don't exist
        File pdC = new File(FileLocs.PDC);
        File pdS = new File(FileLocs.PDS);

        pdC.mkdirs();
        pdS.mkdirs();

    }

    public void initConfig() {
        this.saveDefaultConfig();
    }
}
