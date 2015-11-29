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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.StringUtils;

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

		File rewardFile = new File(configDir, "rewards.json");
		ArrayList<Reward> ttt = new ArrayList<Reward>();
		ttt.add(new Reward(12, new ArrayList<String>(Arrays
				.asList(new String[] { "kakck", "miea", "läu" }))));
		ttt.add(new Reward(24, new ArrayList<String>(Arrays
				.asList(new String[] { "mau", "erk" }))));
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

	void validate(ArrayList<Quest> lis) {
		ArrayList<String> names = new ArrayList<String>();
		for (Quest q : lis) {
			if (names.contains(q.name))
				throw new RuntimeException(q.name + " isn't unique");
			names.add(q.name);
			if (q.name.length() > 10)
				throw new RuntimeException(q.name + " is longer than 10");
			boolean item = false, block = false, entity = false;
			if (GameRegistry.findItem(q.modID, q.object) != null)
				item = true;
			if (GameRegistry.findBlock(q.modID, q.object) != null)
				block = true;
			if (EntityList.stringToClassMapping.containsKey(q.object))
				entity = true;
			if (!item && !block && !entity)
				throw new RuntimeException(q.object + " doesn't exist");
			if (q.layer < 1 || q.layer > 255)
				throw new RuntimeException("layer out of range 1-255");
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

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {

		MinecraftForge.EVENT_BUS.register(new LayerHandler());

		MinecraftForge.EVENT_BUS.register(new QuestHandler());
		FMLCommonHandler.instance().bus().register(new QuestHandler());
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
		MinecraftForge.EVENT_BUS.register(new SyncHandler());
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
		FMLCommonHandler.instance().bus().register(new KeyHandler());
		proxy.registerHandlers();

	}

	@Mod.EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		event.registerServerCommand(new MyCommand());
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		validate(questList);
	}

	@SubscribeEvent
	public void bre(BlockEvent.BreakEvent e) {
		if (!e.world.isRemote) {
			// e.setCanceled(true);
			// PacketHandler.INSTANCE.sendTo(new SyncClientPacket(
			// (EntityPlayerMP) e.getPlayer()), (EntityPlayerMP) e
			// .getPlayer());

		}
	}

	@SubscribeEvent
	public void cr(LivingUpdateEvent e) {
		if (e.entityLiving instanceof EntityPlayer
				&& e.entityLiving.worldObj.isRemote) {
			// System.out.println(PlayerInformation.get(
			// (EntityPlayer) e.entityLiving).getQuestNums());
		}
	}

}
