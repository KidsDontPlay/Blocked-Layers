package mrriegel.blockedlayers.handler;

import java.util.ArrayList;

import mrriegel.blockedlayers.entity.PlayerInformation;
import mrriegel.blockedlayers.init.ModBlocks;
import mrriegel.blockedlayers.packet.Packet;
import mrriegel.blockedlayers.utility.BlockLocation;
import mrriegel.blockedlayers.utility.MyUtils;
import mrriegel.blockedlayers.utility.NBTHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class LayerHandler16 {
	int layer = 16;

	@SubscribeEvent
	public void handleLayer(TickEvent.PlayerTickEvent event) {
		EntityPlayer player = event.player;
		PlayerInformation pro = PlayerInformation.get(player);

		World world = player.worldObj;
		Block target = world.getBlock(trueVal(player.posX), layer,
				trueVal(player.posZ));
		if (ConfigurationHandler.level) {
			if (!world.isRemote && pro.isL16()
					&& player.experienceLevel >= ConfigurationHandler.level16) {
				if (target.getUnlocalizedName().equals(
						ModBlocks.un.getUnlocalizedName())) {
					world.setBlock(trueVal(player.posX), layer,
							trueVal(player.posZ), Blocks.air);
				}
				return;
			}
			if (world.isRemote && Packet.l16
					&& player.experienceLevel >= ConfigurationHandler.level16) {
				if (target.getUnlocalizedName().equals(
						ModBlocks.un.getUnlocalizedName())) {
					world.setBlock(trueVal(player.posX), layer,
							trueVal(player.posZ), Blocks.air);
				}
				return;
			}
		} else {
			if (!world.isRemote && pro.isL16()) {
				if (target.getUnlocalizedName().equals(
						ModBlocks.un.getUnlocalizedName())) {
					world.setBlock(trueVal(player.posX), layer,
							trueVal(player.posZ), Blocks.air);
				}
				return;
			}
			if (world.isRemote && Packet.l16) {
				if (target.getUnlocalizedName().equals(
						ModBlocks.un.getUnlocalizedName())) {
					world.setBlock(trueVal(player.posX), layer,
							trueVal(player.posZ), Blocks.air);
				}
				return;
			}
		}

		if (!player.capabilities.isCreativeMode
				&& world.provider.dimensionId == 0) {

			if (player.posY <= layer + 3) {
				if (!target.getMaterial().isSolid()) {
					world.setBlock(trueVal(player.posX), layer,
							trueVal(player.posZ), ModBlocks.un);

				}
			}
		}
		if (player.capabilities.isCreativeMode) {
			if (target.getUnlocalizedName().equals(
					ModBlocks.un.getUnlocalizedName())) {
				world.setBlock(trueVal(player.posX), layer,
						trueVal(player.posZ), Blocks.air);
			}
		}
		if (!(player.posY <= layer + 3)
				&& target.getUnlocalizedName().equals(
						ModBlocks.un.getUnlocalizedName())) {
			world.setBlock(trueVal(player.posX), layer, trueVal(player.posZ),
					Blocks.air);

		}

		for (BlockLocation bl : MyUtils.getAroundBlocks(world,
				trueVal(player.posX), layer, trueVal(player.posZ))) {
			if (world.getBlock(bl.x, bl.y, bl.z).getUnlocalizedName()
					.equals(ModBlocks.un.getUnlocalizedName())) {
				world.setBlock(bl.x, bl.y, bl.z, Blocks.air);

			}

		}
	}

	int x = 1;

	@SubscribeEvent
	public void antiCheat(TickEvent.PlayerTickEvent event) {
		EntityPlayer player = event.player;
		PlayerInformation pro = PlayerInformation.get(player);

		World world = player.worldObj;
		int pY = layer + 1;
		x++;

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

		if (x % 30 == 0 && !player.capabilities.isCreativeMode
				&& world.provider.dimensionId == 0) {
			x = 1;
			if (!world.isRemote && player.posY < layer + 1) {
				for (int y = layer + 1; y < 255; y++) {
					if ((world.getBlock(trueVal(player.posX), y,
							trueVal(player.posZ)).getMaterial() == Material.air)
							&& (world.getBlock(trueVal(player.posX), y + 1,
									trueVal(player.posZ)).getMaterial() == Material.air)
							&& !(world.getBlock(trueVal(player.posX), y - 1,
									trueVal(player.posZ)).getMaterial()
									.isLiquid())
							&& (world.getBlock(trueVal(player.posX), y - 1,
									trueVal(player.posZ)).getMaterial() != Material.air)) {
						pY = y;
						break;
					}
				}
				player.addChatMessage(new ChatComponentText("Don't be evil!"));
				player.setPositionAndUpdate(player.posX, pY, player.posZ);
			}
		}
	}

	@SubscribeEvent
	public void warning(TickEvent.PlayerTickEvent event) {
		EntityPlayer player = event.player;
		PlayerInformation pro = PlayerInformation.get(player);

		World world = player.worldObj;

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

		if (!world.isRemote && !player.capabilities.isCreativeMode
				&& world.provider.dimensionId == 0) {

			for (BlockLocation bl2 : MyUtils.getAroundBlocks(world,
					trueVal(player.posX), layer, trueVal(player.posZ))) {
				Block b = world.getBlock(bl2.x, bl2.y, bl2.z);
				if (b.getMaterial() != Material.air
						&& (!b.getMaterial().isSolid() || b
								.getUnlocalizedName().equals(
										"tile.pressurePlate"))) {
					ArrayList<ItemStack> lis = b.getDrops(world, bl2.x, bl2.y,
							bl2.z, world.getBlockMetadata(bl2.x, bl2.y, bl2.z),
							0);
					world.setBlock(bl2.x, bl2.y, bl2.z, Blocks.air);
					for (ItemStack stack : lis) {
						EntityItem i = new EntityItem(world, bl2.x, bl2.y,
								bl2.z, stack);
						world.spawnEntityInWorld(i);
					}
				}
			}
		}
	}

	private int trueVal(double num) {
		if (num < 0) {
			num--;
		}
		return (int) num;
	}
}