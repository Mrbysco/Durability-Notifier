package com.mrbysco.durabilitynotifier.config;

import com.mrbysco.durabilitynotifier.EventHandler;
import com.mrbysco.durabilitynotifier.Reference;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.EnumValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

public class DurabilityConfig {

    public static class Client {
    	//General
        public final IntValue Percentage;
    	public final BooleanValue SendMessage;
    	public final EnumValue<ChatFormatting> SentMessageColor;
    	public final BooleanValue PlaySound;

    	//Sound
    	public final ConfigValue<String> soundlocation;
    	public final DoubleValue volume;
    	
    	Client(ForgeConfigSpec.Builder builder) {
        	builder.comment("General settings")
            .push("general");

        	Percentage = builder
        			.comment("Sets the percentage the mod checks for [default: 10] (1 to 100)")
        			.defineInRange("Percentage", 10, 1, 100);
        	
        	SendMessage = builder
        			.comment("Change this option to let it not display a chat message (if you have sound enabled) [default: true]")
        			.define("SendMessage", true);
        	
        	SentMessageColor = builder
        			.comment("Change this option to change the color / formatting of the message (if you have mesages enabled) [default: YELLOW]")
        			.defineEnum("SentMessageColor", ChatFormatting.YELLOW);
        	
        	PlaySound = builder
        			.comment("Change this option to let it play a sound (configurable in the sound tab) [default: false]")
        			.define("PlaySound", true);

			builder.pop();
        	builder.comment("Sound settings")
            .push("sound");

        	soundlocation = builder
        			.comment("Change this option to change the color / formatting of the message (if you have sound enabled) [default: minecraft:block.note_block.pling]")
        			.define("soundlocation", "minecraft:block.note_block.pling", o -> (o instanceof String loc) && ResourceLocation.isValidResourceLocation(loc));
        	
        	volume = builder
        			.comment("Sets the sound volume [default: 0.6] (0 to 1.0)")
        			.defineInRange("volume", 0.6, 0.0, 1.0);
        	
        	builder.pop();
        	
        }
	}
    
    public static final ForgeConfigSpec clientSpec;
    public static final Client CLIENT;
    static {
        final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
        clientSpec = specPair.getRight();
        CLIENT = specPair.getLeft();
    }
    
	@SubscribeEvent
    public static void onLoad(final ModConfigEvent.Loading configEvent) {
		Reference.LOGGER.debug("Loaded Durability Notifier's config file {}", configEvent.getConfig().getFileName());
    }

    @SubscribeEvent
    public static void onFileChange(final ModConfigEvent.Reloading configEvent) {
		Reference.LOGGER.fatal("Durability Notifier's config just got changed on the file system!");
    }

    @SubscribeEvent
    public static void onFileChange(final ModConfigEvent configEvent) {
		if(Reference.MOD_ID.equals(configEvent.getConfig().getModId())) {
			EventHandler.percentage = CLIENT.Percentage.get();

			EventHandler.sendMessage = CLIENT.SendMessage.get();
			EventHandler.messageColor = CLIENT.SentMessageColor.get();

			EventHandler.playSound = CLIENT.PlaySound.get();
			EventHandler.soundVolume = CLIENT.volume.get().floatValue();
			ResourceLocation soundLocation = ResourceLocation.tryParse(CLIENT.soundlocation.get());
			if(soundLocation != null) {
				SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(soundLocation);
				if (sound != null) {
					EventHandler.soundLocation = soundLocation;
					EventHandler.chosenSound = sound;
				} else {
					EventHandler.soundLocation = SoundEvents.NOTE_BLOCK_PLING.getLocation();
					EventHandler.chosenSound = SoundEvents.NOTE_BLOCK_PLING;
					Reference.LOGGER.warn("Could not locate the following sound: " + soundLocation + ". Perhaps you misspelled it. Falling back to default!");
				}
			}
		}
    }
}