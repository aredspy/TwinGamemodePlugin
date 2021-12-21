package me.sabre.twingamemode;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.util.Arrays;
import java.util.List;

public class DividerGenerator extends ChunkGenerator {

    final private FileConfiguration config;

    public DividerGenerator(FileConfiguration config) {
        this.config = config;
    }

    //ChunkGenerator overrides
    //AKA more useless boiler plate
    @Override
    public boolean shouldGenerateNoise(){
        return true;
    }

    @Override
    public boolean shouldGenerateSurface() {
        return true;
    }

    @Override
    public boolean shouldGenerateBedrock() {
        return true;
    }

    @Override
    public boolean shouldGenerateCaves() {
        return true;
    }

    @Override
    public boolean shouldGenerateDecorations() {
        return true;
    }

    @Override
    public boolean shouldGenerateMobs() {
        return true;
    }

    @Override
    public boolean shouldGenerateStructures() {
        return true;
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        return Arrays.asList((BlockPopulator)new DividerPopulator(this.config));
    }

}
