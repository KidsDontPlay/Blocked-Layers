package mrriegel.blockedlayers.handler;

import java.util.ArrayList;
import java.util.List;

import mrriegel.blockedlayers.BlockedLayers;
import mrriegel.blockedlayers.entity.PlayerInformation;
import mrriegel.blockedlayers.init.ModBlocks;
import mrriegel.blockedlayers.packet.Packet;
import mrriegel.blockedlayers.reference.Reference;
import mrriegel.blockedlayers.utility.NBTHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ChallengeHandler64 {

	@SubscribeEvent
	public void eat(PlayerUseItemEvent.Finish event) {
		EntityPlayer player = event.entityPlayer;
		PlayerInformation pro = PlayerInformation.get(player);
		if (!player.worldObj.isRemote
				&& player.getCurrentEquippedItem().getItem()
						.equals(Items.cooked_beef)
				|| player.getCurrentEquippedItem().getItem()
						.equals(Items.cooked_porkchop)) {
			if (!pro.isSteak()) {
				pro.setSteakNum(pro.getSteakNum() + 1);
				if (pro.getSteakNum() == 5) {
					pro.setSteak(true);
					player.addChatMessage(new ChatComponentText("Meat done!"));

				}
			}
		}
	}

	@SubscribeEvent
	public void harvest(BreakEvent event) {
		EntityPlayer player = event.getPlayer();
		PlayerInformation pro = PlayerInformation.get(player);
		Block block = event.block;
		if (block.equals(Blocks.wheat)) {
			if (!pro.isWheat()) {
				pro.setWheatNum(pro.getWheatNum() + 1);
				if (pro.getWheatNum() == 10) {
					pro.setWheat(true);
					player.addChatMessage(new ChatComponentText("Wheat done!"));

				}
			}
		}
	}

	@SubscribeEvent
	public void till(UseHoeEvent event) {
		EntityPlayer player = event.entityPlayer;
		PlayerInformation pro = PlayerInformation.get(player);
		World world = player.worldObj;
		MovingObjectPosition mop = Minecraft.getMinecraft().renderViewEntity
				.rayTrace(6, 1.0F);

		if (mop != null && !world.isRemote) {
			Block b = world.getBlock(mop.blockX, mop.blockY, mop.blockZ);
			if ((b.equals(Blocks.dirt) || b.equals(Blocks.grass))
					&& !pro.isFarmland()) {
				pro.setFarmlandNum(pro.getFarmlandNum() + 1);
				if (pro.getFarmlandNum() == 20) {
					pro.setFarmland(true);
					player.addChatMessage(new ChatComponentText(
							"Farmland done!"));

				}
			}
		}

	}

	@SubscribeEvent
	public void breakStone(BreakEvent event) {
		EntityPlayer player = event.getPlayer();
		PlayerInformation pro = PlayerInformation.get(player);
		try {
			if (!pro.isStonepick()
					&& player.getCurrentEquippedItem().getItem()
							.equals(Items.stone_pickaxe)
					&& player.getCurrentEquippedItem().getItemDamage() > 129) {
				pro.setStonepick(true);
				player.addChatMessage(new ChatComponentText("Stone Pick done!"));

			}
		} catch (NullPointerException e) {
		}

	}

	@SubscribeEvent
	public void breakDirt(BreakEvent event) {
		EntityPlayer player = event.getPlayer();
		PlayerInformation pro = PlayerInformation.get(player);
		try {
			if (!pro.isStoneshovel()
					&& player.getCurrentEquippedItem().getItem()
							.equals(Items.stone_shovel)
					&& player.getCurrentEquippedItem().getItemDamage() > 129) {
				pro.setStoneshovel(true);
				player.addChatMessage(new ChatComponentText(
						"Stone Shovel done!"));

			}
		} catch (NullPointerException e) {
		}
	}

	@SubscribeEvent
	public void sleep(PlayerSleepInBedEvent event) {
		EntityPlayer player = event.entityPlayer;
		PlayerInformation pro = PlayerInformation.get(player);
		if (!player.worldObj.isRemote && !pro.isBed()) {
			pro.setBed(true);
			player.addChatMessage(new ChatComponentText("Bed done!"));

		}
	}

	@SubscribeEvent
	public void breed(EntityInteractEvent event) {
		EntityPlayer player = event.entityPlayer;
		PlayerInformation pro = PlayerInformation.get(player);
		Entity target = event.target;
		if (player.worldObj.isRemote) {
			return;
		}
		if (target instanceof EntityCow || target instanceof EntitySheep) {
			if (player.getCurrentEquippedItem().getItem().equals(Items.wheat)) {
				if (!pro.isBaby()) {
					pro.setBabyNum(pro.getBabyNum() + 1);
					if (pro.getBabyNum() == 2) {
						pro.setBaby(true);
						player.addChatMessage(new ChatComponentText(
								"Breeding done!"));

					}
				}
			}
		} else if (target instanceof EntityPig) {
			if (player.getCurrentEquippedItem().getItem().equals(Items.carrot)) {
				if (!pro.isBaby()) {
					pro.setBabyNum(pro.getBabyNum() + 1);
					if (pro.getBabyNum() == 2) {
						pro.setBaby(true);
						player.addChatMessage(new ChatComponentText(
								"\"Breeding done!\""));

					}
				}
			}
		} else if (target instanceof EntityChicken) {
			if (player.getCurrentEquippedItem().getItem()
					.equals(Items.wheat_seeds)) {
				if (!pro.isBaby()) {
					pro.setBabyNum(pro.getBabyNum() + 1);
					if (pro.getBabyNum() == 2) {
						pro.setBaby(true);
						player.addChatMessage(new ChatComponentText(
								"\"Breeding done!\""));

					}
				}
			}
		}
	}

	@SubscribeEvent
	public void killZombie(LivingDeathEvent event) {
		Entity e = event.entity;
		DamageSource source = event.source;

		if (source.getSourceOfDamage() instanceof EntityPlayer
				&& e instanceof EntityZombie) {
			EntityPlayer player = (EntityPlayer) source.getSourceOfDamage();
			PlayerInformation pro = PlayerInformation.get(player);
			if (!pro.isZombie()) {
				pro.setZombieNum(pro.getZombieNum() + 1);
				if (pro.getZombieNum() == 3) {
					pro.setZombie(true);
					player.addChatMessage(new ChatComponentText("Zombie done!"));

				}
			}
		}
	}

	@SubscribeEvent
	public void killSpider(LivingDeathEvent event) {
		Entity e = event.entity;
		DamageSource source = event.source;

		if (source.getSourceOfDamage() instanceof EntityPlayer
				&& e instanceof EntitySpider) {
			EntityPlayer player = (EntityPlayer) source.getSourceOfDamage();
			PlayerInformation pro = PlayerInformation.get(player);
			if (!pro.isSpider()) {
				pro.setSpiderNum(pro.getSpiderNum() + 1);
				if (pro.getSpiderNum() == 3) {
					pro.setSpider(true);
					player.addChatMessage(new ChatComponentText("Spider done!"));

				}
			}
		}
	}

	@SubscribeEvent
	public void last(PlayerInteractEvent event) {
		EntityPlayer player = event.entityPlayer;
		PlayerInformation pro = PlayerInformation.get(player);
		World world = player.worldObj;

		if (!player.worldObj.isRemote && !pro.isL64() && pro.isSteak()
				&& pro.isWheat() && pro.isBaby() && pro.isStonepick()
				&& pro.isStoneshovel() && pro.isBed() && pro.isZombie()
				&& pro.isSpider() && pro.isFarmland()) {

			pro.setL64(true);
			player.addChatMessage(new ChatComponentText("Layer64 released!"));

			if (ConfigurationHandler.bonus) {
				ArrayList<ItemStack> lis = new ArrayList<ItemStack>();
				lis.add(new ItemStack(Items.bucket));
				lis.add(new ItemStack(Items.bread, 10));
				lis.add(new ItemStack(Blocks.torch, 32));
				ItemStack s = new ItemStack(Items.bow);
				s.addEnchantment(Enchantment.power, 2);
				s.addEnchantment(Enchantment.unbreaking, 1);
				lis.add(s);
				lis.add(new ItemStack(Items.arrow, 64));
				lis.add(new ItemStack(Items.reeds));
				for (ItemStack st : lis) {
					EntityItem i = new EntityItem(world, player.posX,
							player.posY, player.posZ, st);
					world.spawnEntityInWorld(i);

				}
			}
		}

	}
}
