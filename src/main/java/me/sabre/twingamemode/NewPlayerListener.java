package me.sabre.twingamemode;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.bukkit.entity.Player;

public class NewPlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        //if new player, copy create saves
        if (!player.hasPlayedBefore()) {

            //save player data (create save file)
            player.saveData();

            //get player data location
            String worldname = player.getWorld().getName();
            String uuid = player.getUniqueId().toString();
            String savefile = uuid + ".dat";
            String savefile1 = uuid + ".dat_old";

            Path save = Paths.get(worldname, "playerdata", savefile);
            Path save1 = Paths.get(worldname ,"playerdata", savefile1);

            //manually create _old file
            try {
                Files.copy(save, save1, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                System.out.println("[TwinGamemode] ERROR: Failed to create new save playerdata for " + player.getName());
                System.out.println(e);
            }

            //copy overwrite TO SURVIVAL and CREATIVE
            Path copyS = Paths.get(FileLocs.PDS, savefile);
            Path copyS1 = Paths.get(FileLocs.PDS, savefile1);
            Path copyC = Paths.get(FileLocs.PDC, savefile);
            Path copyC1 = Paths.get(FileLocs.PDC, savefile1);


            try {
                Files.copy(save, copyS, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(save1, copyS1, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(save, copyC, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(save1, copyC1, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                System.out.println("[TwinGamemode] ERROR: Failed to save new playerdata for " + player.getName());
                System.out.println(e);
            }
        }
    }
}
