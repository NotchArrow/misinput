package com.notcharrow.misinput.mixin;

import com.notcharrow.misinput.config.ConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PlayerInteractEntityC2SPacket.class)
public class PlayerEntityMixin {

	@Inject(method = "attack", at = @At("HEAD"), cancellable = true)
	private static void init(Entity entity, boolean playerSneaking, CallbackInfoReturnable<PlayerInteractEntityC2SPacket> cir) {
		MinecraftClient client = MinecraftClient.getInstance();

		if (entity instanceof PlayerEntity player && client.player != null) {
			String username = player.getName().getString().toLowerCase();
			List<String> usernames = ConfigManager.config.usernames;
			for (String user: usernames) {
				if (user.toLowerCase().equals(username)) {
					client.player.playSound(SoundEvents.BLOCK_ANVIL_PLACE, 0.5f, 1.0f);
					cir.cancel();
					break;
				}
			}
		}
	}
}