package com.mrbysco.durabilitynotifier.mixin;

import com.mrbysco.durabilitynotifier.callback.ClickAirCallback;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {

	@Inject(method = "useItem(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;ensureHasSentCarriedItem()V",
					shift = At.Shift.AFTER
			))
	private void rightClickAir(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
		ClickAirCallback.RIGHT_CLICK_EVENT.invoker().interact(player, hand);
	}
}
