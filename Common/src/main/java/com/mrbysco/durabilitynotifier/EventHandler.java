package com.mrbysco.durabilitynotifier;

import com.mrbysco.durabilitynotifier.platform.Services;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class EventHandler {
	public static float soundVolume = 0.6f;
//	public static ResourceLocation soundLocation = SoundEvents.NOTE_BLOCK_PLING.getLocation();
//	public static SoundEvent chosenSound = SoundEvents.NOTE_BLOCK_PLING;

	public static void checkDurability(ItemStack stack, Player player) {
		double DurabilityChecking = 1 - (Services.PLATFORM.getPercentage() / 100.0);

		if (!stack.isEmpty())
			checkDurability(stack, player, DurabilityChecking);
	}

	public static void checkDurability(ItemStack stack, Player playerIn, double checkNumber) {
		if (stack != null && stack.getMaxDamage() != 0) {
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
		MutableComponent part1 = new TranslatableComponent("durabilitynotifier.warning.part1", stack.getDisplayName()).withStyle(messageColor);
		MutableComponent part2 = new TranslatableComponent("durabilitynotifier.warning.part2").withStyle(messageColor);
		MutableComponent percentage = new TextComponent(Services.PLATFORM.getPercentage() + "%" + " ").withStyle(ChatFormatting.RED);
		MutableComponent part3 = new TranslatableComponent("durabilitynotifier.warning.part3").withStyle(messageColor);
		player.displayClientMessage(part1.append(part2).append(percentage).append(part3), true);
	}

	public static void playSound(Player player) {
		SoundEvent chosenSound = Services.PLATFORM.getChosenSound();
		if (chosenSound != null) {
			player.playSound(chosenSound, Services.PLATFORM.getSoundVolume(), 1F);
		} else {
			Reference.LOGGER.warn("Could not locate the following sound: {}. Perhaps you misspelled it.", Services.PLATFORM.getChosenSound());
		}
	}
}
