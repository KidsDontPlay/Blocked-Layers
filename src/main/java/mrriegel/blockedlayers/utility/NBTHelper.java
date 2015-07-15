package mrriegel.blockedlayers.utility;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/** by Pahimar */

public class NBTHelper {
	public static boolean hasTag(ItemStack itemStack, String keyName) {
		return itemStack != null && itemStack.stackTagCompound != null
				&& itemStack.stackTagCompound.hasKey(keyName);
	}

	public static void removeTag(ItemStack itemStack, String keyName) {
		if (itemStack.stackTagCompound != null) {
			itemStack.stackTagCompound.removeTag(keyName);
		}
	}

	/**
	 * Initializes the NBT Tag Compound for the given ItemStack if it is null
	 *
	 * @param itemStack
	 *            The ItemStack for which its NBT Tag Compound is being checked
	 *            for initialization
	 */
	private static void initNBTTagCompound(ItemStack itemStack) {
		if (itemStack.stackTagCompound == null) {
			itemStack.setTagCompound(new NBTTagCompound());
		}
	}

	public static void setLong(ItemStack itemStack, String keyName,
			long keyValue) {
		initNBTTagCompound(itemStack);

		itemStack.stackTagCompound.setLong(keyName, keyValue);
	}

	// String
	public static String getString(ItemStack itemStack, String keyName) {
		initNBTTagCompound(itemStack);

		if (!itemStack.stackTagCompound.hasKey(keyName)) {
			setString(itemStack, keyName, "");
		}

		return itemStack.stackTagCompound.getString(keyName);
	}

	public static void setString(ItemStack itemStack, String keyName,
			String keyValue) {
		initNBTTagCompound(itemStack);

		itemStack.stackTagCompound.setString(keyName, keyValue);
	}

	// boolean
	public static boolean getBoolean(ItemStack itemStack, String keyName) {
		initNBTTagCompound(itemStack);

		if (!itemStack.stackTagCompound.hasKey(keyName)) {
			setBoolean(itemStack, keyName, false);
		}

		return itemStack.stackTagCompound.getBoolean(keyName);
	}

	public static void setBoolean(ItemStack itemStack, String keyName,
			boolean keyValue) {
		initNBTTagCompound(itemStack);

		itemStack.stackTagCompound.setBoolean(keyName, keyValue);
	}

	// byte
	public static byte getByte(ItemStack itemStack, String keyName) {
		initNBTTagCompound(itemStack);

		if (!itemStack.stackTagCompound.hasKey(keyName)) {
			setByte(itemStack, keyName, (byte) 0);
		}

		return itemStack.stackTagCompound.getByte(keyName);
	}

	public static void setByte(ItemStack itemStack, String keyName,
			byte keyValue) {
		initNBTTagCompound(itemStack);

		itemStack.stackTagCompound.setByte(keyName, keyValue);
	}

	// short
	public static short getShort(ItemStack itemStack, String keyName) {
		initNBTTagCompound(itemStack);

		if (!itemStack.stackTagCompound.hasKey(keyName)) {
			setShort(itemStack, keyName, (short) 0);
		}

		return itemStack.stackTagCompound.getShort(keyName);
	}

	public static void setShort(ItemStack itemStack, String keyName,
			short keyValue) {
		initNBTTagCompound(itemStack);

		itemStack.stackTagCompound.setShort(keyName, keyValue);
	}

	// int
	public static int getInt(ItemStack itemStack, String keyName) {
		initNBTTagCompound(itemStack);

		if (!itemStack.stackTagCompound.hasKey(keyName)) {
			setInteger(itemStack, keyName, 0);
		}

		return itemStack.stackTagCompound.getInteger(keyName);
	}

	public static void setInteger(ItemStack itemStack, String keyName,
			int keyValue) {
		initNBTTagCompound(itemStack);

		itemStack.stackTagCompound.setInteger(keyName, keyValue);
	}

	// long
	public static long getLong(ItemStack itemStack, String keyName) {
		initNBTTagCompound(itemStack);

		if (!itemStack.stackTagCompound.hasKey(keyName)) {
			setLong(itemStack, keyName, 0);
		}

		return itemStack.stackTagCompound.getLong(keyName);
	}

