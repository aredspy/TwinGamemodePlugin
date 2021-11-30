package me.sabre.twingamemode;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.GameMode;
import org.bukkit.util.Vector;

public class GamemodeListener implements Listener{

    final private double Cx;
    final private double Cy;
    final private double Cz;
    final private double Sx;
    final private double Sy;
    final private double Sz;
    final private Vector zeroVector;

    public GamemodeListener(FileConfiguration config) {
        this.Cx = config.getDouble("creativeTPoffset.x");
        this.Cy = config.getDouble("creativeTPoffset.y");
        this.Cz = config.getDouble("creativeTPoffset.z");
        this.Sx = config.getDouble("survivalTPoffset.x");
        this.Sy = config.getDouble("survivalTPoffset.y");
        this.Sz = config.getDouble("survivalTPoffset.z");
        this.zeroVector = new Vector(0, 0, 0);
    }

    @EventHandler
    public void onPlayerGameChange(PlayerGameModeChangeEvent event) {

        Player player = event.getPlayer();
        GameMode mode = event.getNewGameMode();

        //debug
        //Bukkit.broadcastMessage("Player " + player + " changed gamemode to " + mode);

        //change player save data
        swapSave(player, mode);
    }

    //swap in appropriate game save and load
    private void swapSave(Player player, GameMode mode) {

        //save data
        player.saveData();

        //get player data location
        String worldname = player.getWorld().getName();
        String uuid = player.getUniqueId().toString();
        String savefile = uuid + ".dat";
        String savefile1 = uuid + ".dat_old";

        Path save = Paths.get(worldname, "playerdata", savefile);
        Path save1 = Paths.get(worldname ,"playerdata", savefile1);

        //debug
        //System.out.println("---------------------------");
        //System.out.println(worldname);
        //System.out.println(uuid);
        //System.out.println(savefile);
        //System.out.println(save);
        //System.out.println("---------------------------");

        //location offset
        Location offset = new Location(player.getWorld(), 0, 0 ,0);

        if (mode == GameMode.CREATIVE) {

            //copy overwrite TO SURVIVAL
            Path copy = Paths.get(FileLocs.PDS, savefile);
            Path copy1 = Paths.get(FileLocs.PDS, savefile1);

            try {
                Files.copy(save, copy, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(save1, copy1, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                System.out.println("[TwinGamemode] ERROR: Failed to copy save playerdata for " + player.getName());
                e.printStackTrace();
                return;
            }

            //copy overwrite FROM CREATIVE
            Path update = Paths.get(FileLocs.PDC, savefile);
            Path update1 = Paths.get(FileLocs.PDC, savefile1);

            try {
                Files.copy(update, save, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(update1, save1, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                System.out.println("[TwinGamemode] ERROR: Failed to copy load playerdata for " + player.getName());
                e.printStackTrace();
                return;
            }


            offset = new Location(player.getWorld(), this.Cx, this.Cy, this.Cz);

        } else if (mode == GameMode.SURVIVAL) {

            //copy overwrite TO CREATIVE
            Path copy = Paths.get(FileLocs.PDC, savefile);
            Path copy1 = Paths.get(FileLocs.PDC, savefile1);

            try {
                Files.copy(save, copy, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(save1, copy1, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                System.out.println("[TwinGamemode] ERROR: Failed to copy save playerdata for " + player.getName());
                e.printStackTrace();
                return;
            }

            //copy overwrite FROM SURVIVAL
            Path update = Paths.get(FileLocs.PDS, savefile);
            Path update1 = Paths.get(FileLocs.PDS, savefile1);

            try {
                Files.copy(update, save, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(update1, save1, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                System.out.println("[TwinGamemode] ERROR: Failed to copy load playerdata for " + player.getName());
                e.printStackTrace();
                return;
            }

            offset = new Location(player.getWorld(), this.Sx, this.Sy, this.Sz);
        }

        //debug
        //System.out.println("[TwinGamemode] Successfully swapped save files!");

        //current location
        Location location = player.getLocation();
        location.add(offset);

        //load new player data
        player.loadData();

        //teleport to original location with configured offset
        player.teleport(location);
        //zero out velocity
        player.setVelocity(zeroVector);
    }
}
