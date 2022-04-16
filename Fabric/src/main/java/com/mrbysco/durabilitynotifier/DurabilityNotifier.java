package com.mrbysco.durabilitynotifier;

import com.mrbysco.durabilitynotifier.callback.ClickAirCallback;
import com.mrbysco.durabilitynotifier.callback.PlayerTickCallback;
import com.mrbysco.durabilitynotifier.config.DurabilityConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;

public class DurabilityNotifier implements ClientModInitializer {
	private Thread watchThread = null;
	public static DurabilityConfig config;

	@Override
	public void onInitializeClient() {
		// Use Fabric to bootstrap the Common mod.
		CommonClass.init();

		ConfigHolder<DurabilityConfig> holder = AutoConfig.register(DurabilityConfig.class, Toml4jConfigSerializer::new);
		config = holder.getConfig();
		try {
			var watchService = FileSystems.getDefault().newWatchService();
			Paths.get("config").register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
			watchThread = new Thread(() -> {
				WatchKey key;
				try {
					while ((key = watchService.take()) != null) {
						if (Thread.currentThread().isInterrupted()) {
							watchService.close();
							break;
						}
						for (WatchEvent<?> event : key.pollEvents()) {
							if (event.kind() == StandardWatchEventKinds.OVERFLOW) {
								continue;
							}
							if (((Path) event.context()).endsWith("durabilitynotifier.toml")) {
								Reference.LOGGER.info("Reloading DurabilityNotifier config");
								if (holder.load()) {
									config = holder.getConfig();
								}
							}
						}
						key.reset();
					}
				} catch (InterruptedException ignored) {
				} catch (IOException e) {
					Reference.LOGGER.error("Failed to close filesystem watcher", e);
				}
			}, "Durability Notifier Config Watcher");
			watchThread.start();
		} catch (IOException e) {
			Reference.LOGGER.error("Failed to create filesystem watcher for configs", e);
		}

		// Some code like events require special initialization from the
		// loader specific code.
		AttackBlockCallback.EVENT.register((player, world, hand, pos, face) -> {
			EventHandler.checkDurability(player.getItemInHand(hand), player);
			return InteractionResult.PASS;
		});

		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			EventHandler.checkDurability(player.getItemInHand(hand), player);
			return InteractionResult.PASS;
		});

		ClickAirCallback.EVENT.register((player, hand) -> {
			EventHandler.checkDurability(player.getItemInHand(hand), player);
			return InteractionResult.PASS;
		});

		AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			EventHandler.checkDurability(player.getItemInHand(hand), player);
			return InteractionResult.PASS;
		});

		PlayerTickCallback.EVENT.register((player) -> {
			Level level = player.level;
			if (level.isClientSide && player.level.getGameTime() % 80 == 0) {
				if (DurabilityNotifier.config == null)
					DurabilityNotifier.config = AutoConfig.getConfigHolder(DurabilityConfig.class).getConfig();
				if (DurabilityNotifier.config.general.checkArmor) {
					for (ItemStack itemStack : player.getInventory().armor) {
						EventHandler.checkDurability(itemStack, player);
					}
				}
			}
			return InteractionResult.PASS;
		});
	}
}
