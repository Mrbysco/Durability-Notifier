package com.mrbysco.durabilitynotifier.config;

import com.mrbysco.durabilitynotifier.Reference;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class DurabilityConfig {

	public static class Client {
		//General
		public final ModConfigSpec.IntValue Percentage;
		public final ModConfigSpec.BooleanValue CheckArmor;
		public final ModConfigSpec.ConfigValue<List<? extends String>> ArmorFilter;
		public final ModConfigSpec.BooleanValue FilterItems;
		public final ModConfigSpec.ConfigValue<List<? extends String>> ItemFilter;

		//Message
		public final ModConfigSpec.BooleanValue SendMessage;
		public final ModConfigSpec.EnumValue<ChatFormatting> SentMessageColor;

		//Sound
		public final ModConfigSpec.BooleanValue PlaySound;
		public final ModConfigSpec.ConfigValue<String> soundlocation;
		public final ModConfigSpec.DoubleValue volume;
		public final ModConfigSpec.IntValue soundCooldown;

		Client(net.neoforged.neoforge.common.ModConfigSpec.Builder builder) {
			builder.comment("General settings")
					.push("general");

			Percentage = builder
					.comment("Sets the percentage the mod checks for [default: 10] (1 to 100)")
					.defineInRange("Percentage", 10, 1, 100);

			CheckArmor = builder
					.comment("Dictates if it should also actively check armor [default: false]")
					.define("CheckArmor", false);

			ArmorFilter = builder
					.comment("If CheckArmor is enabled, any armor that is not in this list will be ignored (Empty list = all armor will be checked) [default: []]")
					.defineListAllowEmpty("ArmorFilter", List.of(), String::new, object -> object instanceof String);

			FilterItems = builder
					.comment("Filter which items are durability checked [default: false]")
					.define("FilterItems", false);

			ItemFilter = builder
					.comment("If FilterItems is enabled, any item that is not in this list will be ignored (Empty list = all items will be checked) [default: []]")
					.defineListAllowEmpty("ItemFilter", List.of(), String::new, object -> object instanceof String);

			builder.pop();
			builder.comment("Message settings")
					.push("message");

			SendMessage = builder
					.comment("Change this option to let it not display a chat message (if you have sound enabled) [default: true]")
					.define("SendMessage", true);

			SentMessageColor = builder
					.comment("Change this option to change the color / formatting of the message (if you have messages enabled) [default: YELLOW]")
					.defineEnum("SentMessageColor", ChatFormatting.YELLOW);

			builder.pop();
			builder.comment("Sound settings")
					.push("sound");

			PlaySound = builder
					.comment("Change this option to let it play a sound (configurable in the sound tab) [default: false]")
					.define("PlaySound", true);

			soundlocation = builder
					.comment("The id of the sound that is played (if you have sound enabled) [default: minecraft:block.note_block.pling]")
					.define("soundlocation", "minecraft:block.note_block.pling", o -> (o instanceof String loc) && Identifier.tryParse(loc) != null);

			volume = builder
					.comment("Sets the sound volume [default: 0.6] (0 to 1.0)")
					.defineInRange("volume", 0.6, 0.0, 1.0);

			soundCooldown = builder
					.comment("The amount of milliseconds between notification sound plays [default: 500 (half a second)]")
					.defineInRange("soundCooldown", 500, 0, Integer.MAX_VALUE);

			builder.pop();

		}
	}

	public static final net.neoforged.neoforge.common.ModConfigSpec clientSpec;
	public static final Client CLIENT;

	static {
		final Pair<Client, net.neoforged.neoforge.common.ModConfigSpec> specPair = new net.neoforged.neoforge.common.ModConfigSpec.Builder().configure(Client::new);
		clientSpec = specPair.getRight();
		CLIENT = specPair.getLeft();
	}

	@SubscribeEvent
	public static void onLoad(final ModConfigEvent.Loading configEvent) {
		Reference.LOGGER.debug("Loaded Durability Notifier's config file {}", configEvent.getConfig().getFileName());
	}

	@SubscribeEvent
	public static void onFileChange(final ModConfigEvent.Reloading configEvent) {
		Reference.LOGGER.warn("Durability Notifier's config just got changed on the file system!");
	}
}