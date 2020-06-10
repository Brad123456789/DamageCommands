package com.Brad.DamageCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.md_5.bungee.api.ChatColor;

import java.io.IOException;
import java.sql.Timestamp;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
public class Main extends JavaPlugin {	
	// Variables
	String config_type = "";
	String config_command  = "";
	boolean config_enabled = false;
	String config_joinmessage = "";
	String prefix = ChatColor.GOLD + "[" + ChatColor.DARK_RED + "DamageCommand" + ChatColor.GOLD + "]" + ChatColor.GREEN;
	String command_temp = "";
	@Override
	public void onEnable()	{
		this.saveDefaultConfig();
		config_type = this.getConfig().getString("TYPE");
		config_command = this.getConfig().getString("Command");
		config_enabled = this.getConfig().getBoolean("Enabled");
		config_joinmessage = this.getConfig().getString("JoinMessage");
		getServer().getPluginManager().registerEvents(new MyListener(), this);
		getLogger().info("Custom KillCommand Plugin by Brad **LOADED** Sucessfully!");
	}
	@Override
	public void onDisable()	{
		getLogger().info("Custom KillCommand Plugin by Brad **UNLOADED** Sucessfully!");
	}	
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("DC")) {
        	if(args.length == 1) {config_enabled = Boolean.parseBoolean(args[0]);}
            sender.sendMessage(prefix  + " Currently " + config_enabled + ".");
            return true;
        }
        return false;
    }  
    public class MyListener implements Listener {  	
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
    	if(config_joinmessage.toLowerCase() != "none") {
    		event.getPlayer().sendMessage(config_joinmessage);
    	}
    }    
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event){
        Entity e = event.getEntity();
        if (e instanceof Player && config_enabled == true /* && event.getCause() == DamageCause.FALL */){       	
            final Integer damage = (int)event.getFinalDamage();        
        	command_temp = config_command;
        	command_temp = command_temp.replace("%Player%",  event.getEntity().getName()); 
        	command_temp = command_temp.replace("%Prefix%",  prefix);
        	command_temp = command_temp.replace("%Damage%",  damage.toString());
        	command_temp = command_temp.replace("%Time%",    new Timestamp(System.currentTimeMillis()).toLocaleString() );
        	command_temp = command_temp.replace("%Enabled%", "true");
        	command_temp = command_temp.replace("%Type%",    config_type);
        	command_temp = command_temp.replace("%Command%", config_command);
        	command_temp = command_temp.replace("%Event%", event.getCause().name());        	  	    
        	 switch(config_type.toLowerCase())  { 
                 case "server": 
                	 Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command_temp); 
                     break; 
                 case "system": 
                	 try {Runtime.getRuntime().exec(command_temp);} catch (IOException ex) {event.getEntity().sendMessage(prefix + ChatColor.RED + " Error: " + ChatColor.DARK_RED + ex.getMessage());}
                     break;  
                 default: 
                	 System.out.println(prefix + " There is a problem with the Current Configuration settings ("+config_type+"), please check the config.");
                	 break;
             }        	      	
        }       
    }   
}
    
}