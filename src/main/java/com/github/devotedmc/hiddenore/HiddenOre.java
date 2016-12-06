package com.github.devotedmc.hiddenore;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.github.devotedmc.hiddenore.commands.CommandHandler;
import com.github.devotedmc.hiddenore.listeners.BlockBreakListener;
import com.github.devotedmc.hiddenore.listeners.ExploitListener;
import com.github.devotedmc.hiddenore.tracking.BreakTracking;

public class HiddenOre extends JavaPlugin {

	private static HiddenOre plugin;

	private static CommandHandler commandHandler;

	private static BreakTracking tracking;
	private BukkitTask trackingSave;
	
	private static BlockBreakListener breakHandler;
	private static ExploitListener exploitHandler;

	@Override
	public void onEnable() {
		plugin = this;
		
		saveDefaultConfig();
		reloadConfig();
		Config.loadConfig();
		
		tracking = new BreakTracking();
		tracking.load();
		trackingSave = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {
			public void run() {
				tracking.save();
			}
		}, Config.getTrackSaveInterval(), Config.getTrackSaveInterval());
		
		exploitHandler = new ExploitListener(plugin);
		this.getServer().getPluginManager().registerEvents(exploitHandler, this);

		breakHandler = new BlockBreakListener(plugin);
		this.getServer().getPluginManager().registerEvents(breakHandler, this);
				
		commandHandler = new CommandHandler(this);
		this.getCommand("hiddenore").setExecutor(commandHandler);
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
