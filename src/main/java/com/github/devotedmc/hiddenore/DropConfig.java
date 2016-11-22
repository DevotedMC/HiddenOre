package com.github.devotedmc.hiddenore;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.inventory.ItemStack;

public class DropConfig extends LootConfig {
	
	public DropConfig(String dropName, List<DropItemConfig> drops, 
			String prefix, DropLimitsConfig limits) {
		super(dropName, drops, prefix, limits);
	}

	public List<ItemStack> renderLoot(String biome, ToolConfig modify) {
		/** multipliers **/
		double min = getMinAmount(biome) + (modify != null ? modify.getMinAmountModifier() : 0.0);
		double max = getMaxAmount(biome) + (modify != null ? modify.getMaxAmountModifier() : 0.0);
		double amount = (min == max) ? min : (double) ((max - min) * Math.random() + min);
		
		if (Config.isDebug)
			HiddenOre.getPlugin().getLogger().log(Level.INFO, "Trigger drop {0} [{1}, {2}] = {3}", 
					new Object[] {dropName, min, max, amount});
		
		List<ItemStack> toDrop = new ArrayList<ItemStack>(drops.size());
		for (DropItemConfig item : drops) {
			toDrop.add(item.render(amount));
		}
		return toDrop;
	}
}
