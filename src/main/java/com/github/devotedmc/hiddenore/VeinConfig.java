package com.github.devotedmc.hiddenore;

import org.bukkit.Location;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

public class VeinConfig {
	
	private String blockConfig;
	private double density;
	private double maxSpan;
	private double densityBonus;
	private double areaHeight;
	private double areaSpan;
	private double heightLength;
	private double densityLength;

	private NoiseGenerator heightNoiseGen;
	private NoiseGenerator densityNoiseGen;
	
	
	public VeinConfig(int seed, double density, double maxSpan, double densityBonus, double areaHeight,
			double areaSpan, double heightLength, double densityLength, String blockConfig) {
		this.blockConfig = blockConfig;
		this.density = density;
		this.maxSpan = maxSpan;
		this.densityBonus = densityBonus;
		this.areaHeight = areaHeight;
		this.areaSpan = areaSpan;
		this.heightLength = heightLength;
		this.densityLength = densityLength;
		this.heightNoiseGen = new SimplexNoiseGenerator(seed);
		this.densityNoiseGen = new SimplexNoiseGenerator(seed * 5646468L);
	}

	public double getOreChance(Location loc) {
		return getOreChance(loc.getX(), loc.getBlockY(), loc.getZ());
	}
	
	public double getOreChance(double x, int y, double z) {
		double chance = Math.abs(y-getVeinHeight(x, z));
		if(chance > maxSpan) return 0;
		return Math.max(((Math.cos(chance * Math.PI / maxSpan) + 1) / 2) * getVeinDensity(x, z), 0);
	}
	
	private double getVeinHeight(double x, double z) {
		return heightNoiseGen.noise(x / heightLength, z / heightLength) * areaSpan + areaHeight;
	}
	
	private double getVeinDensity(double x, double z) {
		return (densityNoiseGen.noise(x / densityLength, z / densityLength) + densityBonus) * density;
	}
	
	public String getBlockConfig() {
		return blockConfig;
	}
}
