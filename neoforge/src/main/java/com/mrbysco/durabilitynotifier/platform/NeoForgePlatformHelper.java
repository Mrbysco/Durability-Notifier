package com.mrbysco.durabilitynotifier.platform;

import com.mrbysco.durabilitynotifier.config.DurabilityConfig;
import com.mrbysco.durabilitynotifier.platform.services.IPlatformHelper;
import net.minecraft.ChatFormatting;

import java.util.ArrayList;
import java.util.List;

public class NeoForgePlatformHelper implements IPlatformHelper {

	@Override
	public int getPercentage() {
		return DurabilityConfig.CLIENT.Percentage.get();
	}

	@Override
	public int getSoundCooldown() {
		return DurabilityConfig.CLIENT.soundCooldown.get();
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

	@Override
	public boolean filterItems() {
		return DurabilityConfig.CLIENT.FilterItems.get();
	}

	@Override
	public List<String> getItemFilter() {
		return new ArrayList<>(DurabilityConfig.CLIENT.ItemFilter.get());
	}
}
