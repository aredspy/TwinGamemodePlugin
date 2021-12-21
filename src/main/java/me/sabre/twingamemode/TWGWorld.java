package me.sabre.twingamemode;

import org.bukkit.World;

import static org.bukkit.Bukkit.getWorld;

//class to hold world config data
public class TWGWorld {

    //Getters and setters are for losers
    // -certified python genius/idiot

    public World world;
    final public double coord;
    final public double offset;
    final public boolean vertical;
    final public boolean positive;
    final public double cOffset;
    final public double sOffset;
    final public int chunkCoord;
    final public String name;

    public TWGWorld(World world, String name, double coord, double offset, boolean vert, boolean cpos) {
        this.world = world;
        this.coord = coord;
        this.offset = offset;
        this.vertical = vert;
        this.positive = cpos;
        this.name = name;

        if (this.positive) {
            this.cOffset = offset;
            this.sOffset = -offset;
        } else {
            this.cOffset = -offset;
            this.sOffset = offset;
        }

        this.chunkCoord = (int) Math.floor(coord / 16);
    }

    //update world object
    public void updateWorlds() {
        this.world = getWorld(this.name);
    }
}
