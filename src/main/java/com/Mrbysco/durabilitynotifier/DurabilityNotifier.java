package com.mrbysco.durabilitynotifier;

import com.mrbysco.durabilitynotifier.config.DurabilityConfigGen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("durabilitynotifier")
public class DurabilityNotifier {
	
    public static final Logger LOGGER = LogManager.getLogger();
	
	public DurabilityNotifier() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, DurabilityConfigGen.clientSpec);
        FMLJavaModLoadingContext.get().getModEventBus().register(DurabilityConfigGen.class);

        MinecraftForge.EVENT_BUS.register(new EventHandler());

		//Make sure the mod being absent on the other network side does not cause the client to display the server as incompatible
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(
				() -> "Trans Rights Are Human Rights",
				(remoteVersionString,networkBool) -> networkBool
		));
    }
}