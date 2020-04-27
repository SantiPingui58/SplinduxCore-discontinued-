package me.santipingui58.splindux.listener;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import com.yapzhenyie.GadgetsMenu.api.GadgetsMenuAPI;
import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameState;
import me.santipingui58.splindux.game.SpleefPlayer;
import me.santipingui58.splindux.game.death.BreakReason;
import me.santipingui58.splindux.game.death.BrokenBlock;
import me.santipingui58.splindux.game.spleef.SpleefArena;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.gui.MutationTokenMenu;
import me.santipingui58.splindux.scoreboard.hologram.HologramManager;
import net.apcat.simplesit.SimpleSitPlayer;
import net.apcat.simplesit.events.PlayerSitEvent;
import net.apcat.simplesit.events.PlayerStopSittingEvent;


public class ServerListener implements Listener {

	private List<SpleefPlayer> spleggDelay = new ArrayList<SpleefPlayer>();
	
	
	@EventHandler
	public void onSign(SignChangeEvent e) {		
			for (int i = 0; i < 4; i++) {
	            String line = e.getLine(i);
	            if (line != null && !line.equals("")) {
	                e.setLine(i, ChatColor.translateAlternateColorCodes('&', line));
	            }
			}
	}
	
	@EventHandler
	public void onSmelt(BlockFadeEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onDecay(LeavesDecayEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler 
	public void onDeath(EntityDamageByEntityEvent e) {
		if (Main.pvp) {
		Entity entity = e.getEntity();
		if (entity instanceof Player) {
			Player p = (Player) entity;
			SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
			if (sp.isInGame()) {
				return;
			}
		}
		}
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (Main.pvp) {
		Entity entity = e.getEntity();
		if (entity instanceof Player) {
			Player p = (Player) entity;
			SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
			if (sp.isInGame()) {
				return;
			}
		}
		}
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onHunger(FoodLevelChangeEvent e) {
		e.setCancelled(true);
	}
	
	/*@EventHandler
	public void onSign(SignChangeEvent e) {
		
			for (int i = 0; i < 4; i++) {
	            String line = e.getLine(i);
	            if (line != null && !line.equals("")) {
	                e.setLine(i, ChatColor.translateAlternateColorCodes('&', line));
	            }
			}
		
		if (e.getLine(0).equalsIgnoreCase("[Spleef]") && e.getLine(1).equalsIgnoreCase("Join")) {
			for (SpleefArena arena : DataManager.getManager().getArenas()) {
				if (arena.getName().equals(e.getLine(2))) {
					e.setLine(0, "§0§l[Spleef]");
					e.setLine(1, "§aJoin");
					e.setLine(2, "§5§l"+arena.getName());
					return;
				}
			}
			e.getPlayer().sendMessage("§cThe arena §b"+ e.getLine(2) + " §cdoesnt exist.");
		} else if (e.getLine(0).equalsIgnoreCase("[Spleef]") && e.getLine(1).equalsIgnoreCase("Leave")) {
			for (SpleefArena arena : DataManager.getManager().getArenas()) {
				if (arena.getName().equals(e.getLine(2))) {
					e.setLine(0, "§0§l[Spleef]");
					e.setLine(1, "§cLeave");
					e.setLine(2, "§5§l"+arena.getName());
					return;
				}
			}
			e.getPlayer().sendMessage("§cThe arena §b"+ e.getLine(2) + " §cdoesnt exist.");
		} else if (e.getLine(0).equalsIgnoreCase("[Spleef]") && e.getLine(1).equalsIgnoreCase("Leaderboard")
				&& e.getLine(2).equalsIgnoreCase("FFAWins"))  {
			e.setCancelled(true);
			LeaderboardManager.getManager().generateWallLeaderboard(LeaderboardType.ALL_TIME_FFA_WINS, Manager.getManager().getSpleefPlayer(e.getPlayer()), e.getBlock().getLocation());
		}
	}
	*/
	
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		 SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
		 if (sp.needsAdminLoginQuestionmark() && !sp.isLogged()) {
			 e.setCancelled(true);
			 return;
		 }
		                                                          
		                                                         
	    if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
	    	
	    	
	    	if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
	    	if (!p.hasPermission("splindux.admin") && !p.getGameMode().equals(GameMode.CREATIVE)) {
	    		if (e.getClickedBlock().getType().equals(Material.CHEST) 
	    			|| e.getClickedBlock().getType().equals(Material.HOPPER) 
	    			|| e.getClickedBlock().getType().equals(Material.ENDER_CHEST) 
	    			|| e.getClickedBlock().getType().equals(Material.DROPPER) 
	    			|| e.getClickedBlock().getType().equals(Material.FURNACE) 
	    			|| e.getClickedBlock().getType().equals(Material.DISPENSER) 
	    			|| e.getClickedBlock().getType().equals(Material.TRAPPED_CHEST) 
	    			|| e.getClickedBlock().getType().equals(Material.WORKBENCH) 
	    			|| e.getClickedBlock().getType().equals(Material.ENCHANTMENT_TABLE)
	    			|| e.getClickedBlock().getType().equals(Material.ANVIL) 	    	
	    				) {
	    			e.setCancelled(true);
	    		}
	    	}
	    	}
	    	
	    	if (p.getItemInHand().isSimilar(DataManager.getManager().gameitems()[9])) {
	    		if (sp.isInGame()) {
	    			SpleefArena arena = sp.getArena();
	    			if (arena.getDeadPlayers1().contains(sp) || arena.getDeadPlayers2().contains(sp)) return;
	    		if (!spleggDelay.contains(sp)) {
		    		Vector speed = p.getLocation().getDirection().multiply(1.4);
		    		Vector shift = speed.getCrossProduct(new Vector(0, 1 ,0)).normalize().multiply(0.15);
		    		Egg egg = p.getWorld().spawn(p.getEyeLocation().add(shift), Egg.class);
		    		egg.setVelocity(speed);   		
		    		egg.setShooter(p);
		    		 p.playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 0.5F, 2.0F);
		    		spleggDelay.add(sp);
		    		new BukkitRunnable() {
		    			public void run() {
		    				spleggDelay.remove(sp);
		    			}
		    		}.runTaskLater(Main.get(), 2L);
		    		}
	    	}
	    	} else if (p.getItemInHand().equals(DataManager.getManager().queueitems()[1])) {
	    			sp.leaveQueue(sp.getArena(),true); 		
	    	} else if (p.getItemInHand().equals(DataManager.getManager().lobbyitems()[1])) {
	    		GadgetsMenuAPI.goBackToMainMenu(p);
	    	} else if (p.getItemInHand().equals(DataManager.getManager().queueitems()[0])) {
	    		if (sp.getMutationTokens()>0) {
	    		new MutationTokenMenu(sp).o(p);
	    	} else {
	    		p.sendMessage("§cYou dont have any §dMutation Token §cat the moment.");
	    	} 
	    		}
	    		
	    	
	    }
		
	}
	
	
	
	@EventHandler
	public void onEggThrow(PlayerEggThrowEvent event) {
		 event.setHatching(false);
	}
	
	@EventHandler
	public void onInventoryMove(InventoryClickEvent e) {
		for (ItemStack i : DataManager.getManager().lobbyitems()) {
			if (e.getCurrentItem()!=null) {
				if (e.getCurrentItem().equals(i)) {
					e.setCancelled(true);
					return;
				}
			}
		}
		
		for (ItemStack i : DataManager.getManager().queueitems()) {
			if (e.getCurrentItem()!=null) {
				if (e.getCurrentItem().equals(i)) {
					e.setCancelled(true);
					return;
				}
			}
		}

		
	        if(e.getSlotType() == InventoryType.SlotType.ARMOR)  {
	            e.setCancelled(true);
	        } 
	
		
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	  public void onProjectileHit(ProjectileHitEvent e)  {
		if (e.getEntity().getShooter() instanceof Player) {
	    Player p = (Player)e.getEntity().getShooter();
	    SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
	    if (sp.isInGame()) {
	    	if (sp.getArena().getState().equals(GameState.GAME)) {
	    BlockIterator iterator = new BlockIterator(e.getEntity().getWorld(), e.getEntity().getLocation().toVector(), e.getEntity().getVelocity().normalize(), 0.0D, 4);
	    Block hitblock = null;
	    while (iterator.hasNext()) {
	      hitblock = iterator.next();

	      if (hitblock.getTypeId() != 0)
	      {
	        break;
	      }
	    }
	    if (hitblock.getType() == Material.SNOW_BLOCK)
	    {
	      p.playSound(hitblock.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.5F, 2.0F);
	      hitblock.setType(Material.AIR);
	      SpleefArena arena = sp.getArena();
	      BrokenBlock kill = new BrokenBlock(sp,hitblock.getLocation(),BreakReason.SNOWBALL);
			arena.getBrokenBlocks().add(kill);   
	    }
	    
	    
	    if (hitblock.getTypeId()==159 && sp.getArena().getSpleefType().equals(SpleefType.SPLEGG))
	    {
	      p.playSound(hitblock.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.5F, 2.0F);
	      hitblock.setType(Material.AIR);
	      SpleefArena arena = sp.getArena();
	      BrokenBlock kill = new BrokenBlock(sp,hitblock.getLocation(),BreakReason.SHOVEL);
			arena.getBrokenBlocks().add(kill);
			      
	    }
	    
	    	}
	  } 
	    }
		}
	
	@EventHandler
	public static void onCommand(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();	
		 SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
		
		 String msg = e.getMessage();
		    String[] args = msg.split(" ");
		    if ((args.length >= 1) &&  (args[0].startsWith("/"))) {
		    	 if (sp.needsAdminLoginQuestionmark() && !sp.isLogged()) {
		    		 if (!args[0].equalsIgnoreCase("/splinduxregister") && !args[0].equalsIgnoreCase("/splinduxlogin")) {
		    			 e.setCancelled(true);
						 return;
		    		 }	
				 }
		    	
		    	if (args[0].equalsIgnoreCase("/sit")) {
		    		   e.setCancelled(true);
		    		   
				       if (e.getPlayer().hasPermission("splindux.sit")) {
				    	  
				    	   if (!sp.isInGame()) {
				    	   SimpleSitPlayer player = new SimpleSitPlayer(p);
				    	    if (player.isSitting()) {
				    	      player.setSitting(false);
				    	    }
				    	    else if (player.getBukkitPlayer().isOnGround())
				    	      player.setSitting(true);
				    	   } else {
									p.sendMessage("§cYou can't execute this command while playing a match.");								
				    	   }
				       } else {

					p.sendMessage("§cYou don't have permission to execute this command.");
					p.sendMessage("§aYou need a rank "
							+ "§6[Donator] §ato use this, visit the store for more info: §bhttp://jhspleef.buycraft.net/");
						
								} 
				       
				      } else if (args[0].equalsIgnoreCase("/lay")) {
				    	   e.setCancelled(true);
				      }  else if (args[0].equalsIgnoreCase("/plugins") || args[0].equalsIgnoreCase("/pl")) {
				        	 e.getPlayer().sendMessage("§fPlugins(2): §aSplinduxCore§f, §aSlenderSeLaCome");
						        e.setCancelled(true);
				        
				      } else if (args[0].equalsIgnoreCase("/d") || args[0].equalsIgnoreCase("/disguise")) {
				    		if (sp.isInGame()) {
				    			e.setCancelled(true);
									p.sendMessage("§cYou can't execute this command while playing a match.");
								
				    		}
				    	}
		    }
	
		    }
	
	  @EventHandler
	  public void onSit(PlayerSitEvent e) {
			  e.setMessage("§aYou have sat");
		  
	  }
	  
	  
	  @EventHandler
	  public void onExitSit(PlayerStopSittingEvent e) {
			  e.setMessage("§cYou have stood up");		  
	  }
	  

	  
	
	  
	  @EventHandler
	  public void onTeleport(PlayerTeleportEvent e) {
		  Player p = e.getPlayer();
		  SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
		  if (sp==null) return;
		  if (sp.isSpectating() || sp.isGameDead()) {
		 if( e.getCause().equals(TeleportCause.SPECTATE)) {
			 e.setCancelled(true);
		 }		  
	  }
		  }
	  
	  
	  @EventHandler
	  public void onWorldChange(PlayerChangedWorldEvent e) {
			  HologramManager.getManager().sendHolograms(SpleefPlayer.getSpleefPlayer(e.getPlayer()));
		  
	  }
	  

	  

}

