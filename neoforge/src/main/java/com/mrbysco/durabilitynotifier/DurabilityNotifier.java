package com.mrbysco.durabilitynotifier;

import com.mrbysco.durabilitynotifier.config.DurabilityConfig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.IExtensionPoint;
import net.neoforged.fml.IExtensionPoint.DisplayTest;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.TickEvent.PlayerTickEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@Mod(Reference.MOD_ID)
public class DurabilityNotifier {

	public DurabilityNotifier() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, DurabilityConfig.clientSpec);
		FMLJavaModLoadingContext.get().getModEventBus().register(DurabilityConfig.class);

		if (FMLEnvironment.dist == Dist.CLIENT) {
			NeoForge.EVENT_BUS.addListener(this::onLeftClickBlock);
			NeoForge.EVENT_BUS.addListener(this::onLeftClickEmpty);
			NeoForge.EVENT_BUS.addListener(this::onRightClickBlock);
			NeoForge.EVENT_BUS.addListener(this::onAttackEntity);
			NeoForge.EVENT_BUS.addListener(this::onInventoryTick);
		}

		//Make sure the mod being absent on the other network side does not cause the client to display the server as incompatible
		ModLoadingContext.get().registerExtensionPoint(DisplayTest.class, () ->
				new IExtensionPoint.DisplayTest(() -> "Trans Rights Are Human Rights", (remoteVersionString, networkBool) -> networkBool));
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

	private void onAttackEntity(final AttackEntityEvent event) {
		final Player player = event.getEntity();
		EventHandler.checkDurability(player.getMainHandItem(), player);
	}

	private void onInventoryTick(final PlayerTickEvent event) {
		if (event.phase == net.neoforged.neoforge.event.TickEvent.Phase.START) return;

		Player player = event.player;
		if (player.level().getGameTime() % 80 == 0) {
			if (DurabilityConfig.CLIENT.CheckArmor.get()) {
				for (ItemStack itemStack : player.getInventory().armor) {
					EventHandler.checkDurability(itemStack, player);
				}
			}
		}
	}
}