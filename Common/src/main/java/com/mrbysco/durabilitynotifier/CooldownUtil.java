package com.mrbysco.durabilitynotifier;

import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CooldownUtil {
	public static Map<ItemStack, Long> cooldownMap = Collections.synchronizedMap(new HashMap<>());

	public static void putOnCooldown(ItemStack stack) {
		cooldownMap.put(stack, System.currentTimeMillis());
	}

	public static boolean isNotOnCooldown(ItemStack stack, long time) {
		if(isAvailable(stack, time)) {
			cooldownMap.put(stack, System.currentTimeMillis());
			return true;
		} else {
			return false;
		}
	}

	public static boolean isAvailable(ItemStack stack, Long time) {
		if(cooldownMap.containsKey(stack)) {
			long lastUsed = cooldownMap.get(stack);
			return System.currentTimeMillis() >= (lastUsed + time);
		}
		return true;
	}
}
