package com.mrbysco.durabilitynotifier;

import com.mrbysco.durabilitynotifier.config.DurabilityConfigGen;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.IExtensionPoint.DisplayTest;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
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
		ModLoadingContext.get().registerExtensionPoint(DisplayTest.class,()->
				new IExtensionPoint.DisplayTest(() -> "Trans Rights Are Human Rights",
						(remoteVersionString,networkBool) -> networkBool));
    }
}