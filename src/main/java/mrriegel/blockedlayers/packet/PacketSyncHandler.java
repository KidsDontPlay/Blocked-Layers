package mrriegel.blockedlayers.packet;

import mrriegel.blockedlayers.BlockedLayers;
import mrriegel.blockedlayers.entity.PlayerInformation;
import mrriegel.blockedlayers.proxy.ServerProxy;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;

public class PacketSyncHandler implements IMessageHandler<Packet, IMessage> {

	@Override
	public IMessage onMessage(Packet message, MessageContext ctx) {
		return null;
	}

}
