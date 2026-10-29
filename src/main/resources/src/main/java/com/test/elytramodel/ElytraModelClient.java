package com.test.elytramodel;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

public class ElytraModelClient implements ClientModInitializer {
    private static int boostTicks = 0;

    @Override
    public void onInitializeClient() {
        UseBlockCallback.EVENT.register((player, level, hand, hit) -> {
            if (!level.isClientSide()) return InteractionResult.PASS;
            if (!player.getItemInHand(hand).is(Items.FIREWORK_ROCKET)) return InteractionResult.PASS;

            Direction face = hit.getDirection();
            if (face.getAxis().isHorizontal()) {
                boostTicks = 20;
            }
            return InteractionResult.PASS;
        });

        ClientTickEvents.END_CLIENT_TICK.register(mc -> {
            LocalPlayer p = mc.player;
            if (p == null || boostTicks <= 0) return;
            boostTicks--;

            Vec3 look = p.getLookAngle();
            Vec3 v = p.getDeltaMovement();
            p.setDeltaMovement(v.add(
                look.x * 0.1 + (look.x * 1.5 - v.x) * 0.5,
                look.y * 0.1 + (look.y * 1.5 - v.y) * 0.5,
                look.z * 0.1 + (look.z * 1.5 - v.z) * 0.5
            ));
        });
    }
}
