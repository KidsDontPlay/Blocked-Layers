package mrriegel.blockedlayers.handler;

import java.util.Map.Entry;

import mrriegel.blockedlayers.BlockedLayers;
import mrriegel.blockedlayers.entity.PlayerInformation;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class QuestHandler {

	@SubscribeEvent
	public void eatItem(PlayerUseItemEvent.Finish event) {
		for (int i = 0; i < BlockedLayers.instance.questList.size(); i++) {
			if (!BlockedLayers.doIt.get(i).equals("eat")) {
				continue;
			}
			Item target = GameRegistry.findItem(BlockedLayers.modID.get(i),
					BlockedLayers.what.get(i));
			int meta;
			if (BlockedLayers.meta.get(i).equals("*")) {
				meta = event.entityPlayer.getCurrentEquippedItem()
						.getItemDamage();
			} else {
				meta = Integer.parseInt(BlockedLayers.meta.get(i));
			}
			int number = Integer.valueOf(BlockedLayers.number.get(i));
			String name = BlockedLayers.names.get(i);
			EntityPlayer player = event.entityPlayer;
			PlayerInformation pro = PlayerInformation.get(player);

			if (!player.worldObj.isRemote
					&& player.getCurrentEquippedItem().getItem().equals(target)
					&& meta == player.getCurrentEquippedItem().getItemDamage()) {
				if (!pro.getQuestBools().get(name)) {
					pro.getQuestNums().put(name + "Num",
							pro.getQuestNums().get(name + "Num") + 1);
					if (pro.getQuestNums().get(name + "Num") == number) {
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
	public void breakBlock(BreakEvent event) {
		for (int i = 0; i < BlockedLayers.instance.questList.size(); i++) {
			if (!BlockedLayers.doIt.get(i).equals("break")) {
				continue;
			}
			String name = BlockedLayers.names.get(i);
			Block target = GameRegistry.findBlock(BlockedLayers.modID.get(i),
					BlockedLayers.what.get(i));
			int meta;
			if (BlockedLayers.meta.get(i).equals("*")) {
				meta = event.blockMetadata;
			} else {
				meta = Integer.parseInt(BlockedLayers.meta.get(i));
			}
			int number = Integer.valueOf(BlockedLayers.number.get(i));

			EntityPlayer player = event.getPlayer();
			PlayerInformation pro = PlayerInformation.get(player);

			if (!player.worldObj.isRemote && event.block.equals(target)
					&& event.blockMetadata == meta) {
				if (!pro.getQuestBools().get(name)) {
					pro.getQuestNums().put(name + "Num",
							pro.getQuestNums().get(name + "Num") + 1);
					if (pro.getQuestNums().get(name + "Num") == number) {
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
	public void kill(LivingDeathEvent event) {
		if (event.entity instanceof EntitySkeleton) {
			for (int i = 0; i < BlockedLayers.instance.questList.size(); i++) {
				if (!BlockedLayers.doIt.get(i).equals("kill")) {
					continue;
				}
				String name = BlockedLayers.names.get(i);
				String upperName = BlockedLayers.what.get(i).substring(0, 1)
						.toUpperCase()
						+ BlockedLayers.what.get(i).substring(1);
				Class target = (Class) EntityList.stringToClassMapping
						.get(upperName);

				int number = Integer.valueOf(BlockedLayers.number.get(i));

				EntitySkeleton e = (EntitySkeleton) event.entity;
				int meta = Integer.parseInt(BlockedLayers.meta.get(i));
				DamageSource source = event.source;

				if (!e.worldObj.isRemote
						&& source.getSourceOfDamage() instanceof EntityPlayer
						&& target.isInstance(e) && e.getSkeletonType() == meta) {
					EntityPlayer player = (EntityPlayer) source
							.getSourceOfDamage();
					PlayerInformation pro = PlayerInformation.get(player);
					if (!pro.getQuestBools().get(name)) {
						pro.getQuestNums().put(name + "Num",
								pro.getQuestNums().get(name + "Num") + 1);
						if (pro.getQuestNums().get(name + "Num") == number) {
							pro.getQuestBools().put(name, true);
							player.addChatMessage(new ChatComponentText(name
									.substring(0, 1).toUpperCase()
									+ name.substring(1) + " done!"));

						}
					}
				}
			}
		} else {

			for (int i = 0; i < BlockedLayers.instance.questList.size(); i++) {
				if (!BlockedLayers.doIt.get(i).equals("kill")) {
					continue;
				}
				String name = BlockedLayers.names.get(i);
				String upperName = BlockedLayers.what.get(i).substring(0, 1)
						.toUpperCase()
						+ BlockedLayers.what.get(i).substring(1);
				Class target = (Class) EntityList.stringToClassMapping
						.get(upperName);

				int number = Integer.valueOf(BlockedLayers.number.get(i));

				Entity e = event.entity;
				DamageSource source = event.source;

				if (!e.worldObj.isRemote
						&& source.getSourceOfDamage() instanceof EntityPlayer
						&& target.isInstance(e)) {
					EntityPlayer player = (EntityPlayer) source
							.getSourceOfDamage();
					PlayerInformation pro = PlayerInformation.get(player);
					if (!pro.getQuestBools().get(name)) {
						pro.getQuestNums().put(name + "Num",
								pro.getQuestNums().get(name + "Num") + 1);
						if (pro.getQuestNums().get(name + "Num") == number) {
							pro.getQuestBools().put(name, true);
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
	public void shear(EntityInteractEvent event) {
		for (int i = 0; i < BlockedLayers.instance.questList.size(); i++) {
			if (!BlockedLayers.doIt.get(i).equals("shear")) {
				continue;
			}
			String name = BlockedLayers.names.get(i);

			int number = Integer.valueOf(BlockedLayers.number.get(i));

			String upperName = BlockedLayers.what.get(i).substring(0, 1)
					.toUpperCase()
					+ BlockedLayers.what.get(i).substring(1);
			Class classTarget = (Class) EntityList.stringToClassMapping
					.get(upperName);

			EntityPlayer player = event.entityPlayer;
			PlayerInformation pro = PlayerInformation.get(player);
			Entity entTarget = event.target;
			if (!IShearable.class.isInstance(entTarget)) {
				continue;
			}
			IShearable ish = (IShearable) entTarget;

			if (player.worldObj.isRemote) {
				continue;
			}
			if (player.getCurrentEquippedItem() == null) {
				continue;
			}
			if (classTarget.isInstance(entTarget)
					&& ish.isShearable(player.getCurrentEquippedItem(),
							player.worldObj, 0, 0, 0)) {
				if (player.getCurrentEquippedItem().getItem() instanceof ItemShears) {
					if (!pro.getQuestBools().get(name)) {
						pro.getQuestNums().put(name + "Num",
								pro.getQuestNums().get(name + "Num") + 1);
						if (pro.getQuestNums().get(name + "Num") == number) {
							pro.getQuestBools().put(name, true);
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
	public void last(PlayerInteractEvent event) {
		EntityPlayer player = event.entityPlayer;
		PlayerInformation pro = PlayerInformation.get(player);
		World world = player.worldObj;
		for (Entry<Integer, Boolean> entry : pro.getLayerBools().entrySet()) {
			boolean ll = true;
			for (int i = 0; i < BlockedLayers.instance.questList.size(); i++) {
				if (BlockedLayers.instance.questList.get(i).getLayer() == entry
						.getKey()) {
					if (!pro.getQuestBools().get(BlockedLayers.instance.questList.get(i).getName())) {
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
			}
		}
	}
}
