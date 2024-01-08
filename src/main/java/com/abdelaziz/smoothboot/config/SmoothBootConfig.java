package com.abdelaziz.smoothboot.config;

import com.abdelaziz.smoothboot.SmoothBoot;
import net.minecraft.util.Mth;

public class SmoothBootConfig {
	public ThreadCount threadCount = new ThreadCount();
	public ThreadPriority threadPriority = new ThreadPriority();

	public static class ThreadCount {
		public int bootstrap = 1;
		public int main = Mth.clamp(Runtime.getRuntime().availableProcessors() - 1, 1, SmoothBoot.getMaxBackgroundThreads());
	}

	public static class ThreadPriority {
		public int game = 5;
		public int bootstrap = 1;
		public int main = 1;
		public int io = 1;
		public int integratedServer = 5;
		public int modLoading = 1;
	}

	public void validate() {
		threadCount.bootstrap = Math.max(threadCount.bootstrap, 1);
		threadCount.main = Math.max(threadCount.main, 1);

		threadPriority.game = Mth.clamp(threadPriority.game, 1, 10);
		threadPriority.integratedServer = Mth.clamp(threadPriority.integratedServer, 1, 10);
		threadPriority.bootstrap = Mth.clamp(threadPriority.bootstrap, 1, 10);
		threadPriority.main = Mth.clamp(threadPriority.main, 1, 10);
		threadPriority.io = Mth.clamp(threadPriority.io, 1, 10);
		threadPriority.modLoading = Mth.clamp(threadPriority.modLoading, 1,10);
	}
}
