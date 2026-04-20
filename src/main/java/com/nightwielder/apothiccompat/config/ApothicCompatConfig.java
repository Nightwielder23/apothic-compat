package com.nightwielder.apothiccompat.config;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.nightwielder.apothiccompat.ApothicCompat;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

public final class ApothicCompatConfig {
    private static final String FILE_NAME = "apothic_compat.toml";
    private static final String IMC_METHOD = "loot_category_override";
    private static final Set<String> VALID_CATEGORIES = Set.of(
            "sword", "heavy_weapon", "bow", "crossbow", "shield",
            "helmet", "chestplate", "leggings", "boots", "pickaxe",
            "shovel", "none");

    private static final String DEFAULT_CONTENTS = """
            # Apothic Compat — user-defined loot category overrides.
            #
            # Maps Minecraft items (or item tags) to Apotheosis loot categories so the
            # affinity system can roll the right gem/affix pools on modded gear that
            # Apotheosis does not categorize on its own. Entries here are sent to
            # Apotheosis via IMC at startup, alongside the mod's built-in compat
            # modules.
            #
            # Valid loot category names (Apotheosis 7.4.8):
            #   sword, heavy_weapon, bow, crossbow, shield,
            #   helmet, chestplate, leggings, boots, pickaxe, shovel, none
            #
            # Keys MUST be quoted because item and tag IDs contain a ':' separator
            # (namespace:path), which TOML does not allow in bare keys.

            # ----------------------------------------------------------------------
            # Per-item overrides.
            #   key   = full item id (namespace:path)
            #   value = loot category name from the list above
            #
            # Example:
            #   "ruins:greatsword" = "heavy_weapon"
            #   "simplyswords:greathammer" = "heavy_weapon"
            # ----------------------------------------------------------------------
            [item_overrides]

            # ----------------------------------------------------------------------
            # Per-tag overrides. Every item carrying the tag receives the category.
            #   key   = full tag id (namespace:path)
            #   value = loot category name from the list above
            #
            # Tags are resolved at IMC time, so datapack-only tags that load with a
            # world may not be visible here — prefer item_overrides for those.
            #
            # Example:
            #   "simplyswords:greathammers" = "heavy_weapon"
            # ----------------------------------------------------------------------
            [tag_overrides]
            """;

    private ApothicCompatConfig() {}

    public static void load() {
        Path path = FMLPaths.CONFIGDIR.get().resolve(FILE_NAME);
        ensureDefaultFile(path);
        try (CommentedFileConfig config = CommentedFileConfig.builder(path).sync().build()) {
            config.load();
            processItemOverrides(config);
            processTagOverrides(config);
        } catch (Exception e) {
            ApothicCompat.LOGGER.error("Failed to read {}", FILE_NAME, e);
        }
    }

    private static void ensureDefaultFile(Path path) {
        if (Files.exists(path)) return;
        try {
            Files.createDirectories(path.getParent());
            Files.writeString(path, DEFAULT_CONTENTS);
        } catch (IOException e) {
            ApothicCompat.LOGGER.error("Failed to create default {}", FILE_NAME, e);
        }
    }

    private static void processItemOverrides(CommentedFileConfig config) {
        Object raw = config.get("item_overrides");
        if (!(raw instanceof UnmodifiableConfig section)) return;
        for (UnmodifiableConfig.Entry entry : section.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (!(value instanceof String categoryName)) {
                ApothicCompat.LOGGER.warn("[item_overrides] '{}' must map to a string category, got {}", key, value);
                continue;
            }
            if (!VALID_CATEGORIES.contains(categoryName)) {
                ApothicCompat.LOGGER.warn("[item_overrides] '{}' uses unknown category '{}'", key, categoryName);
                continue;
            }
            ResourceLocation id = ResourceLocation.tryParse(key);
            if (id == null) {
                ApothicCompat.LOGGER.warn("[item_overrides] invalid item id '{}'", key);
                continue;
            }
            Item item = ForgeRegistries.ITEMS.getValue(id);
            if (item == null) {
                ApothicCompat.LOGGER.info("[item_overrides] item '{}' not present; skipping", key);
                continue;
            }
            InterModComms.sendTo("apotheosis", IMC_METHOD, () -> Map.entry(item, categoryName));
        }
    }

    private static void processTagOverrides(CommentedFileConfig config) {
        Object raw = config.get("tag_overrides");
        if (!(raw instanceof UnmodifiableConfig section)) return;
        for (UnmodifiableConfig.Entry entry : section.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (!(value instanceof String categoryName)) {
                ApothicCompat.LOGGER.warn("[tag_overrides] '{}' must map to a string category, got {}", key, value);
                continue;
            }
            if (!VALID_CATEGORIES.contains(categoryName)) {
                ApothicCompat.LOGGER.warn("[tag_overrides] '{}' uses unknown category '{}'", key, categoryName);
                continue;
            }
            ResourceLocation id = ResourceLocation.tryParse(key);
            if (id == null) {
                ApothicCompat.LOGGER.warn("[tag_overrides] invalid tag id '{}'", key);
                continue;
            }
            TagKey<Item> tagKey = TagKey.create(Registries.ITEM, id);
            ITag<Item> tag = ForgeRegistries.ITEMS.tags().getTag(tagKey);
            if (tag.isEmpty()) {
                ApothicCompat.LOGGER.info("[tag_overrides] tag '{}' empty or not yet bound; skipping", key);
                continue;
            }
            for (Item item : tag) {
                InterModComms.sendTo("apotheosis", IMC_METHOD, () -> Map.entry(item, categoryName));
            }
        }
    }
}
