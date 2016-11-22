package com.github.devotedmc.hiddenore;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.inventory.ItemStack;

public class TransformConfig extends LootConfig {

	private boolean dropIfTransformFails;
	private int maxDropsIfTransformFails;
	private String failConfig;
	
	public TransformConfig(String dropName, List<DropItemConfig> drops, String failConfig, 
			boolean dropIfTransformFails, int maxDropsIfTransformFails, String prefix, 
			DropLimitsConfig limits) {
		super(dropName, drops, prefix, limits);
		this.dropIfTransformFails = dropIfTransformFails;
		this.maxDropsIfTransformFails = maxDropsIfTransformFails;
		this.failConfig = failConfig;
	}
	
	public List<ItemStack> renderLoot(String biome, ToolConfig modify) {
		double min = getMinAmount(biome) + (modify != null ? modify.getMinAmountModifier() : 0.0);
		double max = getMaxAmount(biome) + (modify != null ? modify.getMaxAmountModifier() : 0.0);
		double amount = (min == max) ? min : (double) ((max - min) * Math.random() + min);
		
		if (Config.isDebug)
			HiddenOre.getPlugin().getLogger().log(Level.INFO, "Trigger transform {0} [{1}, {2}] = {3}", 
					new Object[] {dropName, min, max, amount});
		
		List<ItemStack> toTransform = new ArrayList<ItemStack>(drops.size());
		for (DropItemConfig item : drops) {
			if (item.canTransform())
				toTransform.add(item.render(amount));
		}
		return toTransform;
	}
	
	public boolean isDropIfTransformFails() {
		return dropIfTransformFails;
	}
	
	public int getMaxDropsIfTransformFails() {
		return maxDropsIfTransformFails;
	}
	
	public String getFailConfig() {
		return failConfig;
	}
}
