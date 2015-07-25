package mrriegel.blockedlayers.handler;

import mrriegel.blockedlayers.entity.PlayerInformation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.player.PlayerEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class SyncHandler {

	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event) {
		if (event.entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entity;
			if (PlayerInformation.get((EntityPlayer) event.entity) == null) {
				PlayerInformation.register(player);
			}
		}
	}

	@SubscribeEvent
	public void onCloning(PlayerEvent.Clone event) {
		PlayerInformation newInfo = PlayerInformation.get(event.entityPlayer);
		PlayerInformation oldInfo = PlayerInformation.get(event.original);
		newInfo.setLayerBools(oldInfo.getLayerBools());
		newInfo.setQuestBools(oldInfo.getQuestBools());
		newInfo.setQuestNums(oldInfo.getQuestNums());

	}

}
