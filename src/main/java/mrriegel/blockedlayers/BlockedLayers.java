package mrriegel.blockedlayers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import mrriegel.blockedlayers.handler.ConfigurationHandler;
import mrriegel.blockedlayers.handler.LayerHandler;
import mrriegel.blockedlayers.handler.MyCommand;
import mrriegel.blockedlayers.handler.PacketHandler;
import mrriegel.blockedlayers.handler.QuestHandler;
import mrriegel.blockedlayers.handler.SyncHandler;
import mrriegel.blockedlayers.packet.Packet;
import mrriegel.blockedlayers.packet.PacketSyncHandler;
import mrriegel.blockedlayers.proxy.IProxy;
import mrriegel.blockedlayers.reference.Reference;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class BlockedLayers {

	@Mod.Instance(Reference.MOD_ID)
	public static BlockedLayers instance;

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static IProxy proxy;

	public static SimpleNetworkWrapper network;
	public ArrayList<Quest> questList;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) throws IOException {
		File configDir = new File(event.getModConfigurationDirectory(),
				"BlockedLayers");
		ConfigurationHandler.load(new File(configDir, "config.cfg"));

		File questFile = new File(configDir, "quests.json");
		ArrayList<Quest> tmp = new ArrayList<Quest>();
		tmp.add(new Quest("uni", "break", "block", "minecraft", 64, 0, 12));
		tmp.add(new Quest("uniq", "breako", "tier", "AE2", 22, 2, 8));
		if (!questFile.exists()) {
			questFile.createNewFile();
			FileWriter fw = new FileWriter(questFile);
			fw.write(new Gson().toJson(tmp));
			fw.close();
		}

		questList = new Gson().fromJson(new BufferedReader(new FileReader(
				questFile)), new TypeToken<ArrayList<Quest>>() {
		}.getType());

		PacketHandler.init();

	}

	@Mod.EventHandler
	public void serverLoad(FMLServerStartingEvent event) {

		// event.registerServerCommand(new MyCommand());
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {

		MinecraftForge.EVENT_BUS.register(new LayerHandler());

		// MinecraftForge.EVENT_BUS.register(new QuestHandler());
		// FMLCommonHandler.instance().bus().register(new QuestHandler());

		MinecraftForge.EVENT_BUS.register(new SyncHandler());
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);

	}

	@SubscribeEvent
	public void bre(BlockEvent.BreakEvent e) {
		if (!e.world.isRemote)
			e.setCanceled(true);
	}

	@SubscribeEvent
	public void cr(PlayerEvent.ItemCraftedEvent e) {
		if (!e.player.worldObj.isRemote) {
			ItemStack s = e.crafting.copy();
			if (s.stackSize == 0)
				s.stackSize += 1;
			System.out.println(s.copy());
		}
	}

}
