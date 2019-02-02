package com.Mrbysco.durabilitynotifier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.Mrbysco.durabilitynotifier.config.DurabilityConfigGen;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLModLoadingContext;

@Mod("durabilitynotifier")
public class DurabilityNotifier {
	
	// Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
	
	public DurabilityNotifier() {

        // Register the setup method for modloading
        FMLModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        
        FMLModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, DurabilityConfigGen.spec);
        FMLModLoadingContext.get().getModEventBus().register(DurabilityConfigGen.class);

        // Register ourselves for server, registry and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
    	// NOTHING
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
    	LOGGER.debug("Generating/loading the Durability handler");
    }

    private void processIMC(final InterModProcessEvent event)
    {
    	// NOTHING
    }

	@SubscribeEvent
	public void checkItem(final PlayerInteractEvent.LeftClickBlock event) {
		ItemStack itemStack = event.getItemStack();
		EntityPlayer player = event.getEntityPlayer();
		double DurabilityChecking = 1 - (DurabilityConfigGen.general.Percentage.get() / 100.0);

		if(!itemStack.isEmpty())
		checkDurability(itemStack, player, DurabilityChecking, true);
	}
	
	@SubscribeEvent
	public void checkItem2(final PlayerInteractEvent.LeftClickEmpty event) {
		ItemStack itemStack = event.getItemStack();
		EntityPlayer player = event.getEntityPlayer();
		double DurabilityChecking = 1 - (DurabilityConfigGen.general.Percentage.get() / 100.0);
		
		if(!itemStack.isEmpty())
		checkDurability(itemStack, player, DurabilityChecking, false);
	}
	
	@SubscribeEvent
	public void checkItem3(final PlayerInteractEvent.RightClickBlock event) {
		ItemStack itemStack = event.getItemStack();
		EntityPlayer player = event.getEntityPlayer();
		double DurabilityChecking = 1 - (DurabilityConfigGen.general.Percentage.get() / 100.0);

		if(!itemStack.isEmpty())
		checkDurability(itemStack, player, DurabilityChecking, false);
	}
	
	
	public void checkDurability(ItemStack stack, EntityPlayer playerIn, double checkNumber, boolean clickedBlock){
		if (stack != null) {
			if (stack.getMaxDamage() != 0) {
				if (((double) stack.getDamage() / stack.getMaxDamage()) > checkNumber){
					if (DurabilityConfigGen.general.SendMessage.get() == true)
					{
						sendMessage(playerIn, stack);
					}

					if (DurabilityConfigGen.general.PlaySound.get() == true)
					{
						//This guy really wanted something special. So explosion sounds it is.
						if (playerIn != null && playerIn.getGameProfile().getName().equalsIgnoreCase("Dcat682"))
						{
							playerIn.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1F, 1F);
						}
							
						playSound(playerIn);
					}
				}
			}
			
//			if (clickedBlock)
//			{
//				if(DurabilityConfigGen.server.GiveFatigue.get() == true)
//				{
//					if (stack.getMaxDamage() != 0) {
//						if(stack.getDamage() == stack.getMaxDamage())
//						{
//							PotionEffect effect = playerIn.getActivePotionEffect(MobEffects.MINING_FATIGUE);
//							if (effect != null && effect.getDuration() > 0) 
//							{
//								playerIn.removeActivePotionEffect(MobEffects.MINING_FATIGUE);
//								playerIn.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 4 * 20, 3, true, true));
//							}
//							else
//							{
//								playerIn.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 4 * 20, 3, true, true));
//							}
//						}
//					}
//				}
//			}
			
		}
	}
	
	public void sendMessage(EntityPlayer player, ItemStack stack) {
		TextFormatting color = TextFormatting.getValueByName(DurabilityConfigGen.general.SentMessageColor.get());
		
		ITextComponent message = new TextComponentTranslation("warning.part1", new Object[] {stack.getDisplayName().getString()});
						message.getStyle().setColor(color);
		ITextComponent message2 = new TextComponentString(" " + String.valueOf(DurabilityConfigGen.general.Percentage.get()) + "%" + " ");
						message2.getStyle().setColor(TextFormatting.RED);
		
		ITextComponent message3 = new TextComponentTranslation("warning.part3", new Object[0]);
						message3.getStyle().setColor(color);
		player.sendStatusMessage(
				message.appendSibling(message2).appendSibling(message3),
					true
				);
	}
	
	public void playSound(EntityPlayer player) {
		SoundEvent sound = (SoundEvent)SoundEvent.REGISTRY.get(new ResourceLocation(DurabilityConfigGen.sound.soundlocation.get()));
		float volume = (float) DurabilityConfigGen.sound.volume.get().doubleValue();
		
		if (sound != null)
		{
			player.playSound(sound, volume, 1F);
		}
		else
		{
			LOGGER.warn("Could not locate the following sound: " + DurabilityConfigGen.sound.soundlocation.get()+ ". Perhaps you misspelled it.");
		}
	}
}