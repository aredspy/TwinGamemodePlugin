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
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LocationListener2 implements Listener{

    private class TWGWorld {

        final public World world;
        final public double coord;
        final public double offset;
        final public boolean vertical;
        final public boolean positive;
        final public double cOffset;
        final public double sOffset;

        public TWGWorld(World world, double coord, double offset, boolean vert, boolean cpos) {
            this.world = world;
            this.coord = coord;
            this.offset = offset;
            this.vertical = vert;
            this.positive = cpos;

            if (this.positive) {
                this.cOffset = offset;
                this.sOffset = -offset;
            } else {
                this.cOffset = -offset;
                this.sOffset = offset;
            }
        }
    }

    final private ArrayList<TWGWorld> worlds;

    public LocationListener2(FileConfiguration config) {

        Logger logger =  PaperPluginLogger.getLogger("TWG");

        //init worlds
        worlds = new ArrayList<>();
        List<Map<?,?>> worlds = config.getMapList("worlds");

        try {

            for (Map<?, ?> map : worlds) {
                Map<String, ?> world = (Map<String, ?>) map.get("level");
                String worldname = (String) world.get("name");

                Map<String, ?> divider = (Map<String, ?>) world.get("divider");
                double coord = (double) divider.get("coord");
                double offset = (double) divider.get("offset");
                boolean vert = divider.get("type").equals("vertical");
                boolean cpos = divider.get("creative").equals("positive");

                World worldObj = Bukkit.getWorld(worldname);

                TWGWorld worldW = new TWGWorld(worldObj, coord, offset, vert, cpos);
                this.worlds.add(worldW);
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "[TwinGamemode] Failed to load config!");
            logger.throwing("LocationListener2", "LocationListener2()", e);
        }

    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event) {

        Player player = event.getPlayer();
        World world = player.getWorld();
        Location location = player.getLocation();
        GameMode gamemode = player.getGameMode();

        //ignore for operators
        if (player.isOp()) {
            return;
        }

        //ignore if not in config worlds
        for(TWGWorld w : this.worlds) {
            if (w.world.equals(world)) {

                //check position n stuff

                //check if location is in creative zone
                double crossCoord;

                if (w.vertical) {
                    crossCoord = location.getX();
                }else {
                    crossCoord = location.getZ();
                }

                if (w.positive) {
                    if(crossCoord > w.coord) {
                        if(gamemode != GameMode.CREATIVE) {
                            player.setGameMode(GameMode.CREATIVE);
                            //swap save
                            swapSave(player, w, gamemode);
                        }
                    } else {
                        if (gamemode != GameMode.SURVIVAL) {
                            player.setGameMode(GameMode.SURVIVAL);
                            //swap save
                            swapSave(player, w, gamemode);
                        }
                    }
                }else {
                    if(crossCoord < w.coord) {
                        if(gamemode != GameMode.CREATIVE) {
                            player.setGameMode(GameMode.CREATIVE);
                            //swap save
                            swapSave(player, w, gamemode);
                        }
                    } else {
                        if (gamemode != GameMode.SURVIVAL) {
                            player.setGameMode(GameMode.SURVIVAL);
                            //swap save
                            swapSave(player, w, gamemode);
                        }
                    }
                }

                //to skip rest of world checks
                break;
            }
        }

    }

    private void swapSave(Player player, TWGWorld world, GameMode mode) {

        //logger
        Logger logger = PaperPluginLogger.getLogger("TWG");

        //save data
        player.saveData();

        //get player data location
        World worldO = world.world;
        String worldname = worldO.getName();
        String uuid = player.getUniqueId().toString();
        String savefile = uuid + ".dat";
        String savefile1 = uuid + ".dat_old";

        Path save = Paths.get(worldname, "playerdata", savefile);
        Path save1 = Paths.get(worldname ,"playerdata", savefile1);

        //location offset
        double offsetRaw = 0;
        Location offset;

        if (mode == GameMode.CREATIVE) {
            offsetRaw = world.cOffset;
        } else if (mode == GameMode.SURVIVAL) {
            offsetRaw = world.sOffset;
        }

        if (world.vertical) {
            offset = new Location(worldO, offsetRaw, 0 ,0);
        }else {
            offset = new Location(worldO, 0, 0 ,offsetRaw);
        }

        if (mode == GameMode.CREATIVE) {

            //copy overwrite TO SURVIVAL
            Path copy = Paths.get(FileLocs.PDS, savefile);
            Path copy1 = Paths.get(FileLocs.PDS, savefile1);

            try {
                Files.copy(save, copy, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(save1, copy1, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "[TwinGamemode] ERROR: Failed to copy save playerdata for " + player.getName());
                logger.throwing("LocationListener2", "swapSave()", e);
                return;
            }

            //copy overwrite FROM CREATIVE
            Path update = Paths.get(FileLocs.PDC, savefile);
            Path update1 = Paths.get(FileLocs.PDC, savefile1);

            try {
                Files.copy(update, save, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(update1, save1, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "[TwinGamemode] ERROR: Failed to copy save playerdata for " + player.getName());
                logger.throwing("LocationListener2", "swapSave()", e);
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
                logger.log(Level.SEVERE, "[TwinGamemode] ERROR: Failed to copy save playerdata for " + player.getName());
                logger.throwing("LocationListener2", "swapSave()", e);
                return;
            }

            //copy overwrite FROM SURVIVAL
            Path update = Paths.get(FileLocs.PDS, savefile);
            Path update1 = Paths.get(FileLocs.PDS, savefile1);

            try {
                Files.copy(update, save, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(update1, save1, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "[TwinGamemode] ERROR: Failed to copy save playerdata for " + player.getName());
                logger.throwing("LocationListener2", "swapSave()", e);
                return;
            }

        }

        //current location + run down bumper
        Location location = bumpDown(player.getLocation());
        location.add(offset);

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
}
