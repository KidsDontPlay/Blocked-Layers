package mrriegel.blockedlayers.packet;

import io.netty.buffer.ByteBuf;
import mrriegel.blockedlayers.handler.PacketHandler;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class KeyPacket implements IMessage,
		IMessageHandler<KeyPacket, IMessage> {

	public KeyPacket() {
	}

	@Override
	public IMessage onMessage(KeyPacket message, final MessageContext ctx) {
		IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.worldObj;
		mainThread.addScheduledTask(new Runnable() {
			@Override
			public void run() {
				PacketHandler.INSTANCE.sendTo(
						new SyncClientPacket(
								ctx.getServerHandler().playerEntity), ctx
								.getServerHandler().playerEntity);
			}
		});
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}

}
