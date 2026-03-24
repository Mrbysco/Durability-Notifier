package com.mrbysco.durabilitynotifier;

import com.mrbysco.durabilitynotifier.callback.ClickAirCallback;
import com.mrbysco.durabilitynotifier.callback.PlayerTickCallback;
import com.mrbysco.durabilitynotifier.config.DurabilityConfig;
import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.fml.config.ModConfig;

import java.util.List;

public class DurabilityNotifier implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ConfigRegistry.INSTANCE.register(Reference.MOD_ID, ModConfig.Type.CLIENT, DurabilityConfig.clientSpec);

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

		ClickAirCallback.LEFT_CLICK_EVENT.register((player, hand) -> {
			EventHandler.checkDurability(player.getItemInHand(hand), player);
			return InteractionResult.PASS;
		});

		ClickAirCallback.RIGHT_CLICK_EVENT.register((player, hand) -> {
			EventHandler.checkDurability(player.getItemInHand(hand), player);
			return InteractionResult.PASS;
		});

		AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			EventHandler.checkDurability(player.getItemInHand(hand), player);
			return InteractionResult.PASS;
		});

		PlayerTickCallback.EVENT.register((player) -> {
			Level level = player.level();
			if (level.isClientSide() && level.getGameTime() % 80 == 0) {
				if (DurabilityConfig.CLIENT.CheckArmor.get()) {
					List<? extends String> armorFilter = DurabilityConfig.CLIENT.ArmorFilter.get();
					for (EquipmentSlot equipmentslot : EquipmentSlotGroup.ARMOR) {
						ItemStack itemStack = player.getItemBySlot(equipmentslot);
						if (armorFilter.isEmpty() || armorFilter.contains(BuiltInRegistries.ITEM.getKey(itemStack.getItem()).toString())) {
							EventHandler.checkDurability(itemStack, player);
						}
					}
				}
			}
			return InteractionResult.PASS;
		});
	}
}
