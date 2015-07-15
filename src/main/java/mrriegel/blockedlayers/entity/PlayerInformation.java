package mrriegel.blockedlayers.entity;

import com.sun.corba.se.spi.activation.Server;

import mrriegel.blockedlayers.proxy.ServerProxy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class PlayerInformation implements IExtendedEntityProperties {

	public final static String EXT_PROP_NAME = "PlayerInformation";

	private final EntityPlayer player;
	private boolean l64, l32, l16, steak, wheat, baby, stonepick, stoneshovel,
			bed, zombie, spider, farmland, cake, anvil, ender, bat, armor,
			ignite, creeper, skeleton, clay, quarz, eme, apple, potion, blaze,
			cube;
	private int blazeNum, cubeNum, enderNum, creeperNum, skeletonNum, clayNum,
			spiderNum, zombieNum, babyNum, farmlandNum, wheatNum, steakNum;

	public PlayerInformation(EntityPlayer player) {
		this.player = player;
		this.l64 = false;
		this.l32 = false;
		this.l16 = false;
		this.steak = false;
		this.wheat = false;
		this.baby = false;
		this.stonepick = false;
		this.stoneshovel = false;
		this.bed = false;
		this.zombie = false;
		this.spider = false;
		this.farmland = false;
		this.cake = false;
		this.anvil = false;
		this.ender = false;
		this.bat = false;
		this.armor = false;
		this.ignite = false;
		this.creeper = false;
		this.skeleton = false;
		this.clay = false;
		this.quarz = false;
		this.eme = false;
		this.apple = false;
		this.potion = false;
		this.blaze = false;
		this.cube = false;
		this.blazeNum = 0;
		this.cubeNum = 0;
		this.enderNum = 0;
		this.creeperNum = 0;
		this.skeletonNum = 0;
		this.clayNum = 0;
		this.spiderNum = 0;
		this.zombieNum = 0;
		this.babyNum = 0;
		this.farmlandNum = 0;
		this.wheatNum = 0;
		this.steakNum = 0;
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
		properties.setBoolean("steak", this.steak);
		properties.setBoolean("wheat", this.wheat);
		properties.setBoolean("baby", this.baby);
		properties.setBoolean("stonepick", this.stonepick);
		properties.setBoolean("stoneshovel", this.stoneshovel);
		properties.setBoolean("bed", this.bed);
		properties.setBoolean("zombie", this.zombie);
		properties.setBoolean("spider", this.spider);
		properties.setBoolean("farmland", this.farmland);
		properties.setBoolean("cake", this.cake);
		properties.setBoolean("anvil", this.anvil);
		properties.setBoolean("ender", this.ender);
		properties.setBoolean("bat", this.bat);
		properties.setBoolean("armor", this.armor);
		properties.setBoolean("ignite", this.ignite);
		properties.setBoolean("creeper", this.creeper);
		properties.setBoolean("skeleton", this.skeleton);
		properties.setBoolean("clay", this.clay);
		properties.setBoolean("eme", this.eme);
		properties.setBoolean("apple", this.apple);
		properties.setBoolean("potion", this.potion);
		properties.setBoolean("blaze", this.blaze);
		properties.setBoolean("cube", this.cube);

		properties.setInteger("blazeNum", this.blazeNum);
		properties.setInteger("cubeNum", this.cubeNum);
		properties.setInteger("enderNum", this.enderNum);
		properties.setInteger("creeperNum", this.creeperNum);
		properties.setInteger("skeletonNum", this.skeletonNum);
		properties.setInteger("clayNum", this.clayNum);
		properties.setInteger("spiderNum", this.spiderNum);
		properties.setInteger("zombieNum", this.zombieNum);
		properties.setInteger("babyNum", this.babyNum);
		properties.setInteger("farmlandNum", this.farmlandNum);
		properties.setInteger("wheatNum", this.wheatNum);
		properties.setInteger("steakNum", this.steakNum);

		compound.setTag(EXT_PROP_NAME, properties);

	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		NBTTagCompound properties = (NBTTagCompound) compound
				.getTag(EXT_PROP_NAME);
		this.l64 = properties.getBoolean("l64");
		this.l32 = properties.getBoolean("l32");
		this.l16 = properties.getBoolean("l16");
		this.steak = properties.getBoolean("steak");
		this.wheat = properties.getBoolean("wheat");
		this.baby = properties.getBoolean("baby");
		this.stonepick = properties.getBoolean("stonepick");
		this.stoneshovel = properties.getBoolean("stoneshovel");
		this.bed = properties.getBoolean("bed");
		this.zombie = properties.getBoolean("zombie");
		this.spider = properties.getBoolean("spider");
		this.farmland = properties.getBoolean("farmland");
		this.cake = properties.getBoolean("cake");
		this.anvil = properties.getBoolean("anvil");
		this.ender = properties.getBoolean("ender");
		this.bat = properties.getBoolean("bat");
		this.armor = properties.getBoolean("armor");
		this.ignite = properties.getBoolean("ignite");
		this.creeper = properties.getBoolean("creeper");
		this.skeleton = properties.getBoolean("skeleton");
		this.clay = properties.getBoolean("clay");
		this.eme = properties.getBoolean("eme");
		this.apple = properties.getBoolean("apple");
		this.potion = properties.getBoolean("potion");
		this.blaze = properties.getBoolean("blaze");
		this.cube = properties.getBoolean("cube");

		this.blazeNum = properties.getInteger("blazeNum");
		this.cubeNum = properties.getInteger("cubeNum");
		this.enderNum = properties.getInteger("enderNum");
		this.creeperNum = properties.getInteger("creeperNum");
		this.skeletonNum = properties.getInteger("skeletonNum");
		this.clayNum = properties.getInteger("clayNum");
		this.spiderNum = properties.getInteger("spiderNum");
		this.spiderNum = properties.getInteger("zombieNum");
		this.babyNum = properties.getInteger("babyNum");
		this.farmlandNum = properties.getInteger("farmlandNum");
		this.wheatNum = properties.getInteger("wheatNum");
		this.steakNum = properties.getInteger("steakNum");

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

	public boolean isSteak() {
		return steak;
	}

	public void setSteak(boolean steak) {
		this.steak = steak;
	}

	public boolean isWheat() {
		return wheat;
	}

	public void setWheat(boolean wheat) {
		this.wheat = wheat;
	}

	public boolean isBaby() {
		return baby;
	}

	public void setBaby(boolean baby) {
		this.baby = baby;
	}

	public boolean isStonepick() {
		return stonepick;
	}

	public void setStonepick(boolean stonepick) {
		this.stonepick = stonepick;
	}

	public boolean isStoneshovel() {
		return stoneshovel;
	}

	public void setStoneshovel(boolean stoneshovel) {
		this.stoneshovel = stoneshovel;
	}

	public boolean isBed() {
		return bed;
	}

	public void setBed(boolean bed) {
		this.bed = bed;
	}

	public boolean isZombie() {
		return zombie;
	}

	public void setZombie(boolean zombie) {
		this.zombie = zombie;
	}

	public boolean isSpider() {
		return spider;
	}

	public void setSpider(boolean spider) {
		this.spider = spider;
	}

	public boolean isFarmland() {
		return farmland;
	}

	public void setFarmland(boolean farmland) {
		this.farmland = farmland;
	}

	public boolean isCake() {
		return cake;
	}

	public void setCake(boolean cake) {
		this.cake = cake;
	}

	public boolean isAnvil() {
		return anvil;
	}

	public void setAnvil(boolean anvil) {
		this.anvil = anvil;
	}

	public boolean isEnder() {
		return ender;
	}

	public void setEnder(boolean ender) {
		this.ender = ender;
	}

	public boolean isBat() {
		return bat;
	}

	public void setBat(boolean bat) {
		this.bat = bat;
	}

	public boolean isArmor() {
		return armor;
	}

	public void setArmor(boolean armor) {
		this.armor = armor;
	}

	public boolean isIgnite() {
		return ignite;
	}

	public void setIgnite(boolean ignite) {
		this.ignite = ignite;
	}

	public boolean isCreeper() {
		return creeper;
	}

	public void setCreeper(boolean creeper) {
		this.creeper = creeper;
	}

	public boolean isSkeleton() {
		return skeleton;
	}

	public void setSkeleton(boolean skeleton) {
		this.skeleton = skeleton;
	}

	public boolean isClay() {
		return clay;
	}

	public void setClay(boolean clay) {
		this.clay = clay;
	}

	public boolean isQuarz() {
		return quarz;
	}

	public void setQuarz(boolean quarz) {
		this.quarz = quarz;
	}

	public boolean isEme() {
		return eme;
	}

	public void setEme(boolean eme) {
		this.eme = eme;
	}

	public boolean isApple() {
		return apple;
	}

	public void setApple(boolean apple) {
		this.apple = apple;
	}

	public boolean isPotion() {
		return potion;
	}

	public void setPotion(boolean potion) {
		this.potion = potion;
	}

	public boolean isBlaze() {
		return blaze;
	}

	public void setBlaze(boolean blaze) {
		this.blaze = blaze;
	}

	public boolean isCube() {
		return cube;
	}

	public void setCube(boolean cube) {
		this.cube = cube;
	}

	public int getBlazeNum() {
		return blazeNum;
	}

	public void setBlazeNum(int blazeNum) {
		this.blazeNum = blazeNum;
	}

	public int getCubeNum() {
		return cubeNum;
	}

	public void setCubeNum(int cubeNum) {
		this.cubeNum = cubeNum;
	}

	public int getEnderNum() {
		return enderNum;
	}

	public void setEnderNum(int enderNum) {
		this.enderNum = enderNum;
	}

	public int getCreeperNum() {
		return creeperNum;
	}

	public void setCreeperNum(int creeperNum) {
		this.creeperNum = creeperNum;
	}

	public int getSkeletonNum() {
		return skeletonNum;
	}

	public void setSkeletonNum(int skeletonNum) {
		this.skeletonNum = skeletonNum;
	}

	public int getClayNum() {
		return clayNum;
	}

	public void setClayNum(int clayNum) {
		this.clayNum = clayNum;
	}

	public int getSpiderNum() {
		return spiderNum;
	}

	public void setSpiderNum(int spiderNum) {
		this.spiderNum = spiderNum;
	}

	public int getZombieNum() {
		return zombieNum;
	}

	public void setZombieNum(int zombieNum) {
		this.zombieNum = zombieNum;
	}

	public int getBabyNum() {
		return babyNum;
	}

	public void setBabyNum(int babyNum) {
		this.babyNum = babyNum;
	}

	public int getFarmlandNum() {
		return farmlandNum;
	}

	public void setFarmlandNum(int farmlandNum) {
		this.farmlandNum = farmlandNum;
	}

	public int getWheatNum() {
		return wheatNum;
	}

	public void setWheatNum(int wheatNum) {
		this.wheatNum = wheatNum;
	}

	public int getSteakNum() {
		return steakNum;
	}

	public void setSteakNum(int steakNum) {
		this.steakNum = steakNum;
	}

	public EntityPlayer getPlayer() {
		return player;
	}
	
	
}
