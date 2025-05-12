package com.mrbysco.durabilitynotifier.compat;

import com.mrbysco.durabilitynotifier.EventHandler;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.List;

public class CuriosCompat {
	/**
	 * Checks the durability of curios equipped on the player's inventory
	 *
	 * @param player The player whose curios slots to check
	 */
	public static void checkCurios(Player player, List<? extends String> armorFilter) {
		CuriosApi.getCuriosInventory(player).ifPresent(itemHandler -> {
			IItemHandlerModifiable equipped = itemHandler.getEquippedCurios();
			for (int i = 0; i < equipped.getSlots(); i++) {
				if (equipped.getStackInSlot(i).isEmpty()) {
					continue;
				}
				if (armorFilter.isEmpty() || armorFilter.contains(BuiltInRegistries.ITEM.getKey(equipped.getStackInSlot(i).getItem()).toString())) {
					EventHandler.checkDurability(equipped.getStackInSlot(i), player);
				}
			}
		});
	}
}
