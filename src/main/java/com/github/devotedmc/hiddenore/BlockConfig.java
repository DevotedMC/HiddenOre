package com.github.devotedmc.hiddenore;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class BlockConfig {
	private String material;
	public Set<Byte> subtypes;
	public Collection<MaterialWrapper> validGenTypes;
	public boolean dropMultiple;
	public boolean suppressDrops;
	private List<String> loots;
	private List<String> veins;
	private String prefix;

	public BlockConfig(String material, boolean dropMultiple, boolean suppressDrops, String prefix, Collection<MaterialWrapper> validGenTypes) {
		this(material, null, dropMultiple, suppressDrops, prefix, validGenTypes);
	}

	public BlockConfig(String material, Collection<Byte> subtype, boolean dropMultiple, boolean suppressDrops, String prefix, Collection<MaterialWrapper> validGenTypes) {
		this.material = material;
		this.subtypes = (subtype != null) ? new HashSet<Byte>(subtype) : new HashSet<Byte>();
		this.dropMultiple = dropMultiple;
		this.suppressDrops = suppressDrops;
		this.loots = new LinkedList<String>();
		this.veins = new LinkedList<String>();
		this.prefix = prefix;
		this.validGenTypes = validGenTypes;
	}

	public boolean checkSubType(Byte subtype) {
		return this.subtypes.isEmpty() || this.subtypes.contains(subtype);
	}
	
	public String getMaterial() {
		return this.material;
	}
	
	@SuppressWarnings("deprecation")
	public boolean checkBlock(Block check) {
		return material.equals(check.getType().name()) && checkSubType(check.getData());
	}
	
	/**
	 * Allows check against an expanded list of valid blocks that ores can generate in -- companion blocks, effectively.
	 * For instance, if you separately configure stone types but want generation in stone of all types;
	 * 
	 * @param check
	 * @return
	 */
	public boolean checkGenerateBlock(Block check) {
		if (checkBlock(check)) return true;
		
		if (validGenTypes == null) return false;
		for (MaterialWrapper wrapper : validGenTypes) {
			if (wrapper.checkBlock(check)) return true;
		}
		
		return false;
	}

	public void addDropConfigs(List<String> drops) {
		drops.addAll(drops);
	}
	
	public void addVeinConfigs(List<String> veins) {
		veins.addAll(veins);
	}
	
	public List<String> getLoots() {
		return loots;
	}
	
	public LootConfig getLootConfig(double dice, String biome, ItemStack tool, Location loc) {
		double cumChance = 0.0d;
		double localChance = 0.0d;
		int counted = 0;
		for(String vName : veins) {
			VeinConfig vein = Config.getVein(vName);
			if(vein == null) continue;
			for(String lName : vein.getLoots()) {
				LootConfig loot = Config.getLoot(lName);
				if(loot == null) continue;
				if(loot.dropsWithTool(biome, tool) && loc.getBlockY() <= loot.getMaxY(biome)
					&& loc.getBlockY() >= loot.getMinY(biome)) {
					ToolConfig tc = loot.dropsWithToolConfig(biome, tool);
					localChance = loot.getChance(biome) * (tc == null ? 1.0 : tc.getDropChanceModifier());
					localChance += 1.0;
					localChance *= vein.getOreChance(loc);
					if(dice >= cumChance && dice < cumChance + localChance) {
						return loot;
					}
					cumChance += localChance;
					counted++;
				}
			}
		}
		for(String lName : loots) {
			LootConfig loot = Config.getLoot(lName);
			if(loot == null) continue;
			if(loot.dropsWithTool(biome, tool) && loc.getBlockY() <= loot.getMaxY(biome)
				&& loc.getBlockY() >= loot.getMinY(biome)) {
				ToolConfig tc = loot.dropsWithToolConfig(biome, tool);
				localChance = loot.getChance(biome) * (tc == null ? 1.0 : tc.getDropChanceModifier());
				if(dice >= cumChance && dice < cumChance + localChance) {
					return loot;
				}
				cumChance += localChance;
				counted++;
			}
		}
		if(Config.isDebug) {
			HiddenOre.getPlugin().getLogger()
					.log(Level.INFO, "{0} tested {1} cumm {2} dice", new Object[] {counted, Double.toString(cumChance), Double.toString(dice)});
		}
		return null;
	}
	
	static class MaterialWrapper {
		String material;
		Set<Byte> subtypes;
		
		public MaterialWrapper(String material, Collection<Byte> subtypes) {
			this.material = material;
			this.subtypes = (subtypes != null) ? new HashSet<Byte>(subtypes) : new HashSet<Byte>();	
		}

		public boolean checkSubType(Byte subtype) {
			return this.subtypes.isEmpty() || this.subtypes.contains(subtype);
		}
		
		public String getMaterial() {
			return this.material;
		}
		
		@SuppressWarnings("deprecation")
		public boolean checkBlock(Block check) {
			return material.equals(check.getType().name()) && checkSubType(check.getData());
		}
	}
}
