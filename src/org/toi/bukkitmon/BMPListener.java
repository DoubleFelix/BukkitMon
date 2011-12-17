package org.toi.bukkitmon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;
//import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.plugin.Plugin;
import org.toi.util.tPermissions;
import org.toi.util.tProperties;

public class BMPListener extends PlayerListener {
	
	private Plugin plugin;
	private Hashtable<String, BMMob> ph = new Hashtable<String, BMMob>();
	private Random rnd = new Random();
	private String bmStr = ChatColor.GOLD + "[BukkitMon] " + ChatColor.YELLOW;
	private tPermissions perms;
	private tProperties props;
	private boolean announce = true;
	private ArrayList<String> randommobblacklist = new ArrayList<String>();
	private ArrayList<String> mobblacklist = new ArrayList<String>();
	private String announceString = ChatColor.RED + "SPAWNING --> Watch out! " + ChatColor.YELLOW + "<player>" +
	" spawned " + "<nr>" + " " + "<mobname>" + "s";
	
	public BMPListener(BukkitMon plugin, tPermissions perms, tProperties props)
	{
		this.plugin = plugin;
		this.perms = perms;
		this.props = props;
	}
	
	public void loadConfig()
	{
		try {
            props.load();
            BukkitMon.log.info("[BukkitMon] Config Loaded!");
        } catch (IOException e) {
        	BukkitMon.log.warning("[BukkitMon] Failed to load configuration");
        }
        this.announce = props.getBoolean("announce-spawns", this.announce);
        this.announceString = props.getString("announce-tring", this.announceString);
        ArrayList<String> temp = new ArrayList<String>();
        temp.add("ghast");
        temp.add("cow");
        this.randommobblacklist = props.getStringList("random-blacklist", temp);
        this.mobblacklist = props.getStringList("blacklist", temp);
        props.save();
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
			{
				int c = 0;
				CreatureType mt = mts[rndint];
				while (this.randommobblacklist.contains(mt.getName().toLowerCase()) && c < mts.length)
				{
					mt = mts[rnd.nextInt(mts.length - 1)];
					c++;
				}
				event.setHatchType(mt);
			}
			else
				event.setHatchType(bm.getCreatureType());
			event.setHatching(true);
			if (this.announce)
			{
				String msg = this.announceString;
				try
				{
					msg = msg.replace("<player>", player.getDisplayName());
					msg = msg.replace("<nr>", String.valueOf(event.getNumHatches()));
					msg = msg.replace("<mobname>", event.getHatchType().getName());
				}
				catch (NullPointerException npe){}
				this.plugin.getServer().broadcastMessage(msg);
			}
		}
		else
			event.setHatching(false);
	}
	
	public boolean onPlayerCommand(Player player, String[] split)
	{
		//Player player = event.getPlayer();
		//String[] split = event.getMessage().split(" ");
		//if (split[0].equalsIgnoreCase("/bm"))
		//{
			if (split.length >= 1)
			{
				if (!ph.containsKey(player.getName()))
					ph.put(player.getName(), new BMMob());
				BMMob bm = ph.get(player.getName());
				player.sendMessage(""+this.perms.canPlayerUseCommand(player.getName(),"mobs"));
				player.sendMessage(split[0]+"==mobs:"+split[0].equalsIgnoreCase("mobs"));
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
				else if (split[0].equalsIgnoreCase("CreatureType") /*&& this.perms.canPlayerUseCommand(player.getName(), "CreatureType")*/)
				{
					if (split.length >= 2)
					{
						try{
							CreatureType mt = CreatureType.valueOf(split[1].toUpperCase());
							if (mt != null)
							{
								if (!this.mobblacklist.contains(split[1].toLowerCase()))
								{
									bm.setCreatureType(mt);
									player.sendMessage(this.bmStr + "You set the mob type to " + split[1]);
								}
								else
									player.sendMessage(this.bmStr + split[1] + " is not allowed!");
							}
							else
								player.sendMessage(this.bmStr + "Invalid CreatureType!");
						}catch(org.bukkit.command.CommandException E){
							player.sendMessage("The creature you specified is probably invalid.\n" +
											   "Usage: /bm creaturetype ");
						}
						
					}
					else
						player.sendMessage(this.bmStr + "You need to define a CreatureType");
				}
				else if (split[0].equalsIgnoreCase("randomamount") && this.perms.canPlayerUseCommand(player.getName(), "randomamount"))
				{
					bm.setRndAmount(!bm.isRndAmount());
					player.sendMessage(this.bmStr + "You set random amount to " + bm.isRndAmount());
				}
				else if (split[0].equalsIgnoreCase("randomtype") && this.perms.canPlayerUseCommand(player.getName(), "randomtype"))
				{
					bm.setRndType(!bm.isRndType());
					player.sendMessage(this.bmStr + "You set random type to " + bm.isRndType());
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
				else if ((split[0].equalsIgnoreCase("list") || split[0].equalsIgnoreCase("list")))// && this.perms.canPlayerUseCommand(player.getName(), "list"))
				{
					player.sendMessage(ChatColor.RED + "BukkitMon commands:");
					player.sendMessage(ChatColor.RED + "/bm mobs [#]" + ChatColor.YELLOW + " - Define how many mobs to spawn");
					player.sendMessage(ChatColor.RED + "/bm CreatureType [type]" + ChatColor.YELLOW + " - Define the CreatureType to spawn");
					player.sendMessage(ChatColor.RED + "/bm maxamount" + ChatColor.YELLOW + " - Defines the max amount of mobs you can spawn");
					player.sendMessage(ChatColor.RED + "/bm randomamount" + ChatColor.YELLOW + " - Toggle random amount on/off");
					player.sendMessage(ChatColor.RED + "/bm randomtype" + ChatColor.YELLOW + " - Toggle random mob type on/off");
					player.sendMessage(ChatColor.RED + "/bm activate" + ChatColor.YELLOW + " - Activates the BukkitMon eggs");
					player.sendMessage(ChatColor.RED + "/bm list" + ChatColor.YELLOW + " - Shows a list of avaliable BukkitMon commands");
				}
				else
					player.sendMessage(this.bmStr + "Unknown BukkitMon command (or you don't have permission!)");
			}
			else
				player.sendMessage("perms: "+this.perms.canPlayerUseCommand(player.getName(), "mobs"));
		//}
		return true;
	}
}
