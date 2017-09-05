package com.Mrbysco.durabilitynotifier.handler;

import com.Mrbysco.durabilitynotifier.DurabilityNotifier;
import com.Mrbysco.durabilitynotifier.config.DurabilityConfigGen;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DurabilityHandler {
	
	@SubscribeEvent
	public void checkItem(PlayerInteractEvent.LeftClickBlock event) {
		ItemStack itemStack = event.getItemStack();
		EntityPlayer player = event.getEntityPlayer();
		double DurabilityChecking = 1 - (DurabilityConfigGen.general.Percentage / 100.0);

		if (itemStack != null) {
			if (itemStack.getMaxDamage() != 0) {
				if (((double) itemStack.getItemDamage() / itemStack.getMaxDamage()) > DurabilityChecking){
					if (DurabilityConfigGen.general.SendMessage == true)
					{
						sendMessage(event.getEntityPlayer(), itemStack);
					}
					
					if (DurabilityConfigGen.general.PlaySound == true)
					{
						//This guy really wanted something special. So explosion sounds it is.
						if (player != null && player.getGameProfile().getName().equalsIgnoreCase("Dcat682"))
						{
								player.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1F, 1F);
						}
							
						playSound(event.getEntityPlayer());
					}
                }
            }
			
			if(DurabilityConfigGen.general.GiveFatigue == true)
			{
				if (itemStack.getMaxDamage() != 0) {
					if(itemStack.getItemDamage() == itemStack.getMaxDamage())
					{
						PotionEffect effect = player.getActivePotionEffect(MobEffects.MINING_FATIGUE);
						if (effect != null && effect.getDuration() > 0) 
						{
							player.removeActivePotionEffect(MobEffects.MINING_FATIGUE);
							player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 4 * 20, 3, true, true));
						}
						else
						{
							player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 4 * 20, 3, true, true));
						}
					}
	            }
			}
		}
    }
	
	@SubscribeEvent
	public void checkItem2(PlayerInteractEvent.LeftClickEmpty event) {
		ItemStack itemStack = event.getItemStack();
		EntityPlayer player = event.getEntityPlayer();
		double DurabilityChecking = 1 - (DurabilityConfigGen.general.Percentage / 100.0);
		
		if (itemStack != null) {
			if (itemStack.getMaxDamage() != 0) {
				if (((double) itemStack.getItemDamage() / itemStack.getMaxDamage()) > DurabilityChecking){
					if (DurabilityConfigGen.general.SendMessage == true)
					{
						sendMessage(event.getEntityPlayer(), itemStack);
					}
					
					if (DurabilityConfigGen.general.PlaySound == true)
					{
						//This guy really wanted something special. So explosion sounds it is.
						if (player != null && player.getGameProfile().getName().equalsIgnoreCase("Dcat682"))
						{
								player.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1F, 1F);
						}
							
						playSound(event.getEntityPlayer());
					}
                }
            }
		}
    }
	
	public void sendMessage(EntityPlayer player, ItemStack stack) {
		TextFormatting color = DurabilityConfigGen.general.SentMessageColor;
		
		player.sendStatusMessage(
				new TextComponentString(
						color + stack.getDisplayName() + 
						" has dropped below " + TextFormatting.RED + DurabilityConfigGen.general.Percentage + "%" + 
								TextFormatting.RESET + color + " durability."
                    ),
                    true
            );
	}
	
	public void playSound(EntityPlayer player) {
		SoundEvent sound = (SoundEvent)SoundEvent.REGISTRY.getObject(new ResourceLocation(DurabilityConfigGen.sound.soundlocation));
		float volume = (float) DurabilityConfigGen.sound.volume;
		
		if (sound != null)
		{
			player.playSound(sound, volume, 1F);
		}
		else
		{
			DurabilityNotifier.logger.warn("Could not locate the following sound: " + DurabilityConfigGen.sound.soundlocation + ". Perhaps you misspelled it.");
		}
	}
}