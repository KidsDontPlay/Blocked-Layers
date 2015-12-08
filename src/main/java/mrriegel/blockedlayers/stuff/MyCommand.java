package mrriegel.blockedlayers.stuff;

import java.util.List;

import mrriegel.blockedlayers.BlockedLayers;
import mrriegel.blockedlayers.entity.PlayerInformation;
import mrriegel.blockedlayers.handler.PacketHandler;
import mrriegel.blockedlayers.packet.SyncClientPacket;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

public class MyCommand implements ICommand {

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
		if (sender.getEntityWorld().isRemote)
			return;
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
				sender.addChatMessage(new ChatComponentText(StatCollector
						.translateToLocalFormatted("bl.player.online", args[1])));
				return;
			}
			PlayerInformation pro = PlayerInformation.get(player);
			pro.setTeam(args[2]);
			PacketHandler.INSTANCE.sendTo(new SyncClientPacket(
					(EntityPlayerMP) player), (EntityPlayerMP) player);
			sender.addChatMessage(new ChatComponentText(StatCollector
					.translateToLocalFormatted("bl.player.add",
							player.getDisplayName(), pro.getTeam())));
			Statics.syncTeams(pro.getTeam());
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
					sender.addChatMessage(new ChatComponentText(StatCollector
							.translateToLocalFormatted("bl.player.online",
									args[2])));
					return;
				}
				PlayerInformation pro = PlayerInformation.get(player);
				if (pro.getQuestBools().containsKey(args[3])) {
					pro.getQuestBools().put(args[3], false);
					pro.getQuestNums().put(args[3] + "Num", 0);
					player.addChatMessage(new ChatComponentText(StatCollector
							.translateToLocalFormatted("bl.quest.reset",
									args[3])));
					return;
				}
				sender.addChatMessage(new ChatComponentText(StatCollector
						.translateToLocalFormatted("bl.quest.exist", args[3])));
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
					sender.addChatMessage(new ChatComponentText(StatCollector
							.translateToLocalFormatted("bl.player.online",
									args[2])));
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
					player.addChatMessage(new ChatComponentText(StatCollector
							.translateToLocalFormatted("bl.layer.reset",
									Integer.valueOf(args[3]))));
					return;
				}
				sender.addChatMessage(new ChatComponentText(StatCollector
						.translateToLocalFormatted("bl.layer.exist",
								Integer.valueOf(args[3]))));
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
