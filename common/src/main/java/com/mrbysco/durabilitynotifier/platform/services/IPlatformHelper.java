package com.mrbysco.durabilitynotifier.platform.services;

import net.minecraft.ChatFormatting;
import net.minecraft.sounds.SoundEvent;

public interface IPlatformHelper {

	/**
	 * Get the percentage config value
	 */
	int getPercentage();

	/**
	 * Get the sendMessage config value
	 */
	boolean getSendMessage();

	/**
	 * Get the messageColor config value
	 */
	ChatFormatting getMessageColor();

	/**
	 * Get the playSound config value
	 */
	boolean getPlaySound();

	/**
	 * Get the soundVolume config value
	 */
	float getSoundVolume();

	/**
	 * Get the soundLocation config value
	 */
	String getSoundLocation();

	/**
	 * Get the chosen sound based on the configured soundLocation
	 */
	SoundEvent getChosenSound();

}
