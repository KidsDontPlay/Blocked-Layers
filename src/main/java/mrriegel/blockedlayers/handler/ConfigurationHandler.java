package mrriegel.blockedlayers.handler;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ConfigurationHandler {

	public static Configuration config;

	public static void refreshConfig() {
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
