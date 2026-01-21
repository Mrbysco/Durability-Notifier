package com.mrbysco.durabilitynotifier.platform.services;

import net.minecraft.ChatFormatting;

import java.util.List;

public interface IPlatformHelper {

	/**
	 * Get the percentage config value
	 */
	int getPercentage();

	/**
	 * Get the cooldown config value
	 */
	int getSoundCooldown();

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
	 * If item filtering is enabled
	 */
	boolean filterItems();

	/**
	 * Get the item filter list
	 */
	List<String> getItemFilter();
}
