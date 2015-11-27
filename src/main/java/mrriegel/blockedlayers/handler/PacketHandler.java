package mrriegel.blockedlayers.handler;

import mrriegel.blockedlayers.reference.Reference;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class PacketHandler {
	public static final SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(
			Reference.MOD_ID);

	public static void init() {
		int id = 0;

	}
}
