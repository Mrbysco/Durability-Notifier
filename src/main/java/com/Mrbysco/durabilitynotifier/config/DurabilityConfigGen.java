package com.Mrbysco.durabilitynotifier.config;

import com.Mrbysco.durabilitynotifier.Reference;

import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Reference.MOD_ID)
@Config.LangKey("durabilitynotifier.config.title")
public class DurabilityConfigGen {

	public static final General general = new General();
	
	public static final Sound sound = new Sound();

	public static class General {

		@Config.Comment("Sets the percentage the mod checks for [default: 10] (1 to 100)")
		public int Percentage = 10;
		
		@Config.Comment("Change this option to let it not display a chat message (if you have sound enabled) [default: true]")
		public boolean SendMessage = true;
		
		@Config.Comment("Change this option to change the color / formatting of the message (if you have sound enabled) [default: red]")
		public TextFormatting SentMessageColor = TextFormatting.RED;
		
		@Config.Comment("Change this option to let it play a sound (configurable in the sound tab) [default: false]")
		public boolean PlaySound = false;
		
		@Config.Comment("Change this option to give you mining fatigue so that you won't break your tool (This does not work in vanilla servers) [default: false]")
		public boolean GiveFatigue = false;
	}
	
	public static class Sound {
		@Config.Comment("Changing this option to change what sound is played (only has effect if PlaySound is enabled).")
		public String soundlocation = "block.note.pling";
		
		@Config.Comment("Changing this option to change what sound is played (only has effect if PlaySound is enabled).")
		public double volume = 0.6;
	}

	@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
	private static class EventHandler {

		@SubscribeEvent
		public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(Reference.MOD_ID)) {
				ConfigManager.sync(Reference.MOD_ID, Config.Type.INSTANCE);
			}
		}
	}
}