package mrriegel.blockedlayers.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mrriegel.blockedlayers.reference.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;

public class Un extends Block {

	public Un() {
		super(Material.rock);
		this.setCreativeTab(CreativeTabs.tabMisc);
		this.setBlockName(Reference.MOD_ID + ":" + "un");
		this.setBlockTextureName(Reference.MOD_ID + ":" + "un");
		this.setHardness(Float.MAX_VALUE);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	


}