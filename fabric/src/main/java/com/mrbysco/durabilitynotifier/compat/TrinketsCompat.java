package com.mrbysco.durabilitynotifier.compat;

import com.mrbysco.durabilitynotifier.EventHandler;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class TrinketsCompat {
	/**
	 * Checks the durability of trinkets in the player's inventory
	 *
	 * @param player The player whose trinkets to check
	 */
	public static void checkTrinkets(Player player, List<String> armorFilter) {
		TrinketsApi.getTrinketComponent(player).ifPresent(component ->
				component.forEach((reference, itemStack) -> {
					if (armorFilter.isEmpty() || armorFilter.contains(BuiltInRegistries.ITEM.getKey(itemStack.getItem()).toString())) {
						EventHandler.checkDurability(itemStack, player);
					}
				})
		);
	}
}
