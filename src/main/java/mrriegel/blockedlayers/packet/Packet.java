package mrriegel.blockedlayers.packet;

import mrriegel.blockedlayers.entity.PlayerInformation;
import net.minecraft.entity.player.EntityPlayer;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class Packet implements IMessage {
	public static boolean l64;
	public static boolean l32;
	public static boolean l16;

	public Packet() {
	}

	public Packet(EntityPlayer p) {
		PlayerInformation props = PlayerInformation.get(p);
		this.l64=props.isL64();
		this.l32=props.isL32();
		this.l16=props.isL16();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		
		this.l64 = buf.readBoolean();
		this.l32 = buf.readBoolean();
		this.l16 = buf.readBoolean();

	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(l64);
		buf.writeBoolean(l32);
		buf.writeBoolean(l16);

	}

}
