package com.github.devotedmc.hiddenore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.inventory.ItemStack;

public abstract class LootConfig {
	
	public List<DropItemConfig> drops;
	public String dropName;
	public String prefix;
	public DropLimitsConfig limits;
	private Map<String, DropLimitsConfig> biomeLimits;
	
	public LootConfig(String dropName, List<DropItemConfig> drops,
			String prefix, DropLimitsConfig limits) {
		this.dropName = dropName;
		this.drops = drops;
		this.limits = limits;
		this.biomeLimits = new HashMap<String, DropLimitsConfig>();
	}
	
	public void addBiomeLimits(String biome, DropLimitsConfig limits) {
		biomeLimits.put(biome, limits);
	}

	public Set<String> getBiomes() {
		return biomeLimits.keySet();
	}

	public DropLimitsConfig getBiomeLimits(String biome) {
		return biomeLimits.get(biome);
	}

	public List<String> getTools(String biome) {
		return biomeLimits.containsKey(biome) ? biomeLimits.get(biome).tools : limits.tools;
	}

	public boolean dropsWithTool(String biome, ItemStack tool) {
		List<String> t = getTools(biome);
		if (t == null || t.isEmpty()) {
			/*DIAGNOSTICS*HiddenOre.getPlugin().getLogger().log(Level.INFO, "Drops with {0} special case: no tools set", tool);*/
			return true;
		} else {
			boolean ret = ToolConfig.dropsWithTool(t, tool);
			/*DIAGNOSTICS*HiddenOre.getPlugin().getLogger().log(Level.INFO, "Drops with {0} normal case: {1}",
					new Object[] { tool, ret});*/
			return ret;
		}
	}
	
	public ToolConfig dropsWithToolConfig(String biome, ItemStack tool) {
		List<String> t = getTools(biome);
		if (t == null || t.isEmpty()) {
			/*DIAGNOSTICS*HiddenOre.getPlugin().getLogger().log(Level.INFO, "Out of [] picked null for {0}", tool);*/
			return null; // ToolConfig.getAnyTool(tool);
		} else {
			ToolConfig ret = ToolConfig.getTool(t, tool);
			/*DIAGNOSTICS*HiddenOre.getPlugin().getLogger().log(Level.INFO, "Out of {0} picked {1} for {2}", 
					new Object[] {t, ret, tool});*/
			return ret;
		}
	}

	public int getMinY(String biome) {
		return biomeLimits.containsKey(biome) ? biomeLimits.get(biome).minY : limits.minY;
	}

	public int getMaxY(String biome) {
		return biomeLimits.containsKey(biome) ? biomeLimits.get(biome).maxY : limits.maxY;
	}

	public double getMinAmount(String biome) {
		return biomeLimits.containsKey(biome) ? biomeLimits.get(biome).minAmount : limits.minAmount;
	}

	public double getMaxAmount(String biome) {
		return biomeLimits.containsKey(biome) ? biomeLimits.get(biome).maxAmount : limits.maxAmount;
	}

	public double getChance(String biome) {
		return biomeLimits.containsKey(biome) ? biomeLimits.get(biome).chance : limits.chance;
	}

	private XPConfig getBiomeXP(String biome) {
		return biomeLimits.containsKey(biome) ? biomeLimits.get(biome).xp : limits.xp;
	}
	
	public double getXPChance(String biome) {
		XPConfig xp = getBiomeXP(biome);
		return xp != null ? xp.chance : 0.0d;
	}

	public double getXPMinAmount(String biome) {
		XPConfig xp = getBiomeXP(biome);
		return xp != null ? xp.minAmount : 0.0d;
	}
	
	public double getXPMaxAmount(String biome) {
		XPConfig xp = getBiomeXP(biome);
		return xp != null ? xp.maxAmount : 0.0d;
	}
	
	/**
	 * Gives chance to drop XP
	 * 
	 * @param biome
	 * @param modify
	 * @return XP to drop
	 */
	public int renderXP(String biome, ToolConfig modify) {
		/** multipliers **/
		double min = getXPMinAmount(biome) + (modify != null ? modify.getMinAmountModifier() : 0.0);
		double max = getXPMaxAmount(biome) + (modify != null ? modify.getMaxAmountModifier() : 0.0);
		double amount = (min == max) ? min : (double) ((max - min) * Math.random() + min);
		
		if (Config.isDebug) {
			HiddenOre.getPlugin().getLogger().log(Level.INFO, "Trigger xp {0} [{1}, {2}] = {3}", 
					new Object[] {dropName, min, max, amount});
		}
		
		return (int) Math.round(amount);
	}
	
	/**
	 * Gives even chance of any amount. 
	 * 
	 * @param modify
	 * @param biome
	 * @return Items that can be given
	 */
	public abstract List<ItemStack> renderLoot(String biome, ToolConfig modify);
}
