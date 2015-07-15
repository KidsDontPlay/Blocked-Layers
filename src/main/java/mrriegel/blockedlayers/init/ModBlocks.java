package mrriegel.blockedlayers.init;

import net.minecraft.block.Block;
import cpw.mods.fml.common.registry.GameRegistry;
import mrriegel.blockedlayers.block.Un;
import mrriegel.blockedlayers.reference.Reference;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
public class ModBlocks {
	public static final Block un = new Un();
	
	public static void init(){
		GameRegistry.registerBlock(un, "un");
	}
}
