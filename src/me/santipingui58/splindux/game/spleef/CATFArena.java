package me.santipingui58.splindux.game.spleef;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.robingrether.idisguise.iDisguise;
import me.santipingui58.splindux.scoreboard.ScoreboardType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class CATFArena extends Arena {

	
	private Location flag1;
	private Location flag2;
	
	public CATFArena(String name, Location spawn1, Location spawn2, Location arena1, Location arena2, Location lobby,Material item
		,Location flag1,Location flag2) {
		super(name, spawn1, spawn2, arena1, arena2, lobby, SpleefType.SPLEEF, GameType.CATF, item, 0, 0);
	
	}

	
	
	public Location getFlag1() {
		return this.flag1;
	}
	
	public Location getFlag2() {
		return this.flag2;
	}
	
	public void startGame(boolean sortQueue) {	

		int i = 0;
		if (sortQueue) Collections.shuffle(getQueue());
		int teamSize = getQueue().size()/2;		
		for (SpleefPlayer sp : getQueue()) {
			iDisguise.getInstance().getAPI().undisguise(sp.getPlayer());
			if (i<=getQueue().size()) {
			sp.getPlayer().setGameMode(GameMode.SURVIVAL);
			if (sp.getOptions().hasNightVision()) {
			sp.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,Integer.MAX_VALUE,Integer.MAX_VALUE));
			}
					
			sp.setScoreboard(ScoreboardType._1VS1GAME);				
			sp.stopfly();
			
			if (i<teamSize) {
				getDuelPlayers1().add(sp);
			} else {
				getDuelPlayers2().add(sp);
			}
			i++;
			}}	
		
		reset(false,false);

		for (SpleefPlayer s : getPlayers()) {
			getQueue().remove(s);
			
		}

		List<Player> players = new ArrayList<Player>();
		for (SpleefPlayer sp : getPlayers()) {
			players.add(sp.getPlayer());
			sp.giveGameItems();	
			
			if (sp.getOptions().hasNightVision()) {
				sp.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,Integer.MAX_VALUE,Integer.MAX_VALUE));
				}
			

		}	
		
		
		TextComponent message =null;
		 message = new TextComponent("§aA game between §b" + getTeamName(1) + " §aand §b" + getTeamName(2) + " §ahas started! §7(Right click to spectate)");		
		message.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/spectate "+getDuelPlayers1().get(0).getOfflinePlayer().getName()));
		message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Spectate §a" +getTeamName(1) + " §7-§a " + getTeamName(2)).create()));
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (!players.contains(p)) {
				p.spigot().sendMessage(message);
			}}
	}
	
	
	  
    public void spawnFlags() { 	
   putFlagAtBlock(getFlag1().getBlock(),"red");
   putFlagAtBlock(getFlag2().getBlock(),"blue");
    }
    
    
    
    public void putFlagAtBlock(Block block, String color) {   	
    	block.setType(Material.STANDING_BANNER);
    	Banner banner;
    	if (color.equalsIgnoreCase("red")) {
    		 banner = (Banner) block.getState();
        	banner.setBaseColor(DyeColor.RED);
        	banner.update();      	
    	} else {
    		 banner = (Banner) block.getState();
        	banner.setBaseColor(DyeColor.BLUE);
        	banner.update();
    	}   	
    }
   
	

}
