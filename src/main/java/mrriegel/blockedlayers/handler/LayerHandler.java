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
		Block block = world.getBlock(event.x, event.y, event.z);
		for (Entry<Integer, Boolean> entry : pro.getLayerBools().entrySet()) {
			int layer = entry.getKey();
			if (!world.isRemote && entry.getValue()) {
				continue;
			}

			if (!player.capabilities.isCreativeMode
					&& world.provider.dimensionId == 0) {
				if (!world.isRemote && event.y < layer + 1) {
					event.setCanceled(true);
				}
			}
		}
	}
}
