package org.toi.bukkitmon;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;
import org.toi.util.tPermissions;
import org.toi.util.tProperties;

public class BukkitMon extends JavaPlugin{

	public static final Logger log = Logger.getLogger("Minecraft");
	private String name = "BukkitMon";
	private String version = "v0.3.0 (Venusaur)";
	private tPermissions perms = new tPermissions("BukkitMon" + File.separator + "bukkitmon.perms");
	private tProperties props = new tProperties("BukkitMon" + File.separator + "bukkitmon.properties");
	private final BMPListener playerListener = new BMPListener(this, perms, props);
	
	/*public BukkitMon(){
		super();
		this.registerEvents();
		
		playerListener.loadConfig();
		this.addCommands();
		this.perms.loadPermissions();
		this.perms.savePermissions();
	}*/
	/*public BukkitMon(PluginLoader pluginLoader, Server instance,
			PluginDescriptionFile desc, File folder, File plugin,
			ClassLoader cLoader) {
		super();
		initialize(pluginLoader, instance, desc, folder, plugin, cLoader);
		
		playerListener.loadConfig();
		this.addCommands();
		this.perms.loadPermissions();
		this.perms.savePermissions();
	}*/

	public void onDisable() {
		log.info(name + " " + version + " disabled!");
	}

	public void onEnable() {
		this.registerEvents();
		
		playerListener.loadConfig();
		this.addCommands();
		this.perms.loadPermissions();
		this.perms.savePermissions();
		
		log.info(name + " " + version + " enabled!");
	}
	
	public void addCommands()
	{
		perms.addCmd("mobs");
		perms.addCmd("mobtype");
		perms.addCmd("randomamount");
		perms.addCmd("randomtype");
		perms.addCmd("maxamount");
		perms.addCmd("activate");
		perms.addCmd("list");
	}
	
	public void registerEvents()
	{
		this.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_EGG_THROW, playerListener, Priority.Normal, this);
		//getServer().getPluginManager().registerEvent(Event.Type.PLAYER_COMMAND, playerListener, Priority.Normal, this);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player player=(Player) sender;
		if (cmd.getName().equalsIgnoreCase("bm"))
		{
			playerListener.onPlayerCommand(player, args);
			return true;
		}
		return false;
	}

}
