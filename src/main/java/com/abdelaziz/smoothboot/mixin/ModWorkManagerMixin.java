package com.abdelaziz.smoothboot.mixin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;

import com.abdelaziz.smoothboot.SmoothBoot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraftforge.fml.ModWorkManager;

@Mixin(ModWorkManager.class)
public class ModWorkManagerMixin {
    /**
     * @reason Allow settings priority.
     * @author AbdElAziz, UltimateBoomer
     */
    @Overwrite(remap = false)
    private static ForkJoinWorkerThread newForkJoinWorkerThread(ForkJoinPool pool) {
        ForkJoinWorkerThread thread = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
        String workerName = "modloading-worker-" + thread.getPoolIndex();
        SmoothBoot.LOGGER.debug("Initialized " + workerName);
        thread.setName(workerName);
        thread.setPriority(SmoothBoot.config.threadPriority.modLoading);
        thread.setContextClassLoader(Thread.currentThread().getContextClassLoader());
        return thread;
    }
}