package me.santipingui58.splindux.commands;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.jumper251.replay.api.ReplayAPI;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.Request;
import me.santipingui58.splindux.game.spleef.SpleefArena;
import me.santipingui58.splindux.game.spleef.SpleefDuel;
import me.santipingui58.splindux.replay.GameReplay;
import me.santipingui58.splindux.replay.ReplayManager;
import me.santipingui58.splindux.utils.Utils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;



public class HoverCommand implements CommandExecutor {

	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
		} else {
	
		if(cmd.getName().equalsIgnoreCase("hover")){
			Player p = (Player) sender;
			 SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
			if (args[0].equalsIgnoreCase("duelaccept")) {
				
				Player p2 = Bukkit.getPlayer(args[1]);
				if (Bukkit.getOnlinePlayers().contains(p2)) {
					 SpleefPlayer challenger = SpleefPlayer.getSpleefPlayer(p2);
					if (challenger.hasDueled(sp)) {
						SpleefDuel duel = challenger.getDuelByDueledPlayer(sp);
						if (!GameManager.getManager().isInGame(sp)) {
							if (!GameManager.getManager().isInGame(challenger)) {
								if (duel.getAcceptedPlayers().contains(sp)) {
									p.sendMessage("§cYou already accepted this request.");	
									return false;
								}
								duel.getAcceptedPlayers().add(sp);
								if (duel.getAcceptedPlayers().size()>=duel.getDueledPlayers().size()) {
								GameManager.getManager().duelGame(challenger, duel.getDueledPlayers(), duel.getArena(),duel.getType());
								} else {
									List<SpleefPlayer> list = new ArrayList<SpleefPlayer>();		
									List<SpleefPlayer> leftToAccept = new ArrayList<SpleefPlayer>();		
									list.add(duel.getChallenger());
									list.addAll(duel.getDueledPlayers());
									
									for (SpleefPlayer splayer : duel.getDueledPlayers()) {
										if (!duel.getAcceptedPlayers().contains(splayer)) {
											leftToAccept.add(splayer);
										}
									}
									TextComponent msg1 = new TextComponent("[ACCEPT]");
									msg1.setColor(net.md_5.bungee.api.ChatColor.GREEN );
									msg1.setBold( true );
									msg1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hover duelaccept " + challenger.getPlayer().getName()));		
									msg1.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aAccept duel request").create()));
									TextComponent msg2 = new TextComponent("[DENY]");
									msg2.setColor( net.md_5.bungee.api.ChatColor.RED );
									msg2.setBold( true );
									msg2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hover dueldeny "  + challenger.getPlayer().getName()));
									msg2.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cDeny duel request").create()));
									
									ComponentBuilder cb = new ComponentBuilder(msg1);
									cb.append(" ");
									cb.append(msg2);
									BaseComponent[] bc =  cb.create();						
									for (SpleefPlayer dueled : list) {										
										dueled.getPlayer().sendMessage("§6"+sp.getOfflinePlayer().getName() + " §ahas accepted the request! §7(Left to accept: " + 
										Utils.getUtils().getPlayerNamesFromList(leftToAccept) + ")");
										if (dueled!=duel.getChallenger()) {
											if (!duel.getAcceptedPlayers().contains(dueled))
										dueled.getPlayer().spigot().sendMessage(bc);
									}
									}
								}
								} else {
								p.sendMessage("§cThis player is already in game!");	
							}
						} else {
							p.sendMessage("§cYou can not execute this command here.");	
						}
					} else {
						p.sendMessage("§cThis duel request has expired or the player didn't send you a duel request!");
					}
				} else {
					p.sendMessage("§cThis duel request has ended since the player isnt online!");
				}
			} else if (args[0].equalsIgnoreCase("dueldeny")) {
				Player p2 = Bukkit.getPlayer(args[1]);
				if (Bukkit.getOnlinePlayers().contains(p2)) {
					 SpleefPlayer challenger = SpleefPlayer.getSpleefPlayer(p2);
					if (challenger.hasDueled(sp)) {
						SpleefDuel duel = challenger.getDuelByDueledPlayer(sp);
						sp.getPlayer().sendMessage("§cYou have denied the duel request from §b" + challenger.getPlayer().getName() + "§c!");
						
						for (SpleefPlayer dueled : duel.getAllPlayers()) {
							if (dueled!=sp)
								dueled.getPlayer().sendMessage("§cThe player §b" + sp.getPlayer().getName() + "§c has denied the request! Duel cancelled.");
						}
						challenger.getDuels().remove(duel);
					} else {
						p.sendMessage("§cThis duel request has expired or the player didn't send you a duel request!");
					}
					} else {
						p.sendMessage("§cThis duel request has ended since the player isnt online!");
					}
			} else if (args[0].equalsIgnoreCase("duelcancel")) {
				
				SpleefDuel duel = sp.getDuelByUUID(UUID.fromString(args[1]));
				if (duel!=null) {
				for (SpleefPlayer dueled : duel.getDueledPlayers()) {
					dueled.getPlayer().sendMessage("§b" + sp.getPlayer().getName() + "§c has cancelled the request.");
				}
				duel.getChallenger().getDuels().remove(duel);
				} else {
					p.sendMessage("§cThis duel request has expired.");
				}
			} else if (args[0].equalsIgnoreCase("record")) {
				if (GameManager.getManager().isInGame(sp)) {
					SpleefArena arena = GameManager.getManager().getArenaByPlayer(sp);
						if (arena.getRecordingRequest()) {
							arena.record();
							arena.cancelRecordingRequest();
							List<Player> list = new ArrayList<Player>();
							List<SpleefPlayer> playerss = new ArrayList<SpleefPlayer>();
									playerss.addAll(arena.getDuelPlayers1());
							playerss.addAll(arena.getDuelPlayers2());
							for (SpleefPlayer spp : playerss) {
								list.add(spp.getPlayer());
							}							
							 Player[] myArray = new Player[list.size()];
							 GameReplay replay = ReplayManager.getManager().createReplay(ReplayManager.getManager().getName(arena));
							ReplayAPI.getInstance().recordReplay(replay.getName(), sender,  list.toArray(myArray));
							sp.getPlayer().sendMessage("§aYou are now recording this game!");
						}
					
				}
			}  else if (args[0].equalsIgnoreCase("crumblecancel")) {
				if (GameManager.getManager().isInGame(sp)) {
					SpleefArena arena = GameManager.getManager().getArenaByPlayer(sp);
				Request request = arena.getCrumbleRequest();		
				if (request!=null) {
					if (request.getChallenger().equals(sp)) {
				for (SpleefPlayer dueled : arena.getViewers()) {
					dueled.getPlayer().sendMessage("§b" + sp.getPlayer().getName() + "§c has cancelled the  crumble request.");
				}
				arena.setCrumbleRequest(null);
					} else {
						p.sendMessage("§cThis duel request has expired.");
					}
				} else {
					p.sendMessage("§cThis duel request has expired.");
				}
			} else {
				p.sendMessage("§cThis crumble request has expired.");
			}
				} else if (args[0].equalsIgnoreCase("crumbledeny")) {
					if (GameManager.getManager().isInGame(sp)) {
						SpleefArena arena = GameManager.getManager().getArenaByPlayer(sp);
						if (arena.getCrumbleRequest()!=null) {
						for (SpleefPlayer dueled : arena.getPlayers()) {
							dueled.getPlayer().sendMessage("§cThe player §b" + sp.getPlayer().getName() + "§c has denied the request! Crumble cancelled.");
						}
						
						arena.setCrumbleRequest(null);
						
						} else {
							p.sendMessage("§cThis crumble request has expired.");
						}
					} else {
						p.sendMessage("§cThis crumble request has expired.");
					}
				} else if (args[0].equalsIgnoreCase("crumbleaccept")) {
					if (GameManager.getManager().isInGame(sp)) {
						SpleefArena arena = GameManager.getManager().getArenaByPlayer(sp);
						if (!arena.getDeadPlayers1().contains(sp) && !arena.getDeadPlayers2().contains(sp)) {
						if (arena.getCrumbleRequest()!=null) {
							Request request = arena.getCrumbleRequest();
							if (!request.getAcceptedPlayers().contains(sp)) {
								request.getAcceptedPlayers().add(sp);
								
								if (request.getAcceptedPlayers().size()>=arena.getPlayers().size()-arena.getDeadPlayers1().size()-arena.getDeadPlayers2().size()) {
									GameManager.getManager().crumbleWithCommand(arena, request.getAmount());
								} else {
									List<SpleefPlayer> leftToAccept = new ArrayList<SpleefPlayer>();		
									
									for (SpleefPlayer splayer : arena.getPlayers()) {
										if (!request.getAcceptedPlayers().contains(splayer) && !splayer.equals(request.getChallenger())
												&& !arena.getDeadPlayers1().contains(splayer)&& !arena.getDeadPlayers2().contains(splayer)) {
											leftToAccept.add(splayer);
										}
									}
									TextComponent msg1 = new TextComponent("[CRUMBLE]");
									msg1.setColor(net.md_5.bungee.api.ChatColor.GREEN );
									msg1.setBold( true );
									msg1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hover crumbleaccept "));		
									msg1.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aAccept crumble request").create()));
									TextComponent msg2 = new TextComponent("[DENY CRUMBLE]");
									msg2.setColor( net.md_5.bungee.api.ChatColor.RED );
									msg2.setBold( true );
									msg2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hover crumbledeny"));
									msg2.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cDeny crumble request").create()));
									
									ComponentBuilder cb = new ComponentBuilder(msg1);
									cb.append(" ");
									cb.append(msg2);
									BaseComponent[] bc =  cb.create();			
									if (leftToAccept.isEmpty()) {
										GameManager.getManager().crumbleWithCommand(arena, request.getAmount());
										return true;
									} 
									for (SpleefPlayer dueled : arena.getPlayers()) {										
										dueled.getPlayer().sendMessage("§6"+sp.getOfflinePlayer().getName() + " §ahas accepted the request! §7(Left to accept: " + 
										Utils.getUtils().getPlayerNamesFromList(leftToAccept) + ")");
										if (dueled!=request.getChallenger()) {
											if (!request.getAcceptedPlayers().contains(dueled) && !arena.getDeadPlayers1().contains(sp) && !arena.getDeadPlayers2().contains(sp))
										dueled.getPlayer().spigot().sendMessage(bc);
									}
									}
								}
								
							} else {
								p.sendMessage("§cYou already accepted this request.");
							}
						}else {
							p.sendMessage("§cThis crumble request has expired.");
						}
				} else {
					p.sendMessage("§cOnly alive players can execute this command.");
				}
					} else {
						p.sendMessage("§cThis crumble request has expired.");
					}
						} else if (args[0].equalsIgnoreCase("playtocancel")) {
							if (GameManager.getManager().isInGame(sp)) {
								SpleefArena arena = GameManager.getManager().getArenaByPlayer(sp);
							Request request = arena.getPlayToRequest();		
							if (request!=null) {
								if (request.getChallenger().equals(sp)) {
							for (SpleefPlayer dueled : arena.getViewers()) {
								dueled.getPlayer().sendMessage("§b" + sp.getPlayer().getName() + "§c has cancelled the  playto request.");
							}
							arena.setPlayToRequest(null);
								} else {
									p.sendMessage("§cThis duel request has expired.");
								}
							} else {
								p.sendMessage("§cThis duel request has expired.");
							}
						} else {
							p.sendMessage("§cThis crumble request has expired.");
						}
							} else if (args[0].equalsIgnoreCase("playtodeny")) {
								if (GameManager.getManager().isInGame(sp)) {
									SpleefArena arena = GameManager.getManager().getArenaByPlayer(sp);
									if (arena.getPlayToRequest()!=null) {
									for (SpleefPlayer dueled : arena.getPlayers()) {
										dueled.getPlayer().sendMessage("§cThe player §b" + sp.getPlayer().getName() + "§c has denied the request! Playto cancelled.");
									}
									
									arena.setPlayToRequest(null);
									
									} else {
										p.sendMessage("§cThis crumble request has expired.");
									}
								} else {
									p.sendMessage("§cThis crumble request has expired.");
								}
							} else if (args[0].equalsIgnoreCase("playtoaccept")) {
								if (GameManager.getManager().isInGame(sp)) {
									SpleefArena arena = GameManager.getManager().getArenaByPlayer(sp);
									if (arena.getPlayToRequest()!=null) {
										Request request = arena.getPlayToRequest();
										if (!request.getAcceptedPlayers().contains(sp)) {
											request.getAcceptedPlayers().add(sp);
											
											if (request.getAcceptedPlayers().size()>=arena.getPlayers().size()-arena.getDeadPlayers1().size()-arena.getDeadPlayers2().size()) {
												GameManager.getManager().playToWithCommand(arena, request.getAmount());
											} else {
												List<SpleefPlayer> leftToAccept = new ArrayList<SpleefPlayer>();		
												
												for (SpleefPlayer splayer : arena.getPlayers()) {
													if (!request.getAcceptedPlayers().contains(splayer) && !splayer.equals(request.getChallenger())) {
														leftToAccept.add(splayer);
													}
												}
												TextComponent msg1 = new TextComponent("[PLAYTO]");
												msg1.setColor(net.md_5.bungee.api.ChatColor.GREEN );
												msg1.setBold( true );
												msg1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hover playtoaccept "));	
												msg1.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aAccept play to request").create()));
												
												TextComponent msg2 = new TextComponent("[DENY PLAYTO]");
												
												msg2.setColor( net.md_5.bungee.api.ChatColor.RED );
												msg2.setBold( true );
												msg2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hover playtodeny"));
												msg2.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cDeny play to request").create()));
												ComponentBuilder cb = new ComponentBuilder(msg1);
												cb.append(" ");
												cb.append(msg2);
												BaseComponent[] bc =  cb.create();			
												if (leftToAccept.isEmpty()) {
													GameManager.getManager().playToWithCommand(arena, request.getAmount());
													return true;
												} 
												for (SpleefPlayer dueled : arena.getPlayers()) {										
													dueled.getPlayer().sendMessage("§6"+sp.getOfflinePlayer().getName() + " §ahas accepted the request! §7(Left to accept: " + 
													Utils.getUtils().getPlayerNamesFromList(leftToAccept) + ")");
													if (dueled!=request.getChallenger()) {
														if (!request.getAcceptedPlayers().contains(dueled))
													dueled.getPlayer().spigot().sendMessage(bc);
												}
												}
											}
											
										} else {
											p.sendMessage("§cYou already accepted this request.");
										}
									}else {
										p.sendMessage("§cThis crumble request has expired.");
									}
							
								} else {
									p.sendMessage("§cThis crumble request has expired.");
								}
									} 
			} 
			

}
		
		
		return false;
	}
	


	
}