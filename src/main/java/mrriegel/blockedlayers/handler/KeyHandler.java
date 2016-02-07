package mrriegel.blockedlayers.handler;

import mrriegel.blockedlayers.BlockedLayers;
import mrriegel.blockedlayers.packet.KeyPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import org.lwjgl.input.Keyboard;

public class KeyHandler {
	public static KeyBinding gui = new KeyBinding(BlockedLayers.MOD_ID
			+ ".clipboard", Keyboard.KEY_L, BlockedLayers.MOD_NAME);

	@SubscribeEvent
	public void onKey(InputEvent.KeyInputEvent e) {
		if (gui.isPressed()) {
			PacketHandler.INSTANCE.sendToServer(new KeyPacket());
			EntityPlayer p = Minecraft.getMinecraft().thePlayer;
			p.openGui(BlockedLayers.instance, 0, p.worldObj, 0, 0, 0);
		}
	}
}
