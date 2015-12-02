package mrriegel.blockedlayers;

import java.util.List;

import mrriegel.blockedlayers.entity.PlayerInformation;
import mrriegel.blockedlayers.handler.PacketHandler;
import mrriegel.blockedlayers.packet.SyncClientPacket;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

public class MyCommand implements ICommand {

	protected String fullEntityName;
	protected Entity conjuredEntity;

	@Override
	public int compareTo(Object o) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "bl";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "bl team|reset";
	}

	@Override
	public List getCommandAliases() {
		return null;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (args[0].equals("team")) {
			EntityPlayer player = null;
			for (Object o : MinecraftServer.getServer()
					.getConfigurationManager().playerEntityList) {
				EntityPlayer p = (EntityPlayer) o;
				if (p.getDisplayName().equals(args[1])) {
					player = p;
					break;
				}
			}
			if (player == null) {
				sender.addChatMessage(new ChatComponentText(args[1]
						+ " isn't online."));
				return;
			}
			PlayerInformation pro = PlayerInformation.get(player);
			pro.setTeam(args[2]);
			PacketHandler.INSTANCE.sendTo(new SyncClientPacket(
					(EntityPlayerMP) player), (EntityPlayerMP) player);
			Statics.syncTeams((EntityPlayerMP) player);
			sender.addChatMessage(new ChatComponentText(player.getDisplayName()
					+ " is in Team " + pro.getTeam()));
		} else if (args[0].equals("reset")) {
			if (args[1].equals("quest")) {
				EntityPlayer player = null;
				for (Object o : MinecraftServer.getServer()
						.getConfigurationManager().playerEntityList) {
					EntityPlayer p = (EntityPlayer) o;
					if (p.getDisplayName().equals(args[2])) {
						player = p;
						break;
					}
				}
				if (player == null) {
					sender.addChatMessage(new ChatComponentText(args[2]
							+ " isn't online."));
					return;
				}
				PlayerInformation pro = PlayerInformation.get(player);
				if (pro.getQuestBools().containsKey(args[3])) {
					pro.getQuestBools().put(args[3], false);
					pro.getQuestNums().put(args[3] + "Num", 0);
					player.addChatMessage(new ChatComponentText("Quest "
							+ args[3] + " reset."));
					return;
				}
				sender.addChatMessage(new ChatComponentText("Quest " + args[3]
						+ " doesn't exist."));
				return;
			} else if (args[1].equals("layer")) {
				EntityPlayer player = null;
				for (Object o : MinecraftServer.getServer()
						.getConfigurationManager().playerEntityList) {
					EntityPlayer p = (EntityPlayer) o;
					if (p.getDisplayName().equals(args[2])) {
						player = p;
						break;
					}
				}
				if (player == null) {
					sender.addChatMessage(new ChatComponentText(args[2]
							+ " isn't online."));
					return;
				}
				PlayerInformation pro = PlayerInformation.get(player);
				if (pro.getLayerBools().containsKey(Integer.valueOf(args[3]))) {
					pro.getLayerBools().put(Integer.valueOf(args[3]), false);
					for (Quest q : BlockedLayers.instance.questList) {
						if (q.getLayer() == Integer.valueOf(args[3])) {
							pro.getQuestBools().put(q.getName(), false);
							pro.getQuestNums().put(q.getName() + "Num", 0);
						}

					}
					player.addChatMessage(new ChatComponentText("Layer "
							+ args[3] + " reset."));
					return;
				}
				sender.addChatMessage(new ChatComponentText("Quest " + args[3]
						+ " doesn't exist."));
				return;
			}
		}

	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		if (sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender;
			return MinecraftServer.getServer().isSinglePlayer()
					|| MinecraftServer.getServer().getConfigurationManager()
							.func_152596_g(player.getGameProfile());
		}
		return true;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int var) {
		return false;
	}

}
