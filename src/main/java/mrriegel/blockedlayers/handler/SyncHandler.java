package mrriegel.blockedlayers.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import mrriegel.blockedlayers.entity.PlayerInformation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class SyncHandler {

	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event) {
		if (event.entity instanceof EntityPlayer
				&& !(event.entity instanceof FakePlayer)) {
			EntityPlayer player = (EntityPlayer) event.entity;
			if (PlayerInformation.get((EntityPlayer) event.entity) == null) {
				PlayerInformation.register(player);
			}
		}
	}

	@SubscribeEvent
	public void join(EntityJoinWorldEvent e) {
if(e.entity instanceof EntityPlayerMP){

			MinecraftForge.EVENT_BUS.post(new QuestDoneEvent(
					(EntityPlayer) e.entity));
}
	}

	@SubscribeEvent
	public void onCloning(PlayerEvent.Clone event) {
		PlayerInformation newInfo = PlayerInformation.get(event.entityPlayer);
		PlayerInformation oldInfo = PlayerInformation.get(event.original);
		newInfo.setLayerBools(oldInfo.getLayerBools());
		newInfo.setQuestBools(oldInfo.getQuestBools());
		newInfo.setQuestNums(oldInfo.getQuestNums());
		newInfo.setTeam(oldInfo.getTeam());

	}

	@SubscribeEvent
	public void syncTeams(QuestDoneEvent e) {
		if (e.world.isRemote)
			return;
		if (PlayerInformation.get(e.entityPlayer) == null)
			return;
		System.out.println("mmooooo");
		String ori = PlayerInformation.get(e.entityPlayer).getTeam();
		if (ori.equals(""))
			return;
		ArrayList<EntityPlayerMP> players = new ArrayList<EntityPlayerMP>();
		ArrayList<Boolean> bools = new ArrayList<Boolean>();
		for (Object o : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
			EntityPlayerMP p = (EntityPlayerMP) o;
			if (ori.equals(PlayerInformation.get(p).getTeam()))
				players.add(p);
		}
		HashMap<String, Boolean> fake = new PlayerInformation().getQuestBools();
		for (EntityPlayerMP pp : players) {
			for (Entry<String, Boolean> entry : PlayerInformation.get(pp)
					.getQuestBools().entrySet()) {
				if (entry.getValue())
					fake.put(entry.getKey(), true);
			}
		}
		for (EntityPlayerMP pp : players) {
			PlayerInformation.get(pp).setQuestBools(fake);
		}
	}
}
