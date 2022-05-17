package me.santipingui58.splindux.listener;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;
import org.spigotmc.event.entity.EntityDismountEvent;

import com.github.shynixn.petblocks.api.PetBlocksApi;
import com.github.shynixn.petblocks.api.business.service.PetService;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.GameState;
import me.santipingui58.splindux.game.death.BreakReason;
import me.santipingui58.splindux.game.death.BrokenBlock;
import me.santipingui58.splindux.game.ffa.FFAArena;
import me.santipingui58.splindux.game.mutation.GameMutation;
import me.santipingui58.splindux.game.mutation.MutationType;
import me.santipingui58.splindux.game.spectate.SpectateManager;
import me.santipingui58.splindux.game.spleef.Arena;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefType;
import me.santipingui58.splindux.gui.options.OptionsMenu;
import me.santipingui58.splindux.gui.party.PartyMenu;
import me.santipingui58.splindux.hologram.HologramManager;
import me.santipingui58.splindux.gui.gadgets.GadgetsMenu;
import me.santipingui58.splindux.gui.game.parkour.ParkourMenu;
import me.santipingui58.splindux.gui.game.MutationTokenMenu;
import me.santipingui58.splindux.gui.game.UnrankedMenu;
import me.santipingui58.splindux.gui.game.guild.GuildMainMenu;
import me.santipingui58.splindux.relationships.guilds.Guild;
import me.santipingui58.splindux.relationships.guilds.GuildsManager;
import me.santipingui58.splindux.relationships.parties.Party;
import me.santipingui58.splindux.relationships.parties.PartyManager;
import me.santipingui58.splindux.scoreboard.ScoreboardType;
import me.santipingui58.splindux.utils.Utils;
import net.apcat.simplesit.SimpleSitPlayer;
import net.apcat.simplesit.events.PlayerSitEvent;
import net.apcat.simplesit.events.PlayerStopSittingEvent;


public class ServerListener implements Listener {

	
	private Set<UUID> delay = new HashSet<UUID>();
	
