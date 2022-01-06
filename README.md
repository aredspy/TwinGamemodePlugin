# TwinGamemode

### Play survival and creative in the same Minecraft world
Download from the releases section on the right

## Description

TwinGamemode is a Spigot plugin that allows players to play in creative and survival on a single world by keeping separate save files for each and generating a massive single block insivible barrier wall in each configured world.
The plugin now uses its own builtin listener to check for player locations and updates the gamemode after players traverse past the configured divider.

## Recommended config

In order for the plugin to generate the divider on every chunk, you must enable the server to use TwinGamemode as the world generator. This is currently because 
there is no way to add a block populator fast enough before the first few chunks generate which will leave a very large hole.

TwinGamemode will only generate the wall and leave everything else to the vanilla generators so you won't miss anything.

To do this add the following to your bukkit.yml:

```YAML
worlds:
 world:
  generator: TwinGamemode
```

Add only the worlds you also have listed in the config.yml

Example config:

```YAML
#list of worlds to apply divider and game mode changers
worlds:
  world:
      divider:
        #options: vertical (east-west) | horizontal (north south)
        type: vertical
        #options: any coordinate in double form
        coord: 0.0
        #options: positive (east or south) | negative (west or north)
        creative: positive
        #offset when player changes game mode
        offset: 2.0
```

### WorldGuard Note:
If using worldguard or a similar plugin which changes game data by entering or leaving a defined region, it is recommended you set a buffer region 
that matches your desired offset size in the config.yml

The barrier block divider is pretty good at keeping the sides seperated and preventing players from transferring items to the other side. However, 
you may want to use some worldguard flags to prevents exploiting as the plugin will only do a location check with the offset.

## Extra

### Revision
- Version: 1.2
- Spigot-API: 1.18 (latest)
- Tested on: Spigot 1.18 (latest)

### Changelog

**Version 1.2**
- Added new world divider generator
- Greatly updated config with support for any world
- Increased overall performance
- Potion effects now strip when switching sides
- Added some new debug warning messages
- Added default gamemode enforcement for worlds not in config file (useful for end)

**Version 1.1.1**
- Added new internal location listener
- Updated Y axis bumper to teleport to the closest block
- Added new config file options for divider

**Version 1.1**
- Cleaned up namespace
- Added pseudo Y axis bumper

### Planned Features for the Future
- Ability to disable builtin location listener
- Automatic backups for save files

### Build
Clone repository and build with your favorite Java IDE 
(or use the command line if you're still a nerd who uses Java). 
Make sure to use maven to pull the latest libraries from Spigot. 
Suggested IDE is IntelliJ IDEA because it already has a nice plugin
for Minecraft Server plugin development.
