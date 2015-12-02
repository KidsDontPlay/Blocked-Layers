package mrriegel.blockedlayers.handler;

import java.util.ArrayList;
import java.util.Map.Entry;

import mrriegel.blockedlayers.BlockedLayers;
import mrriegel.blockedlayers.Quest;
import mrriegel.blockedlayers.Reward;
import mrriegel.blockedlayers.Statics;
import mrriegel.blockedlayers.entity.PlayerInformation;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class QuestHandler {

	boolean questValid(PlayerInformation pro, Quest q) {
		if (!ConfigurationHandler.hard)
			return true;
		for (Entry<Integer, Boolean> e : pro.getLayerBools().entrySet()) {
			if (q.getLayer() < e.getKey() && !e.getValue())
				return false;
		}
		return true;
	}

	@SubscribeEvent
	public void eatItem(PlayerUseItemEvent.Finish event) {
		if (event.entityPlayer.worldObj.isRemote)
			return;
		EntityPlayer player = event.entityPlayer;
		PlayerInformation pro = PlayerInformation.get(player);
		for (Quest q : BlockedLayers.instance.questList) {
			String name = q.getName();
			if (!q.getActivity().equals("eat") || !questValid(pro, q)
					|| pro.getQuestBools().get(name)
					|| player.capabilities.isCreativeMode) {
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
					Statics.syncTeams((EntityPlayerMP) player);

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
					|| !questValid(pro, q)
					|| pro.getQuestBools().get(name)
					|| player.capabilities.isCreativeMode
					|| (ConfigurationHandler.withoutSilk && EnchantmentHelper
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
					Statics.syncTeams((EntityPlayerMP) player);

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
			if (!q.getActivity().equals("kill") || !questValid(pro, q)
					|| pro.getQuestBools().get(name)
					|| player.capabilities.isCreativeMode) {
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
					Statics.syncTeams((EntityPlayerMP) player);

				}
			}
		}
	}

	@SubscribeEvent
	public void harvest(HarvestDropsEvent event) {
		if (event.world.isRemote)
			return;
		EntityPlayer player = event.harvester;
		if (player == null)
			return;
		PlayerInformation pro = PlayerInformation.get(player);
		for (Quest q : BlockedLayers.instance.questList) {
			String name = q.getName();
			if (!q.getActivity().equals("harvest") || !questValid(pro, q)
					|| pro.getQuestBools().get(name)
					|| player.capabilities.isCreativeMode) {
				continue;
			}
			ItemStack target = null;
			Item targetItem = GameRegistry
					.findItem(q.getModID(), q.getObject());
			for (ItemStack stack : event.drops) {
				if (q.getMeta() == -1)
					target = new ItemStack(targetItem, 1, stack.getItemDamage());
				else
					target = new ItemStack(targetItem, 1, q.getMeta());
				int number = q.getNumber();

				if (target.isItemEqual(stack)) {
					pro.getQuestNums().put(
							name + "Num",
							pro.getQuestNums().get(name + "Num")
									+ stack.copy().stackSize);
					if (pro.getQuestNums().get(name + "Num") >= number) {
						pro.getQuestBools().put(name, true);
						player.addChatMessage(new ChatComponentText(name
								.substring(0, 1).toUpperCase()
								+ name.substring(1) + " done!"));
						Statics.syncTeams((EntityPlayerMP) player);

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
			if (!q.getActivity().equals("loot") || !questValid(pro, q)
					|| pro.getQuestBools().get(name)
					|| player.capabilities.isCreativeMode) {
				continue;
			}

			ItemStack target = null;
			Item targetItem = GameRegistry
					.findItem(q.getModID(), q.getObject());
			for (EntityItem item : event.drops) {
				ItemStack stack = item.getEntityItem().copy();
				if (q.getMeta() == -1)
					target = new ItemStack(targetItem, 1, stack.getItemDamage());
				else
					target = new ItemStack(targetItem, 1, q.getMeta());
				int number = q.getNumber();

				if (target.isItemEqual(stack)) {
					pro.getQuestNums().put(
							name + "Num",
							pro.getQuestNums().get(name + "Num")
									+ stack.stackSize);
					if (pro.getQuestNums().get(name + "Num") >= number) {
						pro.getQuestBools().put(name, true);
						player.addChatMessage(new ChatComponentText(name
								.substring(0, 1).toUpperCase()
								+ name.substring(1) + " done!"));
						Statics.syncTeams((EntityPlayerMP) player);

					}
				}
			}
		}
	}

	@SubscribeEvent
	public void own(PlayerInteractEvent event) {
		if (event.world.isRemote)
			return;
		EntityPlayer player = event.entityPlayer;
		PlayerInformation pro = PlayerInformation.get(player);
		for (Quest q : BlockedLayers.instance.questList) {
			String name = q.getName();
			if ((!q.getActivity().equals("own") && !q.getActivity().equals(
					"consume"))
					|| !questValid(pro, q)
					|| pro.getQuestBools().get(name)
					|| player.capabilities.isCreativeMode) {
				continue;
			}
			ItemStack target = null;
			Item targetItem = GameRegistry
					.findItem(q.getModID(), q.getObject());
			int meta = 0;
			int num = 0;
			for (int i = 0; i < player.inventory.mainInventory.length; i++) {
				if (player.inventory.mainInventory[i] == null)
					continue;
				ItemStack stack = player.inventory.mainInventory[i].copy();
				if (q.getMeta() == -1) {
					meta = OreDictionary.WILDCARD_VALUE;
					if (targetItem.equals(stack.getItem()))
						num += stack.stackSize;
				} else {
					meta = q.getMeta();
					if (stack.isItemEqual(new ItemStack(targetItem, 1, q
							.getMeta())))
						num += stack.stackSize;
				}
			}

			if (num >= q.getNumber()) {
				if (q.getActivity().equals("consume")) {
					consumeInventoryItem(player.inventory, targetItem, meta,
							q.getNumber());
					player.inventory.markDirty();
				}
				pro.getQuestNums().put(name + "Num", q.getNumber());
				pro.getQuestBools().put(name, true);
				player.addChatMessage(new ChatComponentText(name
						.substring(0, 1).toUpperCase()
						+ name.substring(1)
						+ " done!"));
				Statics.syncTeams((EntityPlayerMP) player);

			}
		}
	}

	boolean consumeInventoryItem(IInventory inv, Item item, int meta, int num) {
		Integer[] i = getSlotsWith(inv, item, meta);
		for (int s : i) {
			ItemStack stack = inv.getStackInSlot(s);
			if (stack.stackSize > num) {
				return decrStackSize(inv, s, num);
			} else if (stack.stackSize == num) {
				inv.setInventorySlotContents(s, null);
				return true;
			} else {
				if (s != i[i.length - 1])
					inv.setInventorySlotContents(s, null);
				num -= stack.stackSize;
			}
		}
		return false;
	}

	Integer[] getSlotsWith(IInventory inv, Item item, int meta) {
		ArrayList<Integer> ar = new ArrayList<Integer>();
		for (int i = 0; i < inv.getSizeInventory()
				- ((inv instanceof InventoryPlayer) ? 4 : 0); ++i) {
			ItemStack stack = inv.getStackInSlot(i);
			if (stack != null
					&& stack.getItem().equals(item)
					&& (stack.getItemDamage() == meta || meta == OreDictionary.WILDCARD_VALUE)) {
				ar.add(i);
			}
		}
		return ar.toArray(new Integer[ar.size()]);
	}

	boolean decrStackSize(IInventory inv, int slot, int num) {
		ItemStack stack = inv.getStackInSlot(slot);
		if (stack == null || stack.stackSize < num)
			return false;
		if (stack.stackSize == num) {
			inv.setInventorySlotContents(slot, null);
			inv.markDirty();
			return true;
		} else {
			stack.stackSize -= num;
			inv.setInventorySlotContents(slot, stack);
			inv.markDirty();
			return true;
		}
	}

	@SubscribeEvent
	public void xp(PlayerPickupXpEvent event) {
		if (event.entityPlayer.worldObj.isRemote)
			return;
		EntityPlayer player = event.entityPlayer;
		PlayerInformation pro = PlayerInformation.get(player);
		for (Quest q : BlockedLayers.instance.questList) {
			String name = q.getName();
			if (!q.getActivity().equals("xp") || !questValid(pro, q)
					|| pro.getQuestBools().get(name)
					|| player.capabilities.isCreativeMode) {
				continue;
			}

			int number = q.getNumber();

			pro.getQuestNums().put(name + "Num",
					pro.getQuestNums().get(name + "Num") + event.orb.xpValue);
			if (pro.getQuestNums().get(name + "Num") >= number) {
				pro.getQuestBools().put(name, true);
				player.addChatMessage(new ChatComponentText(name
						.substring(0, 1).toUpperCase()
						+ name.substring(1)
						+ " done!"));
				Statics.syncTeams((EntityPlayerMP) player);

			}
		}
	}

	@SubscribeEvent
	public void find(PlayerInteractEvent event) {
		if (event.entityPlayer.worldObj.isRemote)
			return;
		EntityPlayer player = event.entityPlayer;
		PlayerInformation pro = PlayerInformation.get(player);
		String currentBiom = event.world.getWorldChunkManager().getBiomeGenAt(
				event.x, event.z).biomeName;
		for (Quest q : BlockedLayers.instance.questList) {
			String name = q.getName();
			if (!q.getActivity().equals("find") || !questValid(pro, q)
					|| pro.getQuestBools().get(name)
					|| player.capabilities.isCreativeMode) {
				continue;
			}
			String biom = q.getObject();
			if (biom.equalsIgnoreCase(currentBiom)) {
				pro.getQuestBools().put(name, true);
				player.addChatMessage(new ChatComponentText(name
						.substring(0, 1).toUpperCase()
						+ name.substring(1)
						+ " done!"));
				Statics.syncTeams((EntityPlayerMP) player);
			}
		}
	}

	@SubscribeEvent
	public void craft(PlayerEvent.ItemCraftedEvent event) {
		if (event.player.worldObj.isRemote)
			return;
		EntityPlayer player = event.player;
		PlayerInformation pro = PlayerInformation.get(player);
		ItemStack oriStack = event.crafting.copy();
		for (Quest q : BlockedLayers.instance.questList) {
			String name = q.getName();
			if (!q.getActivity().equals("craft") || !questValid(pro, q)
					|| pro.getQuestBools().get(name)
					|| player.capabilities.isCreativeMode) {
				continue;
			}
			craftNsmelt(player, q, oriStack);
		}
	}

	@SubscribeEvent
	public void smelt(PlayerEvent.ItemSmeltedEvent event) {
		if (event.player.worldObj.isRemote)
			return;
		EntityPlayer player = event.player;
		PlayerInformation pro = PlayerInformation.get(player);
		ItemStack oriStack = event.smelting.copy();
		System.out.println(oriStack);
		for (Quest q : BlockedLayers.instance.questList) {
			String name = q.getName();
			if (!q.getActivity().equals("craft") || !questValid(pro, q)
					|| pro.getQuestBools().get(name)
					|| player.capabilities.isCreativeMode) {
				continue;
			}
			craftNsmelt(player, q, oriStack);
		}
	}

	public void craftNsmelt(EntityPlayer player, Quest q, ItemStack oriStack) {
		ItemStack stack;
		Item target = GameRegistry.findItem(q.getModID(), q.getObject());
		if (q.getMeta() == -1)
			stack = new ItemStack(target, 1, oriStack.getItemDamage());
		else
			stack = new ItemStack(target, 1, q.getMeta());

		int number = q.getNumber();
		PlayerInformation pro = PlayerInformation.get(player);
		String name = q.getName();
		if (oriStack.isItemEqual(stack)) {
			pro.getQuestNums()
					.put(name + "Num",
							pro.getQuestNums().get(name + "Num")
									+ (oriStack.stackSize == 0 ? 1
											: oriStack.stackSize));
			if (pro.getQuestNums().get(name + "Num") >= number) {
				pro.getQuestBools().put(name, true);
				player.addChatMessage(new ChatComponentText(name
						.substring(0, 1).toUpperCase()
						+ name.substring(1)
						+ " done!"));
				Statics.syncTeams((EntityPlayerMP) player);

			}
		}
	}

	@SubscribeEvent
	public void release(PlayerInteractEvent event) {
		if (event.world.isRemote)
			return;
		EntityPlayer player = event.entityPlayer;
		PlayerInformation pro = PlayerInformation.get(player);
		World world = player.worldObj;
		for (Entry<Integer, Boolean> entry : pro.getLayerBools().entrySet()) {
			boolean ll = true;
			for (Quest q : BlockedLayers.instance.questList) {
				if (q.getLayer() == entry.getKey()) {
					if (!pro.getQuestBools().get(q.getName())) {
						ll = false;
						break;
					}
				}
			}
			if (!pro.getLayerBools().get(entry.getKey()) && ll) {
				pro.getLayerBools().put(entry.getKey(), true);
				player.addChatMessage(new ChatComponentText("Layer "
						+ entry.getKey() + " released!"));
				for (Reward r : BlockedLayers.instance.rewardList) {
					if (!ConfigurationHandler.reward)
						break;
					if (r.getLayer() == entry.getKey()) {
						ArrayList<ItemStack> tmp = new ArrayList<ItemStack>();
						for (String s : r.getRewards()) {
							if (Statics.string2Stack(s) != null)
								tmp.add(Statics.string2Stack(s));
						}
						for (ItemStack s : tmp) {
							if (!player.inventory.addItemStackToInventory(s
									.copy()))
								player.dropPlayerItemWithRandomChoice(s.copy(),
										false);
						}
					}
				}
			}
		}
	}
}
