package com.mrbysco.durabilitynotifier;

import com.mrbysco.durabilitynotifier.config.DurabilityConfig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
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

		CommonClass.init();

		// Some code like events require special initialization from the
		// loader specific code.
		MinecraftForge.EVENT_BUS.addListener(this::onLeftClickBlock);
		MinecraftForge.EVENT_BUS.addListener(this::onLeftClickEmpty);
		MinecraftForge.EVENT_BUS.addListener(this::onRightClickBlock);

		//Make sure the mod being absent on the other network side does not cause the client to display the server as incompatible
		ModLoadingContext.get().registerExtensionPoint(DisplayTest.class, () ->
				new IExtensionPoint.DisplayTest(() -> "Trans Rights Are Human Rights",
						(remoteVersionString, networkBool) -> networkBool));
	}

	// This method exists as a wrapper for the code in the Common project.
	// It takes Forge's event object and passes the parameters along to
	// the Common listener.
	private void onLeftClickBlock(final PlayerInteractEvent.LeftClickBlock event) {
		ItemStack itemStack = event.getItemStack();
		Player player = event.getPlayer();
		EventHandler.checkDurability(itemStack, player);
	}

	private void onLeftClickEmpty(final PlayerInteractEvent.LeftClickEmpty event) {
		ItemStack itemStack = event.getItemStack();
		Player player = event.getPlayer();
		EventHandler.checkDurability(itemStack, player);
	}

	private void onRightClickBlock(final PlayerInteractEvent.RightClickBlock event) {
		ItemStack itemStack = event.getItemStack();
		Player player = event.getPlayer();
		EventHandler.checkDurability(itemStack, player);
	}
}