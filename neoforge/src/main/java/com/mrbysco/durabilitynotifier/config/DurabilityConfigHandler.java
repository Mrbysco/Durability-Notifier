package com.mrbysco.durabilitynotifier.config;

import com.mrbysco.durabilitynotifier.Reference;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.config.ModConfigEvent;

public class DurabilityConfigHandler {

	@SubscribeEvent
	public static void onLoad(final ModConfigEvent.Loading configEvent) {
		Reference.LOGGER.debug("Loaded Durability Notifier's config file {}", configEvent.getConfig().getFileName());
	}

	@SubscribeEvent
	public static void onFileChange(final ModConfigEvent.Reloading configEvent) {
		Reference.LOGGER.warn("Durability Notifier's config just got changed on the file system!");
	}
}