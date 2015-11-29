package mrriegel.blockedlayers.packet;

import java.util.HashMap;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import mrriegel.blockedlayers.entity.PlayerInformation;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class SyncClientPacket implements IMessage,
		IMessageHandler<SyncClientPacket, IMessage> {
	HashMap<Integer, Boolean> layerBools;
	HashMap<String, Boolean> questBools;
	HashMap<String, Integer> questNums;
	String team;

	public SyncClientPacket() {
	}

	public SyncClientPacket(EntityPlayerMP player) {
		PlayerInformation pro = PlayerInformation.get(player);
		layerBools = pro.getLayerBools();
		questBools = pro.getQuestBools();
		questNums = pro.getQuestNums();
		team = pro.getTeam();
	}

	@Override
	public IMessage onMessage(SyncClientPacket message, MessageContext ctx) {
		PlayerInformation pro = PlayerInformation
				.get(Minecraft.getMinecraft().thePlayer);
		pro.setLayerBools(message.layerBools);
		pro.setQuestBools(message.questBools);
		pro.setQuestNums(message.questNums);
		pro.setTeam(message.team);
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.layerBools = new Gson().fromJson(ByteBufUtils.readUTF8String(buf),
				new TypeToken<HashMap<Integer, Boolean>>() {
				}.getType());
		this.questBools = new Gson().fromJson(ByteBufUtils.readUTF8String(buf),
				new TypeToken<HashMap<String, Boolean>>() {
				}.getType());
		this.questNums = new Gson().fromJson(ByteBufUtils.readUTF8String(buf),
				new TypeToken<HashMap<String, Integer>>() {
				}.getType());
		this.team = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		String lb = new Gson().toJson(layerBools);
		String qb = new Gson().toJson(questBools);
		String qn = new Gson().toJson(questNums);
		ByteBufUtils.writeUTF8String(buf, lb);
		ByteBufUtils.writeUTF8String(buf, qb);
		ByteBufUtils.writeUTF8String(buf, qn);
		ByteBufUtils.writeUTF8String(buf, team);

	}
}
