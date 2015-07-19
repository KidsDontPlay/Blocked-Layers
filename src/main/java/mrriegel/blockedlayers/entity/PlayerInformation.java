package mrriegel.blockedlayers.entity;

import java.util.HashMap;
import java.util.Map.Entry;

import mrriegel.blockedlayers.BlockedLayers;
import mrriegel.blockedlayers.proxy.ServerProxy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class PlayerInformation implements IExtendedEntityProperties {

	public final static String EXT_PROP_NAME = "PlayerInformation";

	private final EntityPlayer player;
	private HashMap<String, Boolean> layerBools = new HashMap<String, Boolean>();
	private HashMap<String, Boolean> questBools = new HashMap<String, Boolean>();
	private HashMap<String, Integer> questNums = new HashMap<String, Integer>();

	public PlayerInformation(EntityPlayer player) {
		this.player = player;
		for (String s : BlockedLayers.names) {

			questBools.put(s, false);
			questNums.put(s + "Num", 0);
		}
		for (String s : BlockedLayers.layer) {
			layerBools.put(s, false);
		}

	}

	public static final void register(EntityPlayer player) {
		player.registerExtendedProperties(PlayerInformation.EXT_PROP_NAME,
				new PlayerInformation(player));
	}

	public static final PlayerInformation get(EntityPlayer player) {
		return (PlayerInformation) player.getExtendedProperties(EXT_PROP_NAME);
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		NBTTagCompound properties = new NBTTagCompound();

		for (Entry<String, Boolean> entry : layerBools.entrySet()) {
			properties.setBoolean(entry.getKey(), entry.getValue());
		}

		for (Entry<String, Boolean> entry : questBools.entrySet()) {
			properties.setBoolean(entry.getKey(), entry.getValue());
		}

		// for (String key : bools.keySet()) {
		// properties.setBoolean(key, bools.get(key));
		// }

		for (Entry<String, Integer> entry : questNums.entrySet()) {
			properties.setInteger(entry.getKey(), entry.getValue());
		}

		compound.setTag(EXT_PROP_NAME, properties);

	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		NBTTagCompound properties = (NBTTagCompound) compound
				.getTag(EXT_PROP_NAME);

		for (Entry<String, Boolean> entry : layerBools.entrySet()) {
			entry.setValue(properties.getBoolean(entry.getKey()));
		}
		for (Entry<String, Boolean> entry : questBools.entrySet()) {
			entry.setValue(properties.getBoolean(entry.getKey()));
		}
		for (Entry<String, Integer> entry : questNums.entrySet()) {
			entry.setValue(properties.getInteger(entry.getKey()));
		}

	}

	private static final String getSaveKey(EntityPlayer player) {
		return player.getCommandSenderName() + ":" + EXT_PROP_NAME;
	}

	public static final void loadProxyData(EntityPlayer player) {
		PlayerInformation playerData = PlayerInformation.get(player);
		NBTTagCompound savedData = ServerProxy
				.getEntityData(getSaveKey(player));
		if (savedData != null) {
			playerData.loadNBTData(savedData);
		}
	}

	public static final void saveProxyData(EntityPlayer player) {
		PlayerInformation playerData = PlayerInformation.get(player);
		NBTTagCompound savedData = new NBTTagCompound();

		playerData.saveNBTData(savedData);
		ServerProxy.storeEntityData(getSaveKey(player), savedData);
	}

	@Override
	public void init(Entity entity, World world) {

	}

	public HashMap<String, Boolean> getLayerBools() {
		return layerBools;
	}

	public void setLayerBools(HashMap<String, Boolean> bools) {
		this.layerBools = bools;
	}

	public HashMap<String, Boolean> getQuestBools() {
		return questBools;
	}

	public void setQuestBools(HashMap<String, Boolean> bools) {
		this.questBools = bools;
	}

	public HashMap<String, Integer> getQuestNums() {
		return questNums;
	}

	public void setQuestNums(HashMap<String, Integer> nums) {
		this.questNums = nums;
	}
}
