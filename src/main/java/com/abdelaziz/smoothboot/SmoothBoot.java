package com.abdelaziz.smoothboot;

import com.abdelaziz.smoothboot.config.ConfigHandler;
import com.abdelaziz.smoothboot.config.SmoothBootConfig;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@Mod(SmoothBoot.MOD_ID)
public class SmoothBoot {
	public static final String MOD_ID = "smoothboot";
	public static final String NAME = "Smooth Boot (Reloaded)";
	
	public static final Logger LOGGER = LogManager.getLogger(NAME);
	
	public static SmoothBootConfig config;

	public static boolean initConfig = false;
	public static boolean initBootstrap = false;
	public static boolean initMainWorker = false;
	public static boolean initIOWorker = false;
	
	// Called before mod initialization
	public static void regConfig() {
		// Init config
		try {
			config = ConfigHandler.readConfig();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		
		LOGGER.info(NAME + " config initialized");
	}

	/**
	 * From
	 */
	public static int getMaxBackgroundThreads() {
		String string = System.getProperty("max.bg.threads");
		if (string != null) {
			try {
				int i = Integer.parseInt(string);
				if (i >= 1 && i <= 255) {
					return i;
				}
			}
			catch (NumberFormatException ignored) {}
		}
		return 255;
	}
}
