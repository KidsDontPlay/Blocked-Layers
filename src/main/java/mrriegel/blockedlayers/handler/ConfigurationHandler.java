package mrriegel.blockedlayers.handler;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ConfigurationHandler {

	public static Configuration config;
	public static boolean reward;
	public static boolean onlySilk;

	public static void refreshConfig() {

		reward = config.get("Common", "reward", false, "Enable Rewards")
				.getBoolean();
		onlySilk = config.get("Common", "onlySilk", false, "If true, break only counts with silk touch. May only work with vanilla enchantment")
				.getBoolean();
		if (config.hasChanged()) {
			config.save();
		}

	}

	public static void load(File file) {
		config = new Configuration(file);
		config.load();
		refreshConfig();
	}

}
