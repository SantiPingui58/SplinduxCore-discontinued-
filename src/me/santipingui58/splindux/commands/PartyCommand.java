package me.santipingui58.splindux.commands;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.gui.party.PartyMenu;
import me.santipingui58.splindux.gui.party.PartySelectDuelSpleefTypeMenu;
import me.santipingui58.splindux.relationships.friends.FriendsManager;
import me.santipingui58.splindux.relationships.friends.Friendship;
import me.santipingui58.splindux.relationships.parties.Party;
import me.santipingui58.splindux.relationships.parties.PartyManager;
import me.santipingui58.splindux.relationships.parties.PartyMode;
import me.santipingui58.splindux.utils.Utils;



public class PartyCommand implements CommandExecutor {

	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
		} else {
	
		if(cmd.getName().equalsIgnoreCase("party")){
			boolean b = false;
			if (!b) return true;
			final Player p = (Player) sender;
			 SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
				PartyManager pm = PartyManager.getManager();
				Party party = pm.getParty(p);
				if (args.length==0) {					
					if (party==null) {
						sendHelp(sp);
					} else {
						new PartyMenu(sp, pm.getParty(p).getPartyMode()).o(sp.getPlayer());
					}
				} else {
			
					
					if (args[0].equalsIgnoreCase("create")) {
						if (party==null) {
						new PartyMenu(sp, null).o(sp.getPlayer());
						} else {
							sender.sendMessage("§cYou already created a party.");
						}
					} else if (args[0].equalsIgnoreCase("invite") && args.length>=2) {
				
						if (party==null) {
							p.sendMessage("§cYou need to be in a party to use this command. Create one with §b/party create");
						} else {
							
							StringBuilder builder = new StringBuilder();
						    for (int i = 1; i < args.length; i++)
						    {
						      builder.append(args[i]).append(" ");
						    }
						    
						  String message = builder.toString();
						  List<String> list = new ArrayList<String>(Arrays.asList(message.split(" ")));
							List<SpleefPlayer> sp2 = new ArrayList<SpleefPlayer>();
							List<String> players = new ArrayList<String>();
						  for (String s : list) {
							  Player op = Bukkit.getPlayer(s);
							  if (Bukkit.getOnlinePlayers().contains(op)) {
								  if (!op.equals(p)) {
									  players.add(op.getName());
										 SpleefPlayer dueled = SpleefPlayer.getSpleefPlayer(op);
											  sp2.add(dueled);
								  } else {
									  sender.sendMessage("§cYou cant invite yourself...");
									  return false;
								  }
								 
							  } else {
								  sender.sendMessage("§cThe player §b" + s + "§c does not exist or is not online.");
								  return false;
							  }
						  }
						  
						  if (Utils.getUtils().hasDuplicate(players)) {
								sender.sendMessage("§cYou can only invite the same player once...");
								return false;
							}
						  
						  party.invite(sp2);
						  
						  
						}
					} else if (args[0].equalsIgnoreCase("join") && args.length==2) {
						if (party==null) {
							Player player = Bukkit.getPlayer(args[1]);
							if (Bukkit.getOnlinePlayers().contains(player)) {
								Party joinParty = pm.getParty(player);
								if (joinParty!=null && joinParty.isLeader(player)) {
									if (!joinParty.getMembers().contains(p.getUniqueId())) {
										SpleefPlayer splayer = SpleefPlayer.getSpleefPlayer(player);
										Friendship friendship = FriendsManager.getManager().getFriendship(sp.getUUID(),splayer.getUUID());
										boolean canJoin = joinParty.getPartyMode().equals(PartyMode.PUBLIC) || 
												(joinParty.getPartyMode().equals(PartyMode.PRIVATE) && joinParty.getInvited().contains(p.getUniqueId())) ||
												(joinParty.getPartyMode().equals(PartyMode.ONLY_FRIENDS) && friendship!=null);
										if (canJoin) {
											joinParty.join(p);
										} else {
											sender.sendMessage("§cThis party is private or you are not allowed to join it.");
										}
									} else {
										sender.sendMessage("§cYou already joined this party.");
									}
								} else {
									 sender.sendMessage("§cThe player §b" + args[1] + "§c is not in a party or is not the leader of it.");
								}
							} else {
								 sender.sendMessage("§cThe player §b" + args[1] + "§c does not exist or is not online.");
							}
						} else {
							sender.sendMessage("§cYou are already in a party. To leave your current party use §b/party leave");
						}
					}else if (args[0].equalsIgnoreCase("leave")) {
						if (party!=null) {
							if (!party.isLeader(p)) {
							party.leave(p);
							} else {
								sender.sendMessage("§cYou cannot leave your own party. Use §b/party disband §cor §b/party transfer <Player>");
							}
						} else {
							sender.sendMessage("§cYou are not in a party.");
						}
					}else if (args[0].equalsIgnoreCase("kick") && args.length==2) {
						if (party!=null) {
							if (party.isLeader(p)) {
								Player player = Bukkit.getPlayer(args[1]);
								if (Bukkit.getOnlinePlayers().contains(player)) {
									if (party.getMembers().contains(player.getUniqueId())) {
										party.kick(player);
									} else {
										 sender.sendMessage("§cThe player §b" + args[1] + "§c is not in your party.");
									}
								} else {
									 sender.sendMessage("§cThe player §b" + args[1] + "§c does not exist or is not online.");
								}
							} else {
								sender.sendMessage("§cOnly the leader of the party can do that.");
							}
						} else {
							sender.sendMessage("§cYou are not in a party.");
						}
					}else if (args[0].equalsIgnoreCase("warp")) {
						if (party!=null) {
							if (party.isLeader(p)) {
								party.partyMessage("§aThe leader of the party has warped all players to their game!");
								party.warp(null);
							} else {
								sender.sendMessage("§cOnly the leader of the party can do that.");
							}
						} else {
							sender.sendMessage("§cYou are not in a party.");
						}
					} else if (args[0].equalsIgnoreCase("disband")) {
						if (party!=null) {
							if (party.isLeader(p)) {
								party.disband();
							} else {
								sender.sendMessage("§cOnly the leader of the party can do that.");
							}
						} else {
							sender.sendMessage("§cYou are not in a party.");
						}
					} else if (args[0].equalsIgnoreCase("list")) {
						if (party!=null) {
							String playersName = "";
							for (UUID  gp : party.getMembers()) {
								OfflinePlayer player = Bukkit.getOfflinePlayer(gp);
								String name = player.isOnline() ? "§a" + player.getName() : "§c" + player.getName();
								if (gp.compareTo(party.getLeader())==0) continue;
								if (playersName.equalsIgnoreCase("")) {	
									playersName = name;
								} else {							
									playersName = playersName + ", " + name;
								}
							}
							p.sendMessage("§6-=-=-=-[§5§lParty§6]-=-=-=-");
							sender.sendMessage("§aLeader: §b" + Bukkit.getOfflinePlayer(party.getLeader()).getName());
							sender.sendMessage("§aPlayers §7(" + party.getMembers().size()+ "/"+party.getMaxSize()+"): §b" + playersName);
							p.sendMessage("§6-=-=-=-[§5§lParty§6]-=-=-=-");
						} else {
							sender.sendMessage("§cYou are not in a party.");
						}
					} else if (args[0].equalsIgnoreCase("duels")) {
						if (party!=null) {
							if (party.isLeader(p)) {
								int size = 0;
								try {
									size = Integer.parseInt(args[0]);
									if (size<=party.getAllMembers().size()/2) {
										new PartySelectDuelSpleefTypeMenu(sp, size).o(p);
									} else {
										p.sendMessage("§cNot enough players at party to do a " + size +"VS"+size);
										return false;
									}
								} catch (Exception e) {
									p.sendMessage("§a"+ args[0]+ " §cisnt a valid number.");
									return false;
								}
								
							} else {
								sender.sendMessage("§cOnly the leader of the party can do that.");
							}
						} else {
							sender.sendMessage("§cYou are not in a party.");
						}
					} else if (args[0].equalsIgnoreCase("transfer") && args.length==2) {
						if (party!=null) {
							if (party.isLeader(p)) {
								Player player = Bukkit.getPlayer(args[1]);
								if (Bukkit.getOnlinePlayers().contains(player)) {
									if (party.getMembers().contains(player.getUniqueId()))  { 
										party.transferLeadership(player);
									} else {
										sender.sendMessage("§cThe player §b" + args[1] + "§c is not in your party.");
									}
								} else {
									 sender.sendMessage("§cThe player §b" + args[1] + "§c does not exist or is not online.");
								}
							} else {
								sender.sendMessage("§cOnly the leader of the party can do that.");
							}
						} else {
							sender.sendMessage("§cYou are not in a party.");
						}
					}else {
						sendHelp(sp);
					}
				}
		}

}
		
		
		return false;
	}
	
	private void sendHelp(SpleefPlayer sp) {
		sp.sendMessage("§5/party create §7- §aCreates a party.");
		sp.sendMessage("§5/party invite <Player> §7- §aInvites a player to your party.");
		sp.sendMessage("§5/party join <Player> §7- §aJoin the party of a player. ");
		sp.sendMessage("§5/party leave §7- §aLeave your current party.");
		sp.sendMessage("§5/party kick <Player> §7- §aKick a player from your party.");
		sp.sendMessage("§5/party help - §aDisplays this help.");
		sp.sendMessage("§5/party warp §7- §aTeleport all members of your party to your current game.");
		sp.sendMessage("§5/party disband §7- §aDisbands the party.");
		sp.sendMessage("§5/party transfer <Player> §7- §aTransfer the leadership of your party to another player.");
		sp.sendMessage("§5/pchat §7- §aParty chat.");

		
	}

	
}