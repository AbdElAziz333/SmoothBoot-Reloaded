package com.abdelaziz.smoothboot.util;

import org.apache.logging.log4j.Logger;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;

public class LoggingForkJoinWorkerThread extends ForkJoinWorkerThread {
	private final Logger logger;

	public LoggingForkJoinWorkerThread(ForkJoinPool pool, Logger logger) {
		super(pool);
		this.logger = logger;
	}

	@Override
	protected void onTermination(Throwable throwable) {
		if (throwable != null) {
			logger.warn("{} died", this.getName(), throwable);
		} else {
			logger.debug("{} shutdown", this.getName());
		}

		super.onTermination(throwable);
	}
}