package com.mrbysco.durabilitynotifier.platform;

import com.mrbysco.durabilitynotifier.DurabilityNotifier;
import com.mrbysco.durabilitynotifier.config.DurabilityConfig;
import com.mrbysco.durabilitynotifier.platform.services.IPlatformHelper;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.ChatFormatting;

public class FabricPlatformHelper implements IPlatformHelper {

	@Override
	public int getPercentage() {
		if (DurabilityNotifier.config == null)
			DurabilityNotifier.config = AutoConfig.getConfigHolder(DurabilityConfig.class).getConfig();
		return DurabilityNotifier.config.general.percentage;
	}

	@Override
	public boolean getSendMessage() {
		if (DurabilityNotifier.config == null)
			DurabilityNotifier.config = AutoConfig.getConfigHolder(DurabilityConfig.class).getConfig();
		return DurabilityNotifier.config.message.sendMessage;
	}

	@Override
	public ChatFormatting getMessageColor() {
		if (DurabilityNotifier.config == null)
			DurabilityNotifier.config = AutoConfig.getConfigHolder(DurabilityConfig.class).getConfig();
		return ChatFormatting.getByName(DurabilityNotifier.config.message.messageColor);
	}

	@Override
	public boolean getPlaySound() {
		if (DurabilityNotifier.config == null)
			DurabilityNotifier.config = AutoConfig.getConfigHolder(DurabilityConfig.class).getConfig();
		return DurabilityNotifier.config.sound.playSound;
	}

	@Override
	public float getSoundVolume() {
		if (DurabilityNotifier.config == null)
			DurabilityNotifier.config = AutoConfig.getConfigHolder(DurabilityConfig.class).getConfig();
		return (float) DurabilityNotifier.config.sound.volume;
	}

	@Override
	public String getSoundLocation() {
		if (DurabilityNotifier.config == null)
			DurabilityNotifier.config = AutoConfig.getConfigHolder(DurabilityConfig.class).getConfig();
		return DurabilityNotifier.config.sound.soundLocation;
	}
}
