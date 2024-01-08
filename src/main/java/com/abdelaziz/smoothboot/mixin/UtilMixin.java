package com.abdelaziz.smoothboot.mixin;

import com.abdelaziz.smoothboot.SmoothBoot;
import com.abdelaziz.smoothboot.util.LoggingForkJoinWorkerThread;
import com.google.common.base.Objects;
import net.minecraft.Util;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Mixin(Util.class)
public abstract class UtilMixin {
	@Shadow @Final @Mutable
	private static ExecutorService BACKGROUND_EXECUTOR;

	@Shadow @Final @Mutable
	private static ExecutorService IO_POOL;

	@Shadow @Final
	private static AtomicInteger WORKER_COUNT;

	@Shadow
	private static native void onThreadException(Thread thread, Throwable throwable);

	// Probably not ideal, but this is the only way I found to modify createWorker without causing errors.
	// Redirecting or overwriting causes static initialization to be called too early resulting in NullPointerException being thrown.

	@Inject(method = "backgroundExecutor", at = @At("HEAD"))
	private static void onGetMainWorkerExecutor(CallbackInfoReturnable<Executor> ci) {
		if (!SmoothBoot.initMainWorker) {
			BACKGROUND_EXECUTOR = replaceWorker("Main");
			SmoothBoot.LOGGER.debug("Replaced Main Executor");
			SmoothBoot.initMainWorker = true;
		}
	}

	@Inject(method = "ioPool", at = @At("HEAD"))
	private static void onGetIoWorkerExecutor(CallbackInfoReturnable<Executor> ci) {
		if (!SmoothBoot.initIOWorker) {
			IO_POOL = replaceIoWorker();
			SmoothBoot.LOGGER.debug("Replaced IO Executor");
			SmoothBoot.initIOWorker = true;
		}
	}

	// Replace createNamedService
	private static ExecutorService replaceWorker(String name) {
		if (!SmoothBoot.initConfig) {
			SmoothBoot.regConfig();
			SmoothBoot.initConfig = true;
		}

		//AtomicInteger atomicInteger = new AtomicInteger(1);

		return new ForkJoinPool(Mth.clamp(select(name, SmoothBoot.config.threadCount.bootstrap,
				SmoothBoot.config.threadCount.main), 1, 0x7fff), (forkJoinPool) -> {
			String workerName = "Worker-" + name + "-" + WORKER_COUNT.getAndIncrement();
			SmoothBoot.LOGGER.debug("Initialized " + workerName);

			ForkJoinWorkerThread forkJoinWorkerThread = new LoggingForkJoinWorkerThread(forkJoinPool, SmoothBoot.LOGGER);
			forkJoinWorkerThread.setPriority(select(name, SmoothBoot.config.threadPriority.bootstrap,
					SmoothBoot.config.threadPriority.main));
			forkJoinWorkerThread.setName(workerName);
			return forkJoinWorkerThread;
		}, UtilMixin::onThreadException, true);
	}

	private static ExecutorService replaceIoWorker() {
		//AtomicInteger atomicInteger = new AtomicInteger(1);

		return Executors.newCachedThreadPool((p_240978_0_) -> {
			String workerName = "IO-Worker-" + WORKER_COUNT.getAndIncrement();
			SmoothBoot.LOGGER.debug("Initialized " + workerName);

			Thread thread = new Thread(p_240978_0_);
			thread.setName(workerName);
			thread.setPriority(SmoothBoot.config.threadPriority.io);
			thread.setUncaughtExceptionHandler(UtilMixin::onThreadException);
			return thread;
		});
	}

	private static <T> T select(String name, T bootstrap, T main) {
		return Objects.equal(name, "Bootstrap") ? bootstrap : main;
	}
}