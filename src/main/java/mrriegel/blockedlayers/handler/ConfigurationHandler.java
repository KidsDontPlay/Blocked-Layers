package mrriegel.blockedlayers.handler;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ConfigurationHandler {

	public static Configuration config;
	public static boolean reward, withoutSilk, hard, teams;
	public static int[] dimensionBlack, dimensionWhite;

	public static void refreshConfig() {

		reward = config.get("Common", "reward", false, "Enable Rewards")
				.getBoolean();
		teams = config.get("Common", "teams", true, "Enable Teams")
				.getBoolean();
		withoutSilk = config
				.get("Common",
						"withoutSilk",
						false,
						"If true, \"break\" only counts without silk touch. May only work with vanilla enchantment.")
				.getBoolean();
		hard = config
				.get("Common", "hard", false,
						"If true, quest can only be solved if any quest with higher layer is solved.")
				.getBoolean();
		dimensionBlack = config.get("Common", "dimensionBlack",
				new int[] { -1, 1 }, "blacklist").getIntList();
		dimensionWhite = config.get("Common", "dimensionWhite", new int[] {},
				"whitelist").getIntList();
		if (config.hasChanged()) {
			config.save();
		}
		if (dimensionBlack.length != 0 && dimensionWhite.length != 0)
			throw new RuntimeException(
					"At least one of the lists have to be empty.");
	}

	public static void load(File file) {
		config = new Configuration(file);
		config.load();
		refreshConfig();
	}

}
