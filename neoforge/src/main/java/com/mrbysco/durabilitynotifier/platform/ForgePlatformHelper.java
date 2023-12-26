package com.mrbysco.durabilitynotifier.platform;

import com.mrbysco.durabilitynotifier.config.DurabilityConfig;
import com.mrbysco.durabilitynotifier.platform.services.IPlatformHelper;
import net.minecraft.ChatFormatting;

public class ForgePlatformHelper implements IPlatformHelper {

	@Override
	public int getPercentage() {
		return DurabilityConfig.CLIENT.Percentage.get();
	}

	@Override
	public boolean getSendMessage() {
		return DurabilityConfig.CLIENT.SendMessage.get();
	}

	@Override
	public ChatFormatting getMessageColor() {
		return DurabilityConfig.CLIENT.SentMessageColor.get();
	}

	@Override
	public boolean getPlaySound() {
		return DurabilityConfig.CLIENT.PlaySound.get();
	}

	@Override
	public float getSoundVolume() {
		return DurabilityConfig.CLIENT.volume.get().floatValue();
	}

	@Override
	public String getSoundLocation() {
		return DurabilityConfig.CLIENT.soundlocation.get();
	}
}
