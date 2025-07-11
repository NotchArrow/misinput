package com.notcharrow.misinput.mixin;

import com.notcharrow.misinput.config.ConfigManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

	@Inject(method = "attack", at = @At("HEAD"), cancellable = true)
	private void init(Entity target, CallbackInfo ci) {
		if (target instanceof PlayerEntity) {
			String username = target.getName().getString().toLowerCase();
			List<String> usernames = ConfigManager.config.usernames;
			if (usernames.contains(username)) {
				PlayerEntity player = (PlayerEntity) (Object) this;
				player.playSound(SoundEvents.BLOCK_ANVIL_PLACE, 0.5f, 1.0f);
				ci.cancel();
			}
		}
	}
}