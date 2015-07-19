package mrriegel.blockedlayers;

import java.util.Vector;

import mrriegel.blockedlayers.handler.ConfigurationHandler;
import mrriegel.blockedlayers.handler.LayerHandler;
import mrriegel.blockedlayers.handler.MyCommand;
import mrriegel.blockedlayers.handler.QuestHandler;
import mrriegel.blockedlayers.handler.SyncHandler;
import mrriegel.blockedlayers.packet.Packet;
import mrriegel.blockedlayers.packet.PacketSyncHandler;
import mrriegel.blockedlayers.proxy.IProxy;
import mrriegel.blockedlayers.reference.Reference;
import mrriegel.blockedlayers.utility.MyUtils;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
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

	public static Vector<Class> entitys = new Vector<Class>();

	public static Vector<String> names = new Vector<String>();
	public static Vector<String> layer = new Vector<String>();
	public static Vector<String> doIt = new Vector<String>();
	public static Vector<String> what = new Vector<String>();
	public static Vector<String> modID = new Vector<String>();
	public static Vector<String> meta = new Vector<String>();
	public static Vector<String> number = new Vector<String>();
	public static Vector<String> on = new Vector<String>();
	public static Vector<String> type = new Vector<String>();

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ConfigurationHandler.init(event.getSuggestedConfigurationFile());

		network = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);
		network.registerMessage(PacketSyncHandler.class, Packet.class, 0,
				Side.CLIENT);

		MyUtils.fillVectors();

	}

	@Mod.EventHandler
	public void serverLoad(FMLServerStartingEvent event) {

		event.registerServerCommand(new MyCommand());
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {

		MinecraftForge.EVENT_BUS.register(new LayerHandler());

		MinecraftForge.EVENT_BUS.register(new QuestHandler());
		FMLCommonHandler.instance().bus().register(new QuestHandler());

		MinecraftForge.EVENT_BUS.register(new SyncHandler());

	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}
}
