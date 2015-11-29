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
import net.minecraft.world.World;

public class MyCommand implements ICommand {

	protected String fullEntityName;
	protected Entity conjuredEntity;

	@Override
	public int compareTo(Object o) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "blTeam";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "blTeam <player> <team>";
	}

	@Override
	public List getCommandAliases() {
		return null;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		World world = sender.getEntityWorld();
		if (!(sender instanceof EntityPlayer))
			return;
		if (args.length != 2) {
			sender.addChatMessage(new ChatComponentText("Usage: /"
					+ getCommandUsage(sender)));
			return;
		}
		EntityPlayer player = null;
		for (Object o : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
			EntityPlayer p = (EntityPlayer) o;
			if (p.getDisplayName().equals(args[0])) {
				player = p;
				break;
			}
		}
		if (player == null) {
			sender.addChatMessage(new ChatComponentText(args[0]
					+ " isn't online."));
			return;
		}
		PlayerInformation pro = PlayerInformation.get(player);
		pro.setTeam(args[1]);
		PacketHandler.INSTANCE.sendTo(new SyncClientPacket(
				(EntityPlayerMP) player), (EntityPlayerMP) player);
		Statics.syncTeams((EntityPlayerMP) player);
		sender.addChatMessage(new ChatComponentText(player.getDisplayName()
				+ " is in Team " + pro.getTeam()));

	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		if (!(sender instanceof EntityPlayer))
			return false;
		EntityPlayer player = (EntityPlayer) sender;
		return MinecraftServer.getServer().isSinglePlayer()
				|| MinecraftServer.getServer().getConfigurationManager()
						.func_152596_g(player.getGameProfile());
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
