package mrriegel.blockedlayers.stuff;

public class Quest {
	private String name, activity, object, modID, text;
	private int layer, meta, number;

	public Quest(String name, String activity, String object, String modID,
			String text, int layer, int meta, int number) {
		super();
		this.name = name;
		this.activity = activity;
		this.object = object;
		this.modID = modID;
		this.text = text;
		this.layer = layer;
		this.meta = meta;
		this.number = number;
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

	public String getText() {
		return text;
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