	// float
	public static float getFloat(ItemStack itemStack, String keyName) {
		initNBTTagCompound(itemStack);

		if (!itemStack.stackTagCompound.hasKey(keyName)) {
			setFloat(itemStack, keyName, 0);
		}

		return itemStack.stackTagCompound.getFloat(keyName);
	}

	public static void setFloat(ItemStack itemStack, String keyName,
			float keyValue) {
		initNBTTagCompound(itemStack);

		itemStack.stackTagCompound.setFloat(keyName, keyValue);
	}

	// double
	public static double getDouble(ItemStack itemStack, String keyName) {
		initNBTTagCompound(itemStack);

		if (!itemStack.stackTagCompound.hasKey(keyName)) {
			setDouble(itemStack, keyName, 0);
		}

		return itemStack.stackTagCompound.getDouble(keyName);
	}

	public static void setDouble(ItemStack itemStack, String keyName,
			double keyValue) {
		initNBTTagCompound(itemStack);

		itemStack.stackTagCompound.setDouble(keyName, keyValue);
	}

	/** player */

	public static boolean hasTag(EntityPlayer player, String keyName) {
		return player != null && player.getEntityData() != null
				&& player.getEntityData().hasKey(keyName);
	}

	public static void removeTag(EntityPlayer player, String keyName) {
		if (player.getEntityData() != null) {
			player.getEntityData().removeTag(keyName);
		}
	}

	public static void setLong(EntityPlayer player, String keyName,
			long keyValue) {

		player.getEntityData().setLong(keyName, keyValue);
	}

	// String
	public static String getString(EntityPlayer player, String keyName) {

		if (!player.getEntityData().hasKey(keyName)) {
			setString(player, keyName, "");
		}

		return player.getEntityData().getString(keyName);
	}

	public static void setString(EntityPlayer player, String keyName,
			String keyValue) {

		player.getEntityData().setString(keyName, keyValue);
	}

	// boolean
	public static boolean getBoolean(EntityPlayer player, String keyName) {

		if (!player.getEntityData().hasKey(keyName)) {
			setBoolean(player, keyName, false);
		}

		return player.getEntityData().getBoolean(keyName);
	}

	public static void setBoolean(EntityPlayer player, String keyName,
			boolean keyValue) {

		player.getEntityData().setBoolean(keyName, keyValue);
	}

	// byte
	public static byte getByte(EntityPlayer player, String keyName) {

		if (!player.getEntityData().hasKey(keyName)) {
			setByte(player, keyName, (byte) 0);
		}

		return player.getEntityData().getByte(keyName);
	}

	public static void setByte(EntityPlayer player, String keyName,
			byte keyValue) {

		player.getEntityData().setByte(keyName, keyValue);
	}

	// short
	public static short getShort(EntityPlayer player, String keyName) {

		if (!player.getEntityData().hasKey(keyName)) {
			setShort(player, keyName, (short) 0);
		}

		return player.getEntityData().getShort(keyName);
	}

	public static void setShort(EntityPlayer player, String keyName,
			short keyValue) {

		player.getEntityData().setShort(keyName, keyValue);
	}

	// int
	public static int getInt(EntityPlayer player, String keyName) {

		if (!player.getEntityData().hasKey(keyName)) {
			setInteger(player, keyName, 0);
		}

		return player.getEntityData().getInteger(keyName);
	}

	public static void setInteger(EntityPlayer player, String keyName,
			int keyValue) {

		player.getEntityData().setInteger(keyName, keyValue);
	}

	// long
	public static long getLong(EntityPlayer player, String keyName) {

		if (!player.getEntityData().hasKey(keyName)) {
			setLong(player, keyName, 0);
		}

		return player.getEntityData().getLong(keyName);
	}

	// float
	public static float getFloat(EntityPlayer player, String keyName) {

		if (!player.getEntityData().hasKey(keyName)) {
			setFloat(player, keyName, 0);
		}

		return player.getEntityData().getFloat(keyName);
	}

	public static void setFloat(EntityPlayer player, String keyName,
			float keyValue) {

		player.getEntityData().setFloat(keyName, keyValue);
	}

	// double
	public static double getDouble(EntityPlayer player, String keyName) {

		if (!player.getEntityData().hasKey(keyName)) {
			setDouble(player, keyName, 0);
		}

		return player.getEntityData().getDouble(keyName);
	}

	public static void setDouble(EntityPlayer player, String keyName,
			double keyValue) {

		player.getEntityData().setDouble(keyName, keyValue);
	}
}