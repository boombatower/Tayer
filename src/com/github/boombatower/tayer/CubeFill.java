package com.github.boombatower.tayer;

import org.bukkit.Bukkit;

/**
 * Runnable class for filling cubes in a scheduler thread.
 * 
 * @author boombatower
 */
public class CubeFill implements Runnable {
	/**
	 * Array of {@link Cube}s to fill.
	 */
	protected Cube[] cubes;

	/**
	 * Block type fill.
	 */
	protected int type;

	/**
	 * {@link Bukkit} task ID required to end task.
	 */
	public int taskID;

	/**
	 * The index of the current cube.
	 */
	protected int current = 0;

	/**
	 * Construct a {@link CubeFill} with an array of {@link Cube}s and the block
	 * type fill.
	 * 
	 * @param cubes
	 *        An array of {@link Cube}s.
	 * @param type
	 *        Block type fill.
	 */
	public CubeFill(Cube[] cubes, int type) {
		this.cubes = cubes;
		this.type = type;
	}

	@Override
	public void run() {
		// Fill the current cube and incrament the index.
		cubes[current++].fill(type);
		Bukkit.getLogger().info("Generated cube: " + current);

		// End the thread if all the cubes have been filled.
		if (current == cubes.length) {
			Bukkit.getScheduler().cancelTask(taskID);
			Bukkit.getLogger().info("Cancel task: " + taskID);
		}
	}
}
