package me.sabre.twingamemode;

import org.bukkit.command.*;

import com.destroystokyo.paper.utils.PaperPluginLogger;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TWGenerateWall implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Logger logger =  PaperPluginLogger.getLogger("TWG");

        //only accept this command from console
        if (sender instanceof ConsoleCommandSender || sender instanceof RemoteConsoleCommandSender) {

            //generate wall at coordinates from config
            logger.log(Level.INFO, "[TwinGamemode: Successfully generated dividers!]");
            return true;
        }

        //else
        logger.log(Level.WARNING, "[TwinGamemode] You need to run this command from the server console!");
        return false;
    }
}
