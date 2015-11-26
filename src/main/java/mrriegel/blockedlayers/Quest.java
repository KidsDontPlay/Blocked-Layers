package mrriegel.blockedlayers;

public class Quest {
	String name, activity, object, modID;
	int layer, meta, number;

	public Quest(String name, String activity, String object, String modID,
			int layer, int meta, int number) {
		super();
		this.name = name;
		this.activity = activity;
		this.object = object;
		this.modID = modID;
		this.layer = layer;
		this.meta = meta;
		this.number = number;
	}

	@Override
	public String toString() {
		return "Quest [name=" + name + ", activity=" + activity + ", object="
				+ object + ", modID=" + modID + ", layer=" + layer + ", meta="
				+ meta + ", number=" + number + "]";
	}

	public String getName() {
		return name;
	}

	public String getActivity() {
		return activity;
	}

	public String getObject() {
		return object;
	}

	public String getModID() {
		return modID;
	}

	public int getLayer() {
		return layer;
	}

	public int getMeta() {
		return meta;
	}

	public int getNumber() {
		return number;
	}

}
