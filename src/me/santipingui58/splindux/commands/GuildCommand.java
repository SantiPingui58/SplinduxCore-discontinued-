package me.santipingui58.splindux.commands;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.gui.game.guild.GuildMainMenu;
import me.santipingui58.splindux.relationships.RelationshipRequest;
import me.santipingui58.splindux.relationships.RelationshipRequestType;
import me.santipingui58.splindux.relationships.guilds.Guild;
import me.santipingui58.splindux.relationships.guilds.GuildPlayer;
import me.santipingui58.splindux.relationships.guilds.GuildsManager;
import me.santipingui58.splindux.utils.Utils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;


public class GuildCommand implements CommandExecutor {

	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
		} else {
	
		if(cmd.getName().equalsIgnoreCase("guild")){
			final Player p = (Player) sender;
			 SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
				sp.back();
				if (args.length==0) {					
				sendHelp(sp);
				} else {
					GuildsManager gm = GuildsManager.getManager();
					
					if (args[0].equalsIgnoreCase("create") && args.length>=3) {				
						if (gm.getGuild(sp)==null) {
						//	int value = p.hasPermission("splindux.vip") ? 12500 : 25000;
							int value = 15000;
							if (sp.getCoins()>=value) {
								
								StringBuilder builder = new StringBuilder();
							    for (int i = 2; i < args.length; i++)
							    {
							      builder.append(args[i]).append(" ");
							    }
							    
							  String name = builder.toString();
								if (name.length()<=20) {
									if (name.contains(".") || name.contains(":")) {
										sender.sendMessage(gm.getPrefix()+"§cThe name of the guild can't have special characters.");
										return false;
									}
									
									if (args[1].length()!=3) {
										sender.sendMessage(gm.getPrefix()+"§cThe achronym has to be exactly 3 characters long");
										return false;
									}
									
									Guild guild1 = gm.getGuildByName(name);
									Guild guild2 = gm.getGuildByAchronym(args[1]);
									if (guild1==null && guild2==null) {
										gm.foundGuild(name, args[1].toUpperCase(), value, sp);
									} else {
										sender.sendMessage(gm.getPrefix()+"§cA guild with that name or achronym already exists.");
									}
								} else {
									sender.sendMessage(gm.getPrefix()+"§cThe name of the guild can't have more than 20 characters.");
								}
								
							
							} else {
								sender.sendMessage(gm.getPrefix()+"§cYou don't have enough coins to do that.");
							}
						} else {
							sender.sendMessage(gm.getPrefix()+"§cYou already are in a Guild.");
						}
						
					} else if (args[0].equalsIgnoreCase("add") && args.length==3) {
							if (gm.getGuild(sp)!=null) {
								Guild guild = gm.getGuild(sp);
								if (guild.isAdmin(sp.getUUID(), false)) {
									Player player = Bukkit.getPlayer(args[1]);
									if (Bukkit.getOnlinePlayers().contains(player)) {
										SpleefPlayer splayer = SpleefPlayer.getSpleefPlayer(player);
										if (gm.getGuild(splayer)!=null && gm.getGuild(splayer).equals(guild)) {
											p.sendMessage(gm.getPrefix()+"§cThe player §b" + args[1] + "§c is already a player of your Guild.");	
											return false;
										} 
										if (gm.getGuild(splayer)==null) {
											
											int salary = 0;
											try {
												salary = Integer.parseInt(args[2]);
												if (salary<1) {
													p.sendMessage(gm.getPrefix()+"§cThe salary must be a greater than 1.");
													return false;
												}
											} catch (Exception e) {
												p.sendMessage(gm.getPrefix()+"§a"+ args[2]+ " §cisnt a valid number.");
												return false;
											}
							
											if (guild.getCoins()>=salary*20) {
												if (guild.getPlayers().size()<=5) {
													int min = gm.getFutureMinValue(guild, player.getUniqueId());
													if (min <= salary) {
													String[] argss = {guild.getName(),sp.getOfflinePlayer().getName(),splayer.getOfflinePlayer().getName(), String.valueOf(salary)};
													List<UUID> list1 = new ArrayList<UUID>();
													List<UUID> list2= new ArrayList<UUID>();
													list1.add(sp.getUUID());
													list2.add(splayer.getUUID());
													new RelationshipRequest(list1,list2,RelationshipRequestType.JOIN_GUILD_AS_PLAYER,argss);
													} else {
														p.sendMessage(gm.getPrefix()+"§cMin salary for this player is: §6" + min +" Coins");	
													}
												} else {
													p.sendMessage(gm.getPrefix()+"§cYou can't have more than 7 players on your Guild");	
												}
													
											} else {
												p.sendMessage(gm.getPrefix()+"§cYour guild can't afford that salary.");	
											}
											
										} else {
											p.sendMessage(gm.getPrefix()+"§cThe player §b" + args[1] + "§c is already in another Guild. Use §a/guild buy <player> <new salary>");	
										}
									} else {
										p.sendMessage(gm.getPrefix()+"§cThe player §b" + args[1] + "§c does not exist or is not online.");	
									}
								} else {
									sender.sendMessage(gm.getPrefix()+"§cOnly Admins of the Guild can do that.");
								}
							} else {
								sender.sendMessage(gm.getPrefix()+"§cYou need to be in a Guild to do that.");
							}
							
							//guild minsalary player
							//guild minsalary player guild
					} else if (args[0].equalsIgnoreCase("minsalary") && args.length>=2) {
							Guild guild =  args.length==2 ? gm.getGuild(sp) : gm.getGuildByAchronym(args[2]);
							if (guild!=null) {
								@SuppressWarnings("deprecation")
								OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);		
												int min = gm.getFutureMinValue(guild, player.getUniqueId());
												p.sendMessage(gm.getPrefix()+"§aMin salary for §b" +player.getName() +" §ain the §6§l"+guild.getName() + "§aGuild would be:§6 " + min +" Coins");	
								}  else {
									
									if (args.length==2) {
										sender.sendMessage(gm.getPrefix()+"§c/guild minsalary <Player> <Guild>");
									}else {
										sender.sendMessage(gm.getPrefix()+"§cThe Guild §b" + args[2] +" §cdoes not exist (Use the achronym)");
									}
								}
				} else if (args[0].equalsIgnoreCase("buy") && args.length==3)  {
						if (gm.getGuild(sp)!=null) {
							Guild guild = gm.getGuild(sp);
							if (guild.isAdmin(sp.getUUID(), false)) {
								Player player = Bukkit.getPlayer(args[1]);
								if (Bukkit.getOnlinePlayers().contains(player)) {
									SpleefPlayer splayer = SpleefPlayer.getSpleefPlayer(player);
									if (gm.getGuild(splayer)!=null && gm.getGuild(splayer).equals(guild)) {
										p.sendMessage(gm.getPrefix()+"§cThe player §b" + args[1] + "§c is already a player of your Guild.");	
										return false;
									} 
									if (gm.getGuild(splayer)!=null) {
										Guild otherGuild = gm.getGuild(splayer);
										if (otherGuild.getPlayer(splayer.getUUID())==null) {
											p.sendMessage(gm.getPrefix()+"§cYou can't buy this player.");	
											return false;
										}
										int salary = 0;
										try {
											salary = Integer.parseInt(args[2]);
											if (salary<1) {
												p.sendMessage(gm.getPrefix()+"§cThe salary must be a greater than 1.");
												return false;
											}
										} catch (Exception e) {
											p.sendMessage(gm.getPrefix()+"§a"+ args[2]+ " §cisnt a valid number.");
											return false;
										}
										
										if (guild.getCoins()>=salary*20) {
											if (guild.getCoins()>=gm.getGuild(splayer).getPlayer(splayer.getUUID()).getValue()*1.15) {
											if (guild.getPlayers().size()<=5) {
												int min = gm.getFutureMinValue(guild, player.getUniqueId());
												if (min <= salary) {
												String[] argss = {guild.getName(),sp.getOfflinePlayer().getName(),splayer.getOfflinePlayer().getName(), String.valueOf(salary)};
												List<UUID> list1 = new ArrayList<UUID>();
												List<UUID> list2= new ArrayList<UUID>();
												list1.addAll(guild.getAdmins(false));
												list2.add(splayer.getUUID());
												new RelationshipRequest(list1,list2,RelationshipRequestType.BUY_PLAYER,argss);
												} else {
													p.sendMessage(gm.getPrefix()+"§cMin salary for this player is: §6" + min +" Coins");
												}
											} else {
												p.sendMessage(gm.getPrefix()+"§cYou can't have more than 7 players on your Guild");	
											}
											} else {
												p.sendMessage(gm.getPrefix()+"§cYour guild can't afford that buy.");	
											}
										} else {
											p.sendMessage(gm.getPrefix()+"§cYour guild can't afford that salary.");	
										}
										
										
									} else {
										p.sendMessage(gm.getPrefix()+"§cThe player §b" + args[1] + "§c is not in any Guild. Use §a/guild add <player> <salary>");	
									}
								} else {
									p.sendMessage(gm.getPrefix()+"§cThe player §b" + args[1] + "§c does not exist or is not online.");	
								}
							} else {
								sender.sendMessage(gm.getPrefix()+"§cOnly Admins of the Guild can do that.");
							}
						} else {
							sender.sendMessage(gm.getPrefix()+"§cYou need to be in a Guild to do that.");
						}
					}else if (args[0].equalsIgnoreCase("fire") && args.length==2) {
						if (gm.getGuild(sp)!=null) {
							Guild guild = gm.getGuild(sp);
							if (guild.isAdmin(sp.getUUID(), false)) {
								@SuppressWarnings("deprecation")
								OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);							
								if (player.hasPlayedBefore()) {
									if (guild.getPlayersUUID().contains(player.getUniqueId())) {
										int value = guild.getPlayer(player.getUniqueId()).getValue();
										if (guild.getCoins()>=value*1.15) {
											value = (int) (value*0.25);
											guild.firePlayer(player.getUniqueId(),value);
											p.sendMessage(gm.getPrefix()+"§cYou have fired §b"+ player.getName()+ " §cfrom your guild");
										}else {
											p.sendMessage(gm.getPrefix()+"§cYour guild can't afford that.");	
										}
									} else {
										p.sendMessage(gm.getPrefix()+"§cThe player §b" + args[1] + "§c is not a Player of your Guild");	
									}
								} else {
									p.sendMessage(gm.getPrefix()+"§cThe player §b" + args[1] + "§c does not exist.");	
								}
							} else {
								sender.sendMessage(gm.getPrefix()+"§cOnly Admins of the Guild can do that.");
							}
						} else {
							sender.sendMessage(gm.getPrefix()+"§cYou need to be in a Guild to do that.");
						}
					} else if (args[0].equalsIgnoreCase("resign") && args.length==1) {
						if (gm.getGuild(sp)!=null) {
							Guild guild = gm.getGuild(sp);
							if (!guild.getLeader().equals(sp.getUUID())) {
								GuildPlayer gp = guild.getPlayer(sp.getUUID());
								if (sp.getCoins()>=gp.getSalary()*25) {
									guild.resign(sp,gp.getSalary()*25);
								} else {
									sender.sendMessage(gm.getPrefix()+"§cYou don't have enough coins to do that.");
								}
							} else {
								sender.sendMessage(gm.getPrefix()+"§cA leader can't resign. To delete the Guild do /guild disband");
							}
					} else {
						sender.sendMessage(gm.getPrefix()+"§cYou need to be in a Guild to do that.");
						}
					} else if (args[0].equalsIgnoreCase("disband") && args.length==1) {
						if (gm.getGuild(sp)!=null) {
							Guild guild = gm.getGuild(sp);
							if (guild.getLeader().equals(sp.getUUID())) {
								if (guild.getCoins()>=guild.getAllPlayersValue()) {
									gm.disbandGuild(guild);
									sender.sendMessage(gm.getPrefix()+"§cGuild disbanded!");
								} else {
									sender.sendMessage(gm.getPrefix()+"§cThe Guild doesn't have enough coins to do that.");
								}
							} else {
								sender.sendMessage(gm.getPrefix()+"§cOnly the Leader of the Guild can do that.");
							}
						} else {
							sender.sendMessage("gm.getPrefix()+§cYou need to be in a Guild to do that.");
						}
					} else if (args[0].equalsIgnoreCase("transferable") && args.length==1) {
						if (gm.getGuild(sp)!=null) {
							Guild guild = gm.getGuild(sp);
							if (!guild.getLeader().equals(sp.getUUID())) {
								if (guild.getTransferablePlayers().contains(sp.getUUID())) {
									guild.getTransferablePlayers().remove(sp.getUUID());
									sender.sendMessage(gm.getPrefix()+"§aYou left the transferable players list.");
								} else {
									guild.getTransferablePlayers().add(sp.getUUID());
									sender.sendMessage(gm.getPrefix()+"§aYou have been added to transferable players! Your player value and salary are now 50% off.");
								}
							} else {
								sender.sendMessage(gm.getPrefix()+"§cOnly players can do that.");
							}
						} else {
							sender.sendMessage(gm.getPrefix()+"§cYou need to be in a Guild to do that.");
						}
					} else if (args[0].equalsIgnoreCase("renegotiate") && args.length == 3) {
						if (gm.getGuild(sp)!=null) {
							Guild guild = gm.getGuild(sp);
							if (guild.isAdmin(sp.getUUID(), false)) {
							Player player = Bukkit.getPlayer(args[1]);
							if (Bukkit.getOnlinePlayers().contains(player)) {
								if (guild.getPlayersUUID().contains(player.getUniqueId())) {
									GuildPlayer gp = guild.getPlayer(player.getUniqueId());
									int salary = 0;
									try {
										salary = Integer.parseInt(args[2]);
										if (salary<1) {
											p.sendMessage(gm.getPrefix()+"§cThe salary must be a greater than 1.");
											return false;
										}
									} catch (Exception e) {
										p.sendMessage(gm.getPrefix()+"§a"+ args[0]+ " §cisnt a valid number.");
										return false;
									}
									
									if (guild.getCoins()>=salary*10) {
											
										int min = gm.getFutureMinValue(guild, player.getUniqueId());
										if (min <= salary) {
										guild.renegociate(gp,salary);
										} else {
											p.sendMessage(gm.getPrefix()+"§cMin salary for this player is: §6" + min +" Coins");
										}
									} else {
										p.sendMessage(gm.getPrefix()+"§cYour guild can't afford that salary.");	
									}
								} else {
									p.sendMessage(gm.getPrefix()+"§cThe player §b" + args[1] + "§c is not a Player of your Guild");	
								}
							} else {
								p.sendMessage(gm.getPrefix()+"§cThe player §b" + args[1] + "§c does not exist or is not online.");	
							}
							} else {
								sender.sendMessage(gm.getPrefix()+"§cOnly Admins of the Guild can do that.");
							}
							} else {
								sender.sendMessage(gm.getPrefix()+"§cYou need to be in a Guild to do that.");
							}
					} else if (args[0].equalsIgnoreCase("invite") && args.length==2) {
						if (gm.getGuild(sp)!=null) {
							Guild guild = gm.getGuild(sp);
							Player player = Bukkit.getPlayer(args[1]);
							if (Bukkit.getOnlinePlayers().contains(player)) {
								SpleefPlayer splayer = SpleefPlayer.getSpleefPlayer(player);
								if (gm.getGuild(splayer)==null) {
									TextComponent msg1 = new TextComponent("[Join Guild]");
									msg1.setColor(net.md_5.bungee.api.ChatColor.GREEN );
									msg1.setBold(true);
									msg1.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/guild join " + guild.getName()));		
									msg1.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aJoin Guild").create()));
									
									ComponentBuilder cb = new ComponentBuilder(msg1);
									BaseComponent[] bc =  cb.create();						
									
										sender.sendMessage(gm.getPrefix()+"§aYou have invited §b" + player.getName() + " §ato your Guild!");
										player.sendMessage(gm.getPrefix()+"§b"+ sender.getName()+" §bhas invited you to the guild §6" + guild.getName());
										player.getPlayer().spigot().sendMessage(bc);	
								} else {
									p.sendMessage(gm.getPrefix()+"§cThe player §b" + args[1] + "§c is already in a Guild.");	
								}
							} else {
								p.sendMessage(gm.getPrefix()+"§cThe player §b" + args[1] + "§c does not exist or is not online.");	
							}
						} else {
							sender.sendMessage(gm.getPrefix()+"§cYou need to be in a Guild to do that.");
						}
					} else if (args[0].equalsIgnoreCase("join")) {
						if (gm.getGuild(sp)==null) {
							StringBuilder builder = new StringBuilder();
						    for (int i = 1; i < args.length; i++)
						    {
						      builder.append(args[i]).append(" ");
						    }
						    
						  String name = builder.toString();
							Guild guild = gm.getGuildByName(name);
							if (guild!=null) {
								if (guild.getMaxMembers()>guild.getMembers().size()) {
									if (sp.getCoins()>guild.getMemberFee()*7) {
										if (!guild.getBannedPlayers().contains(sp.getUUID())) {
									guild.getMembers().add(sp.getUUID());
									sender.sendMessage(gm.getPrefix()+"§aYou have joined the §b" + name + " Guild§a! The daily fee is: §6" + guild.getMemberFee() + "Coins");
									guild.broadcast(gm.getPrefix()+"§b"+sp.getName()+" §ahas joined the Guild as a mamber!");
									sp.removeCoins(guild.getMemberFee());
									guild.setCoins(guild.getCoins()+guild.getMemberFee());
										} else {
											sender.sendMessage(gm.getPrefix()+"§cYou are banned from this Guild.");
										}
									} else {
										sender.sendMessage(gm.getPrefix()+"§cYou don't have enough coins to pay the daily fee of the Guild.");
									}
								} else {
									sender.sendMessage(gm.getPrefix()+"§cThe Guild is full!");
								}
							} else {
								sender.sendMessage(gm.getPrefix()+"§cThe guild §b" + name + "§c does not exist.");
							}
						} else {
							sender.sendMessage(gm.getPrefix()+"§cYou are already in a Guild.");
						}
					} else if (args[0].equalsIgnoreCase("promote") && args.length==2) {
						if (gm.getGuild(sp)!=null) {
							Guild guild = gm.getGuild(sp);
							if (guild.getLeader().compareTo(sp.getUUID())==0) {
							@SuppressWarnings("deprecation")
							OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
							if (guild.getAllMembers().contains(player.getUniqueId())) {
								if (guild.getLeader().compareTo(player.getUniqueId())==0) {
									sender.sendMessage(gm.getPrefix()+"§cYou can't promote yourself...");
									return false;
								}
								if (guild.isMod(player.getUniqueId(), true)) {
									guild.removeMod(player.getUniqueId());
									guild.addAdmin(player.getUniqueId());
									guild.broadcast("§b"+player.getName()+" §ahas been promoted to §c§lAdmin§a!");
								} else {
									guild.addMod(player.getUniqueId());
									guild.broadcast("§b"+player.getName()+" §ahas been promoted to §2§lMod§a!");
								}
								
							} else {
								p.sendMessage(gm.getPrefix()+"§cThe player §b" + args[1] + " §c is not a Player of your Guild");	
							}
							} else {
								sender.sendMessage(gm.getPrefix()+"§cOnly the Owner of the Guild can do that.");
							}
							} else {
								sender.sendMessage(gm.getPrefix()+"§cYou need to be in a Guild to do that.");
							}
					} else if (args[0].equalsIgnoreCase("demote") && args.length==2) {
						if (gm.getGuild(sp)!=null) {
							Guild guild = gm.getGuild(sp);
							if (guild.getLeader().compareTo(sp.getUUID())==0) {
							@SuppressWarnings("deprecation")
							OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
							if (guild.getAllMembers().contains(player.getUniqueId())) {
								if (guild.getLeader().compareTo(player.getUniqueId())==0) {
									sender.sendMessage(gm.getPrefix()+"§cYou can't promote yourself...");
									return false;
								}
								if (guild.isAdmin(player.getUniqueId(), true)) {
									guild.addMod(player.getUniqueId());
									guild.removeAdmin(player.getUniqueId());
									guild.broadcast("§b"+player.getName()+" §chas been demoted to §2§lMod");
								} else if (guild.isMod(player.getUniqueId(), true)) {
									guild.removeMod(player.getUniqueId());
									guild.broadcast("§b"+player.getName()+" §chas been demoted to user");
								} else {
									sender.sendMessage(gm.getPrefix()+"§cThis player can't be demoted");
									return false;
								}
								
							} else {
								p.sendMessage(gm.getPrefix()+"§cThe player §b" + args[1] + " §c is not a Player of your Guild");	
							}
							} else {
								sender.sendMessage(gm.getPrefix()+"§cOnly the Owner of the Guild can do that.");
							}
							} else {
								sender.sendMessage(gm.getPrefix()+"§cYou need to be in a Guild to do that.");
							}
					}  else if (args[0].equalsIgnoreCase("kick") && args.length==2) {
						if (gm.getGuild(sp)!=null) {
							Guild guild = gm.getGuild(sp);
							if (guild.isMod(sp.getUUID(), false)) {
							@SuppressWarnings("deprecation")
							OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);			
							if (player.hasPlayedBefore()) {
								if (gm.getGuild(player.getUniqueId()).equals(guild)) {
									if (guild.getMembers().contains(player.getUniqueId())) {
										guild.getMembers().remove(player.getUniqueId());
										if (player.isOnline())  {
											Player playerr = Bukkit.getPlayer(player.getUniqueId());
											playerr.sendMessage("§cYou have been kicked from your Guild!");						
										}
										guild.broadcast("§cThe member §b" + player.getName() + " §chas been kicked from the Guild");
									} else {
										
									}
								} else {
									p.sendMessage(gm.getPrefix()+"§cThe player §b" + args[1] + "§c is not in your guild.");	
								}
							} else {
								p.sendMessage(gm.getPrefix()+"§cThe player §b" + args[1] + "§c does not exist.");	
							}
							} else {
								sender.sendMessage(gm.getPrefix()+"§cOnly the Mods of the Guild can do that.");
							}
								} else {
									sender.sendMessage(gm.getPrefix()+"§cYou need to be in a Guild to do that.");
								}
					} else if (args[0].equalsIgnoreCase("ban") && args.length==2) {
						if (gm.getGuild(sp)!=null) {
							Guild guild = gm.getGuild(sp);
							if (guild.isMod(sp.getUUID(), false)) {
								@SuppressWarnings("deprecation")
								OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);			
								if (player.hasPlayedBefore()) {
									if (guild.getPlayersUUID().contains(p.getUniqueId())) {
										sender.sendMessage("§cYou can only ban members of your guild, not players.");
										return false;
									}
									
									if (guild.getBannedPlayers().contains(player.getUniqueId())) {
										guild.getBannedPlayers().remove(player.getUniqueId());
										guild.broadcast("§cThe player §b" + player.getName() + "§c has been unbanned!");										
									} else {
										guild.broadcast("§cThe player §b" + player.getName() + "§c has been banned!");
										guild.getBannedPlayers().add(player.getUniqueId());
										if (guild.getMembers().contains(player.getUniqueId())) {
											guild.getMembers().remove(player.getUniqueId());
											if (player.isOnline())  {
												Player playerr = Bukkit.getPlayer(player.getUniqueId());
												playerr.sendMessage(gm.getPrefix()+"§cYou have been banned from your Guild!");						
											}
										}
									}
								} else {
									p.sendMessage(gm.getPrefix()+"§cThe player §b" + args[1] + "§c does not exist.");	
								}
							} else {
								sender.sendMessage(gm.getPrefix()+"§cOnly Mods of the Guild can do that.");
							}
							} else {
								sender.sendMessage(gm.getPrefix()+"§cYou need to be in a Guild to do that.");
							}
					} else if (args[0].equalsIgnoreCase("menu")) {
						if (gm.getGuild(sp)!=null) {
							Guild guild = gm.getGuild(sp);
						new GuildMainMenu(sp,guild).o(p);
						} else {
							sender.sendMessage(gm.getPrefix()+"§cYou need to be in a Guild to do that.");
						}
					} else if (args[0].equalsIgnoreCase("list")) {
						if (gm.getGuild(sp)!=null) {
							Guild guild = gm.getGuild(sp);
							sender.sendMessage("§6§l"+guild.getName() + "§7- §fList of Players");
							OfflinePlayer leader = Bukkit.getOfflinePlayer(guild.getLeader());
							if (Bukkit.getOnlinePlayers().contains(leader));

							String on = "§a§l●§7";
							String off = "§c§l●§7";
							String leaderName = leader.isOnline() ?  on+ leader.getName() : off + leader.getName();
							String adminsName = "";
							String modsName = "";
							String playersName = "";
							String membersName = "";
							
							
							int onlineAdmins =0;
							int onlineMods=0;
							int onlinePlayers=0;
							int onlineMembers=0;
							
							for (UUID u : guild.getAdmins(true)) {
								OfflinePlayer player = Bukkit.getOfflinePlayer(u);
								if (u.compareTo(guild.getLeader())==0) continue;
								if (player.isOnline()) onlineAdmins++;
								String an = player.isOnline() ?  on+ player.getName() : off + player.getName();
								if (adminsName.equalsIgnoreCase("")) {	
									adminsName = an;
								} else {							
									adminsName = adminsName + "," + an;
								}
							}
							
							for (UUID u : guild.getMods(true)) {
								OfflinePlayer player = Bukkit.getOfflinePlayer(u);
								if (u.compareTo(guild.getLeader())==0) continue;
								if (player.isOnline()) onlineMods++;
								String an = player.isOnline() ?  on+ player.getName() : off + player.getName();
								if (modsName.equalsIgnoreCase("")) {	
									modsName = an;
								} else {							
									modsName = modsName + "," + an;
								}
							}
							
							for (GuildPlayer gp : guild.getPlayers()) {
								OfflinePlayer player = Bukkit.getOfflinePlayer(gp.getUUID());
								if (gp.getUUID().compareTo(guild.getLeader())==0) continue;
								if (player.isOnline()) onlinePlayers++;
								String an = player.isOnline() ?  on+ player.getName() : off + player.getName();
								if (playersName.equalsIgnoreCase("")) {	
									playersName = an;
								} else {							
									playersName = playersName + "," + an;
								}
							}
							
							for (UUID u : guild.getMembers()) {
								OfflinePlayer player = Bukkit.getOfflinePlayer(u);
								if (u.compareTo(guild.getLeader())==0) continue;
								if (player.isOnline()) onlineMembers++;
								String an = player.isOnline() ?  on+ player.getName() : off + player.getName();
								if (membersName.equalsIgnoreCase("")) {	
									membersName = an;
								} else {							
									membersName = membersName + "," + an;
								}
							}
							
							
							sender.sendMessage("§aLeader: §b" + leaderName);	
							if (!adminsName.equalsIgnoreCase(""))sender.sendMessage("§aAdmins: §7("+ onlineAdmins +")§a: " + adminsName);	
							if (!modsName.equalsIgnoreCase(""))sender.sendMessage("§aMods: §7("+ onlineMods +")§a: " + modsName);	
							if (!playersName.equalsIgnoreCase(""))sender.sendMessage("§aPlayers: §7("+ onlinePlayers+")§a: " + playersName);	
							if (!membersName.equalsIgnoreCase(""))sender.sendMessage("§aMembers: §7("+ onlineMembers +")§a: " + membersName);
							sender.sendMessage("§6§l"+guild.getName() + "§7- §fList of Players");
						} else {
							sender.sendMessage(gm.getPrefix()+"§cYou need to be in a Guild to do that.");
						}
					} else if (args[0].equalsIgnoreCase("allies")) {
						sender.sendMessage("§cComing soon");
					}  else if (args[0].equalsIgnoreCase("rename")) {
						if (gm.getGuild(sp)!=null) {
							Guild guild = gm.getGuild(sp);
							if (guild.isLeader(sp)) {
								if (guild.getCoins()>=10000) {
									
									StringBuilder builder = new StringBuilder();
								    for (int i = 2; i < args.length; i++)
								    {
								      builder.append(args[i]).append(" ");
								    }
								    
								  String name = builder.toString();
								  
								  
								  if (args[1].length()!=3) {
										sender.sendMessage(gm.getPrefix()+"§cThe achronym has to be exactly 3 characters long");
										return false;
									}
									
								  
									if (name.length()<=20) {
										Guild guild1 = gm.getGuildByName(name);
										Guild guild2 = gm.getGuildByAchronym(args[1]);
										if (guild1==null && guild2==null) {
											guild.rename(name, args[1].toUpperCase());
										} else {
											sender.sendMessage(gm.getPrefix()+"§cA guild with that name or achronym already exists.");
										}
									} else {
										sender.sendMessage(gm.getPrefix()+"§cThe name of the guild can't have more than 20 characters.");
									}
									
								} else {
									sender.sendMessage(gm.getPrefix()+"§cThe Guild doesn't have enough coins to do that.");
								}
							} else {
								sender.sendMessage(gm.getPrefix()+"§cOnly the Leader of the Guild can do that.");
							}
						} else {
							sender.sendMessage(gm.getPrefix()+"§cYou need to be in a Guild to do that.");
						}
					} else if (args[0].equalsIgnoreCase("info")) {
						  Guild guild = null;
						if (args.length==1) {
							guild = gm.getGuild(sp);
							if (guild==null) {
								sender.sendMessage(gm.getPrefix()+"§cYou are not in a Guild. To see the info of another Guild do §b/guild info <Guild>");
								return false;
							}
						} else {
						StringBuilder builder = new StringBuilder();
					    for (int i = 1; i < args.length; i++)
					    {
					      builder.append(args[i]).append(" ");
					    }
					    
					  String name = builder.toString();
					  
					 guild = gm.getGuildByName(name);
					 if (guild==null) {
							sender.sendMessage(gm.getPrefix()+"§cThe Guild §b" + name + " §cdoes not exist.");
							return false;
						}
						}
						
						
						String playersName = "";
						for (GuildPlayer gp : guild.getPlayers()) {
							OfflinePlayer player = Bukkit.getOfflinePlayer(gp.getUUID());
							if (gp.getUUID().compareTo(guild.getLeader())==0) continue;
							if (playersName.equalsIgnoreCase("")) {	
								playersName = player.getName();
							} else {							
								playersName = playersName + ", " + player.getName();
							}
						}
						String staffsName = "";
						List<UUID> staff = new ArrayList<UUID>();
						staff.addAll(guild.getAdmins(true));
						staff.addAll(guild.getMods(true));
						for (UUID u : staff) {
							OfflinePlayer player = Bukkit.getOfflinePlayer(u);
							if (u.compareTo(guild.getLeader())==0) continue;
							if (staffsName.equalsIgnoreCase("")) {	
								staffsName = player.getName();
							} else {							
								staffsName = staffsName + ", " + player.getName();
							}
						}
						
						
						sender.sendMessage("");
						sender.sendMessage("");
						sender.sendMessage("");
						sender.sendMessage("§f§l-=-=-=-[§6§l"+guild.getName() + "§7- §aInfo§f§l]-=-=-=-");
						sender.sendMessage("§9Achronym: §e" + guild.getAchronym());
						SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
						sender.sendMessage("§9Foundation date: §f" + format.format(guild.getFoundationDate()) + "(" + DataManager.getManager().getDateDifference(guild.getFoundationDate()) + ")");						
						sender.sendMessage("§9Guild Level: §d" + guild.getLevel());
						sender.sendMessage("§9Guild Value: §6§l" + Utils.getUtils().getStringMoney(guild.getValue()) + " Coins");		
						sender.sendMessage("§aLeader: §b" + Bukkit.getOfflinePlayer(guild.getLeader()).getName());	
						sender.sendMessage("§aStaffs: §b"  + staffsName); 
						sender.sendMessage("§aPlayers §7(" + guild.getPlayers().size()+ "/6): §b" + playersName);
						sender.sendMessage("§aAmount of members: §b" + guild.getMembers().size());
						sender.sendMessage("§aMembership Fee: §6" + guild.getMemberFee() + " Coins");
						sender.sendMessage("§f§l-=-=-=-[§6§l"+guild.getName() + "§7- §aInfo§f§l]-=-=-=-");
						sender.sendMessage("");
						
					}else if (args[0].equalsIgnoreCase("deposit") && args.length==2) {
						if (gm.getGuild(sp)!=null) {
							Guild guild = gm.getGuild(sp);
							if (guild.isLeader(sp)) {
								
								int coins = 0;
								try {
									coins = Integer.parseInt(args[1]);
								} catch (Exception e) {
									p.sendMessage(gm.getPrefix()+"§a"+ args[1]+ " §cisnt a valid number.");
									return false;
								}
								
								if (sp.getCoins()>=coins) {
									guild.setCoins(guild.getCoins()+coins);
									guild.addEarning("LEADER DEPOSIT", coins);
									sp.removeCoins(coins);
									p.sendMessage(gm.getPrefix()+"§aYou have deposit §6§l"+ coins+ " Coins §ato the Guild!");
								} else {
									sender.sendMessage(gm.getPrefix()+"§cYou dont have enough coins to do that.");
								}
								
							} else {
								sender.sendMessage(gm.getPrefix()+"§cOnly the Leader of the Guild can do that.");
							}
						} else {
							sender.sendMessage(gm.getPrefix()+"§cYou need to be in a Guild to do that.");
						}
					}else if (args[0].equalsIgnoreCase("givecoins") && p.isOp()) {
						int salary = 0;
						try {
							salary = Integer.parseInt(args[1]);
							if (salary<1) {
								p.sendMessage(gm.getPrefix()+"§cThe amount must be greater than 1");
								return false;
							}
						} catch (Exception e) {
							p.sendMessage(gm.getPrefix()+"§a"+ args[1]+ " §cisnt a valid number.");
							return false;
						}
						
						StringBuilder builder = new StringBuilder();
					    for (int i = 2; i < args.length; i++)
					    {
					      builder.append(args[i]).append(" ");
					    }
					    
					  String name = builder.toString();
					  Guild guild = gm.getGuildByName(name);
					  if (guild!=null) {
						  guild.addEarning("TOURNAMENT PRIZE", salary);
						  guild.setCoins(guild.getCoins()+salary);
					  } else {
						  sender.sendMessage(gm.getPrefix()+"§cThe guild §b"+name+ "§c does not exist.");
					  }
					}else if (args[0].equalsIgnoreCase("removecoins") && p.isOp()) {
						int salary = 0;
						try {
							salary = Integer.parseInt(args[1]);
							if (salary<1) {
								p.sendMessage(gm.getPrefix()+"§cThe amount must be greater than 1");
								return false;
							}
						} catch (Exception e) {
							p.sendMessage(gm.getPrefix()+"§a"+ args[1]+ " §cisnt a valid number.");
							return false;
						}
						
						StringBuilder builder = new StringBuilder();
					    for (int i = 2; i < args.length; i++)
					    {
					      builder.append(args[i]).append(" ");
					    }
					    
					  String name = builder.toString();
					  Guild guild = gm.getGuildByName(name);
					  if (guild!=null) {
						  guild.addLoss("PENALTY FEE", salary);
						  guild.setCoins(guild.getCoins()-salary);
					  } else {
						  sender.sendMessage(gm.getPrefix()+"§cThe guild §b"+name+ "§c does not exist.");
					  }
					}  else {
						sendHelp(sp);
					}
						
				
				}
		}
			

}
		
		
		return false;
	}
	
	private void sendHelp(SpleefPlayer sp) {
		TextComponent msg9 = new TextComponent("§a/guild create <acronym> <name>");
		msg9.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6Create a Guild. (25000 Coins). Acronmy has to have 3 letters. If you have a rank, you have 50% OFF creating the guild.").create()));
		msg9.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/guild create <acronym> <name>"));
		sp.getPlayer().spigot().sendMessage(msg9);

		TextComponent msg18 = new TextComponent("§a/guild info <Guild>");
		msg18.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6Invite a player to your Guild. You will have to pay daily a salary to the player. "
				+ "if Another guild wants to buy this player they would need to pay the §bPlayer Value, which is Salary x 50").create()));
		msg18.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/guild info <Guild>"));
		sp.getPlayer().spigot().sendMessage(msg18);
		
		
		TextComponent msg1 = new TextComponent("§a/guild add <player> <salary>");
		msg1.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6Add a player to your Guild. You will need to pay him a salary daily, that will set their Player Value").create()));
		msg1.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/guild add <Player> <Salary>"));
		sp.getPlayer().spigot().sendMessage(msg1);
		
		TextComponent msg20 = new TextComponent("§a/guild buy <player> <salary>");
		msg20.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6Buy a player from another Guild. You will have to pay the Guild 100% of the Player Value to make him join your Guild.").create()));
		msg20.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/guild add <Player> <Salary>"));
		sp.getPlayer().spigot().sendMessage(msg20);
		
		TextComponent msg2 = new TextComponent("§a/guild fire <player>");
		msg2.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6Fire a player from your Guild. You will have to pay them 50% of §aPlayer Value §6to the player.")
			.create()));
		msg2.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/guild fire <player>"));
		sp.getPlayer().spigot().sendMessage(msg2);
		
		
		TextComponent msg19 = new TextComponent("§a/guild deposit <coins>");
		msg19.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6Deposit coins to your Guild. Remember that you wont be able to withdraw them.")
			.create()));
		msg19.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/guild deposit <player>"));
		sp.getPlayer().spigot().sendMessage(msg19);
		
		
		TextComponent msg6 = new TextComponent("§a/guild resign");
		msg6.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6Leave your Guild. You will need to pay 50% of your Player Value to become a free agent again.").create()));
		msg6.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/guild resign"));
		sp.getPlayer().spigot().sendMessage(msg6);

		
		TextComponent msg21 = new TextComponent("§a/guild promote <Player>");
		msg21.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6Promote a player or member of your Guild to help you with the Guild.").create()));
		msg21.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/guild promote <Player>"));
		sp.getPlayer().spigot().sendMessage(msg21);
		
		TextComponent msg22 = new TextComponent("§a/guild demote <Player>");
		msg22.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6Demote a player or member of your Guild to their previous rank.").create()));
		msg22.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/guild demote <Player>"));
		sp.getPlayer().spigot().sendMessage(msg22);
		
		TextComponent msg12 = new TextComponent("§a/guild disband");
		msg12.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6Disband your Guild. You will have to pay your players 50% of their value.").create()));
		msg12.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/guild disband"));
		sp.getPlayer().spigot().sendMessage(msg12);
		
		TextComponent msg7 = new TextComponent("§a/guild transferable");
		msg7.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6Put yourself as a transferable player. Your value and salary will be reduced to 50% allowing other Guilds to buy you easier.").create()));
		sp.getPlayer().spigot().sendMessage(msg7);
		
		TextComponent msg3 = new TextComponent("§a/guild renegotiate <player>");
		msg3.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6Send a request to a player of your Guild to change their Salary and their value").create()));
		sp.getPlayer().spigot().sendMessage(msg3);
		
		
		TextComponent msg14 = new TextComponent("§a/guild invite <member>");
		msg14.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6Invite a member to your guild. Them will need to pay a suscription per day.").create()));
		sp.getPlayer().spigot().sendMessage(msg14);
		
		TextComponent msg17 = new TextComponent("§a/guild join <Guild>");
		msg17.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6Join a Guild as a member. You will have to pay a suscription per day.").create()));
		sp.getPlayer().spigot().sendMessage(msg17);
		
		TextComponent msg15 = new TextComponent("§a/guild kick <member>");
		msg15.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6Kick a member from your guild.").create()));
		sp.getPlayer().spigot().sendMessage(msg15);
		
		TextComponent msg16 = new TextComponent("§a/guild ban <member>");
		msg16.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6Ban/unban a member from your guild.").create()));
		sp.getPlayer().spigot().sendMessage(msg16);
		
		TextComponent msg4 = new TextComponent("§a/gchat");
		msg4.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6Chat with the players of your Guild").create()));
		sp.getPlayer().spigot().sendMessage(msg4);
		
		TextComponent msg13 = new TextComponent("§a/guild menu");
		msg13.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6Open guild menu").create()));
		sp.getPlayer().spigot().sendMessage(msg13);
		
		TextComponent msg11 = new TextComponent("§a/guild list");
		msg11.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6Shows all players from the Guild").create()));
		sp.getPlayer().spigot().sendMessage(msg11);
		
		TextComponent msg5 = new TextComponent("§a/guild allies");
		msg5.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6See alliied Guilds of your guild").create()));
		sp.getPlayer().spigot().sendMessage(msg5);
		
		TextComponent msg8 = new TextComponent("§a/guild rename <acronym> <name>");
		msg8.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6Change the name of your Guild. (1000 gCoins)").create()));
		sp.getPlayer().spigot().sendMessage(msg8);
						
		
	}

	
}