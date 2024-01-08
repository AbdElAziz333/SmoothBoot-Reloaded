package com.abdelaziz.smoothboot.mixin.server;

import com.abdelaziz.smoothboot.SmoothBoot;
import net.minecraft.server.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Main.class)
public class MainMixin {
	@Inject(method = "main", at = @At("HEAD"), remap = false)
	private static void onMain(CallbackInfo ci) {
		if (!SmoothBoot.initConfig) {
			SmoothBoot.regConfig();
			SmoothBoot.initConfig = true;
		}

		Thread.currentThread().setPriority(SmoothBoot.config.threadPriority.game);
		SmoothBoot.LOGGER.debug("Initialized server game thread");
	}
}