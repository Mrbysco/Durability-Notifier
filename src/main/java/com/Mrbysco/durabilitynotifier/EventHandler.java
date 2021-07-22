package com.mrbysco.durabilitynotifier;

import com.mrbysco.durabilitynotifier.config.DurabilityConfigGen;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.BaseComponent;
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
		ItemStack itemStack = event.getItemStack();
		Player player = event.getPlayer();
		double DurabilityChecking = 1 - (DurabilityConfigGen.CLIENT.Percentage.get() / 100.0);

		if(!itemStack.isEmpty())
			checkDurability(itemStack, player, DurabilityChecking);
	}

	@SubscribeEvent
	public void checkItem2(final PlayerInteractEvent.LeftClickEmpty event) {
		ItemStack itemStack = event.getItemStack();
		Player player = event.getPlayer();
		double DurabilityChecking = 1 - (DurabilityConfigGen.CLIENT.Percentage.get() / 100.0);

		if(!itemStack.isEmpty())
			checkDurability(itemStack, player, DurabilityChecking);
	}

	@SubscribeEvent
	public void checkItem3(final PlayerInteractEvent.RightClickBlock event) {
		ItemStack itemStack = event.getItemStack();
		Player player = event.getPlayer();
		double DurabilityChecking = 1 - (DurabilityConfigGen.CLIENT.Percentage.get() / 100.0);

		if(!itemStack.isEmpty())
			checkDurability(itemStack, player, DurabilityChecking);
	}


	public void checkDurability(ItemStack stack, Player playerIn, double checkNumber){
		if (stack != null) {
			if (stack.getMaxDamage() != 0) {
				if (((double) stack.getDamageValue() / stack.getMaxDamage()) > checkNumber){
					if (DurabilityConfigGen.CLIENT.SendMessage.get()) {
						sendMessage(playerIn, stack);
					}

					if (DurabilityConfigGen.CLIENT.PlaySound.get()) {
						//This guy really wanted something special. So explosion sounds it is.
						if (playerIn != null && playerIn.getGameProfile().getName().equalsIgnoreCase("Dcat682")) {
							playerIn.playSound(SoundEvents.GENERIC_EXPLODE, 1F, 1F);
						}

						playSound(playerIn);
					}
				}
			}

		}
	}

	public void sendMessage(Player player, ItemStack stack) {
		ChatFormatting color = DurabilityConfigGen.CLIENT.SentMessageColor.get();

		BaseComponent message = new TranslatableComponent("warning.part1", stack.getDisplayName().getString());
		message.withStyle(color);
		TextComponent message2 = new TextComponent(" " + DurabilityConfigGen.CLIENT.Percentage.get() + "%" + " ");
		message2.withStyle(ChatFormatting.RED);
		BaseComponent message3 = new TranslatableComponent("warning.part3", new Object[0]);
		message3.withStyle(color);
		player.displayClientMessage(message.append(message2).append(message3),true);
	}

	public void playSound(Player player) {
		ResourceLocation soundLocaiton = new ResourceLocation(DurabilityConfigGen.CLIENT.soundlocation.get());

		if(ForgeRegistries.SOUND_EVENTS.containsKey(soundLocaiton)) {
			SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(soundLocaiton);
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
