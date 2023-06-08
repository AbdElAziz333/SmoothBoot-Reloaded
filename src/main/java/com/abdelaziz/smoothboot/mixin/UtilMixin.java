package com.abdelaziz.smoothboot.mixin;

import com.abdelaziz.smoothboot.SmoothBoot;
import com.abdelaziz.smoothboot.config.SmoothBootConfig;
import com.abdelaziz.smoothboot.config.SmoothBootConfigHandler;
import com.abdelaziz.smoothboot.util.LoggingForkJoinWorkerThread;
import com.google.common.base.Objects;
import net.minecraft.Util;
import net.minecraft.util.Mth;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Mixin(Util.class)
public abstract class UtilMixin {
	private static boolean initMainWorkerExecutor = false;
	@Shadow
	@Final
	static Logger LOGGER;
	private static boolean initIoWorker = false;
	@Shadow
	@Final
	@Mutable
	private static ExecutorService BACKGROUND_EXECUTOR;
	@Shadow
	@Final
	@Mutable
	private static ExecutorService IO_POOL;
	@Shadow
	@Final
	private static AtomicInteger WORKER_COUNT;

	@Shadow
	private static void onThreadException(Thread thread, Throwable throwable) {
	}

	// Probably not ideal, but this is the only way I found to modify createWorker without causing errors.
	// Redirecting or overwriting causes static initialization to be called too early resulting in NullPointerException being thrown.

	@Inject(method = "backgroundExecutor", at = @At("HEAD"))
	private static void onGetMainWorkerExecutor(CallbackInfoReturnable<Executor> ci) {
		if (!initMainWorkerExecutor) {
			BACKGROUND_EXECUTOR = replMainWorker();
			initMainWorkerExecutor = true;
			SmoothBoot.LOGGER.debug("Replaced Main Executor");
		}
	}

	@Inject(method = "ioPool", at = @At("HEAD"))
	private static void onGetIoWorkerExecutor(CallbackInfoReturnable<Executor> ci) {
		if (!initIoWorker) {
			IO_POOL = replIoWorker();
			initIoWorker = true;
			SmoothBoot.LOGGER.debug("Replaced IO Executor");
		}
	}

	// Replace createNamedService
	private static ExecutorService replMainWorker() {
		if (SmoothBootConfigHandler.config == null) {
			try {
				SmoothBootConfigHandler.readConfig();
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}

		SmoothBootConfig config = SmoothBootConfigHandler.config;
		ExecutorService executorService2 = new ForkJoinPool(Mth.clamp(config.getMainThreads(), 1, 0x7fff), (forkJoinPool) -> {
			String workerName = "Worker-Main-" + WORKER_COUNT.getAndIncrement();
			SmoothBoot.LOGGER.debug("Initialized " + workerName);

			ForkJoinWorkerThread forkJoinWorkerThread = new LoggingForkJoinWorkerThread(forkJoinPool, LOGGER);
			forkJoinWorkerThread.setName(workerName);
			forkJoinWorkerThread.setPriority(config.getMainPriority());
			return forkJoinWorkerThread;
		}, UtilMixin::onThreadException, true);
		return executorService2;
	}

	private static ExecutorService replIoWorker() {
		return Executors.newCachedThreadPool((p_240978_0_) -> {
			String workerName = "IO-Worker-" + WORKER_COUNT.getAndIncrement();
			SmoothBoot.LOGGER.debug("Initialized " + workerName);

			Thread thread = new Thread(p_240978_0_);
			thread.setName(workerName);
			thread.setPriority(SmoothBootConfigHandler.config.getIoPriority());
			thread.setUncaughtExceptionHandler(UtilMixin::onThreadException);
			return thread;
		});
	}
}