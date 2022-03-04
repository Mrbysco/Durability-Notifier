package com.mrbysco.durabilitynotifier.config;

import com.mrbysco.durabilitynotifier.Reference;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.BoundedDiscrete;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.CollapsibleObject;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.minecraft.ChatFormatting;

@Config(name = Reference.MOD_ID)
public class DurabilityConfig implements ConfigData {

	@CollapsibleObject
	public General general = new General();

	@CollapsibleObject
	public Message message = new Message();

	@CollapsibleObject
	public Sound sound = new Sound();

	public static class General {
		//General
		@Comment("Sets the percentage the mod checks for [default: 10] (1 to 100)")
		@BoundedDiscrete(min = 0, max = 100)
		public int percentage = 10;
	}

	public static class Message {
		@Comment("Change this option to let it not display a chat message (if you have sound enabled) [default: true]")
		public boolean sendMessage = true;

		@Comment("Change this option to change the color / formatting of the message (if you have mesages enabled) [default: YELLOW]")
		public ChatFormatting messageColor = ChatFormatting.YELLOW;
	}

	public static class Sound {
		@Comment("Change this option to let it play a sound (configurable in the sound tab) [default: false]")
		public boolean playSound = true;

		@Comment("Change this option to change the color / formatting of the message (if you have sound enabled) [default: minecraft:block.note_block.pling]")
		public String soundLocation = "minecraft:block.note_block.pling";

		@Comment("Sets the sound volume [default: 0.6] (0 to 1.0)")
		@BoundedDiscrete(min = 0, max = 1)
		public double volume = 0.6d;
	}
}