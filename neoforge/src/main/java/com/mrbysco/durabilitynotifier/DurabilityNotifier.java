package com.mrbysco.durabilitynotifier;

import com.mrbysco.durabilitynotifier.config.DurabilityConfig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@Mod(Reference.MOD_ID)
public class DurabilityNotifier {

	public DurabilityNotifier(IEventBus eventBus, Dist dist, ModContainer container) {
		if (dist.isClient()) {
			container.registerConfig(ModConfig.Type.CLIENT, DurabilityConfig.clientSpec);
			eventBus.register(DurabilityConfig.class);
			container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

			NeoForge.EVENT_BUS.addListener(this::onLeftClickBlock);
			NeoForge.EVENT_BUS.addListener(this::onLeftClickEmpty);
			NeoForge.EVENT_BUS.addListener(this::onRightClickBlock);
			NeoForge.EVENT_BUS.addListener(this::onRightClickEmpty);
			NeoForge.EVENT_BUS.addListener(this::onAttackEntity);
			NeoForge.EVENT_BUS.addListener(this::onInventoryTick);
		}
	}

	private void onLeftClickBlock(final PlayerInteractEvent.LeftClickBlock event) {
		EventHandler.checkDurability(event.getItemStack(), event.getEntity());
	}

	private void onLeftClickEmpty(final PlayerInteractEvent.LeftClickEmpty event) {
		EventHandler.checkDurability(event.getItemStack(), event.getEntity());
	}

	private void onRightClickBlock(final PlayerInteractEvent.RightClickBlock event) {
		EventHandler.checkDurability(event.getItemStack(), event.getEntity());
	}

	private void onRightClickEmpty(final PlayerInteractEvent.RightClickItem event) {
		EventHandler.checkDurability(event.getItemStack(), event.getEntity());
	}

	private void onAttackEntity(final AttackEntityEvent event) {
		final Player player = event.getEntity();
		EventHandler.checkDurability(player.getMainHandItem(), player);
	}

	private void onInventoryTick(final PlayerTickEvent.Post event) {
		Player player = event.getEntity();
		if (player.level().getGameTime() % 80 == 0 && DurabilityConfig.CLIENT.CheckArmor.get()) {
			for (int slot = 36; slot <= 39; slot++) {
				ItemStack itemStack = player.getInventory().getItem(slot);
				EventHandler.checkDurability(itemStack, player);
			}
		}
	}
}