package com.mrbysco.durabilitynotifier;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class EventHandler {
	public static int percentage = 10;

	public static boolean sendMessage = true;
	public static ChatFormatting messageColor = ChatFormatting.YELLOW;

	public static boolean playSound = true;
	public static float soundVolume = 0.6f;
	public static ResourceLocation soundLocation = SoundEvents.NOTE_BLOCK_PLING.getLocation();
	public static SoundEvent chosenSound = SoundEvents.NOTE_BLOCK_PLING;

	public static void checkLeftClickBlock(ItemStack stack, Player player) {
		double DurabilityChecking = 1 - (percentage / 100.0);

		if(!stack.isEmpty())
			checkDurability(stack, player, DurabilityChecking);
	}

	public static void checkLeftClickEmpty(ItemStack stack, Player player) {
		double DurabilityChecking = 1 - (percentage / 100.0);

		if(!stack.isEmpty())
			checkDurability(stack, player, DurabilityChecking);
	}

	public static void checkRightClickBlock(ItemStack stack, Player player) {
		double DurabilityChecking = 1 - (percentage / 100.0);

		if(!stack.isEmpty())
			checkDurability(stack, player, DurabilityChecking);
	}

	public static void checkDurability(ItemStack stack, Player playerIn, double checkNumber){
		if (stack != null) {
			if (stack.getMaxDamage() != 0) {
				if (((double) stack.getDamageValue() / stack.getMaxDamage()) > checkNumber){
					if (sendMessage) {
						sendMessage(playerIn, stack);
					}

					if (playSound) {
						//This guy really wanted something special. So explosion sounds it is.
						if (playerIn != null && playerIn.getGameProfile().getId().equals(UUID.fromString("86121150-39f2-4063-831a-3715f2e7f397"))) { //Dcat682
							playerIn.playSound(SoundEvents.GENERIC_EXPLODE, 1F, 1F);
						}

						playSound(playerIn);
					}
				}
			}

		}
	}

	public static void sendMessage(Player player, ItemStack stack) {
		MutableComponent part1 = new TranslatableComponent("durabilitynotifier.warning.part1", stack.getDisplayName()).withStyle(messageColor);
		MutableComponent part2 = new TranslatableComponent("durabilitynotifier.warning.part2").withStyle(messageColor);
		MutableComponent percentage = new TextComponent(EventHandler.percentage + "%" + " ").withStyle(ChatFormatting.RED);
		MutableComponent part3 = new TranslatableComponent("durabilitynotifier.warning.part3").withStyle(messageColor);
		player.displayClientMessage(part1.append(part2).append(percentage).append(part3),true);
	}

	public static void playSound(Player player) {
		if (chosenSound != null) {
			player.playSound(chosenSound, soundVolume, 1F);
		} else {
			Reference.LOGGER.warn("Could not locate the following sound: {}. Perhaps you misspelled it.", soundLocation);
		}
	}
}
