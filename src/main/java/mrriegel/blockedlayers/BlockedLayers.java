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
import mrriegel.blockedlayers.stuff.MyCommand;
import mrriegel.blockedlayers.stuff.Quest;
import mrriegel.blockedlayers.stuff.Reward;
import mrriegel.blockedlayers.stuff.Statics;
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
import cpw.mods.fml.relauncher.ModListHelper;

@Mod(modid = BlockedLayers.MOD_ID, name = BlockedLayers.MOD_NAME, version = BlockedLayers.VERSION)
public class BlockedLayers {
	public static final String MOD_ID = "BlockedLayers";
	public static final String MOD_NAME = "Blocked Layers";
	public static final String VERSION = "1.7.10-2.0";
	public static final String CLIENT_PROXY_CLASS = "mrriegel.blockedlayers.proxy.ClientProxy";
	public static final String COMMON_PROXY_CLASS = "mrriegel.blockedlayers.proxy.CommonProxy";

	@Mod.Instance(BlockedLayers.MOD_ID)
	public static BlockedLayers instance;

	@SidedProxy(clientSide = BlockedLayers.CLIENT_PROXY_CLASS, serverSide = BlockedLayers.COMMON_PROXY_CLASS)
	public static CommonProxy proxy;

	public ArrayList<Quest> questList;
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
		Statics.validateQuests(questList);
		Statics.validateRewards(rewardList);
	}

}
