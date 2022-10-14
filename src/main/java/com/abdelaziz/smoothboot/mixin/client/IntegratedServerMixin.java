package com.abdelaziz.smoothboot.mixin.client;

import com.abdelaziz.smoothboot.SmoothBoot;
import com.abdelaziz.smoothboot.config.SmoothBootConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.server.Services;
import net.minecraft.server.WorldStem;
import net.minecraft.server.level.progress.ChunkProgressListenerFactory;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IntegratedServer.class)
public class IntegratedServerMixin {
    @Inject(method = "<init>", at = @At("RETURN"))
    public void onInit(Thread p_235248_, Minecraft p_235249_, LevelStorageSource.LevelStorageAccess p_235250_,
                       PackRepository p_235251_, WorldStem p_235252_, Services p_235253_,
                       ChunkProgressListenerFactory p_235254_, CallbackInfo ci) {
        p_235248_.setPriority(SmoothBootConfigHandler.config.getIntegratedServerPriority());
        SmoothBoot.LOGGER.debug("Initialized integrated server thread");
    }
}