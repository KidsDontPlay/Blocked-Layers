package mrriegel.blockedlayers.handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

import scala.reflect.internal.Trees.Super;

import com.google.common.reflect.ClassPath;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import mrriegel.blockedlayers.BlockedLayers;
import mrriegel.blockedlayers.entity.PlayerInformation;
import mrriegel.blockedlayers.utility.Hashmaps;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;

public class SelfHandler64 {

	@SubscribeEvent
	public void eatItem(PlayerUseItemEvent.Finish event) {
		for (int i = 0; i < BlockedLayers.layer.size(); i++) {
			if (!BlockedLayers.layer.get(i).equals("64")) {
				continue;
			}
			if (!BlockedLayers.doIt.get(i).equals("eat")) {
				continue;
			}
			Item target = Hashmaps.ItemHash.get(BlockedLayers.what.get(i));

			int number = Integer.valueOf(BlockedLayers.number.get(i));
			String name = BlockedLayers.names.get(i);
			EntityPlayer player = event.entityPlayer;
			PlayerInformation pro = PlayerInformation.get(player);

			if (!player.worldObj.isRemote
					&& player.getCurrentEquippedItem().getItem().equals(target)) {
				if (!pro.getBools().get(name)) {
					pro.getNums().put(name + "Num",
							pro.getNums().get(name + "Num") + 1);
					if (pro.getNums().get(name + "Num") == number) {
						pro.getBools().put(name, true);
						player.addChatMessage(new ChatComponentText(name
								.substring(0, 1).toUpperCase()
								+ name.substring(1) + " done!"));

					}
				}
			}
		}

	}

	@SubscribeEvent
	public void breakBlock(BreakEvent event) {
		for (int i = 0; i < BlockedLayers.layer.size(); i++) {
			if (!BlockedLayers.layer.get(i).equals("64")) {
				continue;
			}
			if (!BlockedLayers.doIt.get(i).equals("break")) {
				continue;
			}

			String name = BlockedLayers.names.get(i);
			Block target = Hashmaps.BlockHash.get(BlockedLayers.what.get(i));

			int number = Integer.valueOf(BlockedLayers.number.get(i));

			EntityPlayer player = event.getPlayer();
			PlayerInformation pro = PlayerInformation.get(player);

			if (!player.worldObj.isRemote && event.block.equals(target)) {
				if (!pro.getBools().get(name)) {
					pro.getNums().put(name + "Num",
							pro.getNums().get(name + "Num") + 1);
					if (pro.getNums().get(name + "Num") == number) {
						pro.getBools().put(name, true);
						player.addChatMessage(new ChatComponentText(name
								.substring(0, 1).toUpperCase()
								+ name.substring(1) + " done!"));

					}
				}
			}
		}
	}

	@SubscribeEvent
	public void kill(LivingDeathEvent event) {
		for (int i = 0; i < BlockedLayers.layer.size(); i++) {
			if (!BlockedLayers.layer.get(i).equals("64")) {
				continue;
			}
			if (!BlockedLayers.doIt.get(i).equals("kill")) {
				continue;
			}
			String name = BlockedLayers.names.get(i);
			String upperName = BlockedLayers.what.get(i).substring(0, 1)
					.toUpperCase()
					+ BlockedLayers.what.get(i).substring(1);
			Class target = Hashmaps.EntityHash.get(BlockedLayers.what.get(i));

			int number = Integer.valueOf(BlockedLayers.number.get(i));

			Entity e = event.entity;
			DamageSource source = event.source;

			if (!e.worldObj.isRemote
					&& source.getSourceOfDamage() instanceof EntityPlayer
					&& target.isInstance(e)) {
				EntityPlayer player = (EntityPlayer) source.getSourceOfDamage();
				PlayerInformation pro = PlayerInformation.get(player);
				if (!pro.getBools().get(name)) {
					pro.getNums().put(name + "Num",
							pro.getNums().get(name + "Num") + 1);
					if (pro.getNums().get(name + "Num") == number) {
						pro.getBools().put(name, true);
						player.addChatMessage(new ChatComponentText(name
								.substring(0, 1).toUpperCase()
								+ name.substring(1) + " done!"));

					}
				}
			}
		}
	}

