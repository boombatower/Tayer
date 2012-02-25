package com.github.boombatower.tayer;

import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class Tayer extends JavaPlugin {
	protected Logger log = Logger.getLogger("Minecraft");
	protected Vector size = new Vector(10, 10, 10);

	public void onEnable() {
		log.info("Sup dawg");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Only a player can issue this command.");
			return false;
		}
		Player player = (Player) sender;

		if(cmd.getName().equalsIgnoreCase("tayer")) {
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
			
			sender.sendMessage("Generating TNT block of size " + size + "!");
			generateBlock(player.getLocation().add(-(size.getX() / 2), -size.getY(), -(size.getZ() / 2)), size);
			return true;
		}
		
		return false;
	}
	
	protected void generateBlock(Location point, Vector size) {
		World world = point.getWorld();
		 
		int x_start = point.getBlockX();
		int y_start = point.getBlockY();
		int z_start = point.getBlockZ();
	 
		int x_lenght = x_start + size.getBlockX();
		int y_lenght = y_start + size.getBlockY();
		int z_lenght = z_start + size.getBlockZ();
	 
		for (int x_operate = x_start; x_operate < x_lenght; x_operate++) { 
			for (int y_operate = y_start; y_operate < y_lenght; y_operate++) {
				for (int z_operate = z_start; z_operate < z_lenght; z_operate++) {
					Block blockToChange = world.getBlockAt(x_operate, y_operate, z_operate);
					blockToChange.setTypeId(46);
				}
			}
		}
	}
}
