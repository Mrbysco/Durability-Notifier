package com.mrbysco.durabilitynotifier;

import com.mrbysco.durabilitynotifier.config.DurabilityConfigGen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("durabilitynotifier")
public class DurabilityNotifier {
	
    public static final Logger LOGGER = LogManager.getLogger();
	
	public DurabilityNotifier() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, DurabilityConfigGen.clientSpec);
        FMLJavaModLoadingContext.get().getModEventBus().register(DurabilityConfigGen.class);

        MinecraftForge.EVENT_BUS.register(this);

		//Make sure the mod being absent on the other network side does not cause the client to display the server as incompatible
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
    }

	@SubscribeEvent
	public void checkItem(final PlayerInteractEvent.LeftClickBlock event) {
		ItemStack itemStack = event.getItemStack();
		PlayerEntity player = event.getPlayer();
		double DurabilityChecking = 1 - (DurabilityConfigGen.CLIENT.Percentage.get() / 100.0);

		if(!itemStack.isEmpty())
			checkDurability(itemStack, player, DurabilityChecking);
	}
	
	@SubscribeEvent
	public void checkItem2(final PlayerInteractEvent.LeftClickEmpty event) {
		ItemStack itemStack = event.getItemStack();
		PlayerEntity player = event.getPlayer();
		double DurabilityChecking = 1 - (DurabilityConfigGen.CLIENT.Percentage.get() / 100.0);
		
		if(!itemStack.isEmpty())
			checkDurability(itemStack, player, DurabilityChecking);
	}
	
	@SubscribeEvent
	public void checkItem3(final PlayerInteractEvent.RightClickBlock event) {
		ItemStack itemStack = event.getItemStack();
		PlayerEntity player = event.getPlayer();
		double DurabilityChecking = 1 - (DurabilityConfigGen.CLIENT.Percentage.get() / 100.0);

		if(!itemStack.isEmpty())
			checkDurability(itemStack, player, DurabilityChecking);
	}
	
	
	public void checkDurability(ItemStack stack, PlayerEntity playerIn, double checkNumber){
		if (stack != null) {
			if (stack.getMaxDamage() != 0) {
				if (((double) stack.getDamage() / stack.getMaxDamage()) > checkNumber){
					if (DurabilityConfigGen.CLIENT.SendMessage.get() == true) {
						sendMessage(playerIn, stack);
					}

					if (DurabilityConfigGen.CLIENT.PlaySound.get() == true) {
						//This guy really wanted something special. So explosion sounds it is.
						if (playerIn != null && playerIn.getGameProfile().getName().equalsIgnoreCase("Dcat682")) {
							playerIn.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1F, 1F);
						}
							
						playSound(playerIn);
					}
				}
			}
			
		}
	}
	
	public void sendMessage(PlayerEntity player, ItemStack stack) {
		TextFormatting color = DurabilityConfigGen.CLIENT.SentMessageColor.get();
		
		TextComponent message = new TranslationTextComponent("warning.part1", new Object[] {stack.getDisplayName().getString()});
						message.mergeStyle(color);
		TextComponent message2 = new StringTextComponent(" " + DurabilityConfigGen.CLIENT.Percentage.get() + "%" + " ");
						message2.mergeStyle(TextFormatting.RED);
		TextComponent message3 = new TranslationTextComponent("warning.part3", new Object[0]);
						message3.mergeStyle(color);
		player.sendStatusMessage(message.append(message2).append(message3),true);
	}
	
	public void playSound(PlayerEntity player) {
		ResourceLocation soundLoc = new ResourceLocation(DurabilityConfigGen.CLIENT.soundlocation.get());
				
		if(ForgeRegistries.SOUND_EVENTS.containsKey(soundLoc)) {
			SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(soundLoc);
			float volume = (float) DurabilityConfigGen.CLIENT.volume.get().doubleValue();
			
			if (sound != null) {
				player.playSound(sound, volume, 1F);
			} else {
				LOGGER.warn("Could not locate the following sound: " + DurabilityConfigGen.CLIENT.soundlocation.get()+ ". Perhaps you misspelled it.");
			}
		} else {
			LOGGER.warn("Could not locate the following sound: " + DurabilityConfigGen.CLIENT.soundlocation.get()+ ". Perhaps you misspelled it.");
		}
	}
}