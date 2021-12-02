package me.sabre.twingamemode;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.Location;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;

import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.entity.Player;
import org.bukkit.GameMode;

public class LocationListener implements Listener{

    final private boolean vertical;
    final private double coord;
    final private boolean positive;

    //init values from config file
    public LocationListener(FileConfiguration config) {

        this.vertical = config.getString("Divider.type").equals("vertical");
        this.coord = config.getDouble("Divider.coord");
        this.positive = config.getString("Divider.creative").equals("positive");

    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event) {

        Player player = event.getPlayer();
        Location location = player.getLocation();
        GameMode gamemode = player.getGameMode();

        //ignore for operators
        if (player.isOp()) {
            return;
        }

        //check if location is in creative zone
        double crossCoord;

        if (this.vertical) {
            crossCoord = location.getX();
        }else {
            crossCoord = location.getZ();
        }

        if (this.positive) {
            if(crossCoord > this.coord) {
                if(gamemode != GameMode.CREATIVE) {
                    player.setGameMode(GameMode.CREATIVE);
                }
            } else {
                if (gamemode != GameMode.SURVIVAL) {
                    player.setGameMode(GameMode.SURVIVAL);
                }
            }
        }else {
            if(crossCoord < this.coord) {
                if(gamemode != GameMode.CREATIVE) {
                    player.setGameMode(GameMode.CREATIVE);
                }
            } else {
                if (gamemode != GameMode.SURVIVAL) {
                    player.setGameMode(GameMode.SURVIVAL);
                }
            }
        }

    }

}
