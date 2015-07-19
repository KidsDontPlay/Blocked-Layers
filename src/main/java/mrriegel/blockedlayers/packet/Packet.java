package mrriegel.blockedlayers.packet;

import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map.Entry;

import mrriegel.blockedlayers.entity.PlayerInformation;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class Packet implements IMessage {
	public static HashMap<String, Boolean> layerBools = new HashMap<String, Boolean>();	

	public void setLayerBools(HashMap<String, Boolean> layerBools) {
		this.layerBools = layerBools;
	}

	public Packet() {
	}

	public Packet(EntityPlayer p) {
		PlayerInformation props = PlayerInformation.get(p);

		for (Entry<String, Boolean> entry : layerBools.entrySet()) {
			entry.setValue(props.getLayerBools().get(entry.getKey()));
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {

		for (Entry<String, Boolean> entry : layerBools.entrySet()) {
			entry.setValue(buf.readBoolean());
		}

	}

	@Override
	public void toBytes(ByteBuf buf) {
		for (Entry<String, Boolean> entry : layerBools.entrySet()) {
			buf.writeBoolean(entry.getValue());
		}

	}

}
