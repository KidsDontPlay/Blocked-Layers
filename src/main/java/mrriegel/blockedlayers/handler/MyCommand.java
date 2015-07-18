package mrriegel.blockedlayers.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import mrriegel.blockedlayers.BlockedLayers;
import mrriegel.blockedlayers.entity.PlayerInformation;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class MyCommand implements ICommand {

	private final List aliases;

	protected String fullEntityName;
	protected Entity conjuredEntity;

	public MyCommand() {
		aliases = new ArrayList();
		aliases.add("bl");

	}

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
		return "bl <text>";
	}

	@Override
	public List getCommandAliases() {
		return this.aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		World world = sender.getEntityWorld();

		if (sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender;

			PlayerInformation pro = PlayerInformation.get(player);
			if (args[0].equals("layer")) {
				player.addChatMessage(new ChatComponentText("Layer 64: "
						+ pro.isL64()));
				player.addChatMessage(new ChatComponentText("Layer 32: "
						+ pro.isL32()));
				player.addChatMessage(new ChatComponentText("Layer 16: "
						+ pro.isL16()));
			} else if (args[0].equals("quest")) {
				if (args.length == 1) {
					for (Entry<String, Boolean> entry : pro.getBools()
							.entrySet()) {
						player.addChatMessage(new ChatComponentText(entry
								.getKey().substring(0, 1).toUpperCase()
								+ entry.getKey().substring(1)
								+ ": "
								+ entry.getValue()));
					}
					return;
				} else if (args[1].equals("64")) {
					for (Entry<String, Boolean> entry : pro.getBools()
							.entrySet()) {
						if (!SelfHandler64.names.contains(entry.getKey())) {
							continue;
						}
						player.addChatMessage(new ChatComponentText(entry
								.getKey().substring(0, 1).toUpperCase()
								+ entry.getKey().substring(1)
								+ ": "
								+ entry.getValue()));
					}
					return;
				} else if (args[1].equals("32")) {
					for (Entry<String, Boolean> entry : pro.getBools()
							.entrySet()) {
						if (!SelfHandler32.names.contains(entry.getKey())) {
							continue;
						}
						player.addChatMessage(new ChatComponentText(entry
								.getKey().substring(0, 1).toUpperCase()
								+ entry.getKey().substring(1)
								+ ": "
								+ entry.getValue()));
					}
					return;
				} else if (args[1].equals("16")) {
					for (Entry<String, Boolean> entry : pro.getBools()
							.entrySet()) {
						if (!SelfHandler16.names.contains(entry.getKey())) {
							continue;
						}
						player.addChatMessage(new ChatComponentText(entry
								.getKey().substring(0, 1).toUpperCase()
								+ entry.getKey().substring(1)
								+ ": "
								+ entry.getValue()));
					}
					return;
				} else {
					player.addChatMessage(new ChatComponentText(
							"Usage: /bl quest <64|32|16>"));
				}

			} else if (args[0].equals("reset")) {
				if (args[1].equals("layer")) {
					if (args[2].equals("64")) {
						pro.setL64(false);
						player.addChatMessage(new ChatComponentText("Layer "
								+ args[2] + " reset."));
					} else if (args[2].equals("32")) {
						pro.setL32(false);
						player.addChatMessage(new ChatComponentText("Layer "
								+ args[2] + " reset."));
					} else if (args[2].equals("16")) {
						pro.setL16(false);
						player.addChatMessage(new ChatComponentText("Layer "
								+ args[2] + " reset."));
					}

				} else if (args[1].equals("quest")) {
					if (BlockedLayers.names.contains(args[2])) {
						pro.getBools().put(args[2], false);
						player.addChatMessage(new ChatComponentText("Quest "
								+ args[2] + " reset."));
					}
				} else {
					player.addChatMessage(new ChatComponentText(
							"Usage: /bl reset <layer|quest> <64|32|16|\"name\">"));
				}
			} else {
				player.addChatMessage(new ChatComponentText(
						"Usage: /bl <layer|quest>"));
			}
		}

	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
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
