package mrriegel.blockedlayers.stuff;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mrriegel.blockedlayers.BlockedLayers;
import mrriegel.blockedlayers.entity.PlayerInformation;
import mrriegel.blockedlayers.handler.ConfigurationHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class QuestGui extends GuiScreen {
	private static final ResourceLocation GuiTextures = new ResourceLocation(
			BlockedLayers.MOD_ID + ":textures/gui/questGUI.png");
	int pages, page;
	int numofentrys = 13;
	int imageWidth = 180;
	int imageHeight = 217;
	int guiLeft = (this.width - this.imageWidth) / 2;
	int guiRight = guiLeft + this.imageWidth;
	int guiTop = (this.height - this.imageHeight) / 2;
	int guiBot = guiTop + this.imageHeight;
	ArrayList<Item> lis = new ArrayList<Item>(Arrays.asList(new Item[] {
			Items.diamond, Items.emerald, Items.coal, Items.redstone,
			Items.iron_ingot, Items.gold_ingot, Items.quartz }));
	ItemStack deco = null;
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
		deco = new ItemStack(lis.get(new Random().nextInt(lis.size())));
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
		if (ConfigurationHandler.teams) {
			String title = pro.getTeam().equals("") ? StatCollector
					.translateToLocal("bl.gui.noteam") : "Team: "
					+ pro.getTeam();
			fontRendererObj.drawString(title,
					this.width / 2 - fontRendererObj.getStringWidth(title) / 2,
					guiTop + 12, 0x000000);
		}
		for (int i = page; i < page + numofentrys && i < qus.size(); i++) {
			String name = qus.get(i).getName();
			fontRendererObj.drawString(qus.get(i).getLayer() + ": " + name
					+ " " + pro.getQuestNums().get(name + "Num") + "/"
					+ qus.get(i).getNumber(), guiLeft + 18, guiTop + 30
					+ (fontRendererObj.FONT_HEIGHT + 4) * in, pro
					.getQuestBools().get(name) ? blue : red);
			in++;
		}
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) throws IOException {
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
					double oo1 = ((double) pro.getQuestNums().get(
							o1.getName() + "Num"))
							/ ((double) o1.getNumber());
					double oo2 = ((double) pro.getQuestNums().get(
							o2.getName() + "Num"))
							/ ((double) o2.getNumber());
					return Double.compare(oo2, oo1);
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
		if (qus.size() > numofentrys) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDepthMask(false);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			// Tessellator tessellator = Tessellator.instance;
			// tessellator.startDrawing(GL11.GL_QUADS);
			// tessellator.setColorRGBA(117, 50, 30, 255);
			// tessellator.addVertex(guiLeft + this.imageWidth - 28,
			// guiTop + 25 + 8, 0);// lo
			// tessellator.addVertex(guiLeft + this.imageWidth - 28,
			// guiTop + 178 + 8, 0);// lu
			// tessellator.addVertex(guiLeft + this.imageWidth - 26,
			// guiTop + 178 + 8, 0);// ru
			// tessellator.addVertex(guiLeft + this.imageWidth - 26,
			// guiTop + 25 + 8, 0);// ro
			// tessellator.draw();
			GL11.glDepthMask(true);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			RenderItem r = mc.getRenderItem();
			int pos = 178 - 25;
			double s = (double) pos / (double) (pages - numofentrys) * page
					+ 178.D;
			r.renderItemAndEffectIntoGUI(deco, guiLeft + this.imageWidth - 35,
					(int) (guiTop + s - pos));
		}
		drawToolTip();

	}

	private void drawToolTip() {
		int i = Mouse.getX() * this.width / this.mc.displayWidth;
		int j = this.height - Mouse.getY() * this.height
				/ this.mc.displayHeight - 1;
		int in = 0;
		Quest qu = null;
		for (int u = page; u < page + numofentrys && u < qus.size(); u++) {
			Quest q = qus.get(u);
			int tmp = guiTop / 2 + 30 + (fontRendererObj.FONT_HEIGHT + 4) * in;
			int tmp2 = guiTop / 2 + 30 + (fontRendererObj.FONT_HEIGHT + 4)
					* (in + 1);
			if (i > (guiLeft + 18) && i < (guiRight - 30) && j > (tmp)
					&& j < (tmp2)) {
				qu = q;
				break;
			}
			in++;
		}
		if (qu == null)
			return;
		List list = new ArrayList();
		if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
			list.add(StatCollector.translateToLocal("bl.gui.shift"));
		else {
			String s = qu.getText();
			List<String> matchList = new ArrayList<String>();
			Pattern regex = Pattern.compile(".{1,25}(?:\\s|$)", Pattern.DOTALL);
			Matcher regexMatcher = regex.matcher(s);
			while (regexMatcher.find()) {
				matchList.add(regexMatcher.group());
			}
			list = new ArrayList<String>(matchList);
		}
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
		GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
		this.drawHoveringText(list, i, j, fontRendererObj);
		GL11.glPopAttrib();
		GL11.glPopAttrib();

	}

	@Override
	public void handleMouseInput() throws IOException {
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
