package com.abdelaziz.smoothboot.config;

import net.minecraft.util.Mth;

public class SmoothBootConfig {
	private int bootstrapThreads = 1;
	private int mainThreads = Math.max(Runtime.getRuntime().availableProcessors() / 2, 1);

	private int gamePriority = 5;
	private int integratedServerPriority = 5;
	private int bootstrapPriority = 1;
	private int mainPriority = 1;
	private int ioPriority = 1;
	private int modLoadingPriority = 1;

	/**
	 * Make sure the config values are within range
	 */
	public void validate() {
		bootstrapThreads = Math.max(bootstrapPriority, 1);
		mainThreads = Math.max(mainThreads, 1);

		gamePriority = Mth.clamp(gamePriority, 1, 10);
		integratedServerPriority = Mth.clamp(integratedServerPriority, 1, 10);
		bootstrapPriority = Mth.clamp(bootstrapPriority, 1, 10);
		mainPriority = Mth.clamp(mainPriority, 1, 10);
		ioPriority = Mth.clamp(ioPriority, 1, 10);
		modLoadingPriority = Mth.clamp(modLoadingPriority, 1, 10);
	}

	public int getBootstrapThreads() {
		return bootstrapThreads;
	}

	public void setBootstrapThreads(int bootstrapThreads) {
		this.bootstrapThreads = bootstrapThreads;
	}

	public int getMainThreads() {
		return mainThreads;
	}

	public void setMainThreads(int mainThreads) {
		this.mainThreads = mainThreads;
	}

	public int getGamePriority() {
		return gamePriority;
	}

	public void setGamePriority(int gamePriority) {
		this.gamePriority = gamePriority;
	}

	public int getIntegratedServerPriority() {
		return integratedServerPriority;
	}

	public void setIntegratedServerPriority(int integratedServerPriority) {
		this.integratedServerPriority = integratedServerPriority;
	}

	public int getBootstrapPriority() {
		return bootstrapPriority;
	}

	public void setBootstrapPriority(int bootstrapPriority) {
		this.bootstrapPriority = bootstrapPriority;
	}

	public int getMainPriority() {
		return mainPriority;
	}

	public void setMainPriority(int mainPriority) {
		this.mainPriority = mainPriority;
	}

	public int getIoPriority() {
		return ioPriority;
	}

	public void setIoPriority(int ioPriority) {
		this.ioPriority = ioPriority;
	}

	public int getModLoadingPriority() {
		return modLoadingPriority;
	}

	public void setModLoadingPriority(int modLoadingPriority) {
		this.modLoadingPriority = modLoadingPriority;
	}
}
