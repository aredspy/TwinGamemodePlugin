package me.sabre.twingamemode;
import com.destroystokyo.paper.utils.PaperPluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.print.Paper;
import java.io.File;
import java.util.logging.Level;

/*
* lazily made by sabre3
*
* the amount of boilerplate and useless overhead in java is insane lmao
* */

public final class TwinGamemode extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        //init dirs
        initDirs();

        //init config
        initConfig();

        //init logger
        PaperPluginLogger.getLogger("TWG");
        PaperPluginLogger.getLogger("TWG").setLevel(Level.FINEST);

        //register command
        this.getCommand("twg_generate").setExecutor(new TWGenerateWall());

        //start new player listener
        getServer().getPluginManager().registerEvents(new NewPlayerListener(), this);

        //start gamemode listener
        //getServer().getPluginManager().registerEvents(new GamemodeListener(this.getConfig()), this);

        //start location listener
        getServer().getPluginManager().registerEvents(new LocationListener2(this.getConfig()), this);
        
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
