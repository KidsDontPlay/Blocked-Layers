package mrriegel.blockedlayers.handler;

import mrriegel.blockedlayers.Quest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class QuestDoneEvent extends PlayerEvent {
	public World world;

	public QuestDoneEvent(EntityPlayer player) {
		super(player);
		this.world = player.worldObj;
	}
}
