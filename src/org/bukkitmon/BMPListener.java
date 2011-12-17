package org.bukkitmon;

import java.util.Hashtable;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.plugin.Plugin;

public class BMPListener extends PlayerListener {
	
	private Plugin plugin;
	private Hashtable<String, BMMob> ph = new Hashtable<String, BMMob>();
	private Random rnd = new Random();
	private String bmStr = ChatColor.GOLD + "[BukkitMon] " + ChatColor.YELLOW;
	private tPermissions perms;
	
	public BMPListener(BukkitMon plugin, tPermissions perms)
	{
		this.plugin = plugin;
		this.perms = perms;
	}
	
	public void onPlayerEggThrow(PlayerEggThrowEvent event)
	{
		Player player = event.getPlayer();
		if (!ph.containsKey(player.getName()))
			ph.put(player.getName(), new BMMob());
		BMMob bm = ph.get(player.getName());
		if (bm.isActive())
		{
			CreatureType[] mts = CreatureType.values();
			int rndint = rnd.nextInt(mts.length - 1);
			if (bm.isRndAmount())
				event.setNumHatches((byte)rnd.nextInt(bm.getMaxAmount()));
			else
				event.setNumHatches(bm.getNrOfMobs());
			if (bm.isRndType())
				event.setHatchType(mts[rndint]);
			else
				event.setHatchType(bm.getCreatureType());
			event.setHatching(true);
			this.plugin.getServer().broadcastMessage(ChatColor.RED + "SPAWNING --> Watch out! " + ChatColor.YELLOW + player.getDisplayName() +
													" spawned " + event.getNumHatches() + " " + event.getHatchType().getName() + "s");
		}
		else
			event.setHatching(false);
	}
	
	public boolean onPlayerCommand(Player player, String[] split)
	{
		//Player player = event.getPlayer();
		//String[] split = event.getMessage().split(" ");
		if (split.length >= 1)
		{
			if (!ph.containsKey(player.getName()))
				ph.put(player.getName(), new BMMob());
			BMMob bm = ph.get(player.getName());
			if (split[0].equalsIgnoreCase("mobs") && this.perms.canPlayerUseCommand(player.getName(), "mobs"))
			{
				if (split.length >= 2)
				{
					try{
						byte val = Byte.valueOf(split[1]);
						if (val <= bm.getMaxAmount())
						{
							bm.setNrOfMobs(val);
							player.sendMessage(this.bmStr + "You set the number of mobs to spawn to: " + val);
						}
						else
							player.sendMessage(this.bmStr + val + " is more than the max amount");
					}
					catch (NumberFormatException nfe){
						player.sendMessage(this.bmStr + "You need to define a valid number");
					}
				}
				else
					player.sendMessage(this.bmStr + "You need to define a number");
			}
			else if (split[0].equalsIgnoreCase("CreatureType") && this.perms.canPlayerUseCommand(player.getName(), "CreatureType"))
			{
				if (split.length >= 2)
				{
					CreatureType mt = CreatureType.valueOf(split[1].toUpperCase());
					if (mt != null)
					{
						bm.setCreatureType(mt);
						player.sendMessage(this.bmStr + "You set the mob type to " + split[1]);
					}
					else
						player.sendMessage(this.bmStr + "Invalid CreatureType!");
				}
				else
					player.sendMessage(this.bmStr + "You need to define a CreatureType");
			}
			else if (split[0].equalsIgnoreCase("randomamount") && this.perms.canPlayerUseCommand(player.getName(), "randomamount"))
			{
				bm.setRndAmount(!bm.isRndAmount());
				player.sendMessage(this.bmStr + "You set random amount to " + bm.isRndAmount());
			}
			else if (split[0].equalsIgnoreCase("randommob") && this.perms.canPlayerUseCommand(player.getName(), "randommob"))
			{
				bm.setRndAmount(!bm.isRndAmount());
				player.sendMessage(this.bmStr + "You set randommob to " + bm.isRndAmount());
			}
			else if (split[0].equalsIgnoreCase("maxamount") && this.perms.canPlayerUseCommand(player.getName(), "maxamount"))
			{
				if (split.length >= 2)
				{
					try{
						byte val = Byte.valueOf(split[1]);
						bm.setMaxAmount(val);
						player.sendMessage(this.bmStr + "You set the max amount of mobs to spawn to: " + val);
					}
					catch (NumberFormatException nfe){
						player.sendMessage(this.bmStr + "You need to define a valid number");
					}
				}
				else
					player.sendMessage(this.bmStr + "You need to define a number");
			}
			else if (split[0].equalsIgnoreCase("activate") && this.perms.canPlayerUseCommand(player.getName(), "activate"))
			{
				bm.setActive(!bm.isActive());
				if (bm.isActive())
					player.sendMessage(this.bmStr + "You activated BukkitMon");
				else
					player.sendMessage(this.bmStr + "You deactivated BukkitMon");
				
			}
			else if (split[0].equalsIgnoreCase("list") && this.perms.canPlayerUseCommand(player.getName(), "list"))
			{
				player.sendMessage(ChatColor.RED + "BukkitMon commands:");
				player.sendMessage(ChatColor.RED + "/bm mobs [#]" + ChatColor.YELLOW + " - Define how many mobs to spawn");
				player.sendMessage(ChatColor.RED + "/bm CreatureType [type]" + ChatColor.YELLOW + " - Define the CreatureType to spawn");
				player.sendMessage(ChatColor.RED + "/bm maxamount" + ChatColor.YELLOW + " - Defines the max amount of mobs you can spawn");
				player.sendMessage(ChatColor.RED + "/bm randomamount" + ChatColor.YELLOW + " - Toggle random amount on/off");
				player.sendMessage(ChatColor.RED + "/bm randommob" + ChatColor.YELLOW + " - Toggle random mob type on/off");
				player.sendMessage(ChatColor.RED + "/bm activate" + ChatColor.YELLOW + " - Activates the BukkitMon eggs");
				player.sendMessage(ChatColor.RED + "/bm list" + ChatColor.YELLOW + " - Shows a list of avaliable BukkitMon commands");
			}
			else
				player.sendMessage(this.bmStr + "Unknown BukkitMon command");
		}
		else
			player.sendMessage(this.bmStr + "Define a function please!");
		return true;
	}
}
