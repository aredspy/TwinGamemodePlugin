package me.sabre.twingamemode;

import com.destroystokyo.paper.utils.PaperPluginLogger;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;

import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LocationListener implements Listener{

    final private ArrayList<TWGWorld> worlds;

    public LocationListener(FileConfiguration config) {
        this.worlds = TwinGamemode.initWorlds(config);
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event) {

        Player player = event.getPlayer();
        GameMode mode = player.getGameMode();
        World world = player.getWorld();
        Location location = player.getLocation();
        GameMode gamemode = player.getGameMode();

        //ignore for operators
        if (player.isOp()) {
            return;
        }

        //ignore if not in config worlds
        for(TWGWorld w : this.worlds) {

            //null check
            while (w.world == null) {
                w.updateWorld();
            }

            if (w.world.equals(world)) {

                //check if location is valid
                double crossCoord;

                if (w.vertical) {
                    crossCoord = location.getX();
                }else {
                    crossCoord = location.getZ();
                }

                //location offset
                double offsetRaw = 0;
                Location offset;

                //inverted for EXPECTED change to gamemode
                if (mode == GameMode.SURVIVAL) {
                    offsetRaw = w.cOffset;
                } else if (mode == GameMode.CREATIVE) {
                    offsetRaw = w.sOffset;
                }

                //double offset to account for gap plus small buffer
                offsetRaw = offsetRaw * 2;

                if (w.vertical) {
                    offset = new Location(w.world, offsetRaw, 0 ,0);
                }else {
                    offset = new Location(w.world, 0, 0 ,offsetRaw);
                }

                //creative is on positive
                if(crossCoord < (w.coord + w.cOffset) && crossCoord > (w.coord + w.sOffset)) {
                    location = safeBump(location.add(offset));
                    if (location == null) {
                        return;
                    }

                    if (gamemode == GameMode.CREATIVE) {
                        swapSave(player, w.world, GameMode.SURVIVAL, location);
                        player.setGameMode(GameMode.SURVIVAL);
                        player.sendMessage("You have entered the survival side!");
                    } else if (gamemode == GameMode.SURVIVAL) {
                        swapSave(player, w.world, GameMode.CREATIVE, location);
                        player.setGameMode(GameMode.CREATIVE);
                        player.sendMessage("You have entered the creative side!");
                    }
                }

                //to skip rest of world checks
                break;
            }
        }

    }

    private void swapSave(Player player, World world, GameMode mode, Location location) {

        //logger
        Logger logger = PaperPluginLogger.getLogger("TWG");

        //save data
        player.saveData();

        //get player data location
        String worldname = world.getName();
        String uuid = player.getUniqueId().toString();
        String savefile = uuid + ".dat";
        String savefile1 = uuid + ".dat_old";

        Path save = Paths.get(worldname, "playerdata", savefile);
        Path save1 = Paths.get(worldname ,"playerdata", savefile1);

        if (mode == GameMode.CREATIVE) {

            //copy overwrite TO SURVIVAL
            Path copy = Paths.get(FileLocs.PDS, savefile);
            Path copy1 = Paths.get(FileLocs.PDS, savefile1);

            try {
                Files.copy(save, copy, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(save1, copy1, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "ERROR: Failed to copy save playerdata for " + player.getName());
                logger.throwing("LocationListener", "swapSave()", e);
                return;
            }

            //copy overwrite FROM CREATIVE
            Path update = Paths.get(FileLocs.PDC, savefile);
            Path update1 = Paths.get(FileLocs.PDC, savefile1);

            try {
                Files.copy(update, save, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(update1, save1, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "ERROR: Failed to copy save playerdata for " + player.getName());
                logger.throwing("LocationListener", "swapSave()", e);
                return;
            }

        } else if (mode == GameMode.SURVIVAL) {

            //copy overwrite TO CREATIVE
            Path copy = Paths.get(FileLocs.PDC, savefile);
            Path copy1 = Paths.get(FileLocs.PDC, savefile1);

            try {
                Files.copy(save, copy, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(save1, copy1, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "ERROR: Failed to copy save playerdata for " + player.getName());
                logger.throwing("LocationListener", "swapSave()", e);
                return;
            }

            //copy overwrite FROM SURVIVAL
            Path update = Paths.get(FileLocs.PDS, savefile);
            Path update1 = Paths.get(FileLocs.PDS, savefile1);

            try {
                Files.copy(update, save, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(update1, save1, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "ERROR: Failed to copy save playerdata for " + player.getName());
                logger.throwing("LocationListener", "swapSave()", e);
                return;
            }

        }

        //load new player data
        player.loadData();
        //teleport to original location with configured offset
        player.teleport(location);
        //zero out velocity
        player.setVelocity(new Vector(0,0,0));
    }

    //bump players down to the nearest vertical block
    private static Location bumpDown(Location location) {
        for (double y = location.getY(); y > 0; y--) {
            if (location.subtract(0, 1, 0).getBlock().getType() == Material.AIR)
                continue;
            break;
        }

        return location.add(0, 1, 0);
    }

    //new safe bumper accounts for adjacent blocks and does a "cocktail shake" to find best block
    //include the offset location when calling
    private static Location safeBump(Location location) {

        boolean check = !location.getBlock().isPassable() && location.getBlock().getRelative(0, 1, 0).isPassable() && location.getBlock().getRelative(0, 2, 0).isPassable();
        boolean lowerHit = false;
        boolean upperHit = false;
        Location lower = location.clone();
        Location upper = location.clone();
        //Logger logger = PaperPluginLogger.getLogger("TwinGamemode");

        //logger.log(Level.SEVERE, "Blocks: " + location.getBlock().getType() + location.getBlock().getRelative(0,1,0).getType() + location.getBlock().getRelative(0,2,0).getType());

        while(!check) {

            if(lowerHit && upperHit)
                return null;

            double diffup = upper.getY() - location.getY();

            if (!lowerHit)
                lower.subtract(0,1,0);
            if (!upperHit)
                upper.add(0,1,0);

            //logger.log(Level.WARNING, "low xyz: " + lower.getX() + " " + lower.getY() + " " + lower.getZ());
            //logger.log(Level.WARNING, "up xyz: " + upper.getX() + " " + upper.getY() + " " + upper.getZ());

            if (lower.getY() == location.getWorld().getMinHeight()) {
                lowerHit = true;
            }

            if (diffup > 10 || upper.getY() == location.getWorld().getMaxHeight()) {
                upperHit = true;
            }

            //logger.log(Level.SEVERE, "BLOCK: " + location.getBlock());
            //logger.log(Level.SEVERE, "BLOCK: " + lower.getBlock());
            //logger.log(Level.SEVERE, "BLOCK: " + upper.getBlock());

            boolean check1 = !lower.getBlock().isPassable() && lower.getBlock().getRelative(0, 1, 0).isPassable() && lower.getBlock().getRelative(0, 2, 0).isPassable();
            boolean check2 = !upper.getBlock().isPassable() && upper.getBlock().getRelative(0, 1, 0).isPassable() && upper.getBlock().getRelative(0, 2, 0).isPassable();

            if (check1)
                location = lower;
            if (check2)
                location = upper;

            check = check1 || check2;
            //logger.log(Level.WARNING, "Check_lower: " + check1 + " Check_upper: " + check2);
        }

        return location.add(0,1,0);
    }

}
