package mrriegel.blockedlayers.handler;

import java.util.ArrayList;

import mrriegel.blockedlayers.entity.PlayerInformation;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

public class ChallengeHandler32 {

	@SubscribeEvent
	public void eatCake(PlayerEvent.ItemCraftedEvent event) {
		EntityPlayer player = event.player;
		PlayerInformation pro = PlayerInformation.get(player);
		World world = player.worldObj;

		if (!world.isRemote) {
			if (event.crafting.getItem().equals(Items.cake) && !pro.isCake()) {
				pro.setCake(true);
				player.addChatMessage(new ChatComponentText("Cake done!"));

			}
		}

	}

	@SubscribeEvent
	public void repair(AnvilRepairEvent event) {
		EntityPlayer player = event.entityPlayer;
		PlayerInformation pro = PlayerInformation.get(player);
		if (!player.worldObj.isRemote && !pro.isAnvil()) {
			pro.setAnvil(true);
			player.addChatMessage(new ChatComponentText("Anvil done!"));

		}
	}

	@SubscribeEvent
	public void killEnder(LivingDeathEvent event) {
		Entity e = event.entity;
		DamageSource source = event.source;

		if (source.getSourceOfDamage() instanceof EntityPlayer
				&& e instanceof EntityEnderman) {
			EntityPlayer player = (EntityPlayer) source.getSourceOfDamage();
			PlayerInformation pro = PlayerInformation.get(player);
			if (!pro.isEnder()) {
				pro.setEnderNum(pro.getEnderNum() + 1);
				if (pro.getEnderNum() == 5) {
					pro.setEnder(true);
					player.addChatMessage(new ChatComponentText("Ender done!"));

				}
			}
		}
	}

	@SubscribeEvent
	public void killBat(LivingDeathEvent event) {
		Entity e = event.entity;
		DamageSource source = event.source;

		if (source.getSourceOfDamage() instanceof EntityPlayer
				&& e instanceof EntityBat) {
			EntityPlayer player = (EntityPlayer) source.getSourceOfDamage();
			PlayerInformation pro = PlayerInformation.get(player);
			if (!pro.isBat()) {
				pro.setBat(true);
				player.addChatMessage(new ChatComponentText("Bat done!"));

			}
		}
	}

	@SubscribeEvent
	public void gearUp(PlayerInteractEvent event) {
		EntityPlayer player = event.entityPlayer;
		PlayerInformation pro = PlayerInformation.get(player);

		try {
			if (!player.worldObj.isRemote
					&& !pro.isArmor()
					&& player.inventory.armorInventory[3].getItem().equals(
							Items.iron_helmet)
					&& player.inventory.armorInventory[2].getItem().equals(
							Items.iron_chestplate)
					&& player.inventory.armorInventory[1].getItem().equals(
							Items.iron_leggings)
					&& player.inventory.armorInventory[0].getItem().equals(
							Items.iron_boots)) {
				pro.setArmor(true);
				player.addChatMessage(new ChatComponentText("Armor done!"));

			}
		} catch (NullPointerException e) {

		}

	}

	@SubscribeEvent
	public void ignite(PlayerInteractEvent event) {
		EntityPlayer player = event.entityPlayer;
		PlayerInformation pro = PlayerInformation.get(player);
		World world = player.worldObj;
		MovingObjectPosition mop = Minecraft.getMinecraft().renderViewEntity
				.rayTrace(6, 1.0F);

		if (mop != null && event.action.equals(Action.RIGHT_CLICK_BLOCK)
				&& !world.isRemote) {
			int blockHitSide = mop.sideHit;

			if (!pro.isIgnite()
					&& player.getCurrentEquippedItem() != null
					&& player.getCurrentEquippedItem().getItem()
							.equals(Items.flint_and_steel) && blockHitSide == 1) {
				pro.setIgnite(true);
				player.addChatMessage(new ChatComponentText("Lighter done!"));

			}
		}
	}

	@SubscribeEvent
	public void killCreeper(LivingDeathEvent event) {
		Entity e = event.entity;
		DamageSource source = event.source;

		if (source.getSourceOfDamage() instanceof EntityPlayer
				&& e instanceof EntityCreeper) {
			EntityPlayer player = (EntityPlayer) source.getSourceOfDamage();
			PlayerInformation pro = PlayerInformation.get(player);
			if (!pro.isCreeper()) {
				pro.setCreeperNum(pro.getCreeperNum() + 1);
				if (pro.getCreeperNum() == 10) {
					pro.setCreeper(true);
					player.addChatMessage(new ChatComponentText("Creeper done!"));

				}
			}
		}
	}

	@SubscribeEvent
	public void killSkel(LivingDeathEvent event) {
		Entity e = event.entity;
		DamageSource source = event.source;

		if (source.getSourceOfDamage() instanceof EntityPlayer
				&& e instanceof EntitySkeleton) {
			EntityPlayer player = (EntityPlayer) source.getSourceOfDamage();
			PlayerInformation pro = PlayerInformation.get(player);
			if (!pro.isSkeleton()) {
				pro.setSkeletonNum(pro.getSkeletonNum() + 1);
				if (pro.getSkeletonNum() == 10) {
					pro.setSkeleton(true);
					player.addChatMessage(new ChatComponentText(
							"Skeleton done!"));

				}
			}
		}
	}

	@SubscribeEvent
	public void getClay(BreakEvent event) {
		EntityPlayer player = event.getPlayer();
		PlayerInformation pro = PlayerInformation.get(player);
		if (!pro.isClay() && event.block.equals(Blocks.clay)) {
			pro.setClayNum(pro.getClayNum() + 1);
			if (pro.getClayNum() == 10) {
				pro.setClay(true);
				player.addChatMessage(new ChatComponentText("Clay done!"));

			}
		}
	}

	@SubscribeEvent
	public void last(PlayerInteractEvent event) {
		EntityPlayer player = event.entityPlayer;
		PlayerInformation pro = PlayerInformation.get(player);
		World world = player.worldObj;
		if (!player.worldObj.isRemote && pro.isL64() && !pro.isL32()
				&& pro.isCake() && pro.isAnvil() && pro.isEnder()
				&& pro.isBat() && pro.isArmor() && pro.isIgnite()
				&& pro.isCreeper() && pro.isSkeleton() && pro.isClay()) {

			pro.setL32(true);
			player.addChatMessage(new ChatComponentText("Layer32 released!"));

			if (ConfigurationHandler.bonus) {
				ArrayList<ItemStack> lis = new ArrayList<ItemStack>();
				lis.add(new ItemStack(Items.cooked_chicken, 64));
				ItemStack s = new ItemStack(Items.iron_sword);
				s.addEnchantment(Enchantment.sharpness, 2);
				s.addEnchantment(Enchantment.unbreaking, 2);
				s.addEnchantment(Enchantment.looting, 1);
				lis.add(s);
				lis.add(new ItemStack(Items.shears));
				lis.add(new ItemStack(Blocks.bookshelf, 5));
				lis.add(new ItemStack(Blocks.melon_block));
				lis.add(new ItemStack(Blocks.pumpkin));
				for (ItemStack st : lis) {
					EntityItem i = new EntityItem(world, player.posX,
							player.posY, player.posZ, st);
					world.spawnEntityInWorld(i);

				}
			}
		}

	}
}
