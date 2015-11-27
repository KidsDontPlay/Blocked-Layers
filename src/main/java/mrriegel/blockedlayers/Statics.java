package mrriegel.blockedlayers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import mrriegel.blockedlayers.entity.PlayerInformation;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class Statics {
	public static void syncTeams(EntityPlayerMP player) {
		if (player.worldObj.isRemote)
			return;
		if (PlayerInformation.get(player) == null)
			return;
		String ori = PlayerInformation.get(player).getTeam();
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
