package com.mrbysco.durabilitynotifier;

import com.mrbysco.durabilitynotifier.platform.Services;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class EventHandler {
	public static void checkDurability(ItemStack stack, Player player) {
		double DurabilityChecking = 1 - (Services.PLATFORM.getPercentage() / 100.0);

		if (!stack.isEmpty())
			checkDurability(stack, player, DurabilityChecking);
	}

	public static void checkDurability(ItemStack stack, Player playerIn, double checkNumber) {
		if (stack != null && stack.isDamageableItem() && stack.getMaxDamage() != 0) {
			if (((double) stack.getDamageValue() / stack.getMaxDamage()) > checkNumber) {
				if (Services.PLATFORM.getSendMessage()) {
					sendMessage(playerIn, stack);
				}

				if (Services.PLATFORM.getPlaySound() && CooldownUtil.isNotOnCooldown(stack, 500L)) {
					//This guy really wanted something special. So explosion sounds it is.
					if (playerIn != null && playerIn.getGameProfile().getId().equals(UUID.fromString("86121150-39f2-4063-831a-3715f2e7f397"))) { //Dcat682
						playerIn.playSound(SoundEvents.GENERIC_EXPLODE, 1F, 1F);
					}

					playSound(playerIn);
				}
			}
		}
	}

	public static void sendMessage(Player player, ItemStack stack) {
		ChatFormatting messageColor = Services.PLATFORM.getMessageColor();
		if (messageColor == null) {
			messageColor = ChatFormatting.YELLOW;
			Reference.LOGGER.warn("Invalid chat color found in config, please check the config");
		}
		MutableComponent part1 = Component.translatable("durabilitynotifier.warning.part1", stack.getDisplayName()).withStyle(messageColor);
		MutableComponent part2 = Component.translatable("durabilitynotifier.warning.part2").withStyle(messageColor);
		MutableComponent percentage = Component.literal(Services.PLATFORM.getPercentage() + "%" + " ").withStyle(ChatFormatting.RED);
		MutableComponent part3 = Component.translatable("durabilitynotifier.warning.part3").withStyle(messageColor);
		player.displayClientMessage(part1.append(part2).append(percentage).append(part3), true);
	}

	public static void playSound(Player player) {
		SoundEvent chosenSound = getChosenSound();
		if (chosenSound != null) {
			player.playSound(chosenSound, Services.PLATFORM.getSoundVolume(), 1F);
		} else {
			Reference.LOGGER.warn("Could not locate the following sound: {}. Perhaps you misspelled it.", Services.PLATFORM.getSoundLocation());
		}
	}

	private static SoundEvent getChosenSound() {
		ResourceLocation soundLocation = ResourceLocation.tryParse(Services.PLATFORM.getSoundLocation());
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
