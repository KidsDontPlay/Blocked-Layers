package mrriegel.blockedlayers.handler;

import mrriegel.blockedlayers.packet.Packet;
import mrriegel.blockedlayers.packet.PacketSyncHandler;
import mrriegel.blockedlayers.reference.Reference;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class PacketHandler {
	public static final SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(
			Reference.MOD_ID);

	public static void init() {
		int id = 0;

		INSTANCE.registerMessage(PacketSyncHandler.class, Packet.class, id++,
				Side.CLIENT);
	}
}
