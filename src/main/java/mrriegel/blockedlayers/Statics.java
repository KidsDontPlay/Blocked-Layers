package mrriegel.blockedlayers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import mrriegel.blockedlayers.entity.PlayerInformation;
import mrriegel.blockedlayers.handler.ConfigurationHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.StringUtils;

import cpw.mods.fml.common.registry.GameRegistry;

public class Statics {
	public static void syncTeams(EntityPlayerMP player) {
		if (!ConfigurationHandler.teams || player.worldObj.isRemote
				|| PlayerInformation.get(player) == null)
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

	public static ItemStack string2Stack(String s) {
		ItemStack stack = null;
		if (StringUtils.countMatches(s, ":") == 3) {
			stack = GameRegistry.findItemStack(s.split(":")[0],
					s.split(":")[1], Integer.valueOf(s.split(":")[3]));
			if (stack != null) {
				stack.setItemDamage(Integer.valueOf(s.split(":")[2]));
			}
		} else if (StringUtils.countMatches(s, ":") == 1) {
			if (OreDictionary.doesOreNameExist(s.split(":")[0])) {
				stack = OreDictionary.getOres(s.split(":")[0]).get(0);
				stack.stackSize = Integer.valueOf(s.split(":")[1]);
			}
		} else
			throw new RuntimeException("wrong reward file");

		return stack;
	}
}
