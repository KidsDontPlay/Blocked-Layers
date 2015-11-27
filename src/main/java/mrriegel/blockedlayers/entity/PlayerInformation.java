package mrriegel.blockedlayers.entity;

import java.util.HashMap;
import java.util.Map.Entry;

import mrriegel.blockedlayers.BlockedLayers;
import mrriegel.blockedlayers.Quest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class PlayerInformation implements IExtendedEntityProperties {

	public final static String EXT_PROP_NAME = "PlayerInformation";

	private HashMap<Integer, Boolean> layerBools = new HashMap<Integer, Boolean>();
	private HashMap<String, Boolean> questBools = new HashMap<String, Boolean>();
	private HashMap<String, Integer> questNums = new HashMap<String, Integer>();
	private String team;

	public PlayerInformation() {
		for (Quest q : BlockedLayers.instance.questList) {
			questBools.put(q.getName(), false);
			questNums.put(q.getName() + "Num", 0);
			layerBools.put(q.getLayer(), false);
		}
		team = "";

	}

	public static final void register(EntityPlayer player) {
		player.registerExtendedProperties(PlayerInformation.EXT_PROP_NAME,
				new PlayerInformation());
	}

	public static final PlayerInformation get(EntityPlayer player) {
		return (PlayerInformation) player.getExtendedProperties(EXT_PROP_NAME);
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		NBTTagCompound properties = new NBTTagCompound();
		for (Entry<Integer, Boolean> entry : layerBools.entrySet()) {
			properties.setBoolean(String.valueOf(entry.getKey()),
					entry.getValue());
		}

		for (Entry<String, Boolean> entry : questBools.entrySet()) {
			properties.setBoolean(entry.getKey(), entry.getValue());
		}

		for (Entry<String, Integer> entry : questNums.entrySet()) {
			properties.setInteger(entry.getKey(), entry.getValue());
		}

		compound.setTag(EXT_PROP_NAME, properties);
		compound.setString("team", team);

	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		NBTTagCompound properties = (NBTTagCompound) compound
				.getTag(EXT_PROP_NAME);

		for (Entry<Integer, Boolean> entry : layerBools.entrySet()) {
			entry.setValue(properties.getBoolean(String.valueOf(entry.getKey())));
		}
		for (Entry<String, Boolean> entry : questBools.entrySet()) {
			entry.setValue(properties.getBoolean(entry.getKey()));
		}
		for (Entry<String, Integer> entry : questNums.entrySet()) {
			entry.setValue(properties.getInteger(entry.getKey()));
		}
		team = compound.getString("team");

	}

	public HashMap<Integer, Boolean> getLayerBools() {
		return layerBools;
	}

	public void setLayerBools(HashMap<Integer, Boolean> layerBools) {
		this.layerBools = layerBools;
	}

	public HashMap<String, Boolean> getQuestBools() {
		return questBools;
	}

	public void setQuestBools(HashMap<String, Boolean> questBools) {
		this.questBools = questBools;
	}

	public HashMap<String, Integer> getQuestNums() {
		return questNums;
	}

	public void setQuestNums(HashMap<String, Integer> questNums) {
		this.questNums = questNums;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	@Override
	public void init(Entity entity, World world) {

	}

}
