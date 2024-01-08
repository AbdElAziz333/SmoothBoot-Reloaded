package com.abdelaziz.smoothboot.mixin.client;

import com.abdelaziz.smoothboot.SmoothBoot;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import net.minecraft.client.Minecraft;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.server.WorldStem;
import net.minecraft.server.level.progress.ChunkProgressListenerFactory;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IntegratedServer.class)
public class IntegratedServerMixin {
    @Inject(method = "<init>", at = @At("RETURN"))
    public void onInit(Thread thread, Minecraft minecraft, LevelStorageSource.LevelStorageAccess levelStorageAccess,
                       PackRepository packRepository, WorldStem worldStem, MinecraftSessionService minecraftSessionService,
                       GameProfileRepository gameProfileRepository, GameProfileCache gameProfileCache,
                       ChunkProgressListenerFactory chunkProgressListenerFactory, CallbackInfo ci) {
        thread.setPriority(SmoothBoot.config.threadPriority.integratedServer);
        SmoothBoot.LOGGER.debug("Initialized integrated server thread");
    }
}