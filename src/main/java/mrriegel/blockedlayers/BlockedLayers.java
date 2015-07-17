package mrriegel.blockedlayers;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Vector;

import com.google.common.reflect.ClassPath;

import net.minecraft.entity.EntityLivingBase;
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
import mrriegel.blockedlayers.handler.ConfigurationHandler;
import mrriegel.blockedlayers.handler.LayerHandler16;
import mrriegel.blockedlayers.handler.LayerHandler32;
import mrriegel.blockedlayers.handler.LayerHandler64;
import mrriegel.blockedlayers.handler.SelfHandler16;
import mrriegel.blockedlayers.handler.SelfHandler32;
import mrriegel.blockedlayers.handler.SelfHandler64;
import mrriegel.blockedlayers.handler.SyncHandler;
import mrriegel.blockedlayers.init.ModBlocks;
import mrriegel.blockedlayers.packet.Packet;
import mrriegel.blockedlayers.packet.PacketSyncHandler;
import mrriegel.blockedlayers.proxy.IProxy;
import mrriegel.blockedlayers.reference.Reference;
import mrriegel.blockedlayers.utility.MyUtils;

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
	public static Vector<String> number = new Vector<String>();
	public static Vector<String> on = new Vector<String>();
	public static Vector<String> type = new Vector<String>();

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ConfigurationHandler.init(event.getSuggestedConfigurationFile());

		ModBlocks.init();
		network = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);
		network.registerMessage(PacketSyncHandler.class, Packet.class, 0,
				Side.CLIENT);

		MyUtils.fillVectors();
		System.out.println(names);

		/* entity list */
		final ClassLoader loader = Thread.currentThread()
				.getContextClassLoader();

		try {
			for (final ClassPath.ClassInfo info : ClassPath.from(loader)
					.getTopLevelClasses()) {
				
				if (info.getName().startsWith("net.minecraft.")) {
					System.out.println("kacke "+info.load());
					final Class<?> clazz = info.load();
					if (EntityLivingBase.class.isAssignableFrom(clazz)) {
						// System.out.println(clazz.getName());
						entitys.add(clazz);
					}

				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {

		FMLCommonHandler.instance().bus().register(new LayerHandler64());
		FMLCommonHandler.instance().bus().register(new LayerHandler32());
		FMLCommonHandler.instance().bus().register(new LayerHandler16());
		MinecraftForge.EVENT_BUS.register(new LayerHandler64());
		MinecraftForge.EVENT_BUS.register(new LayerHandler32());
		MinecraftForge.EVENT_BUS.register(new LayerHandler16());

		MinecraftForge.EVENT_BUS.register(new SelfHandler64());
		MinecraftForge.EVENT_BUS.register(new SelfHandler32());
		MinecraftForge.EVENT_BUS.register(new SelfHandler16());
		FMLCommonHandler.instance().bus().register(new SelfHandler64());
		FMLCommonHandler.instance().bus().register(new SelfHandler32());
		FMLCommonHandler.instance().bus().register(new SelfHandler16());

		MinecraftForge.EVENT_BUS.register(new SyncHandler());

	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}
}
