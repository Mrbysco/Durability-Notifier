package com.mrbysco.durabilitynotifier.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;

public interface ClickAirCallback {
	Event<ClickAirCallback> EVENT = EventFactory.createArrayBacked(ClickAirCallback.class,
			(listeners) -> (player, hand) -> {
				for (ClickAirCallback event : listeners) {
					InteractionResult result = event.interact(player, hand);

					if (result != InteractionResult.PASS) {
						return result;
					}
				}

				return InteractionResult.PASS;
			}
	);

	InteractionResult interact(Player player, InteractionHand hand);
}
