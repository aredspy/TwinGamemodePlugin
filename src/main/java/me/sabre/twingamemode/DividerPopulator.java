package me.sabre.twingamemode;

import com.destroystokyo.paper.utils.PaperPluginLogger;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DividerPopulator extends BlockPopulator {

    final private ArrayList<TWGWorld> worlds;

    public DividerPopulator(FileConfiguration config) {
        this.worlds = TwinGamemode.initWorlds(config);
    }

    @Override
    public void populate(@NotNull WorldInfo worldInfo, @NotNull Random random, int x, int z, @NotNull LimitedRegion chunk) {
        //logger
        //Logger logger = PaperPluginLogger.getLogger("TWG");

        //check if applies to world
        for (TWGWorld w : this.worlds) {

            if (w.name.equals(worldInfo.getName())) {
                int coord;
                if(w.vertical) {
                    coord = x;
                } else {
                    coord = z;
                }

                if (coord == w.chunkCoord) {
                    //generate divider
                    //logger.log(Level.WARNING, "Generating wall at x,z:" + x + ","+ z + " for " + w.name);
                    generateWall(worldInfo, chunk, x, z, w.coord, w.vertical);

                }
                break;
            }
        }
    }

    public void generateWall(WorldInfo worldInfo, LimitedRegion chunk, int x, int z, double coordD, boolean vert) {

        int coord = (int) coordD;//(int) Math.abs(Math.floor((coordD % 16)));
        int relMin;

        //debug
        //Logger logger = PaperPluginLogger.getLogger("TWG");
        //logger.log(Level.WARNING, "min height: " + worldInfo.getMinHeight());

        //replace air with barrier
        if (vert) {

            relMin = z * 16;

            for (int i = worldInfo.getMinHeight(); i < worldInfo.getMaxHeight(); i++) {
                for (int j = relMin; j < relMin + 16; j++) {
                    BlockData block = chunk.getBlockData(coord, i, j);

                    if (block.getMaterial() != Material.BEDROCK) {
                        chunk.setType(coord, i, j, Material.BARRIER);
                    }
                }
            }
        } else {

            relMin = x * 16;

            for (int i = worldInfo.getMinHeight(); i < worldInfo.getMaxHeight(); i++) {
                for (int j = relMin; j < relMin + 16; j++) {
                    BlockData block = chunk.getBlockData(j, i, coord);

                    if (block.getMaterial() != Material.BEDROCK) {
                        chunk.setType(j, i, coord, Material.BARRIER);
                    }
                }
            }
        }

    }

}
