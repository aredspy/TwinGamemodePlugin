package me.sabre.twingamemode;

//class to hold world config data
public class TWGWorld {

    //Getters and setters are for losers
    // -certified python genius/idiot

    final public double coord;
    final public double offset;
    final public boolean vertical;
    final public boolean positive;
    final public double cOffset;
    final public double sOffset;
    final public int chunkCoord;
    final public String name;
    final public String savename;

    public TWGWorld(String name, double coord, double offset, boolean vert, boolean cpos) {
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

        if(name.endsWith("_nether")){
            this.savename = name.replaceAll("_nether", "");
        }else if (name.endsWith("_the_end")) {
            this.savename = name.replaceAll("_the_end", "");
        } else {
            this.savename = name;
        }

        this.chunkCoord = (int) Math.floor(coord / 16);
    }
}
