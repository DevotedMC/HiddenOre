package com.github.devotedmc.hiddenore;

import java.util.Collections;
import java.util.List;
import org.bukkit.Location;

@FunctionalInterface
interface RegionNameSupplier {
	RegionNameSupplier NOOP_REGION_ITERATOR = (location) -> Collections.emptyList();

	List<String> getRegionsForLocation(Location location);
}
