package mrriegel.blockedlayers.handler;

import java.util.Map.Entry;

import mrriegel.blockedlayers.entity.PlayerInformation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.world.BlockEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class LayerHandler {

	@SubscribeEvent
	public void disallow(BlockEvent.BreakEvent event) {
		boolean currentBlacklisted = isIn(ConfigurationHandler.dimensionBlack,
				event.world.provider.dimensionId);
		boolean currentWhitelisted = isIn(ConfigurationHandler.dimensionWhite,
				event.world.provider.dimensionId);
		String bool = ConfigurationHandler.dimensionBlack.length == 0 ? ConfigurationHandler.dimensionWhite.length == 0 ? "nothing"
				: "white"
				: "black";
		if (event.world.isRemote || event.getPlayer() == null
				|| event.getPlayer() instanceof FakePlayer
				|| event.getPlayer().capabilities.isCreativeMode
				|| (bool.equals("black") && currentBlacklisted)
				|| (bool.equals("white") && !currentWhitelisted))
			return;

		EntityPlayer player = event.getPlayer();
		PlayerInformation pro = PlayerInformation.get(player);
		for (Entry<Integer, Boolean> entry : pro.getLayerBools().entrySet()) {
			if (entry.getValue()) {
				continue;
			}
			if (event.y < entry.getKey() + 1) {
				event.setCanceled(true);
				return;
			}

		}
	}

	boolean isIn(int[] ar, int i) {
		for (int o : ar)
			if (i == o)
				return true;
		return false;
	}
}
