package com.github.boombatower.tayer;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

/**
 * Primary Tayer plugin class.
 * 
 * @author boombatower
 */
public class Tayer extends JavaPlugin {
	/**
	 * Keep track of the size used by each player.
	 */
	protected HashMap<String, Vector> sizes = new HashMap<String, Vector>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		// Only allow players with a location.
		if (!(sender instanceof Player)) {
			sender.sendMessage("Only a player can issue this command.");
			return false;
		}

		// Cast player object and determine player name.
		Player player = (Player) sender;
		String name = player.getName();

		if (cmd.getName().equalsIgnoreCase("tayer")) {
			// Load previous size used by player, otherwise use size of 10 as default.
			Vector size;
			if (sizes.containsKey(name)) {
				size = sizes.get(name);
			}
			else {
				sizes.put(name, size = new Vector(10, 10, 10));
			}

			// Parse and apply the arguments based on the format.
			if (args.length == 1) {
				size.setX(Integer.parseInt(args[0]));
				size.setY(Integer.parseInt(args[0]));
				size.setZ(Integer.parseInt(args[0]));
			}
			else if (args.length == 2) {
				size.setX(Integer.parseInt(args[0]));
				size.setY(Integer.parseInt(args[1]));
				size.setZ(Integer.parseInt(args[0]));
			}
			else if (args.length == 3) {
				size.setX(Integer.parseInt(args[0]));
				size.setY(Integer.parseInt(args[1]));
				size.setZ(Integer.parseInt(args[2]));
			}

			// Calculate the start location for a cube centered underneath the player.
			Location start = player.getLocation().add(-(size.getX() / 2), -size.getY(), -(size.getZ() / 2));

			// Correct the cube if it overflows past the top of bottom of the map.
			if (start.getY() < 0) {
				size.setY((int) (size.getY() + start.getY()));
				start.setY(0);
			}
			// @TODO Find variable for world height.
			else if (start.getY() + size.getY() > 128) {
				size.setY((int) (size.getY() - start.getY() + 128));
			}

			// Broadcast a message about TNT creation.
			getServer().broadcastMessage(name + " is generating " + size + " block of TNT!");

			// Generate the request cube and recursively divide if necessary.
			Cube cube = new Cube(start, size);
			Cube[] cubes = cube.divide(16000); // @TODO Make configurable.

			// Fill the cubes with TNT using a background thread.
			CubeFill fill = new CubeFill(cubes, 46);
			int taskID = getServer().getScheduler().scheduleAsyncRepeatingTask(this, fill, 5L, 80L);
			fill.taskID = taskID;
			return true;
		}

		return false;
	}
}
