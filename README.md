# Movecraft-HitPoints

Movecraft-HitPoints is an addon plugin for the Movecraft plugin, designed to enhance the ship-building experience by adding hit points to your craft. It allows you to create ships that not only look impressive but also have a realistic durability aspect.

## Features

- Hit points calculation: Hit points are calculated based on the craft's size, configurable multipliers, and the presence of specific modifier blocks.
- Damage calculation: Damage inflicted on a craft is calculated based on TNT explosions and configurable multipliers.
- Visual indicators: The plugin provides visual indicators in the form of a boss bar, changing color based on the craft's hit points status.
- Torpedo damage: Configurable multiplier for the damage caused by torpedo explosions.
- Customizable hit point and damage modifiers: You can configure hit point and damage multipliers for specific craft types.

## Dependencies

- Movecraft: [https://github.com/APDevTeam/Movecraft](https://github.com/APDevTeam/Movecraft)

## Configuration

The plugin offers global settings and per-craft settings that can be adjusted in the configuration file (`config.yml`). Here are the key configuration options:

**Global Settings:**

- BaseHitPointMultiplier: Coefficient for hit points based on craft size.
- BaseDamageMultiplier: Coefficient for multiplying damage done by TNT.
- DamageThreshold: Fraction of hit points at which the boss bar turns yellow.
- CriticalThreshold: Fraction of hit points at which the craft takes physical damage and the boss bar turns red.
- TorpedoDamageMultiplier: Multiplier for the damage caused by torpedo explosions.
- BlockDamageMultiplier: A list of blocks and their respective values that increase damage per hit by TNT.
- HitPointModifierBlocks: A list of blocks and their respective values that additively increase hit points.
- IgnoreBlockProtection: A list of blocks that you don't want to be protected by the HP system. Useful if you want certain blocks like armor or engines to get destroyed. 

**Per-Craft Settings:**

- IsHitPointCraft: Indicates if this craft type uses the hit point system.
- CraftHitPointModifier: Custom hit point modifier for the craft.
- CraftDamageModifier: Custom damage modifier for the craft.

## Permissions

- `movecrafthitpoints.check`: Allows players to use the `/hitpoint check` command.
- `movecrafthitpoints.remove`: Allows players to use the `/hitpoint remove` command.
- `movecrafthitpoints.reload`: Allows players to use the `/hitpoint reload` command.

## Usage

1. Install the Movecraft plugin and ensure it is functioning correctly.
2. Place the Movecraft-HitPoints plugin in the plugins folder of your server.
3. Start or reload the server to generate the default configuration file.
4. Adjust the configuration settings in the `config.yml` file to customize the hit points and damage calculation.
5. Use the appropriate permissions to allow players to access the `/hitpoint` commands.
6. Players can now use the `/hitpoint` commands to check hit points, remove hit points, or reload the plugin.

Enjoy building and battling with your hit point enhanced ships!

## Contributing

If you encounter any issues or have suggestions for improvement, please feel free to open an issue or submit a pull request on the plugin's GitHub repository.

We appreciate your contributions to make Movecraft-HitPoints even better!

## License

Movecraft-HitPoints is released under the MIT License. See [LICENSE](LICENSE) for details.

Make sure to review the license before using or contributing to the project.
