package com.abdelaziz.smoothboot.mixin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;

import com.abdelaziz.smoothboot.SmoothBoot;
import com.abdelaziz.smoothboot.config.SmoothBootConfigHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraftforge.fml.ModWorkManager;

@Mixin(ModWorkManager.class)
public class ModWorkManagerMixin {
    @Overwrite(remap = false)
    public static ForkJoinWorkerThread newForkJoinWorkerThread(ForkJoinPool pool) {
        ForkJoinWorkerThread thread = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
        String workerName = "modloading-worker-" + thread.getPoolIndex();
        SmoothBoot.LOGGER.debug("Initialized " + workerName);
        thread.setName("modloading-worker-" + thread.getPoolIndex());
        thread.setPriority(SmoothBootConfigHandler.config.getModLoadingPriority());
        thread.setContextClassLoader(Thread.currentThread().getContextClassLoader());
        return thread;
    }
}