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
}