	@SubscribeEvent
	public void use(EntityInteractEvent event) {
		for (int i = 0; i < BlockedLayers.layer.size(); i++) {
			if (!BlockedLayers.layer.get(i).equals("64")) {
				continue;
			}
			if (!BlockedLayers.doIt.get(i).equals("use")) {
				continue;
			}
			if (!BlockedLayers.type.get(i).equals("entity")) {
				continue;
			}
			String name = BlockedLayers.names.get(i);
			Item target = Hashmaps.ItemHash.get(BlockedLayers.what.get(i));

			int number = Integer.valueOf(BlockedLayers.number.get(i));

			Class classTarget = Hashmaps.EntityHash
					.get(BlockedLayers.on.get(i));

			EntityPlayer player = event.entityPlayer;
			PlayerInformation pro = PlayerInformation.get(player);
			Entity entTarget = event.target;
			if (player.worldObj.isRemote) {
				return;
			}
			if (classTarget.isInstance(entTarget)) {
				if (player.getCurrentEquippedItem().getItem().equals(target)) {
					if (!pro.getBools().get(name)) {
						pro.getNums().put(name + "Num",
								pro.getNums().get(name + "Num") + 1);
						if (pro.getNums().get(name + "Num") == number) {
							pro.getBools().put(name, true);
							player.addChatMessage(new ChatComponentText(name
									.substring(0, 1).toUpperCase()
									+ name.substring(1) + " done!"));

						}
					}
				}
			}

		}
	}

	@SubscribeEvent
	public void craft(PlayerEvent.ItemCraftedEvent event) {
		for (int i = 0; i < BlockedLayers.layer.size(); i++) {
			if (!BlockedLayers.layer.get(i).equals("64")) {
				continue;
			}
			if (!BlockedLayers.doIt.get(i).equals("craft")) {
				continue;
			}
			String name = BlockedLayers.names.get(i);
			Item target = Hashmaps.ItemHash.get(BlockedLayers.what.get(i));
			ItemStack stack = event.crafting;

			int number = Integer.valueOf(BlockedLayers.number.get(i));

			Block block = Hashmaps.BlockHash.get(BlockedLayers.what.get(i));

			EntityPlayer player = event.player;
			PlayerInformation pro = PlayerInformation.get(player);
			World world = player.worldObj;

			if (!world.isRemote && BlockedLayers.type.get(i).equals("item")) {
				if (event.crafting.getItem().equals(target)
						&& !pro.getBools().get(name)) {
					pro.getNums().put(name + "Num",
							pro.getNums().get(name + "Num") + stack.stackSize);
					if (pro.getNums().get(name + "Num") >= number) {
						pro.getBools().put(name, true);
						player.addChatMessage(new ChatComponentText(name
								.substring(0, 1).toUpperCase()
								+ name.substring(1) + " done!"));

					}
				}
			}

			ItemStack stackBlock = new ItemStack(block);
			Item itemBlock = stackBlock.getItem();
			if (!world.isRemote && BlockedLayers.type.get(i).equals("block")) {
				if (event.crafting.getItem().equals(itemBlock)
						&& !pro.getBools().get(name)) {
					pro.getNums().put(name + "Num",
							pro.getNums().get(name + "Num") + stack.stackSize);
					if (pro.getNums().get(name + "Num") >= number) {
						pro.getBools().put(name, true);
						player.addChatMessage(new ChatComponentText(name
								.substring(0, 1).toUpperCase()
								+ name.substring(1) + " done!"));

					}
				}
			}

		}
	}

	@SubscribeEvent
	public void last(PlayerInteractEvent event) {
		EntityPlayer player = event.entityPlayer;
		PlayerInformation pro = PlayerInformation.get(player);
		World world = player.worldObj;

		boolean ll64 = true;
		Vector<String> names = new Vector<String>();
		for (int i = 0; i < BlockedLayers.names.size(); i++) {
			String name = BlockedLayers.names.get(i);
			if (BlockedLayers.layer.get(i).equals("64")) {
				names.add(name);
			}
		}
		if (names.size() == 0) {
			ll64 = false;
		}

		for (String key : pro.getBools().keySet()) {
			for (String s : names) {
				if (pro.getBools().containsKey(s)) {
					if (!pro.getBools().get(s)) {
						ll64 = false;
						break;
					}
				}
			}
		}
		if (!player.worldObj.isRemote && !pro.isL64() && ll64) {

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
