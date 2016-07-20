package com.github.devotedmc.hiddenore;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.github.devotedmc.hiddenore.commands.CommandHandler;
import com.github.devotedmc.hiddenore.listeners.BlockBreakListener;

public class HiddenOre extends JavaPlugin {

	private static HiddenOre plugin;

	private static CommandHandler commandHandler;

	private static BlockBreakListener breakHandler;

	@Override
	public void onEnable() {
		plugin = this;
		
		saveDefaultConfig();
		reloadConfig();
		Config.loadConfig();
		
		breakHandler = new BlockBreakListener(plugin);
		this.getServer().getPluginManager().registerEvents(breakHandler, this);
				
		commandHandler = new CommandHandler(this);
		this.getCommand("hiddenore").setExecutor(commandHandler);
	}

	@Override
	public void onDisable() {
	}

	public static HiddenOre getPlugin() {
		return plugin;
	}

	public BlockBreakListener getBreakListener() {
		return breakHandler;
	}
}
