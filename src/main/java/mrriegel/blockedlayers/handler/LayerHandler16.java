package mrriegel.blockedlayers.handler;

import mrriegel.blockedlayers.entity.PlayerInformation;
import mrriegel.blockedlayers.packet.Packet;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class LayerHandler16 {
	int layer = 16;

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
			if (!world.isRemote && pro.isL16()
					&& player.experienceLevel >= ConfigurationHandler.level16) {
				return;
			}
			if (world.isRemote && Packet.l16
					&& player.experienceLevel >= ConfigurationHandler.level16) {
				return;
			}
		} else {
			if (!world.isRemote && pro.isL16()) {
				return;
			}
			if (world.isRemote && Packet.l16) {
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
