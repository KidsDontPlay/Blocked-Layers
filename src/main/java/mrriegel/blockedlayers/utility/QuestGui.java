package mrriegel.blockedlayers.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import mrriegel.blockedlayers.BlockedLayers;
import mrriegel.blockedlayers.Quest;
import mrriegel.blockedlayers.entity.PlayerInformation;
import mrriegel.blockedlayers.reference.Reference;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class QuestGui extends GuiScreen {
	private static final ResourceLocation GuiTextures = new ResourceLocation(
			Reference.MOD_ID + ":textures/gui/questGUI.png");
	int pages, page;
	int numofentrys = 13;
	int imageWidth = 180;
	int imageHeight = 217;
	int guiLeft = (this.width - this.imageWidth) / 2;
	int guiRight = guiLeft + this.imageWidth;
	int guiTop = (this.height - this.imageHeight) / 2;
	int guiBot = guiTop + this.imageHeight;
	GuiButton change;

	enum Mode {
		ALPHABET, PROZENT, LAYER;
		private static Mode[] vals = values();

		public Mode next() {
			return vals[(this.ordinal() + 1) % vals.length];
		}
	}

	Mode mode = Mode.LAYER;
	ArrayList<Quest> qus = new ArrayList<Quest>(
			BlockedLayers.instance.questList);

	public QuestGui(EntityPlayer player) {
		PlayerInformation pro = PlayerInformation.get(player);
		page = 0;
		pages = pro.getQuestBools().size();
	}

	@Override
	public void initGui() {
		super.initGui();
		change = new GuiButton(0, ((this.width - this.imageWidth) / 2) - 20,
				(this.height - this.imageHeight) / 2, 20, 20, mode.toString()
						.substring(0, 1).toUpperCase());
		buttonList.add(change);
	}

	void drawMaps() {
		int in = 0;
		final int red = 0xCD0000;
		final int blue = 0x3A5FCD;
		PlayerInformation pro = PlayerInformation.get(mc.thePlayer);
		sort(pro);
		String title = pro.getTeam().equals("") ? "No Team" : pro.getTeam();
		fontRendererObj.drawString(title,
				this.width / 2 - fontRendererObj.getStringWidth(title) / 2,
				(this.height - this.imageHeight) / 2 + 12, 0x000000);
		for (int i = page; i < page + numofentrys && i < qus.size(); i++) {
			String name = qus.get(i).getName();
			fontRendererObj.drawString(qus.get(i).getLayer() + ": " + name
					+ " " + pro.getQuestNums().get(name + "Num") + "/"
					+ qus.get(i).getNumber(),
					(this.width - this.imageWidth) / 2 + 18,
					(this.height - this.imageHeight) / 2 + 30
							+ (fontRendererObj.FONT_HEIGHT + 4) * in, pro
							.getQuestBools().get(name) ? blue : red);
			in++;
		}
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		super.actionPerformed(p_146284_1_);
		if (p_146284_1_.id == 0) {
			mode = mode.next();
		}
		change.displayString = mode.toString().substring(0, 1).toUpperCase();
	}

	private void sort(final PlayerInformation pro) {
		if (mode == Mode.LAYER) {
			Collections.sort(qus, new Comparator<Quest>() {
				@Override
				public int compare(Quest o1, Quest o2) {
					return o1.getLayer() > o2.getLayer() ? -1 : 1;
				}
			});
		} else if (mode == Mode.ALPHABET) {
			Collections.sort(qus, new Comparator<Quest>() {
				@Override
				public int compare(Quest o1, Quest o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});
		} else if (mode == Mode.PROZENT) {
			Collections.sort(qus, new Comparator<Quest>() {
				@Override
				public int compare(Quest o1, Quest o2) {
					double oo1 = (double) pro.getQuestNums().get(
							o1.getName() + "Num")
							/ (double) o1.getNumber();
					double oo2 = (double) pro.getQuestNums().get(
							o2.getName() + "Num")
							/ (double) o2.getNumber();
					return oo1 > oo2 ? -1 : 1;
				}
			});
		}
	}

	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(GuiTextures);
		guiLeft = (this.width - this.imageWidth) / 2;
		guiRight = guiLeft + this.imageWidth;
		guiTop = (this.height - this.imageHeight) / 2;
		guiBot = guiTop + this.imageHeight;
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.imageWidth,
				this.imageHeight);

		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
		drawMaps();

		RenderItem r = new RenderItem();
		r.renderItemAndEffectIntoGUI(fontRendererObj, mc.getTextureManager(),
				new ItemStack(Items.diamond), guiLeft + this.imageWidth - 35,
				/*(int) (guiTop + 25D + (double)page * (365D / (double)pages))*/
				((8000-35)/qus.size()-numofentrys<1?1:qus.size()-numofentrys)*page+35);
		buggy

	}

	@Override
	public void handleMouseInput() {
		super.handleMouseInput();
		int i = Mouse.getX() * this.width / this.mc.displayWidth;
		int j = this.height - Mouse.getY() * this.height
				/ this.mc.displayHeight - 1;
		if (i > (guiLeft) && i < (guiRight) && j > (guiTop) && j < (guiBot)) {
			int mouse = Mouse.getEventDWheel();
			if (mouse == 0)
				return;
			if (mouse < 0 && page < pages - numofentrys)
				page++;
			else if (mouse > 0 && page > 0)
				page--;
		}

	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

}
