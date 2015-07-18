package mrriegel.blockedlayers.handler;

import mrriegel.blockedlayers.BlockedLayers;
import mrriegel.blockedlayers.entity.PlayerInformation;
import mrriegel.blockedlayers.packet.Packet;
import mrriegel.blockedlayers.proxy.ServerProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class SyncHandler {
	

	@SubscribeEvent
	public void onLivingDeathEvent(LivingDeathEvent event) {
		if (!event.entity.worldObj.isRemote
				&& event.entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entity;
			NBTTagCompound playerData = new NBTTagCompound();
			((PlayerInformation) (player
					.getExtendedProperties(PlayerInformation.EXT_PROP_NAME)))
					.saveNBTData(playerData);
			ServerProxy.storeEntityData(player.getCommandSenderName(),
					playerData);
			PlayerInformation.saveProxyData((EntityPlayer) event.entity);
		}
	}

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
	public void respawn(EntityJoinWorldEvent event) {
		if (!event.entity.worldObj.isRemote
				&& event.entity instanceof EntityPlayer && !event.entity.isDead) {
			EntityPlayer player = (EntityPlayer) event.entity;

			NBTTagCompound playerData = ServerProxy.getEntityData(player
					.getCommandSenderName());
			if (playerData != null) {
				(player.getExtendedProperties(PlayerInformation.EXT_PROP_NAME))
						.loadNBTData(playerData);
			}
			PlayerInformation props = PlayerInformation.get(player);

			/* sync */
			if (player instanceof EntityPlayerMP) {
				BlockedLayers.network.sendTo(new Packet(player),
						(EntityPlayerMP) player);
			}

			

		}

	}

	@SubscribeEvent
	public void sync(PlayerInteractEvent event) {

		EntityPlayer player = event.entityPlayer;
		if (player instanceof EntityPlayerMP) {
			BlockedLayers.network.sendTo(new Packet(player),
					(EntityPlayerMP) player);
		}
	}

}
