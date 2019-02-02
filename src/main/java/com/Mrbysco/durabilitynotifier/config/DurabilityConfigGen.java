package com.Mrbysco.durabilitynotifier.config;

import static net.minecraftforge.fml.Logging.CORE;
import static net.minecraftforge.fml.loading.LogMarkers.FORGEMOD;

import com.Mrbysco.durabilitynotifier.DurabilityNotifier;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;

public class DurabilityConfigGen {
	private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
    public static final General general = new General(CLIENT_BUILDER);
    public static final Sound sound = new Sound(CLIENT_BUILDER);

	public static class General {
        public final IntValue Percentage;
    	public final BooleanValue SendMessage;
    	public final ConfigValue<String> SentMessageColor;
    	public final BooleanValue PlaySound;

    	General(ForgeConfigSpec.Builder builder) {
        	builder.comment("General settings")
            .push("general");

        	Percentage = builder
        			.comment("Sets the percentage the mod checks for [default: 10] (1 to 100)")
        			.defineInRange("Percentage", 10, 1, 100);
        	
        	SendMessage = builder
        			.comment("Change this option to let it not display a chat message (if you have sound enabled) [default: true]")
        			.define("SendMessage", true);
        	
        	SentMessageColor = builder
        			.comment("Change this option to change the color / formatting of the message (if you have sound enabled) [default: yellow]")
        			.define("SentMessageColor", "yellow");
        	
        	PlaySound = builder
        			.comment("Change this option to let it play a sound (configurable in the sound tab) [default: false]")
        			.define("PlaySound", true);
        	
        	builder.pop();
        }
	}
	
	public static class Sound {
    	public final ConfigValue<String> soundlocation;
    	public final DoubleValue volume;
    	
    	Sound(ForgeConfigSpec.Builder builder) {
        	builder.comment("Sound settings")
            .push("sound");

        	soundlocation = builder
        			.comment("Change this option to change the color / formatting of the message (if you have sound enabled) [default: minecraft:block.note_block.pling]")
        			.define("soundlocation", "minecraft:block.note_block.pling");
        	
        	volume = builder
        			.comment("Sets the percentage the mod checks for [default: 10] (1 to 100)")
        			.defineInRange("volume", 0.6, 0.0, 5.0);
        	
        	builder.pop();
        }
	}
    
	public static final ForgeConfigSpec spec = CLIENT_BUILDER.build();
	    
	@SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {
		DurabilityNotifier.LOGGER.debug(FORGEMOD, "Loaded Durability Notifier's config file {}", configEvent.getConfig().getFileName());
    }

    @SubscribeEvent
    public static void onFileChange(final ModConfig.ConfigReloading configEvent) {
        DurabilityNotifier.LOGGER.fatal(CORE, "Durability Notifier's config just got changed on the file system!");
    }
}