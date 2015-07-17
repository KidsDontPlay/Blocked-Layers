package mrriegel.blockedlayers.entity;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Vector;

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
	private boolean l64, l32, l16;
	private HashMap<String, Boolean> bools = new HashMap<String, Boolean>();
	private HashMap<String, Integer> nums = new HashMap<String, Integer>();

	public PlayerInformation(EntityPlayer player) {
		this.player = player;
		this.l64 = false;
		this.l32 = false;
		this.l16 = false;
		for (String s : BlockedLayers.names) {
			bools.put(s, false);
			nums.put(s + "Num", 0);
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

		properties.setBoolean("l64", this.l64);
		properties.setBoolean("l32", this.l32);
		properties.setBoolean("l16", this.l16);

		for (Entry<String, Boolean> entry : bools.entrySet()) {

			properties.setBoolean(entry.getKey(), entry.getValue());
		}

		// for (String key : bools.keySet()) {
		// properties.setBoolean(key, bools.get(key));
		// }

		for (Entry<String, Integer> entry : nums.entrySet()) {
			properties.setInteger(entry.getKey(), entry.getValue());
		}

		compound.setTag(EXT_PROP_NAME, properties);

	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		NBTTagCompound properties = (NBTTagCompound) compound
				.getTag(EXT_PROP_NAME);
		this.l64 = properties.getBoolean("l64");
		this.l32 = properties.getBoolean("l32");
		this.l16 = properties.getBoolean("l16");

		for (Entry<String, Boolean> entry : bools.entrySet()) {
			entry.setValue(properties.getBoolean(entry.getKey()));
		}
		for (Entry<String, Integer> entry : nums.entrySet()) {
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

	public boolean isL64() {
		return l64;
	}

	public void setL64(boolean l64) {
		this.l64 = l64;
	}

	public boolean isL32() {
		return l32;
	}

	public void setL32(boolean l32) {
		this.l32 = l32;
	}

	public boolean isL16() {
		return l16;
	}

	public void setL16(boolean l16) {
		this.l16 = l16;
	}

	public HashMap<String, Boolean> getBools() {
		return bools;
	}

	public void setBools(HashMap<String, Boolean> bools) {
		this.bools = bools;
	}

	public HashMap<String, Integer> getNums() {
		return nums;
	}

	public void setNums(HashMap<String, Integer> nums) {
		this.nums = nums;
	}
}
