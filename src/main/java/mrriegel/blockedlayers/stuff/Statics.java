package mrriegel.blockedlayers.stuff;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import mrriegel.blockedlayers.entity.PlayerInformation;
import mrriegel.blockedlayers.handler.ConfigurationHandler;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;

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

	public static void validateQuests(ArrayList<Quest> lis) {
		ArrayList<String> names = new ArrayList<String>();
		for (Quest q : lis) {
			if (names.contains(q.getName()))
				throw new RuntimeException(q.getName() + " isn't unique");
			names.add(q.getName());
			if (q.getName().length() > 16)
				throw new RuntimeException(q.getName() + " is longer than 16");
			boolean item = false, block = false, entity = false;
			if (GameRegistry.findItem(q.getModID(), q.getObject()) != null
					|| q.getObject() == null || q.getModID() == null)
				item = true;
			if (GameRegistry.findBlock(q.getModID(), q.getObject()) != null
					|| q.getObject() == null || q.getModID() == null)
				block = true;
			if (EntityList.stringToClassMapping.containsKey(q.getObject())
					|| q.getObject() == null || q.getModID() == null)
				entity = true;
			if (!item && !block && !entity)
				throw new RuntimeException(q.getObject() + " doesn't exist");
			if (q.getLayer() < 1 || q.getLayer() > 255)
				throw new RuntimeException("layer out of range 1-255");
		}
	}

	public static void validateRewards(ArrayList<Reward> lis) {
		for (Reward r : lis) {
			if (!ConfigurationHandler.reward)
				break;
			for (String s : r.getRewards()) {
				if (Statics.string2Stack(s) == null)
					throw new RuntimeException(s + " is not available.");
			}

		}
	}

	public static void fillQuestsFirst(FileWriter fw) throws IOException {
		ArrayList<Quest> tmp = new ArrayList<Quest>();
		tmp.add(new Quest("apple", "eat", "apple", "minecraft", "eat apple",
				64, 0, 6));
		tmp.add(new Quest("quick", "eat", "potion", "minecraft", "swiftness",
				54, 8226, 1));
		tmp.add(new Quest("sheepy", "break", "wool", "minecraft", "pink", 54,
				6, 2));
		tmp.add(new Quest("logs", "break", "log", "minecraft", "woods", 64, -1,
				5));
		tmp.add(new Quest("pearl", "kill", "Enderman", "minecraft", "ender",
				54, 0, 2));
		tmp.add(new Quest("wither", "kill", "Skeleton", "minecraft", "coal",
				54, 1, 1));
		tmp.add(new Quest("rich", "harvest", "diamond", "minecraft", "bright",
				14, 0, 8));
		tmp.add(new Quest("wheat", "harvest", "wheat", "minecraft", "bread",
				64, 0, 4));
		tmp.add(new Quest("zombie", "loot", "rotten_flesh", "minecraft",
				"rotten", 54, 0, 2));
		tmp.add(new Quest("goldy", "own", "gold_ingot", "minecraft", "golden",
				54, 0, 8));
		tmp.add(new Quest("hunger", "consume", "cooked_chicken", "minecraft",
				"chick", 64, 0, 16));
		tmp.add(new Quest("xp", "xp", null, null, "green", 54, 0, 300));
		tmp.add(new Quest("hot", "find", "Desert", null, "without water", 34,
				0, 1));
		tmp.add(new Quest("bread", "craft", "bread", "minecraft", "ham", 54, 0,
				12));
		tmp.add(new Quest("charcoal", "craft", "coal", "minecraft", "fuel", 54,
				1, 8));
		fw.write(new Gson().toJson(tmp));
	}

	public static void fillRewardsFirst(FileWriter fw) throws IOException {
		ArrayList<Reward> ttt = new ArrayList<Reward>();
		ttt.add(new Reward(64,
				new ArrayList<String>(Arrays
						.asList(new String[] { "minecraft:stone_pickaxe:0:1",
								"minecraft:stone_shovel:0:1",
								"minecraft:stone_axe:0:1",
								"minecraft:cooked_beef:0:10" }))));
		ttt.add(new Reward(12, new ArrayList<String>(Arrays
				.asList(new String[] { "minecraft:golden_apple:0:5",
						"minecraft:golden_apple:1:1" }))));
		fw.write(new Gson().toJson(ttt));
	}
}
