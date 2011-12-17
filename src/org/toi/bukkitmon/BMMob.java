package org.toi.bukkitmon;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;

public class BMMob {
	
	private byte nrOfMobs = 1;
	private CreatureType CreatureType = org.bukkit.entity.CreatureType.CHICKEN;
	private boolean rndAmount = false;
	private boolean rndType = false;
	private boolean active = true;
	private byte maxAmount = 10;
	
	public BMMob()
	{
		
	}
	
	public boolean isRndAmount() {
		return rndAmount;
	}

	public void setRndAmount(boolean rndAmount) {
		this.rndAmount = rndAmount;
	}

	public boolean isRndType() {
		return rndType;
	}

	public void setRndType(boolean rndType) {
		this.rndType = rndType;
	}

	public byte getNrOfMobs() {
		return nrOfMobs;
	}

	public void setNrOfMobs(byte nrOfMobs) {
		this.nrOfMobs = nrOfMobs;
	}

	public CreatureType getCreatureType() {
		return CreatureType;
	}

	public void setCreatureType(CreatureType CreatureType) {
		this.CreatureType = CreatureType;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public byte getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(byte maxAmount) {
		this.maxAmount = maxAmount;
	}
	

	
}
