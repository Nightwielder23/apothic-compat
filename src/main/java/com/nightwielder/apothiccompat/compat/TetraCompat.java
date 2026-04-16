package com.nightwielder.apothiccompat.compat;

import com.google.common.collect.Multimap;
import dev.shadowsoffire.apotheosis.adventure.loot.LootCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

public final class TetraCompat {
    private static final String NAMESPACE = "tetra";
    private static final String IMC_METHOD = "loot_category_override";
    private static final double HEAVY_WEAPON_THRESHOLD = 8.0;

    private TetraCompat() {}

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
        if (item instanceof BowItem) return LootCategory.BOW;
        if (item instanceof CrossbowItem) return LootCategory.CROSSBOW;
        if (item instanceof SwordItem) {
            return getAttackDamage(item) > HEAVY_WEAPON_THRESHOLD ? LootCategory.HEAVY_WEAPON : LootCategory.SWORD;
        }
        return null;
    }

    private static double getAttackDamage(Item item) {
        Multimap<Attribute, AttributeModifier> mods = item.getDefaultAttributeModifiers(EquipmentSlot.MAINHAND);
        for (AttributeModifier m : mods.get(Attributes.ATTACK_DAMAGE)) {
            if (m.getOperation() == AttributeModifier.Operation.ADDITION) return m.getAmount();
        }
        return 0;
    }
}
