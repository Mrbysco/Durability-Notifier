package com.Mrbysco.durabilitynotifier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.Mrbysco.durabilitynotifier.config.DurabilityConfigGen;
import com.Mrbysco.durabilitynotifier.handler.DurabilityHandler;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MOD_ID, 
	name = Reference.MOD_NAME, 
	version = Reference.VERSION, 
	acceptedMinecraftVersions = Reference.ACCEPTED_VERSIONS)

public class DurabilityNotifier {
	
	@Mod.Instance(Reference.MOD_ID)
	public static DurabilityNotifier instance;

	public static final Logger logger = LogManager.getLogger(Reference.MOD_ID);

	@Mod.EventHandler
	public void preinit(FMLPreInitializationEvent event)
	{
		logger.debug("Generating/loading the Durability Notifier config");
		MinecraftForge.EVENT_BUS.register(new DurabilityConfigGen());
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		logger.debug("Generating/loading the Durability handler");
		MinecraftForge.EVENT_BUS.register(new DurabilityHandler());
	}

	@Mod.EventHandler
	public void postinit(FMLPostInitializationEvent event)
	{
		
	}
}