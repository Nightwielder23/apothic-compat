package com.nightwielder.apothiccompat.compat;

import dev.shadowsoffire.apotheosis.adventure.loot.LootCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShieldItem;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

/**
 * Spartan Shields adds only shields; scan the namespace and accept anything
 * that extends {@link ShieldItem}. Non-shield items (if any are added in a
 * future update) fall through untouched.
 */
public final class SpartanShieldsCompat {
    private static final String NAMESPACE = "spartanshields";
    private static final String IMC_METHOD = "loot_category_override";

    private SpartanShieldsCompat() {}

    public static void send() {
        for (ResourceLocation id : ForgeRegistries.ITEMS.getKeys()) {
            if (!NAMESPACE.equals(id.getNamespace())) continue;
            Item item = ForgeRegistries.ITEMS.getValue(id);
            if (!(item instanceof ShieldItem)) continue;
            String name = LootCategory.SHIELD.getName();
            InterModComms.sendTo("apotheosis", IMC_METHOD, () -> Map.entry(item, name));
        }
    }
}
