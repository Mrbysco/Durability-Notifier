package com.mrbysco.durabilitynotifier;

import com.mrbysco.durabilitynotifier.config.DurabilityConfigGen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;

public class EventHandler {
	@SubscribeEvent
	public void checkItem(final PlayerInteractEvent.LeftClickBlock event) {
		checkDurability(event.getItemStack(), event.getPlayer());
	}

	@SubscribeEvent
	public void checkItem2(final PlayerInteractEvent.LeftClickEmpty event) {
		checkDurability(event.getItemStack(), event.getPlayer());
	}

	@SubscribeEvent
	public void checkItem3(final PlayerInteractEvent.RightClickBlock event) {
		checkDurability(event.getItemStack(), event.getPlayer());
	}

	public void checkDurability(ItemStack stack, PlayerEntity player) {
		double DurabilityChecking = 1 - (DurabilityConfigGen.CLIENT.Percentage.get() / 100.0);

		if(!stack.isEmpty())
			checkDurability(stack, player, DurabilityChecking);
	}

	public void checkDurability(ItemStack stack, PlayerEntity playerIn, double checkNumber){
		if (stack != null) {
			if (stack.getMaxDamage() != 0) {
				if (((double) stack.getDamageValue() / stack.getMaxDamage()) > checkNumber){
					if (DurabilityConfigGen.CLIENT.SendMessage.get()) {
						sendMessage(playerIn, stack);
					}

					if (DurabilityConfigGen.CLIENT.PlaySound.get() && CooldownUtil.isNotOnCooldown(stack, 500L)) {
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

	public void sendMessage(PlayerEntity player, ItemStack stack) {
		TextFormatting messageColor = DurabilityConfigGen.CLIENT.SentMessageColor.get();

		IFormattableTextComponent part1 = new TranslationTextComponent("durabilitynotifier.warning.part1", stack.getDisplayName()).withStyle(messageColor);
		IFormattableTextComponent part2 = new TranslationTextComponent("durabilitynotifier.warning.part2").withStyle(messageColor);
		IFormattableTextComponent percentage = new StringTextComponent(DurabilityConfigGen.CLIENT.Percentage.get() + "%" + " ").withStyle(TextFormatting.RED);
		IFormattableTextComponent part3 = new TranslationTextComponent("durabilitynotifier.warning.part3").withStyle(messageColor);
		player.displayClientMessage(part1.append(part2).append(percentage).append(part3),true);
	}

	public void playSound(PlayerEntity player) {
		ResourceLocation soundLoc = new ResourceLocation(DurabilityConfigGen.CLIENT.soundlocation.get());

		if(ForgeRegistries.SOUND_EVENTS.containsKey(soundLoc)) {
			SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(soundLoc);
			float volume = (float) DurabilityConfigGen.CLIENT.volume.get().doubleValue();

			if (sound != null) {
				player.playSound(sound, volume, 1F);
			} else {
				DurabilityNotifier.LOGGER.warn("Could not locate the following sound: " + DurabilityConfigGen.CLIENT.soundlocation.get()+ ". Perhaps you misspelled it.");
			}
		} else {
			DurabilityNotifier.LOGGER.warn("Could not locate the following sound: " + DurabilityConfigGen.CLIENT.soundlocation.get()+ ". Perhaps you misspelled it.");
		}
	}
}
