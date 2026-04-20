# Apothic Compat

A lightweight server side Minecraft 1.20.1 Forge mod that adds Apotheosis loot category compatibility for popular weapon mods that are missing it. Uses the official Apotheosis IMC override API for maximum compatibility.

## What This Mod Does

Apotheosis assigns loot categories to items to determine what affixes and gem sockets they can receive. Many popular weapon mods are missing these assignments or have incorrect ones, meaning their weapons cannot receive Apotheosis affixes or gems. This mod injects the correct categories at load time using Apotheosis's own IMC override system.

## Supported Mods

All compat modules are soft dependencies. Each module only activates if both Apotheosis and the target mod are present.

**Samurai Dynasty** Katanas registered as swords, spears as heavy weapons, kama and sai as swords.

**Dread Steel** The Dreadsteel Scythe registered as a heavy weapon. The Dreadsteel Shield registered as a shield.

**Tetra** Fixes Tetra bows and crossbows being miscategorized. Fixes weapons upgraded with Planar Stabilizers being miscategorized as pickaxes. Categories assigned by item class and attack damage.

**Weapons of Miracles** All weapons registered with correct categories. Note: this mod uses the mod ID wom internally.

**L'Ender's Cataclysm** All weapons, armor, and tools registered with correct categories by explicit item ID including swords, heavy weapons, shields, bows, and full armor sets.

**Cataclysm Weaponry** All weapons registered with correct categories based on item type.

**Universal Fallback** Covers any other mod not listed above. Any item that Apotheosis has not already categorized is assigned the correct category based on its Java class type. Covers swords, axes, bows, crossbows, tridents, pickaxes, shovels, and all armor slots.

## Config File

A config file is generated at `config/apothic_compat.toml` on first launch. You can add custom item and tag overrides here without needing to write any scripts.

```toml
[item_overrides]
"ruins:greatsword" = "heavy_weapon"

[tag_overrides]
"simplyswords:greathammers" = "heavy_weapon"
```

Valid category names: `sword`, `heavy_weapon`, `bow`, `crossbow`, `shield`, `helmet`, `chestplate`, `leggings`, `boots`, `pickaxe`, `shovel`, `none`

## Reload Command

After editing the config, run `/apothiccompat reload` or `/ac reload` (op level 2) to apply changes without restarting. The command will skip if the config has not been modified.

## Requirements

Minecraft 1.20.1, Forge 47.x, Apotheosis 7.4.x. All other mods are optional soft dependencies.

## Installation

Place the jar in your mods folder. No configuration needed. Each compat module only activates if the corresponding mod is present.

## Server Side Only

This mod only needs to be installed on the server.

## License

MIT, Copyright 2026 Nightwielder23. https://github.com/Nightwielder23/apothic-compat/blob/main/LICENSE

## Source Code

https://github.com/Nightwielder23/apothic-compat

## Author

Made by Nightwielder23: https://github.com/Nightwielder23