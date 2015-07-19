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
			if (args[0] == null) {
				player.addChatMessage(new ChatComponentText(
						"Usage: /bl <layer|quest|reset>"));
				return;
			}
			if (args[0].equals("layer")) {
				if (args.length == 1) {
					for (Entry<String, Boolean> entry : pro.getLayerBools()
							.entrySet()) {
						player.addChatMessage(new ChatComponentText("Layer "
								+ entry.getKey() + ": " + entry.getValue()));
					}
				} else {
					for (Entry<String, Boolean> entry : pro.getLayerBools()
							.entrySet()) {
						if (args[1].equals(entry.getKey())) {
							player.addChatMessage(new ChatComponentText(
									"Layer "
											+ entry.getKey().substring(0, 1)
													.toUpperCase()
											+ entry.getKey().substring(1)
											+ ": " + entry.getValue()));
							return;
						}

					}
					player.addChatMessage(new ChatComponentText(
							"No valid layer."));
				}
			} else if (args[0].equals("quest")) {
				if (args.length == 1) {
					for (Entry<String, Boolean> entry : pro.getQuestBools()
							.entrySet()) {
						player.addChatMessage(new ChatComponentText(entry
								.getKey().substring(0, 1).toUpperCase()
								+ entry.getKey().substring(1)
								+ ": "
								+ entry.getValue()));
					}
					return;
				} else {
					for (Entry<String, Boolean> entry : pro.getQuestBools()
							.entrySet()) {
						if (args[1].equals(entry.getKey())) {
							player.addChatMessage(new ChatComponentText(entry
									.getKey().substring(0, 1).toUpperCase()
									+ entry.getKey().substring(1)
									+ ": "
									+ entry.getValue()));
							return;
						}
					}
					player.addChatMessage(new ChatComponentText(
							"No valid quest."));
				}

			} else if (args[0].equals("reset")) {
				if (args.length == 1) {
					player.addChatMessage(new ChatComponentText(
							"Usage: /bl reset <layer|quest> <\"quest\"|\"name\">"));
				} else if (args[1].equals("layer")) {
					for (Entry<String, Boolean> entry : pro.getLayerBools()
							.entrySet()) {
						if (args.length == 2) {
							player.addChatMessage(new ChatComponentText(
									"Usage: /bl reset <layer|quest> <\"quest\"|\"name\">"));
							return;
						} else if (args[2].equals(entry.getKey())) {
							entry.setValue(false);
							player.addChatMessage(new ChatComponentText(
									"Layer " + entry.getKey() + ": "
											+ " reset."));
						}
					}

				} else if (args[1].equals("quest")) {
					if (BlockedLayers.names.contains(args[2])) {
						if (args.length == 2) {
							player.addChatMessage(new ChatComponentText(
									"Usage: /bl reset <layer|quest> <\"quest\"|\"name\">"));
							return;
						} else
							pro.getQuestBools().put(args[2], false);
							pro.getQuestNums().put(args[2]+"Num", 0);
						player.addChatMessage(new ChatComponentText("Quest "
								+ args[2] + " reset."));
					}
				} else {
					player.addChatMessage(new ChatComponentText(
							"Usage: /bl reset <layer|quest> <\"quest\"|\"name\">"));
				}
			} else {
				player.addChatMessage(new ChatComponentText(
						"Usage: /bl <layer|quest|reset>"));
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
