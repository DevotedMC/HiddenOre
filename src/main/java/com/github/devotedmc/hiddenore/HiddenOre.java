package com.github.devotedmc.hiddenore;

import com.github.devotedmc.hiddenore.commands.CommandHandler;
import com.github.devotedmc.hiddenore.listeners.BlockBreakListener;
import com.github.devotedmc.hiddenore.listeners.ExploitListener;
import com.github.devotedmc.hiddenore.listeners.WorldGenerationListener;
import com.github.devotedmc.hiddenore.tracking.BreakTracking;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class HiddenOre extends JavaPlugin {

	private static HiddenOre plugin;
	public static Map<String,Config> instances;

	private static CommandHandler commandHandler;

	private static BreakTracking tracking;
	private BukkitTask trackingSave;

	private static BlockBreakListener breakHandler;
	private static ExploitListener exploitHandler;
	private static List<WorldGenerationListener> worldGen;

	@Override
	public void onEnable() {
		plugin = this;
		instances = new HashMap<String, Config>();

		saveDefaultConfig();
		reloadConfig();
		Config.loadConfig();

		tracking = new BreakTracking();
		tracking.load();
		trackingSave = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {
			public void run() {
				tracking.save();
			}
		}, Config.trackSave, Config.trackSave);

		exploitHandler = new ExploitListener(plugin);
		this.getServer().getPluginManager().registerEvents(exploitHandler, this);

		breakHandler = new BlockBreakListener(plugin);
		this.getServer().getPluginManager().registerEvents(breakHandler, this);

		commandHandler = new CommandHandler(this);
		this.getCommand("hiddenore").setExecutor(commandHandler);

		worldGen = new ArrayList<WorldGenerationListener>();
		
		//This is done before the the list of instances is made.
	/*	for (World world : getServer().getWorlds()) {
			Config i = Config.instances.get(world.getName());
			if(i == null){
				this.getLogger().log(Level.WARNING, "Config i is null you fucking retard");
			}
			ConfigurationSection worldGenConfig = i.getWorldGenerations(world.getName());
			if (worldGenConfig != null) {
				this.getLogger().log(Level.INFO, "Registered Ore Generation Suppression Listener for World {0}", world.getName());
				WorldGenerationListener list = new WorldGenerationListener(worldGenConfig, world.getName());
				this.getServer().getPluginManager().registerEvents(list, this);
				worldGen.add(list);
			}
		}*/
	}

	@Override
	public void onDisable() {
		tracking.save();
		trackingSave.cancel();
	}

	public static HiddenOre getPlugin() {
		return plugin;
	}

	public BreakTracking getTracking() {
		return tracking;
	}

	public BlockBreakListener getBreakListener() {
		return breakHandler;
	}
}
