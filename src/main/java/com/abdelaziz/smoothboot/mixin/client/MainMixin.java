package com.abdelaziz.smoothboot.mixin.client;

import java.io.IOException;

import com.abdelaziz.smoothboot.SmoothBoot;
import com.abdelaziz.smoothboot.SmoothBootState;
import com.abdelaziz.smoothboot.config.SmoothBootConfigHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.main.Main;

@Mixin(Main.class)
public class MainMixin {
	@Inject(method = "main", at = @At("HEAD"), remap = false)
	private static void onMain(CallbackInfo ci) throws IOException {
		if (!SmoothBootState.initConfig) {
			SmoothBootConfigHandler.readConfig();
			SmoothBootState.initConfig = true;
		}

		Thread.currentThread().setPriority(SmoothBootConfigHandler.config.getGamePriority());
		SmoothBoot.LOGGER.debug("Initialized client game thread");
	}
}