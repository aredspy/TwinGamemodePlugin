package me.sabre.twingamemode;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;

import java.util.ArrayList;

public class WorldInitListener implements Listener {

    final private ArrayList<TWGWorld> worlds;
    final private FileConfiguration config;

    public WorldInitListener(FileConfiguration config) {
        this.worlds = TwinGamemode.initWorlds(config);
        this.config = config;
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onWorldInit(WorldInitEvent event) {
        //add divider populator
        World world = event.getWorld();

        for(TWGWorld w : this.worlds) {

            if (w.name.equals(world.getName())) {
                world.getPopulators().add(new DividerPopulator(this.config));
                break;
            }

        }
    }
}
