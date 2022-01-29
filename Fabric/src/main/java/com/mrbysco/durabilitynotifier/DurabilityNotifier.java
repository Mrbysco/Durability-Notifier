package com.mrbysco.durabilitynotifier;

import com.mrbysco.durabilitynotifier.callback.ClickAirCallback;
import com.mrbysco.durabilitynotifier.config.DurabilityConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;

public class DurabilityNotifier implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Use Fabric to bootstrap the Common mod.
        CommonClass.init();


        AutoConfig.register(DurabilityConfig.class, Toml4jConfigSerializer::new);
        AutoConfig.getConfigHolder(DurabilityConfig.class).registerSaveListener((manager, data) -> {
            DurabilityNotifier.refreshConfigValues();
            return InteractionResult.PASS;
        });

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

        ClickAirCallback.EVENT.register((player, hand) -> {
            EventHandler.checkDurability(player.getItemInHand(hand), player);
            return InteractionResult.PASS;
        });
    }

    public static void refreshConfigValues() {
        DurabilityConfig config = AutoConfig.getConfigHolder(DurabilityConfig.class).getConfig();
        EventHandler.percentage = config.general.percentage;

        EventHandler.sendMessage = config.message.sendMessage;
        EventHandler.messageColor = config.message.messageColor;

        EventHandler.playSound = config.sound.playSound;
        EventHandler.soundVolume = (float)config.sound.volume;
        ResourceLocation soundLocation = ResourceLocation.tryParse(config.sound.soundLocation);
        if(soundLocation != null) {
            SoundEvent sound = Registry.SOUND_EVENT.get(soundLocation);
            if (sound != null) {
                EventHandler.soundLocation = soundLocation;
                EventHandler.chosenSound = sound;
            } else {
                EventHandler.soundLocation = SoundEvents.NOTE_BLOCK_PLING.getLocation();
                EventHandler.chosenSound = SoundEvents.NOTE_BLOCK_PLING;
                config.sound.soundLocation = SoundEvents.NOTE_BLOCK_PLING.getLocation().toString();
                Reference.LOGGER.warn("Could not locate the following sound: " + soundLocation + ". Perhaps you misspelled it. Falling back to default!");
            }
        }
    }
}
