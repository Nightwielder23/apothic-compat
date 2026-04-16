# Apothic Compat

A lightweight server side Minecraft 1.20.1 Forge mod that adds Apotheosis loot category compatibility for popular weapon mods that are missing it.

## What This Mod Does

Apotheosis assigns loot categories to items to determine what affixes and gem sockets they can receive. Many popular weapon mods are missing these category assignments, meaning their weapons cannot receive Apotheosis affixes or gems. This mod injects the correct categories at runtime.

## Supported Mods

All compat modules are soft dependencies. Each module only activates if both Apotheosis and the target mod are present.

**Samurai Dynasty** Katanas, kama, and sai are registered as swords and spears as heavy weapons.

**Dread Steel** The Dreadsteel Scythe is registered as a heavy weapon. The Dreadsteel Shield is registered as a shield.

**Tetra** Fixes Tetra bows and crossbows being miscategorized as light weapons. Fixes weapons upgraded with Planar Stabilizers being miscategorized as pickaxes.

**Weapons of Miracles** All weapons are assigned the correct category based on their item type.

**Cataclysm Weaponry** All weapons are assigned the correct category based on their item type.

## Requirements

Minecraft 1.20.1, Forge 47.x, Apotheosis 7.x. All other mods are optional soft dependencies.

## Installation

Place the jar in your mods folder. The mod activates only for mods that are present. No configuration needed.

## Server Side Only

This mod only needs to be installed on the server.

## License

MIT, Copyright 2026 Nightwielder23. https://github.com/Nightwielder23/apothic-compat/blob/main/LICENSE

## Source Code

https://github.com/Nightwielder23/apothic-compat

## Author

Made by Nightwielder23: https://github.com/Nightwielder23