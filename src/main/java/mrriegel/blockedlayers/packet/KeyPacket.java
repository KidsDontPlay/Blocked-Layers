package mrriegel.blockedlayers.packet;

import mrriegel.blockedlayers.BlockedLayers;
import mrriegel.blockedlayers.handler.PacketHandler;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class KeyPacket implements IMessage,
		IMessageHandler<KeyPacket, IMessage> {

	public KeyPacket() {
	}

	@Override
	public IMessage onMessage(KeyPacket message, MessageContext ctx) {
		PacketHandler.INSTANCE.sendTo(new SyncClientPacket(ctx.getServerHandler().playerEntity),
				ctx.getServerHandler().playerEntity);
		ctx.getServerHandler().playerEntity.openGui(BlockedLayers.instance, 0,
				ctx.getServerHandler().playerEntity.worldObj, 0, 0, 0);
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}

}
