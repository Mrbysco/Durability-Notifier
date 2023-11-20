package com.mrbysco.durabilitynotifier.platform;

import com.mrbysco.durabilitynotifier.Reference;
import com.mrbysco.durabilitynotifier.config.DurabilityConfig;
import com.mrbysco.durabilitynotifier.platform.services.IPlatformHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

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

	@Override
	public SoundEvent getChosenSound() {
		ResourceLocation soundLocation = ResourceLocation.tryParse(DurabilityConfig.CLIENT.soundlocation.get());
		if (soundLocation != null) {
			SoundEvent sound = BuiltInRegistries.SOUND_EVENT.get(soundLocation);
			if (sound != null) {
				return sound;
			} else {
				Reference.LOGGER.warn("Could not locate the following sound: " + soundLocation + ". Perhaps you misspelled it. Falling back to default!");
				return SoundEvents.NOTE_BLOCK_PLING.value();
			}
		}
		return null;
	}
}
