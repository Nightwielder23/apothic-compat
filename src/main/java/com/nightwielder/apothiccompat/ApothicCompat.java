package com.nightwielder.apothiccompat;

import com.mojang.logging.LogUtils;
import com.nightwielder.apothiccompat.compat.CataclysmWeaponryCompat;
import com.nightwielder.apothiccompat.compat.DreadSteelCompat;
import com.nightwielder.apothiccompat.compat.SamuraiDynastyCompat;
import com.nightwielder.apothiccompat.compat.TetraCompat;
import com.nightwielder.apothiccompat.compat.UniversalCompat;
import com.nightwielder.apothiccompat.compat.WeaponsOfMiraclesCompat;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(ApothicCompat.MODID)
public class ApothicCompat {
    public static final String MODID = "apothic_compat";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ApothicCompat(FMLJavaModLoadingContext context) {
        IEventBus modBus = context.getModEventBus();
        modBus.addListener(this::sendCategoryOverrides);
    }

    private void sendCategoryOverrides(InterModEnqueueEvent event) {
        if (!ModList.get().isLoaded("apotheosis")) {
            LOGGER.info("Apotheosis not present; skipping all compat modules.");
            return;
        }
        if (ModList.get().isLoaded("samurai_dynasty")) SamuraiDynastyCompat.send();
        if (ModList.get().isLoaded("dreadsteel")) DreadSteelCompat.send();
        if (ModList.get().isLoaded("tetra")) TetraCompat.send();
        if (ModList.get().isLoaded("weaponsofmiracles")) WeaponsOfMiraclesCompat.send();
        if (ModList.get().isLoaded("cataclysmweaponry")) CataclysmWeaponryCompat.send();
        UniversalCompat.send();
    }
}
