package mrriegel.blockedlayers.handler;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ConfigurationHandler {
	public static Configuration configuration;

	public static boolean bonus;
	public static boolean level;
	public static int level64;
	public static int level32;
	public static int level16;

	public static void init(File file) {
		if (configuration == null) {
			configuration = new Configuration(file);
		}
		try {
			configuration.load();

			bonus = configuration.get(Configuration.CATEGORY_GENERAL, "bonus",
					true, "reward items").getBoolean();
			level = configuration.get(Configuration.CATEGORY_GENERAL, "level",
					false, "needs level").getBoolean();
			level64 = configuration.get(Configuration.CATEGORY_GENERAL,
					"level64", 0, "amount of level").getInt();
			level32 = configuration.get(Configuration.CATEGORY_GENERAL,
					"level32", 10, "amount of level").getInt();
			level16 = configuration.get(Configuration.CATEGORY_GENERAL,
					"level16", 30, "amount of level").getInt();
		} catch (Exception e) {

		} finally {
			if (configuration.hasChanged()) {
				configuration.save();
			}
		}

	}

}
