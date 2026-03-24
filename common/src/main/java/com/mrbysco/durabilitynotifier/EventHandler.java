package com.mrbysco.durabilitynotifier;

import com.mrbysco.durabilitynotifier.config.DurabilityConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class EventHandler {
	public static void checkDurability(@NotNull ItemStack stack, @NotNull Player player) {
		double DurabilityChecking = 1 - (DurabilityConfig.CLIENT.Percentage.get() / 100.0);
		if (!stack.isEmpty())
			checkDurability(stack, player, DurabilityChecking);
	}

	public static void checkDurability(@NotNull ItemStack stack, @NotNull Player player, double checkNumber) {
		if (!stack.isEmpty() && stack.isDamageableItem() && stack.getMaxDamage() != 0) {
			if (DurabilityConfig.CLIENT.FilterItems.get()) {
				boolean inFilter = DurabilityConfig.CLIENT.ItemFilter.get()
						.contains(BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
				if (!inFilter) return;
			}
			if (((double) stack.getDamageValue() / stack.getMaxDamage()) > checkNumber) {
				if (DurabilityConfig.CLIENT.SendMessage.get()) {
					sendMessage(player, stack);
				}

				if (DurabilityConfig.CLIENT.playSound.get() && CooldownUtil.isNotOnCooldown(stack, DurabilityConfig.CLIENT.soundCooldown.get())) {
					//This guy really wanted something special. So explosion sounds it is.
					if (player.getGameProfile().id().equals(UUID.fromString("86121150-39f2-4063-831a-3715f2e7f397"))) { //Dcat682
						player.level().playLocalSound(player.blockPosition(), SoundEvents.GENERIC_EXPLODE.value(),
								SoundSource.PLAYERS, 1F, 1F, false);
					}

					playSound(player);
				}
			}
		}
	}

	public static void sendMessage(@NotNull Player player, @NotNull ItemStack stack) {
		ChatFormatting messageColor = DurabilityConfig.CLIENT.SentMessageColor.get();
		if (messageColor == null) {
			messageColor = ChatFormatting.YELLOW;
			Reference.LOGGER.warn("Invalid chat color found in config, please check the config");
		}
		String warningString = Component.translatable("durabilitynotifier.warning").getString();
		warningString = warningString
				.replace("%item%", stack.getDisplayName().getString())
				.replace("%percent%", DurabilityConfig.CLIENT.Percentage.get() + "§c%§r");
		MutableComponent warning = Component.literal(warningString).withStyle(messageColor);

		player.sendOverlayMessage(warning);
	}

	public static void playSound(@NotNull Player player) {
		SoundEvent chosenSound = getChosenSound();
		if (chosenSound != null) {
			player.level().playLocalSound(player.blockPosition(), chosenSound, SoundSource.PLAYERS, DurabilityConfig.CLIENT.volume.get().floatValue(), 1F, false);
		} else {
			Reference.LOGGER.warn("Could not locate the following sound: {}. Perhaps you misspelled it.", DurabilityConfig.CLIENT.soundLocation.get());
		}
	}

	@Nullable
	private static SoundEvent getChosenSound() {
		Identifier soundLocation = Identifier.tryParse(DurabilityConfig.CLIENT.soundLocation.get());
		if (soundLocation != null) {
			SoundEvent sound = BuiltInRegistries.SOUND_EVENT.getValue(soundLocation);
			if (sound != null) {
				return sound;
			} else {
				Reference.LOGGER.warn("Could not locate the following sound: {}. Perhaps you misspelled it. Falling back to default!", soundLocation);
				return SoundEvents.NOTE_BLOCK_PLING.value();
			}
		}
		return null;
	}
}
