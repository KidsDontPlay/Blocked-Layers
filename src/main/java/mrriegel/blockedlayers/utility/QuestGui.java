package mrriegel.blockedlayers.utility;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class QuestGui extends GuiScreen {
	EntityPlayer player;

	public QuestGui(EntityPlayer player) {
		this.player = player;
	}

	@Override
	public void initGui() {
		super.initGui();
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

}
