package mrriegel.blockedlayers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import mrriegel.blockedlayers.handler.ConfigurationHandler;
import mrriegel.blockedlayers.handler.GuiHandler;
import mrriegel.blockedlayers.handler.KeyHandler;
import mrriegel.blockedlayers.handler.LayerHandler;
import mrriegel.blockedlayers.handler.PacketHandler;
import mrriegel.blockedlayers.handler.QuestHandler;
import mrriegel.blockedlayers.handler.SyncHandler;
import mrriegel.blockedlayers.proxy.CommonProxy;
import mrriegel.blockedlayers.reference.Reference;
import net.minecraft.entity.EntityList;
import net.minecraftforge.common.MinecraftForge;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class BlockedLayers {

	@Mod.Instance(Reference.MOD_ID)
	public static BlockedLayers instance;

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
	public static CommonProxy proxy;

	public ArrayList<Quest> questList;
	public ArrayList<Reward> rewardList;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) throws IOException {
		File configDir = new File(event.getModConfigurationDirectory(),
				"BlockedLayers");
		ConfigurationHandler.load(new File(configDir, "config.cfg"));

		File questFile = new File(configDir, "quests.json");
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
		if (!questFile.exists()) {
			questFile.createNewFile();
			FileWriter fw = new FileWriter(questFile);
			fw.write(new Gson().toJson(tmp));
			fw.close();
		}

		questList = new Gson().fromJson(new BufferedReader(new FileReader(
				questFile)), new TypeToken<ArrayList<Quest>>() {
		}.getType());

		File rewardFile = new File(configDir, "rewards.json");
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
		if (!rewardFile.exists()) {
			rewardFile.createNewFile();
			FileWriter fw = new FileWriter(rewardFile);
			fw.write(new Gson().toJson(ttt));
			fw.close();
		}

		rewardList = new Gson().fromJson(new BufferedReader(new FileReader(
				rewardFile)), new TypeToken<ArrayList<Reward>>() {
		}.getType());

		PacketHandler.init();

	}

	void validateQuests(ArrayList<Quest> lis) {
		ArrayList<String> names = new ArrayList<String>();
		for (Quest q : lis) {
			if (names.contains(q.getName()))
				throw new RuntimeException(q.getName() + " isn't unique");
			names.add(q.getName());
			if (q.getName().length() > 10)
				throw new RuntimeException(q.getName() + " is longer than 10");
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

	void validateRewards(ArrayList<Reward> lis) {
		for (Reward r : lis) {
			if (!ConfigurationHandler.reward)
				break;
			for (String s : r.getRewards()) {
				if (Statics.string2Stack(s) == null)
					throw new RuntimeException(s + " is not available.");
			}

		}
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {

		MinecraftForge.EVENT_BUS.register(new LayerHandler());

		MinecraftForge.EVENT_BUS.register(new QuestHandler());
		FMLCommonHandler.instance().bus().register(new QuestHandler());
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
		MinecraftForge.EVENT_BUS.register(new SyncHandler());
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(new KeyHandler());
		proxy.registerHandlers();

	}

	@Mod.EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		event.registerServerCommand(new MyCommand());
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		validateQuests(questList);
		validateRewards(rewardList);
	}

}
