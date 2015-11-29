package mrriegel.blockedlayers.utility;

import java.util.HashMap;

import mrriegel.blockedlayers.BlockedLayers;
import mrriegel.blockedlayers.entity.PlayerInformation;
import mrriegel.blockedlayers.reference.Reference;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class QuestGui extends GuiScreen {
	private static final ResourceLocation GuiTextures = new ResourceLocation(
			Reference.MOD_ID + ":textures/gui/questGUI.png");
	int pages, page;
	int numofentrys = 13;
	private int imageWidth = 180;
	private int imageHeight = 217;

	public QuestGui(EntityPlayer player) {
		PlayerInformation pro = PlayerInformation.get(player);
		page = 0;
		pages = (int) Math.ceil((double) pro.getQuestBools().size()
				/ (double) numofentrys);
	}

	void drawMaps() {
		int in = 0;
		final int red = 0xCD0000;
		final int blue = 0x3A5FCD;
		PlayerInformation pro = PlayerInformation.get(mc.thePlayer);
		String title = pro.getTeam().equals("") ? "No Team" : pro.getTeam();
		fontRendererObj.drawString(title,
				this.width / 2 - fontRendererObj.getStringWidth(title) / 2,
				(this.height - this.imageHeight) / 2 + 12, 0x000000);
		for (int i = page; i < page + numofentrys
				&& i < BlockedLayers.instance.questList.size(); i++) {
			String name = BlockedLayers.instance.questList.get(i).getName();
			fontRendererObj.drawString(
					BlockedLayers.instance.questList.get(i).getLayer()
							+ ": "
							+ name
							+ " "
							+ pro.getQuestNums().get(name + "Num")
							+ "/"
							+ BlockedLayers.instance.questList.get(i)
									.getNumber(),
					(this.width - this.imageWidth) / 2 + 18,
					(this.height - this.imageHeight) / 2 + 30
							+ (fontRendererObj.FONT_HEIGHT + 4) * in, pro
							.getQuestBools().get(name) ? blue : red);
			in++;
		}
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GuiTextures);
		int k = (this.width - this.imageWidth) / 2;
		int l = (this.height - this.imageHeight) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.imageWidth,
				this.imageHeight);

		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
		drawMaps();

	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

}
