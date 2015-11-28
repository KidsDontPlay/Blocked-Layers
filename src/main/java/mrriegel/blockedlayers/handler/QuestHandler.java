package mrriegel.blockedlayers.handler;

import java.util.ArrayList;
import java.util.Map.Entry;

import mrriegel.blockedlayers.BlockedLayers;
import mrriegel.blockedlayers.Quest;
import mrriegel.blockedlayers.Reward;
import mrriegel.blockedlayers.Statics;
import mrriegel.blockedlayers.entity.PlayerInformation;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class QuestHandler {

	@SubscribeEvent
	public void eatItem(PlayerUseItemEvent.Finish event) {
		if (event.entityPlayer.worldObj.isRemote)
			return;
		EntityPlayer player = event.entityPlayer;
		PlayerInformation pro = PlayerInformation.get(player);
		for (Quest q : BlockedLayers.instance.questList) {
			String name = q.getName();
			if (!q.getActivity().equals("eat") || pro.getQuestBools().get(name)) {
				continue;
			}
			ItemStack stack;
			Item target = GameRegistry.findItem(q.getModID(), q.getObject());
			if (q.getMeta() == -1)
				stack = new ItemStack(target, 1, event.entityPlayer
						.getCurrentEquippedItem().getItemDamage());
			else
				stack = new ItemStack(target, 1, q.getMeta());

			int number = q.getNumber();

			if (player.getCurrentEquippedItem().isItemEqual(stack)) {
				pro.getQuestNums().put(name + "Num",
						pro.getQuestNums().get(name + "Num") + 1);
				if (pro.getQuestNums().get(name + "Num") >= number) {
					pro.getQuestBools().put(name, true);
					player.addChatMessage(new ChatComponentText(name.substring(
							0, 1).toUpperCase()
							+ name.substring(1) + " done!"));

				}
			}
		}

	}

	@SubscribeEvent
	public void breakBlock(BreakEvent event) {
		if (event.getPlayer().worldObj.isRemote)
			return;
		EntityPlayer player = event.getPlayer();
		PlayerInformation pro = PlayerInformation.get(player);
		for (Quest q : BlockedLayers.instance.questList) {
			String name = q.getName();
			if (!q.getActivity().equals("break")
					|| pro.getQuestBools().get(name)
					|| (ConfigurationHandler.onlySilk && EnchantmentHelper
							.getSilkTouchModifier(player))) {
				continue;
			}

			Block target = GameRegistry.findBlock(q.getModID(), q.getObject());
			int meta;
			if (q.getMeta() == -1) {
				meta = event.blockMetadata;
			} else {
				meta = q.getMeta();
			}
			int number = q.getNumber();

			if (event.block.equals(target) && event.blockMetadata == meta) {
				pro.getQuestNums().put(name + "Num",
						pro.getQuestNums().get(name + "Num") + 1);
				if (pro.getQuestNums().get(name + "Num") >= number) {
					pro.getQuestBools().put(name, true);
					player.addChatMessage(new ChatComponentText(name.substring(
							0, 1).toUpperCase()
							+ name.substring(1) + " done!"));

				}
			}
		}
	}

	@SubscribeEvent
	public void kill(LivingDeathEvent event) {
		if (event.entityLiving.worldObj.isRemote
				|| !(event.source.getEntity() instanceof EntityPlayer))
			return;
		EntityPlayer player = (EntityPlayer) event.source.getEntity();
		PlayerInformation pro = PlayerInformation.get(player);
		for (Quest q : BlockedLayers.instance.questList) {
			String name = q.getName();
			if (!q.getActivity().equals("kill")
					|| pro.getQuestBools().get(name)) {
				continue;
			}

			Class target = (Class) EntityList.stringToClassMapping.get(q
					.getObject());

			int number = q.getNumber();
			int meta = q.getMeta();
			Entity e = event.entity;

			if (target.isInstance(e)
					&& (!(e instanceof EntitySkeleton) || ((EntitySkeleton) e)
							.getSkeletonType() == meta)) {
				pro.getQuestNums().put(name + "Num",
						pro.getQuestNums().get(name + "Num") + 1);
				if (pro.getQuestNums().get(name + "Num") == number) {
					pro.getQuestBools().put(name, true);
					player.addChatMessage(new ChatComponentText(name.substring(
							0, 1).toUpperCase()
							+ name.substring(1) + " done!"));

				}
			}
		}
	}

	@SubscribeEvent
	public void harvest(HarvestDropsEvent event) {
		if (event.world.isRemote)
			return;
		EntityPlayer player = event.harvester;
		PlayerInformation pro = PlayerInformation.get(player);
		for (Quest q : BlockedLayers.instance.questList) {
			String name = q.getName();
			if (!q.getActivity().equals("harvest")
					|| pro.getQuestBools().get(name)) {
				continue;
			}
			ItemStack target = null;
			Item targetItem = GameRegistry
					.findItem(q.getModID(), q.getObject());
			for (ItemStack stack : event.drops) {
				int meta;
				if (q.getMeta() == -1)
					target = new ItemStack(targetItem, 1, stack.getItemDamage());
				else
					target = new ItemStack(targetItem, 1, q.getMeta());
				int number = q.getNumber();

				if (target.isItemEqual(stack)) {
					pro.getQuestNums().put(name + "Num",
							pro.getQuestNums().get(name + "Num") + 1);
					if (pro.getQuestNums().get(name + "Num") >= number) {
						pro.getQuestBools().put(name, true);
						player.addChatMessage(new ChatComponentText(name
								.substring(0, 1).toUpperCase()
								+ name.substring(1) + " done!"));
						break;

					}
				}
			}
		}
	}

	@SubscribeEvent
	public void loot(LivingDropsEvent event) {
		if (event.entityLiving.worldObj.isRemote
				|| !(event.source.getEntity() instanceof EntityPlayer))
			return;
		EntityPlayer player = (EntityPlayer) event.source.getEntity();
		PlayerInformation pro = PlayerInformation.get(player);
		for (Quest q : BlockedLayers.instance.questList) {
			String name = q.getName();
			if (!q.getActivity().equals("loot")
					|| pro.getQuestBools().get(name)) {
				continue;
			}

			ItemStack target = null;
			Item targetItem = GameRegistry
					.findItem(q.getModID(), q.getObject());
			for (EntityItem item : event.drops) {
				ItemStack stack = item.getEntityItem().copy();
				int meta;
				if (q.getMeta() == -1)
					target = new ItemStack(targetItem, 1, stack.getItemDamage());
				else
					target = new ItemStack(targetItem, 1, q.getMeta());
				int number = q.getNumber();

				if (target.isItemEqual(stack)) {
					pro.getQuestNums().put(name + "Num",
							pro.getQuestNums().get(name + "Num") + 1);
					if (pro.getQuestNums().get(name + "Num") >= number) {
						pro.getQuestBools().put(name, true);
						player.addChatMessage(new ChatComponentText(name
								.substring(0, 1).toUpperCase()
								+ name.substring(1) + " done!"));
						break;

					}
				}
			}
		}
	}

	@SubscribeEvent
	public void craft(PlayerEvent.ItemCraftedEvent event) {
		for (int i = 0; i < BlockedLayers.instance.questList.size(); i++) {
			if (!BlockedLayers.doIt.get(i).equals("craft")) {
				continue;
			}
			String name = BlockedLayers.names.get(i);
			Item target = GameRegistry.findItem(BlockedLayers.modID.get(i),
					BlockedLayers.what.get(i));
			ItemStack stack = event.crafting;

			int number = Integer.valueOf(BlockedLayers.number.get(i));

			Block block = GameRegistry.findBlock(BlockedLayers.modID.get(i),
					BlockedLayers.what.get(i));
			int meta;
			if (BlockedLayers.meta.get(i).equals("*")) {
				meta = event.crafting.getItemDamage();
			} else {
				meta = Integer.parseInt(BlockedLayers.meta.get(i));
			}

			EntityPlayer player = event.player;
			PlayerInformation pro = PlayerInformation.get(player);
			World world = player.worldObj;

			if (!world.isRemote
					&& Block.getBlockFromItem(event.crafting.getItem())
							.getMaterial().equals(Material.air)
					&& event.crafting.getItemDamage() == meta) {
				if (event.crafting.getItem().equals(target)
						&& !pro.getQuestBools().get(name)) {
					pro.getQuestNums().put(
							name + "Num",
							pro.getQuestNums().get(name + "Num")
									+ stack.stackSize);
					if (pro.getQuestNums().get(name + "Num") >= number) {
						pro.getQuestBools().put(name, true);
						player.addChatMessage(new ChatComponentText(name
								.substring(0, 1).toUpperCase()
								+ name.substring(1) + " done!"));
						continue;

					}
				}
			}

			ItemStack stackBlock = new ItemStack(block);
			Item itemBlock = stackBlock.getItem();
			if (!world.isRemote
					&& !Block.getBlockFromItem(event.crafting.getItem())
							.getMaterial().equals(Material.air)
					&& event.crafting.getItemDamage() == meta) {
				if (event.crafting.getItem().equals(itemBlock)
						&& !pro.getQuestBools().get(name)) {
					pro.getQuestNums().put(
							name + "Num",
							pro.getQuestNums().get(name + "Num")
									+ stack.stackSize);
					if (pro.getQuestNums().get(name + "Num") >= number) {
						pro.getQuestBools().put(name, true);
						player.addChatMessage(new ChatComponentText(name
								.substring(0, 1).toUpperCase()
								+ name.substring(1) + " done!"));

					}
				}
			}

		}
	}

	@SubscribeEvent
	public void smelt(PlayerEvent.ItemSmeltedEvent event) {
		for (int i = 0; i < BlockedLayers.instance.questList.size(); i++) {
			if (!BlockedLayers.doIt.get(i).equals("craft")) {
				continue;
			}
			String name = BlockedLayers.names.get(i);
			Item target = GameRegistry.findItem(BlockedLayers.modID.get(i),
					BlockedLayers.what.get(i));
			ItemStack stack = event.smelting;

			int number = Integer.valueOf(BlockedLayers.number.get(i));

			Block block = GameRegistry.findBlock(BlockedLayers.modID.get(i),
					BlockedLayers.what.get(i));
			int meta;
			if (BlockedLayers.meta.get(i).equals("*")) {
				meta = event.smelting.getItemDamage();
			} else {
				meta = Integer.parseInt(BlockedLayers.meta.get(i));
			}

			EntityPlayer player = event.player;
			PlayerInformation pro = PlayerInformation.get(player);
			World world = player.worldObj;

			if (!world.isRemote
					&& Block.getBlockFromItem(event.smelting.getItem())
							.getMaterial().equals(Material.air)
					&& event.smelting.getItemDamage() == meta) {
				if (event.smelting.getItem().equals(target)
						&& !pro.getQuestBools().get(name)) {
					pro.getQuestNums().put(
							name + "Num",
							pro.getQuestNums().get(name + "Num")
									+ stack.stackSize);
					if (pro.getQuestNums().get(name + "Num") >= number) {
						pro.getQuestBools().put(name, true);
						player.addChatMessage(new ChatComponentText(name
								.substring(0, 1).toUpperCase()
								+ name.substring(1) + " done!"));

					}
				}
			}

			ItemStack stackBlock = new ItemStack(block);
			Item itemBlock = stackBlock.getItem();
			if (!world.isRemote
					&& !Block.getBlockFromItem(event.smelting.getItem())
							.getMaterial().equals(Material.air)
					&& event.smelting.getItemDamage() == meta) {
				if (event.smelting.getItem().equals(itemBlock)
						&& !pro.getQuestBools().get(name)) {
					pro.getQuestNums().put(
							name + "Num",
							pro.getQuestNums().get(name + "Num")
									+ stack.stackSize);
					if (pro.getQuestNums().get(name + "Num") >= number) {
						pro.getQuestBools().put(name, true);
						player.addChatMessage(new ChatComponentText(name
								.substring(0, 1).toUpperCase()
								+ name.substring(1) + " done!"));

					}
				}
			}

		}
	}

	@SubscribeEvent
	public void release(PlayerInteractEvent event) {
		EntityPlayer player = event.entityPlayer;
		PlayerInformation pro = PlayerInformation.get(player);
		World world = player.worldObj;
		for (Entry<Integer, Boolean> entry : pro.getLayerBools().entrySet()) {
			boolean ll = true;
			for (int i = 0; i < BlockedLayers.instance.questList.size(); i++) {
				if (BlockedLayers.instance.questList.get(i).getLayer() == entry
						.getKey()) {
					if (!pro.getQuestBools().get(
							BlockedLayers.instance.questList.get(i).getName())) {
						ll = false;
						break;
					}
				}

			}
			if (!player.worldObj.isRemote
					&& !pro.getLayerBools().get(entry.getKey()) && ll) {
				pro.getLayerBools().put(entry.getKey(), true);
				player.addChatMessage(new ChatComponentText("Layer "
						+ entry.getKey() + " released!"));
				Statics.syncTeams((EntityPlayerMP) player);
				for (Reward r : BlockedLayers.instance.rewardList) {
					if (!ConfigurationHandler.reward)
						break;
					if (r.getLayer() == entry.getKey()) {
						ArrayList<ItemStack> tmp = new ArrayList<ItemStack>();
						for (String s : r.getRewards()) {
							if (BlockedLayers.string2Stack(s) != null)
								tmp.add(BlockedLayers.string2Stack(s));
						}
						for (ItemStack s : tmp) {
							if (!player.inventory.addItemStackToInventory(s
									.copy()))
								player.dropPlayerItemWithRandomChoice(s.copy(),
										false);
						}
						break;
					}
				}
			}
		}
	}
}
