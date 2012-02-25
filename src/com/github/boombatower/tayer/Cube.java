package com.github.boombatower.tayer;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

/**
 * Representation of a cube.
 * 
 * @author boombatower
 */
public class Cube {
	/**
	 * Corner point of the cube.
	 */
	protected Location point;

	/**
	 * Length of sides.
	 */
	protected Vector size;

	/**
	 * Construct a cube given a corner {@link Location} and size {@link Vector}.
	 * 
	 * @param point
	 *        Corner point of cube.
	 * @param size
	 *        Length of sides.
	 */
	public Cube(Location point, Vector size) {
		this.point = point;
		this.size = size;
	}

	public Location getPoint() {
		return point;
	}

	public void setPoint(Location point) {
		this.point = point;
	}

	public Vector getSize() {
		return size;
	}

	public void setSize(Vector size) {
		this.size = size;
	}

	/**
	 * Fill the {@link Cube} with a specific block type.
	 * 
	 * @param type
	 *        Block type.
	 * @see http://www.minecraftwiki.net/wiki/Data_values
	 */
	public void fill(int type) {
		World world = point.getWorld();
		Vector end = end();
		for (int x = point.getBlockX(); x < end.getBlockX(); x++) {
			for (int y = point.getBlockY(); y < end.getBlockY(); y++) {
				for (int z = point.getBlockZ(); z < end.getBlockZ(); z++) {
					Block block = world.getBlockAt(x, y, z);
					block.setTypeId(type);
				}
			}
		}
	}

	/**
	 * Fill an array of {@link Cube}s with a specific block type.
	 * 
	 * @param cubes
	 *        An array of {@link Cube}s.
	 * @param type
	 *        Block type.
	 */
	public static void fill(Cube[] cubes, int type) {
		for (Cube cube : cubes) {
			cube.fill(type);
		}
	}

	/**
	 * Calculate the volume of the {@link Cube}.
	 * 
	 * @return The volume of the {@link Cube}.
	 */
	public double volume() {
		return size.getX() * size.getY() * size.getZ();
	}

	/**
	 * Determine the "end" point or point opposite the start point.
	 * 
	 * @return The "end" point or point opposite the start point.
	 */
	public Vector end() {
		return new Vector(point.getX(), point.getY(), point.getZ()).add(size);
	}

	/**
	 * Divide the {@link Cube} into 8 equal subcubes.
	 * 
	 * @return An array containing 8 equal subcubes.
	 */
	public Cube[] divide() {
		// Extract world and determine the size of 1/8 the current cube.
		World world = point.getWorld();
		Vector half = size.clone();
		half.multiply(0.5);

		// Extract the x, y, and z coords of the start point.
		double x = point.getX();
		double y = point.getY();
		double z = point.getZ();

		// Generate the 8 subcubes.
		Cube[] cubes = new Cube[8];
		cubes[0] = new Cube(new Location(world, x, y, z), half);
		cubes[1] = new Cube(new Location(world, x + half.getX(), y, z), half);

		cubes[2] = new Cube(new Location(world, x, y + half.getY(), z), half);
		cubes[3] = new Cube(new Location(world, x + half.getX(), y + half.getY(), z), half);

		cubes[4] = new Cube(new Location(world, x, y, z + half.getZ()), half);
		cubes[5] = new Cube(new Location(world, x + half.getX(), y, z + half.getZ()), half);

		cubes[6] = new Cube(new Location(world, x, y + half.getY(), z + half.getZ()), half);
		cubes[7] = new Cube(new Location(world, x + half.getX(), y + half.getY(), z + half.getZ()), half);

		return cubes;
	}

	/**
	 * Divide the current {@link Cube} if it exceeds the max volume.
	 * 
	 * @param max
	 *        The maximum volume allowed.
	 * @return An array containing equal subcubes.
	 */
	public Cube[] divide(double max) {
		if (volume() > max) {
			return divide(divide(), max);
		}
		return new Cube[] { this };
	}

	/**
	 * Divide the {@link Cube}s within an array if they exceed the max volume.
	 * 
	 * @param cubes
	 *        An array of {@link Cube}s.
	 * @param max
	 *        The maximum volume allowed.
	 * @return An array containing equal subcubes.
	 */
	public static Cube[] divide(Cube[] cubes, double max) {
		// Assume the cubes are the same size so check the first cube's volume.
		if (cubes[0].volume() > max) {
			// Generate a new array with 8 times as many cubes.
			Cube[] sub = new Cube[cubes.length * 8];
			int i = 0;
			for (Cube cube : cubes) {
				for (Cube c : cube.divide()) {
					sub[i++] = c;
				}
			}
			return divide(sub, max);
		}
		return cubes;
	}
}
