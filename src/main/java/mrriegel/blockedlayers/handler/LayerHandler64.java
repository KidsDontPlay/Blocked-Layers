package mrriegel.blockedlayers.handler;

import java.util.ArrayList;

import mrriegel.blockedlayers.BlockedLayers;
import mrriegel.blockedlayers.entity.PlayerInformation;
import mrriegel.blockedlayers.init.ModBlocks;
import mrriegel.blockedlayers.packet.Packet;
import mrriegel.blockedlayers.utility.BlockLocation;
import mrriegel.blockedlayers.utility.MyUtils;
import mrriegel.blockedlayers.utility.NBTHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class LayerHandler64 {
	int layer = 64;

	@SubscribeEvent
	public void antiCheat(PlayerInteractEvent event) {
		EntityPlayer player = event.entityPlayer;
		PlayerInformation pro = PlayerInformation.get(player);
		if (!event.action.equals(Action.LEFT_CLICK_BLOCK)) {
			return;
		}
		World world = player.worldObj;
		MovingObjectPosition mop = Minecraft.getMinecraft().renderViewEntity
				.rayTrace(6, 1.0F);
		if (mop == null) {
			return;
		}
		Block block = world.getBlock(mop.blockX, mop.blockY, mop.blockZ);
		if (ConfigurationHandler.level) {
			if (!world.isRemote && pro.isL64()
					&& player.experienceLevel >= ConfigurationHandler.level64) {
				return;
			}
			if (world.isRemote && Packet.l64
					&& player.experienceLevel >= ConfigurationHandler.level64) {
				return;
			}
		} else {
			if (!world.isRemote && pro.isL64()) {
				return;
			}
			if (world.isRemote && Packet.l64) {
				return;
			}
		}

		if (!player.capabilities.isCreativeMode
				&& world.provider.dimensionId == 0) {
			if (!world.isRemote && mop.blockY < layer + 1) {
				System.out.println("cancelled");
				event.setCanceled(true);
			}
		}
	}

}
