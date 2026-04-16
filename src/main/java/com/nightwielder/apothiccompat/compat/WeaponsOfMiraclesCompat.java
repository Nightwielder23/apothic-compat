package com.nightwielder.apothiccompat.compat;

import dev.shadowsoffire.apotheosis.adventure.loot.LootCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

public final class WeaponsOfMiraclesCompat {
    private static final String NAMESPACE = "weaponsofmiracles";
    private static final String IMC_METHOD = "loot_category_override";

    private WeaponsOfMiraclesCompat() {}

    public static void send() {
        for (ResourceLocation id : ForgeRegistries.ITEMS.getKeys()) {
            if (!NAMESPACE.equals(id.getNamespace())) continue;
            Item item = ForgeRegistries.ITEMS.getValue(id);
            if (item == null) continue;
            LootCategory cat = categorize(item);
            if (cat == null) continue;
            String name = cat.getName();
            InterModComms.sendTo("apotheosis", IMC_METHOD, () -> Map.entry(item, name));
        }
    }

    private static LootCategory categorize(Item item) {
        if (item instanceof SwordItem) return LootCategory.SWORD;
        if (item instanceof AxeItem) return LootCategory.HEAVY_WEAPON;
        if (item instanceof BowItem) return LootCategory.BOW;
        if (item instanceof CrossbowItem) return LootCategory.CROSSBOW;
        return null;
    }
}
