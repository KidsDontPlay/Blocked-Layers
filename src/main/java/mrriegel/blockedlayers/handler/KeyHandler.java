package mrriegel.blockedlayers.handler;

import mrriegel.blockedlayers.BlockedLayers;
import mrriegel.blockedlayers.packet.KeyPacket;
import mrriegel.blockedlayers.reference.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;

public class KeyHandler {
	public static KeyBinding gui = new KeyBinding("key.gui", Keyboard.KEY_L,
			"key.categories." + Reference.MOD_ID);

	@SubscribeEvent
	public void onKey(InputEvent.KeyInputEvent e) {
		if (gui.isPressed()) {
			PacketHandler.INSTANCE.sendToServer(new KeyPacket());
			EntityPlayer p = Minecraft.getMinecraft().thePlayer;
			p.openGui(BlockedLayers.instance, 0, p.worldObj, 0, 0, 0);
		}
	}
}
