package mrriegel.blockedlayers.handler;

import java.util.Map.Entry;

import mrriegel.blockedlayers.entity.PlayerInformation;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class LayerHandler {

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
		for (Entry<String, Boolean> entry : pro.getLayerBools().entrySet()) {
			int layer = Integer.parseInt(entry.getKey());
			if (!world.isRemote && entry.getValue()) {
				return;
			}

			if (!player.capabilities.isCreativeMode
					&& world.provider.dimensionId == 0) {
				if (!world.isRemote && mop.blockY < layer + 1) {
					event.setCanceled(true);
				}
			}
		}
	}
}
