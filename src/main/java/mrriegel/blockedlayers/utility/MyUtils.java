package mrriegel.blockedlayers.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import mrriegel.blockedlayers.BlockedLayers;
import net.minecraft.world.World;

public class MyUtils {
	public static ArrayList<BlockLocation> getAroundBlocks(World world, int x,
			int y, int z) {
		ArrayList<BlockLocation> lis = new ArrayList<BlockLocation>();

		lis.add(new BlockLocation(x - 1, y, z));
		lis.add(new BlockLocation(x + 1, y, z));
		lis.add(new BlockLocation(x - 1, y, z - 1));
		lis.add(new BlockLocation(x + 1, y, z - 1));
		lis.add(new BlockLocation(x - 1, y, z + 1));
		lis.add(new BlockLocation(x + 1, y, z + 1));
		lis.add(new BlockLocation(x, y, z - 1));
		lis.add(new BlockLocation(x, y, z + 1));
		return lis;

	}

	public static void fillVectors() {
		File file = new File("challenge.scr");
		if (!file.exists()) {
			try {
				file.createNewFile();
				PrintWriter pw = new PrintWriter(file, "UTF-8");
				pw.println("#use: addChallenge(name,layer,activity,thing,modID,meta,quantity,onWhat,type)");
				pw.println();
				pw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));

			String line;
			try {
				while ((line = br.readLine()) != null) {
					if (line.length() == 0 || line.charAt(0) == '#') {
						continue;
					}
					if (line.indexOf("(") != -1
							&& line.substring(0, line.indexOf("(")).equals(
									"addChallenge")) {
						line = line.substring(line.indexOf("(") + 1,
								line.indexOf(")"));
						String[] parts = line.split(",");
						BlockedLayers.names.add(parts[0]);
						BlockedLayers.layer.add(parts[1]);
						BlockedLayers.doIt.add(parts[2]);
						BlockedLayers.what.add(parts[3]);
						BlockedLayers.modID.add(parts[4]);
						BlockedLayers.meta.add(parts[5]);
						BlockedLayers.number.add(parts[6]);
						BlockedLayers.type.add(parts[7]);

					}

				}
			} catch (IOException e) {
				System.out.println("ring ioe");

			}
		} catch (FileNotFoundException e1) {
			System.out.println("ring fnfe");

		}

	}
}