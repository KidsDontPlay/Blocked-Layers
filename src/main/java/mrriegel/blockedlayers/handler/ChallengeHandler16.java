package mrriegel.blockedlayers.handler;

import java.util.ArrayList;

import mrriegel.blockedlayers.entity.PlayerInformation;
import mrriegel.blockedlayers.utility.NBTHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ChallengeHandler16 {

	@SubscribeEvent
	public void collectQuarz(BreakEvent event) {
		EntityPlayer player = event.getPlayer();
		PlayerInformation pro = PlayerInformation.get(player);
		if (!pro.isQuarz() && event.block.equals(Blocks.quartz_ore)
				&& player.worldObj.provider.dimensionId == -1) {
			pro.setQuarz(true);
			player.addChatMessage(new ChatComponentText("Quarz done!"));

		}
	}

	@SubscribeEvent
	public void getEmerald(BreakEvent event) {
		EntityPlayer player = event.getPlayer();
		PlayerInformation pro = PlayerInformation.get(player);
		if (!pro.isEme() && event.block.equals(Blocks.emerald_ore)) {
			pro.setEme(true);
			player.addChatMessage(new ChatComponentText("Emerald done!"));

		}
	}

	@SubscribeEvent
	public void eatApple(PlayerUseItemEvent.Finish event) {
		EntityPlayer player = event.entityPlayer;
		PlayerInformation pro = PlayerInformation.get(player);
		if (player.getCurrentEquippedItem().getItem()
				.equals(Items.golden_apple)) {
			if (!player.worldObj.isRemote && !pro.isApple()) {

				pro.setApple(true);
				player.addChatMessage(new ChatComponentText(
						"Golden Apple done!"));

			}
		}

	}

	@SubscribeEvent
	public void drink(PlayerUseItemEvent.Finish event) {
		EntityPlayer player = event.entityPlayer;
		PlayerInformation pro = PlayerInformation.get(player);
		if (player.getCurrentEquippedItem().getItem().equals(Items.potionitem)
				&& player.getCurrentEquippedItem().getItemDamage() != 0) {
			if (!player.worldObj.isRemote && !pro.isPotion()) {

				pro.setPotion(true);
				player.addChatMessage(new ChatComponentText("Potion done!"));

			}
		}

	}

	@SubscribeEvent
	public void killBlaze(LivingDeathEvent event) {
		Entity e = event.entity;
		DamageSource source = event.source;

		if (source.getSourceOfDamage() instanceof EntityPlayer
				&& e instanceof EntityBlaze) {
			EntityPlayer player = (EntityPlayer) source.getSourceOfDamage();
			PlayerInformation pro = PlayerInformation.get(player);
			if (!pro.isBlaze()) {
				pro.setBlazeNum(pro.getBlazeNum() + 1);
				if (pro.getBlazeNum() == 5) {
					pro.setBlaze(true);
					player.addChatMessage(new ChatComponentText("Blaze done!"));

				}
			}
		}
	}

	@SubscribeEvent
	public void killCube(LivingDeathEvent event) {
		Entity e = event.entity;
		DamageSource source = event.source;

		if (source.getSourceOfDamage() instanceof EntityPlayer
				&& e instanceof EntityMagmaCube) {
			EntityPlayer player = (EntityPlayer) source.getSourceOfDamage();
			PlayerInformation pro = PlayerInformation.get(player);
			if (!pro.isCube()) {
				pro.setCubeNum(pro.getCubeNum() + 1);
				if (pro.getCubeNum() == 5) {
					pro.setCube(true);
					player.addChatMessage(new ChatComponentText("Cube done!"));

				}
			}
		}
	}

	@SubscribeEvent
	public void last(PlayerInteractEvent event) {
		EntityPlayer player = event.entityPlayer;
		PlayerInformation pro = PlayerInformation.get(player);
		World world = player.worldObj;
		if (!player.worldObj.isRemote && pro.isL32() && !pro.isL16()
				&& pro.isQuarz() && pro.isEme() && pro.isApple()
				&& pro.isPotion() && pro.isBlaze() && pro.isCube()) {

			pro.setL16(true);
			player.addChatMessage(new ChatComponentText("Layer16 released!"));

			if (ConfigurationHandler.bonus) {
				ArrayList<ItemStack> lis = new ArrayList<ItemStack>();
				lis.add(new ItemStack(Items.cooked_beef, 64));
				ItemStack s = new ItemStack(Items.iron_pickaxe);
				s.addEnchantment(Enchantment.efficiency, 3);
				s.addEnchantment(Enchantment.unbreaking, 3);
				s.addEnchantment(Enchantment.looting, 2);
				lis.add(s);
				lis.add(new ItemStack(Items.golden_apple));
				lis.add(new ItemStack(Blocks.glowstone, 32));
				lis.add(new ItemStack(Items.nether_wart, 16));
				lis.add(new ItemStack(Items.blaze_rod, 4));
				lis.add(new ItemStack(Items.ghast_tear));
				for (ItemStack st : lis) {
					EntityItem i = new EntityItem(world, player.posX,
							player.posY, player.posZ, st);
					world.spawnEntityInWorld(i);

				}
			}
		}

	}
}
