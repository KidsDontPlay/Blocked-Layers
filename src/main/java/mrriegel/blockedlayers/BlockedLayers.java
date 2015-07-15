package mrriegel.blockedlayers;

import net.minecraft.init.Blocks;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import mrriegel.blockedlayers.handler.ChallengeHandler16;
import mrriegel.blockedlayers.handler.ChallengeHandler32;
import mrriegel.blockedlayers.handler.ChallengeHandler64;
import mrriegel.blockedlayers.handler.ConfigurationHandler;
import mrriegel.blockedlayers.handler.LayerHandler16;
import mrriegel.blockedlayers.handler.LayerHandler32;
import mrriegel.blockedlayers.handler.LayerHandler64;
import mrriegel.blockedlayers.handler.SyncHandler;
import mrriegel.blockedlayers.init.ModBlocks;
import mrriegel.blockedlayers.packet.Packet;
import mrriegel.blockedlayers.packet.PacketSyncHandler;
import mrriegel.blockedlayers.proxy.IProxy;
import mrriegel.blockedlayers.reference.Reference;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class BlockedLayers {

	@Mod.Instance(Reference.MOD_ID)
	public static BlockedLayers instance;

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static IProxy proxy;

	public static SimpleNetworkWrapper network;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ConfigurationHandler.init(event.getSuggestedConfigurationFile());

		ModBlocks.init();
		network = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);
		network.registerMessage(PacketSyncHandler.class, Packet.class, 0,
				Side.CLIENT);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {

		FMLCommonHandler.instance().bus().register(new LayerHandler64());
		FMLCommonHandler.instance().bus().register(new LayerHandler32());
		FMLCommonHandler.instance().bus().register(new LayerHandler16());
		MinecraftForge.EVENT_BUS.register(new LayerHandler64());
		MinecraftForge.EVENT_BUS.register(new LayerHandler32());
		MinecraftForge.EVENT_BUS.register(new LayerHandler16());

		MinecraftForge.EVENT_BUS.register(new ChallengeHandler64());
		MinecraftForge.EVENT_BUS.register(new ChallengeHandler32());
		MinecraftForge.EVENT_BUS.register(new ChallengeHandler16());
		FMLCommonHandler.instance().bus().register(new ChallengeHandler64());
		FMLCommonHandler.instance().bus().register(new ChallengeHandler32());
		FMLCommonHandler.instance().bus().register(new ChallengeHandler16());

		MinecraftForge.EVENT_BUS.register(new SyncHandler());

	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}
}
