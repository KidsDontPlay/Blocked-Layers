package mrriegel.blockedlayers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import mrriegel.blockedlayers.handler.ConfigurationHandler;
import mrriegel.blockedlayers.handler.GuiHandler;
import mrriegel.blockedlayers.handler.KeyHandler;
import mrriegel.blockedlayers.handler.LayerHandler;
import mrriegel.blockedlayers.handler.PacketHandler;
import mrriegel.blockedlayers.handler.QuestHandler;
import mrriegel.blockedlayers.handler.SyncHandler;
import mrriegel.blockedlayers.proxy.CommonProxy;
import mrriegel.blockedlayers.stuff.MyCommand;
import mrriegel.blockedlayers.stuff.Quest;
import mrriegel.blockedlayers.stuff.Reward;
import mrriegel.blockedlayers.stuff.Statics;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

@Mod(modid = BlockedLayers.MOD_ID, name = BlockedLayers.MOD_NAME, version = BlockedLayers.VERSION)
public class BlockedLayers {
	public static final String MOD_ID = "BlockedLayers";
	public static final String MOD_NAME = "Blocked Layers";
	public static final String VERSION = "2.2";
	public static final String CLIENT_PROXY_CLASS = "mrriegel.blockedlayers.proxy.ClientProxy";
	public static final String COMMON_PROXY_CLASS = "mrriegel.blockedlayers.proxy.CommonProxy";

	@Mod.Instance(BlockedLayers.MOD_ID)
	public static BlockedLayers instance;

	@SidedProxy(clientSide = BlockedLayers.CLIENT_PROXY_CLASS, serverSide = BlockedLayers.COMMON_PROXY_CLASS)
	public static CommonProxy proxy;

	public ArrayList<Quest> questList;
	public HashMap<String, Quest> questMap;
	public ArrayList<Reward> rewardList;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) throws IOException {
		File configDir = new File(event.getModConfigurationDirectory(),
				"BlockedLayers");
		ConfigurationHandler.load(new File(configDir, "config.cfg"));

		File questFile = new File(configDir, "quests.json");
		if (!questFile.exists()) {
			questFile.createNewFile();
			FileWriter fw = new FileWriter(questFile);
			Statics.fillQuestsFirst(fw);
			fw.close();
		}

		questList = new Gson().fromJson(new BufferedReader(new FileReader(
				questFile)), new TypeToken<ArrayList<Quest>>() {
		}.getType());
		questMap = new HashMap<String, Quest>();
		for (Quest q : questList)
			questMap.put(q.getName(), q);

		File rewardFile = new File(configDir, "rewards.json");
		if (!rewardFile.exists()) {
			rewardFile.createNewFile();
			FileWriter fw = new FileWriter(rewardFile);
			Statics.fillRewardsFirst(fw);
			fw.close();
		}

		rewardList = new Gson().fromJson(new BufferedReader(new FileReader(
				rewardFile)), new TypeToken<ArrayList<Reward>>() {
		}.getType());

		PacketHandler.init();

	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new LayerHandler());
		MinecraftForge.EVENT_BUS.register(new QuestHandler());
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
		MinecraftForge.EVENT_BUS.register(new SyncHandler());
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new KeyHandler());
		proxy.registerHandlers();

	}

	@Mod.EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		event.registerServerCommand(new MyCommand());
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		Statics.validateQuests(questList);
		Statics.validateRewards(rewardList);
	}

}
