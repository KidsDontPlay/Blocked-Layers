package mrriegel.blockedlayers.proxy;

import mrriegel.blockedlayers.handler.KeyHandler;
import cpw.mods.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerHandlers() {
		ClientRegistry.registerKeyBinding(KeyHandler.gui);
	}
}
