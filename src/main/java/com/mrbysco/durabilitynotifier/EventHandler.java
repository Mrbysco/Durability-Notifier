package com.mrbysco.durabilitynotifier;

import com.mrbysco.durabilitynotifier.config.DurabilityConfigGen;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

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

	public void checkDurability(ItemStack stack, Player player) {
		double DurabilityChecking = 1 - (DurabilityConfigGen.CLIENT.Percentage.get() / 100.0);

		if(!stack.isEmpty())
			checkDurability(stack, player, DurabilityChecking);
	}

	public void checkDurability(ItemStack stack, Player playerIn, double checkNumber) {
		if (stack != null && stack.getMaxDamage() != 0) {
			if (((double) stack.getDamageValue() / stack.getMaxDamage()) > checkNumber) {
				if (DurabilityConfigGen.CLIENT.SendMessage.get()) {
					sendMessage(playerIn, stack);
				}

				if (DurabilityConfigGen.CLIENT.PlaySound.get() && CooldownUtil.isNotOnCooldown(stack, 500L)) {
					//This guy really wanted something special. So explosion sounds it is.
					if (playerIn != null && playerIn.getGameProfile().getName().equalsIgnoreCase("Dcat682")) {
						playerIn.playSound(SoundEvents.GENERIC_EXPLODE, 1F, 1F);
					}

					playSound(playerIn);
				}
			}
		}
	}

	public void sendMessage(Player player, ItemStack stack) {
		ChatFormatting color = DurabilityConfigGen.CLIENT.SentMessageColor.get();

		MutableComponent part1 = new TranslatableComponent("durabilitynotifier.warning.part1", stack.getDisplayName()).withStyle(color);
		MutableComponent part2 = new TranslatableComponent("durabilitynotifier.warning.part2").withStyle(color);
		MutableComponent percentage = new TextComponent(DurabilityConfigGen.CLIENT.Percentage.get() + "%" + " ").withStyle(ChatFormatting.RED);
		MutableComponent part3 = new TranslatableComponent("durabilitynotifier.warning.part3").withStyle(color);
		player.displayClientMessage(part1.append(part2).append(percentage).append(part3),true);
	}

	public void playSound(Player player) {
		ResourceLocation soundLocation = new ResourceLocation(DurabilityConfigGen.CLIENT.soundlocation.get());

		if(ForgeRegistries.SOUND_EVENTS.containsKey(soundLocation)) {
			SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(soundLocation);
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
