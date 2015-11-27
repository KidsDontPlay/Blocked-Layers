package mrriegel.blockedlayers;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

public class Reward {
	int layer;
	ArrayList<String> rewards;

	public Reward(int layer, ArrayList<String> rewards) {
		this.layer = layer;
		this.rewards = rewards;
	}

	public int getLayer() {
		return layer;
	}

	public ArrayList<String> getRewards() {
		return rewards;
	}
}
