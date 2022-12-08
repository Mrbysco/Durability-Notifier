package com.mrbysco.durabilitynotifier;

import com.mrbysco.durabilitynotifier.config.DurabilityConfig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.IExtensionPoint.DisplayTest;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Reference.MOD_ID)
public class DurabilityNotifier {

	public DurabilityNotifier() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, DurabilityConfig.clientSpec);
		FMLJavaModLoadingContext.get().getModEventBus().register(DurabilityConfig.class);

		MinecraftForge.EVENT_BUS.addListener(this::onLeftClickBlock);
		MinecraftForge.EVENT_BUS.addListener(this::onLeftClickEmpty);
		MinecraftForge.EVENT_BUS.addListener(this::onRightClickBlock);
		MinecraftForge.EVENT_BUS.addListener(this::onAttackEntity);
		MinecraftForge.EVENT_BUS.addListener(this::onInventoryTick);

		//Make sure the mod being absent on the other network side does not cause the client to display the server as incompatible
		ModLoadingContext.get().registerExtensionPoint(DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> "Trans Rights Are Human Rights", (remoteVersionString, networkBool) -> networkBool));
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
		if (event.phase == TickEvent.Phase.START) return;

		Player player = event.player;
		Level level = player.level;
		if (level.isClientSide && player.level.getGameTime() % 80 == 0) {
			if (DurabilityConfig.CLIENT.CheckArmor.get()) {
				for (ItemStack itemStack : player.getInventory().armor) {
					EventHandler.checkDurability(itemStack, player);
				}
			}
		}
	}
}