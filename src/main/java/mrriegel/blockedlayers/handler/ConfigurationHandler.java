package mrriegel.blockedlayers.handler;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ConfigurationHandler {
	public static Configuration configuration;

	public static void init(File file) {
		if (configuration == null) {
			configuration = new Configuration(file);
		}
		try {
			configuration.load();

		} catch (Exception e) {

		} finally {
			if (configuration.hasChanged()) {
				configuration.save();
			}
		}

	}

}
