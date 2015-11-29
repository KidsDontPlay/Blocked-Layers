package mrriegel.blockedlayers.handler;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ConfigurationHandler {

	public static Configuration config;
	public static boolean reward;
	public static boolean withoutSilk;
	public static boolean hard;
	public static boolean teams;

	public static void refreshConfig() {

		reward = config.get("Common", "reward", false, "Enable Rewards")
				.getBoolean();
		teams = config.get("Common", "teams", false, "Enable Teams")
				.getBoolean();
		withoutSilk = config
				.get("Common",
						"onlySilk",
						false,
						"If true, \"break\" only counts without silk touch. May only work with vanilla enchantment.")
				.getBoolean();
		hard = config
				.get("Common", "hard", false,
						"If true, quest can only be solved if any quest with higher layer is solved.")
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