	@EventHandler
	public void onDecay(BlockFormEvent e) {
		if (e.getBlock().getType().equals(Material.CONCRETE_POWDER) || e.getBlock().getType().equals(Material.CONCRETE)) e.setCancelled(true);
	}
	
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
	public void onFishing(PlayerFishEvent e) {
			Utils.getUtils().setBiteTime(e.getHook(), 2);
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
	
		if (e.getDamager().getType().equals(EntityType.SNOWBALL)) {
			e.setDamage(0.2);
		}
		
		
		Entity entity = e.getEntity();
		if (entity instanceof Player && e.getDamager() instanceof Player) {
			Player p = (Player) entity;
			Player damager = (Player) e.getDamager();
			p.setHealth(20.0);
			damager.setHealth(20.0);
			SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
			SpleefPlayer sp2 = SpleefPlayer.getSpleefPlayer(damager);
			
			if (e.getDamager().getType().equals(EntityType.SNOWBALL)) {
				FFAArena ffa = GameManager.getManager().getFFAArenaByArena(sp.getArena());
				for (GameMutation mutation : ffa.getInGameMutations()) {
					if (mutation.getType().equals(MutationType.KOHI_SPLEEF)) {
						return;
					}
				}
			}
			
			if (Main.pvp) {
			if (sp==null || sp2 ==null) return;			
			if (sp2.isSpectating() || sp.isSpectating()) e.setCancelled(true);
			
			if (sp.isInArena() && sp2.isInArena()) {
				Arena arena = sp.getArena();
				if (arena.getGameType().equals(GameType.DUEL)) {
				if (arena.getDuelPlayers1().contains(sp) && arena.getDuelPlayers2().contains(sp2)) return;
				if (arena.getDuelPlayers2().contains(sp) && arena.getDuelPlayers1().contains(sp2)) return;
			} else {
				return;
			} 
			}
				
			}
			
				
		
		} 
		
		e.setCancelled(true);
	}
	

	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
			e.setRespawnLocation(e.getPlayer().getWorld().getName().equalsIgnoreCase("event") ? new Location(e.getPlayer().getWorld(),0,100,0) : Main.lobby);
	}
	
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			Set<ItemStack> toRemove = new HashSet<ItemStack>();
			for (ItemStack i : p.getInventory().getContents()) {
				for (ItemStack item : DataManager.getManager().gameitems()) if (item.isSimilar(i)) toRemove.add(i); 
				for (ItemStack item : DataManager.getManager().queueitems()) if (item.isSimilar(i)) toRemove.add(i); 
				for (ItemStack item : DataManager.getManager().lobbyitems()) if (item.isSimilar(i)) toRemove.add(i); 		
			}
			e.getDrops().removeAll(toRemove);
		}
	}
	
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		DamageCause cause = e.getCause();
		if (cause.equals(DamageCause.FALL) || cause.equals(DamageCause.BLOCK_EXPLOSION) || cause.equals(DamageCause.ENTITY_EXPLOSION)) {
			e.setCancelled(true);
		}
		
		Entity entity = e.getEntity();
		if (entity instanceof Player) {
			Player p = (Player) entity;
			p.setHealth(20.0);
			SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
			if (sp==null) return;
			if (sp.isInGame() && sp.getArena().getGameType().equals(GameType.FFA)) {
			if (e.getCause().equals(DamageCause.PROJECTILE)) {
				FFAArena ffa = GameManager.getManager().getFFAArenaByArena(sp.getArena());
				for (GameMutation mutation : ffa.getInGameMutations()) {
					if (mutation.getType().equals(MutationType.KOHI_SPLEEF)) {
						return;
					}
				}
			}
			}
			
			
			if (Main.pvp) {
				if (sp.isSpectating()) e.setCancelled(true);			
				if (sp.isInArena()) return;
				}
		}		
		e.setCancelled(true);
	}
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);	
		if (sp.isInGame() && sp.getArena().getGameType().equals(GameType.DUEL) 
				&& (e.getItemDrop().getItemStack().getType().equals(Material.DIAMOND_SPADE)
				|| e.getItemDrop().getItemStack().getType().equals(Material.IRON_SPADE))
				) {
			new BukkitRunnable() {
				public void run() {
			p.performCommand("reset");
			}
			}.runTaskLater(Main.get(), 4L);
		}
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onHunger(FoodLevelChangeEvent e) {
		e.setCancelled(true);
	}
	
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEntityEvent  e) {
		if (e.getRightClicked() instanceof Player && e.getPlayer().getWorld().getName().equalsIgnoreCase("arenas")) {
			Player p = e.getPlayer();
			SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
			if (sp.isSpectating()) {
				Player clicked = (Player) e.getRightClicked();
				if (sp.getArena()==null) return;
				for (SpleefPlayer spectating : sp.getArena().getPlayers()) {
					if (spectating.getPlayer().equals(clicked)) {
						sp.getPlayer().setGameMode(GameMode.SPECTATOR);
						sp.getPlayer().setSpectatorTarget(clicked);
						break;
					}
				}
			}
		}
	}
	
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		 SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
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
	    	DataManager dm = DataManager.getManager();
	    	if (p.getItemInHand().isSimilar(dm.gameitems()[9])) {
	    		if (sp.isInGame() && sp.getPlayer().getWorld().getName().equalsIgnoreCase("arenas")) {
	    			Arena arena = sp.getArena();
	    			if (!p.getItemInHand().isSimilar(DataManager.getManager().gameitems()[9])) return;
	    			if (arena.getDeadPlayers1().contains(sp) || arena.getDeadPlayers2().contains(sp)) return;
	    		
	    			
	    			
	    			if (!GameManager.getManager().getInteract().containsKey(sp)) {
	    				GameManager.getManager().getInteract().put(sp, System.currentTimeMillis());
	    			} 
	    			
	    			long time = System.currentTimeMillis()- GameManager.getManager().getInteract().get(sp);
	    			if (time >=190) {
						Vector speed = p.getLocation().getDirection().multiply(1.4);
			    		Vector shift = speed.getCrossProduct(new Vector(0, 1 ,0)).normalize().multiply(0.15);
			    		Egg egg = p.getWorld().spawn(p.getEyeLocation().add(shift), Egg.class);
			    		egg.setVelocity(speed);   		
			    		egg.setShooter(p);
			    		 sp.getPlayer().playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 0.5F, 2.0F);	    
			    		for (SpleefPlayer spp : sp.getArena().getViewers()) {
			    			if (spp.isSpectating()) {
			    		 spp.getPlayer().playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 0.5F, 2.0F);	    			
			    			}
			    		}	
		    			
			    		
	    				GameManager.getManager().getInteract().put(sp, System.currentTimeMillis());
	    			} 
	    			
	    			
	    		
	    	}
	    		return;
	    	}
	    	if (p.getItemInHand().equals(dm.queueitems()[1])) {
	    			sp.leaveQueue(sp.getArena(),true,true); 		
	    			sp.getPlayer().getInventory().clear();
	    			new BukkitRunnable() {

						@Override
						public void run() {
							sp.giveLobbyItems();
							
						}
	    			
	    			}.runTaskLater(Main.get(), 2L);
	    	} else if (p.getItemInHand().equals(dm.lobbyitems()[0])) {
	    		new GadgetsMenu(sp).o(p);
	    		//GadgetsMenuAPI.goBackToMainMenu(p);
	    	}  else if (p.getItemInHand().equals(dm.queueitems()[0])) {
	    		FFAArena ffa = GameManager.getManager().getFFAArenaByArena(sp.getArena());
	    		if (!ffa.isInEvent()) {
	    			if (sp.getMutationTokens()>0) {
	    		new MutationTokenMenu(sp).o(p);
	    			} else {
	    				sp.sendMessage("§cYou don't have more Mutation Tokens");
	    			}
	    		} else {
	    			sp.sendMessage("§cYou can't add Mutations when an FFA Event is started.");
	    		}
	    	}  else if (p.getItemInHand().equals(dm.lobbyitems()[0])) {
	    		new GadgetsMenu(sp).o(p);
	    		//GadgetsMenuAPI.goBackToMainMenu(p);
	    	} else if (p.getItemInHand().equals(dm.lobbyitems()[6])) {
	    		Party party = PartyManager.getManager().getParty(p);
	    		new PartyMenu(sp, party==null ? null : party.getPartyMode()).o(p);
	    		} else if (p.getItemInHand().equals(dm.lobbyitems()[2])) {
	    			new OptionsMenu(sp).o(p);
	    		} else if (p.getItemInHand().equals(dm.lobbyitems()[5])) {
	    			Guild guild = GuildsManager.getManager().getGuild(sp);
	    			if (guild!=null) {
	    			new GuildMainMenu(sp,guild).o(p);
	    			} else {
	    				sp.sendMessage("§cYou need to be in a Guild to use this. Use §b/guild create <Achronym> <Name>");
	    			}
	    		} else if (p.getItemInHand().equals(dm.spectateitems()[0])) {
	    			
	    			if (delay.contains(p.getUniqueId())) {
	    				p.sendMessage("§cPlease wait 5 seconds before using this again.");
	    			} else {
	    				
	    				SpectateManager sm = SpectateManager.getManager();
	    				sm.showOrHideSpectators(sp,sp.isHidingSpectators());
	    				delay.add(p.getUniqueId());
	    				new BukkitRunnable() {
	    					public void run() {
	    						delay.remove(p.getUniqueId());
	    					}
	    				}.runTaskLaterAsynchronously(Main.get(),5*20L);
	    			}
	    		
    			
    		} else if (p.getItemInHand().equals(dm.gameitems()[10])) {
    			
    			sp.getPlayer().performCommand("pause");			
		} else if (p.getItemInHand().equals(dm.gameitems()[11])) {
			
			sp.getPlayer().performCommand("resume");
	} else {
    			if (DataManager.getManager().areQueuesClosed() && ! p.hasPermission("splindux.staff")) {
    				p.sendMessage("§cQueues are currently closed.");
    				return;
    			}
    			
    			
    			
	    			 if (p.getItemInHand().equals(dm.lobbyitems()[4])) {
	    				 Party party = PartyManager.getManager().getParty(p);
	    					if (party!=null && !party.isLeader(p)) {
	    						p.sendMessage("§cOnly the party leader can join a game.");
	    						return;
	    					}
		    			new UnrankedMenu(sp).o(p);
		    		} else if (p.getItemInHand().equals(dm.lobbyitems()[1])) {
		    			Party party = PartyManager.getManager().getParty(p);
						if (party!=null && !party.isLeader(p)) {
							p.sendMessage("§cOnly the party leader can join a game.");
							return;
						}
		    			new ParkourMenu(sp).o(p);
		    		} 
	    			
	    		} 
	    		
	    	
	    }
		
	}
	
	
	@EventHandler
	public void onBurn(BlockBurnEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onSpread(BlockSpreadEvent   e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onSnowMan(EntityBlockFormEvent e) {
		e.setCancelled(true);
	}
	
	
	@EventHandler
	public void onEggThrow(PlayerEggThrowEvent event) {
		 event.setHatching(false);
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onInventoryMove(InventoryClickEvent e) {
		
		
		if (e.getWhoClicked() instanceof Player) {
			Player p = (Player) e.getWhoClicked();
			SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
			if (!p.getGameMode().equals(GameMode.CREATIVE) && !sp.isInGame()) {
				if (!e.getInventory().equals(p.getInventory()) ) {
				e.setCancelled(true);	
				}
			}
		}
		
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
	        
	    if (sp.getArena().getSpleefType().equals(SpleefType.SPLEGG)) {
	 	      hitblock.setType(Material.AIR);
	 	      Arena arena = sp.getArena();
	 	      BrokenBlock kill = new BrokenBlock(sp,hitblock.getLocation(),BreakReason.SNOWBALL);
	 	 	FFAArena ffa = GameManager.getManager().getFFAArenaByArena(arena);
	 	 	if (ffa!=null) ffa.getBrokenBlocks().add(kill);   
	    } else {
	    	 if (hitblock.getType() == Material.SNOW_BLOCK || hitblock.getType().equals(Material.CONCRETE_POWDER) || hitblock.getType().equals(Material.CONCRETE))
	 	    {
	 	     p.playSound(hitblock.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.5F, 2.0F);
	 	      hitblock.setType(Material.AIR);
	 	      Arena arena = sp.getArena();
	 	      BrokenBlock kill = new BrokenBlock(sp,hitblock.getLocation(),BreakReason.SNOWBALL);
	 	     FFAArena ffa = GameManager.getManager().getFFAArenaByArena(arena);
		 	 	if (ffa!=null) ffa.getBrokenBlocks().add(kill);   
	 	    }
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
									+ "§5§l[Extreme] §ato use this, visit the store for more info: §bhttp://store.splindux.com/");
						
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
				    	}else if (args[0].equalsIgnoreCase("/ride")) {
				    		if (sp.isInGame()) {
				    			e.setCancelled(true);
									p.sendMessage("§cYou can't execute this command while playing a match.");
								
				    		}
				    	} else if (args[0].equalsIgnoreCase("/nick") && args.length>1) {
				    		if (!args[1].matches(("^\\w{3,16}$"))) {
				    			p.sendMessage("§cYou can't use this nick.");
				    			e.setCancelled(true);
				    		}
				    		
				    		Collection<PotionEffect> potions = p.getActivePotionEffects();
				    		boolean fly = p.getAllowFlight();
				    		new BukkitRunnable() {
				    			public void run() {
				    				if (fly) {
				    					p.setAllowFlight(true);
				    					p.setFlying(true);
				    				}
				    				
				    				for (PotionEffect effect : potions)
				    				p.addPotionEffect(effect);
				    				
				    			
				    			}
				    		}.runTaskLater(Main.get(), 40L);
				    	}   
		    }
	
		    }
	
	
	@EventHandler
	public void onPlayerCommandSend(TabCompleteEvent e)
	{
	    Set<String> toRemove = new HashSet<String>();
	    for (String s : e.getCompletions()) 
	    	if (s.contains(":")) toRemove.add(s);
	    if (e.getCompletions()!=null)
	    e.getCompletions().removeAll(toRemove);
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
	  public void onSneak(PlayerToggleSneakEvent e) {
			  Player p = e.getPlayer();
			  SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
			if (p.getGameMode().equals(GameMode.SPECTATOR) && sp.getSpleefArenaSpectating()!=null && p.getSpectatorTarget()!=null) {
				p.setGameMode(GameMode.ADVENTURE);
				p.setAllowFlight(true);
				p.setFlying(true);			
		  }
			/*
			if (sp.isInGame()) {
				Arena arena = sp.getArena();
				if (arena.getGameType().equals(GameType.FFA) && arena.getSpleefType().equals(SpleefType.SPLEEF)) return;
			}
			*/
			p.eject();
	  }
	
	@EventHandler
    public void onVehicle(EntityDismountEvent  event) {
       if (event.getEntityType().equals(EntityType.PLAYER)) {
    	     if (event.getDismounted() instanceof ArmorStand) {
    		   SpleefPlayer sp = SpleefPlayer.getSpleefPlayer((Player) event.getEntity());
    		   if (sp.isInArena()) {
   				Arena arena = sp.getArena();
   				if (arena.getGameType().equals(GameType.FFA) && arena.getSpleefType().equals(SpleefType.SPLEEF)) {
   					Bukkit.getScheduler().runTaskLater(Main.get(), () -> {
   						if (!event.getDismounted().isDead()) {
   							event.getDismounted().addPassenger(sp.getPlayer());
   						}
   					}, 1L);
   				}
   			}
    	   }
       }
    }
	
	  
	  @EventHandler
	  public void onTeleport(PlayerTeleportEvent e) {
		  Player p = e.getPlayer();
		  SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
		  if (sp==null) return;
		  if (sp.getSpleefArenaSpectating()!=null || sp.isGameDead()) {
		 if( e.getCause().equals(TeleportCause.SPECTATE)) {
			 e.setCancelled(true);
		 }		  
	  }
		  }
	   
	  @EventHandler
	  public void onWorldChange(PlayerChangedWorldEvent e) {
		  
		  new BukkitRunnable() {
			  public void run() {
				  Player p = e.getPlayer();
				  SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
				  if (p.getWorld().getName().equalsIgnoreCase("world")) {
					  sp.setScoreboard(ScoreboardType.LOBBY);
						DataManager.getManager().getLobbyPlayers().add(p.getUniqueId());
						DataManager.getManager().getPlayingPlayers().remove(p.getUniqueId());
					  
					  new BukkitRunnable() {
						  public void run () {
							  if (!p.hasPermission("splindux.fly")) {
								  p.setFlying(false);
								  p.setAllowFlight(false);
							  }
						  }
					  }.runTaskLater(Main.get(), 3L);
				  }
				  
				  sp.updateScoreboard();		
				  if (p.isOnline()) {
					  HologramManager.getManager().showHolograms(p.getUniqueId());
					  
						if (p.hasPermission("splindux.pet") && p.getWorld().getName().equalsIgnoreCase("world")) {
							PetService petMetaService = PetBlocksApi.INSTANCE.resolve(PetService.class);
							
							Bukkit.getScheduler().runTask(Main.get(), () -> {
								petMetaService.getOrSpawnPetFromPlayer(p);
							});
							
							}
				  }
			  }
		  }.runTaskAsynchronously(Main.get());	 
	  }

	  

	   
	   @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	   private void onSandFall(EntityChangeBlockEvent event){
	       if(event.getEntityType()==EntityType.FALLING_BLOCK && event.getTo()==Material.AIR){
	               event.setCancelled(true);
	               //Update the block to fix a visual client bug, but don't apply physics
	               event.getBlock().getState().update(false, false);
	           }
	       }
	   
	   

	
}

