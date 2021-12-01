# TwinGamemode

### Play survival and creative in the same Minecraft world
Download from the releases section on the right

## Description

TwinGamemode is a Spigot plugin that allows players to play in creative and survival on a single world by keeping separate save files for each.
The plugin now uses its own builtin listener to check for player locations and updates the gamemode after players traverse past the configured divider

## Recommended worldguard config

If using worldguard or a similar plugin which changes game data by entering or leaving a defined region, it is recommended you set a buffer region 
as well as a teleport offset (if required due to lag) in the config.yml

To prevent players from easily transferring creative items to the survival side of a server, create a small buffer region (16 blocks wide, infinite length) 
and disable appropriate flags (building, pvp, damage, ender pearl, accessing chests, etc.)

Example config:

```YAML
Divider:
  type: vertical #a divider is created from east-west
  coord: 127.0 #On Z coordinate 127
  creative: positive #Creative side will be east

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

## Extra

### Revision
- Version: 1.1.1
- Spigot-API: 1.18 (latest)
- Tested on: Spigot 1.18 (latest)

### Changelog

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
