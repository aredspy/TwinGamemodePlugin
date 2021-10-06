# TwinGamemode

### Play survival and creative in the same Minecraft world

## Description

TwinGamemode is a Spigot plugin that allows players to play in creative and survival on a single world by keeping separate save files for each.
The plugin itself does not handle gamemode changes and instead only listens for when a player gamemode changes. This allows it to be easily used
with other plugins such as worldguard regions which automagically changes gamemode based on location. You can also just use simple command blocks
and the plugin will work just fine.

## Recommended worldguard config

If using worldguard or a similar plugin which changes gamemodes by entering or leaving a defined region, it is recommended you set a buffer region 
as well as a teleport offset in the config.yml

To prevent players from easily transferring creative items to the survival side of a server, create a small buffer region (16 blocks wide, infinite length) 
and disable appropriate flags (building, pvp, damage, ender pearl, accessing chests, etc.)

Another thing you must do is add at least a 1 block offset to the teleport config. Worldguard in particular is very picky with player locations, and when this
plugin detects a gamemode change, worldguard may potentially desync causing the save file to fail in updating and contaminate the opposite gamemode save file 
(survival will be become creative, players may get stuck in a death loop). Adding an offset will prevent worldguard from getting confused and will simply make 
players "hop" forward whenever they cross a region border.

To determine the offset, just use the F3 stats to check the coordinates of the region and determine the direction away from it.

Example:

Region border = -50x, 75y, 100z

Walking 1 block away from the region results in -51x, 75y, 100z

Therefore, the x offset is -1

The configuration may look like (assuming this is a creative region):

```YAML
#offset when player changes to creative (ie entering the creative region)
creativeTPoffset:
  x: 1.0
  y: 0.0
  z: 0.0
#offset when player changes to survival (ie leaving the creative region)
survivalTPoffset:
  x: -1.0
  y: 0.0
  z: 0.0
```
