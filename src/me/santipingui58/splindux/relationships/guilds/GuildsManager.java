package me.santipingui58.splindux.relationships.guilds;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.hikari.HikariAPI;
import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.spleef.Arena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.gui.game.guild.duel.GuildAcceptDuelMenu;
import me.santipingui58.splindux.relationships.RelationshipRequest;
import me.santipingui58.splindux.relationships.RelationshipRequestType;
import me.santipingui58.splindux.stats.StatsManager;
import me.santipingui58.splindux.stats.ranking.RankingManager;

public class GuildsManager {

	private static GuildsManager manager;	
	 public static GuildsManager getManager() {
	        if (manager == null)
	        	manager = new GuildsManager();
	        return manager;
	    }
	 
	 
	 
	 private List<Guild> guilds = new ArrayList<Guild>();
	 private List<RelationshipRequest> join_players_requests = new ArrayList<RelationshipRequest>();	 
	 private List<RelationshipRequest> join_members_requests = new ArrayList<RelationshipRequest>();
	 private List<RelationshipRequest> renegociate_requests = new ArrayList<RelationshipRequest>();
	 private List<RelationshipRequest> duel_requests = new ArrayList<RelationshipRequest>();
	 private List<RelationshipRequest> buy_player_requests = new ArrayList<RelationshipRequest>();
	 private List<GuildDuel> guildDuels = new ArrayList<GuildDuel>();	 	 
	 public List<RelationshipRequest> getJoinGuildPlayersRequests() {
		 return this.join_players_requests;
	 }
	 
	 public List<RelationshipRequest> getJoinGuildMembersRequests() {
		 return this.join_members_requests;
	 }
	 
	 public List<RelationshipRequest> getRenegociateRequests() {
		 return this.renegociate_requests;
	 }
	 
	 public List<RelationshipRequest> getDuelRequests() {
		 return this.duel_requests;
	 }
	 
	 public List<RelationshipRequest> getBuyPlayerRequests() {
		 return this.buy_player_requests;
	 }
	 
	 public List<GuildDuel> getGuildDuels() {
		 return this.guildDuels;
	 }
	 
	 public GuildDuel getGuildDuelByCreator(UUID creator) {
		 for (GuildDuel duels : this.guildDuels) {
			 if (duels.getCreator().equals(creator)) return duels;
		 }
		return null;
		 
	 }
	 
		public GuildDuel getGuildDuelBySender(Guild guild) {
			for (GuildDuel duels : this.guildDuels) {
				 if (duels.getGuild().equals(guild)) return duels;
			 }
			return null;
		}

	 
	 
	 public GuildDuel getGuildDuelByReceiver(UUID receiver) {
		 for (GuildDuel duels : this.guildDuels) {
			 if (duels.getReceiver().equals(receiver)) return duels;
		 }
		return null;
		 
	 }
	 
	 public List<RelationshipRequest> getRequestsByType(RelationshipRequestType type) {
		 switch (type) {
		case GUILD_DUEL: return this.duel_requests;
		case JOIN_GUILD_AS_MEMBER:
			return this.join_members_requests;
		case JOIN_GUILD_AS_PLAYER:
			return this.join_players_requests;
		case RENEGOCIATE_GUILD:
			return this.renegociate_requests;
		case BUY_PLAYER:
			return this.buy_player_requests;
		case FRIENDS:
			break;		 
		 }
		return null;
	 }
	 
	 public RelationshipRequest getRequest(RelationshipRequestType type,UUID sp1, UUID sp2) {
		 
		 for (RelationshipRequest fr: getRequestsByType(type)) {
				if ((fr.getReceptor().contains(sp2) && fr.getSender().contains(sp1)) || (fr.getReceptor().contains(sp1) && fr.getSender().contains(sp2))) {
					return fr;
				}
			}
			return null;
	 }
	 

	 public List<Guild> getGuilds() {
		 return this.guilds;
	 }
	 
	 
	 public Guild getGuild(SpleefPlayer sp) {
		 for (Guild guild : this.guilds) {
			 
			 if (guild.getAllMembers().contains(sp.getUUID())) {
				 return guild;
			 }
		 }
		 return null;
	 }
	 
	 public Guild getGuild(UUID uuid) {
		 for (Guild guild : this.guilds) {
			
			 if (guild.getAllMembers().contains(uuid)) {
				 return guild;
			 }
		 }
		 return null;
	 }

	public Guild getGuildByAchronym(String achronym) {
		 for (Guild guild : this.guilds) {
			 if (guild.getAchronym().equalsIgnoreCase(achronym)) {
				 return guild;
			 }
		 }
		 return null;
	}
	
	
	public Guild getGuildByName(String name) {
		 for (Guild guild : this.guilds) {
			 if (guild.getName().equalsIgnoreCase(name)) {
				 return guild;
			 }
		 }
		 return null;
	}

	public void saveGuilds() {
		FileConfiguration config = Main.guilds.getConfig();
		if (getGuilds().size()==0) return;
		
		for (Guild guild : getGuilds()) {
			UUID u = guild.getUUID();
			config.set("guilds."+u+".name", guild.getName());
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
			String date =  format.format(guild.getFoundationDate());	
			config.set("guilds."+u+".foundation_date", date);
			config.set("guilds."+u+".leader", guild.getLeader().toString());
			config.set("guilds."+u+".leaderName", Bukkit.getOfflinePlayer(guild.getLeader()).getName());
			config.set("guilds."+u+".achronym", guild.getAchronym());
			config.set("guilds."+u+".coins", guild.getCoins());
			config.set("guilds."+u+".level", guild.getLevel());
			
			config.set("guilds."+u+".players",null);
			for (GuildPlayer gp : guild.getPlayers()) {
				config.set("guilds."+u+".players."+gp.getUUID().toString(), gp.getSalary());
			}
			
			List<String> members = new ArrayList<String>();
			List<String> admins = new ArrayList<String>();
			List<String> mods = new ArrayList<String>();
			List<String> banned = new ArrayList<String>();
			List<String> transferable = new ArrayList<String>();
			
			guild.getMembers().forEach((p) -> members.add(p.toString()));
			guild.getAdmins(true).forEach((p) -> admins.add(p.toString()));
			guild.getMods(true).forEach((p) -> mods.add(p.toString()));
			guild.getBannedPlayers().forEach((p) -> banned.add(p.toString()));
			guild.getTransferablePlayers().forEach((p) -> transferable.add(p.toString()));
			
			config.set("guilds."+u+".members", members);
			config.set("guilds."+u+".admins", admins);
			config.set("guilds."+u+".mods", mods);
			config.set("guilds."+u+".banned", guild.getBannedPlayers());
			config.set("guilds."+u+".transferable", guild.getTransferablePlayers());
		}
		Main.guilds.saveConfig();
	}
	
	
	public void loadGuilds() {
		FileConfiguration config = Main.guilds.getConfig();
		 if (Main.guilds.getConfig().contains("guilds")) {
			 Set<String> uuids = Main.guilds.getConfig().getConfigurationSection("guilds").getKeys(false);
			 for (String u : uuids) {
				 UUID uuid = UUID.fromString(u);
				 String name = config.getString("guilds."+u+".name");
				Date foundation_date;
				try {
					foundation_date = new SimpleDateFormat("dd-MM-yyyy").parse(config.getString("guilds."+u+".foundation_date"));
				} catch (ParseException e) {
					foundation_date = new Date();
				}
				
				 UUID leader = UUID.fromString(config.getString("guilds."+u+".leader"));
				 String achronym = config.getString("guilds."+u+".achronym");
				 int coins = config.getInt("guilds."+u+".coins");
				 int level = config.getInt("guilds."+u+".level");
				 
				 List<GuildPlayer> players = new ArrayList<GuildPlayer>();
				 List<UUID> members = new ArrayList<UUID>();
				 List<UUID> transferables = new ArrayList<UUID>();
				 List<UUID> banned = new ArrayList<UUID>();
				 List<UUID> mods = new ArrayList<UUID>();
				 List<UUID> admins = new ArrayList<UUID>();
				 
				 if (config.contains("guilds."+u+".players")) {
				 for (String s :  Main.guilds.getConfig().getConfigurationSection("guilds."+u+".players").getKeys(false)) {
					 GuildPlayer gp = new GuildPlayer(UUID.fromString(s), config.getInt("guilds."+u+".players."+s));
					 players.add(gp);				 
				 }
				 }
				 for (String s : config.getStringList("guilds."+u+".members")) members.add(UUID.fromString(s));
				 for (String s : config.getStringList("guilds."+u+".transferables")) transferables.add(UUID.fromString(s));
				 for (String s : config.getStringList("guilds."+u+".banned")) banned.add(UUID.fromString(s));
				 for (String s : config.getStringList("guilds."+u+".mods")) mods.add(UUID.fromString(s));
				 for (String s : config.getStringList("guilds."+u+".admins")) admins.add(UUID.fromString(s));
				 
				 Guild guild = new Guild(uuid,name,achronym,coins,leader,players,members,level,transferables,banned,mods,admins,foundation_date);
				 for (GuildPlayer gp : guild.getPlayers()) gp.setGuild(guild);
				 getGuilds().add(guild);
			 }
			 }
		 
	}

	public void foundGuild(String name, String achronym, int coins,SpleefPlayer leader) {
		Guild guild = new Guild(name, achronym,leader.getUUID(),12500);//coins);
		getGuilds().add(guild);
		leader.sendMessage("§aGuild founded!");
		guild.addEarning("GUILD FOUND", coins);
		leader.removeCoins(coins);
		GuildsManager.getManager().guildLog(":arrow_up: **"+leader.getOfflinePlayer().getName()+"** has founded the guild **"+name.toUpperCase()+"** :arrow_up:");
	}

	@SuppressWarnings("deprecation")
	public void disbandGuild(Guild guild) {
		
		Date date = guild.getFoundationDate();
		Date now = new Date();
		String month1 = getMonth(date.getMonth());
		String month2 = getMonth(now.getMonth());
		guildLog(":x:The guild **"+ guild.getName().toUpperCase()+"** has been disbanded! All players are now free agents again. ("+month1+"/"+date.getYear()+" - "+month2+"/"+now.getYear()+" :x:");		
		getGuilds().remove(guild); 
		Main.guilds.getConfig().set("guilds."+guild.getUUID().toString(), null);
		Main.guilds.saveConfig();
		for (GuildPlayer gp : guild.getPlayers()) {
			OfflinePlayer pa = Bukkit.getOfflinePlayer(gp.getUUID());
			SpleefPlayer temp = SpleefPlayer.getSpleefPlayer(pa);
			if (temp==null) {
				 new SpleefPlayer(pa.getUniqueId());
				HikariAPI.getManager().loadData(pa.getUniqueId());
			}
				new BukkitRunnable() {
					public void run() {		
					SpleefPlayer splayer = SpleefPlayer.getSpleefPlayer(pa);
					splayer.addCoins(gp.getValue()/2);				
			}
					}.runTaskLaterAsynchronously(Main.get(), 3L);
			
		}
		
		
	}
	
	
	public void payPlayers() {
		new BukkitRunnable() {
			public void run() {
		for (Guild guild : getGuilds()) {
			
			for (GuildPlayer gp : guild.getPlayers()) {
				if (guild.getCoins()>=gp.getSalary()) {
					
					if (gp.getSalary()*1.2<gp.getMinSalary()) {
						renegociate(guild.getLeader(),gp.getUUID(), gp.getMinSalary());
					}
					
					
				OfflinePlayer pa = Bukkit.getOfflinePlayer(gp.getUUID());
				SpleefPlayer temp = SpleefPlayer.getSpleefPlayer(pa);
				if (temp==null) {
					 new SpleefPlayer(pa.getUniqueId());
					HikariAPI.getManager().loadData(pa.getUniqueId());
				}
					new BukkitRunnable() {
					
						public void run() {		
							SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(pa);
							sp.addCoins(gp.getSalary());
							guild.setCoins(guild.getCoins()-gp.getSalary());
				}
						}.runTaskLaterAsynchronously(Main.get(), 3L);
				
			} else {
				 bankruptcy(guild);
				 break;
			}
				
			}
			guild.addLoss("PLAYERS PAYMENT", guild.getDailyPlayersSalary());
			OfflinePlayer pa = Bukkit.getOfflinePlayer(guild.getLeader());
			SpleefPlayer temp = SpleefPlayer.getSpleefPlayer(pa);
			if (temp==null) {
				 new SpleefPlayer(pa.getUniqueId());
				HikariAPI.getManager().loadData(pa.getUniqueId());
			}
				new BukkitRunnable() {
					public void run() {		
						int salary = guild.getCoins()/100;
						SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(pa);
						sp.addCoins(salary);
						guild.setCoins(guild.getCoins()-salary);
						guild.addLoss("LEADER REVENUE",salary);
			}
					}.runTaskLaterAsynchronously(Main.get(), 3L);
			
		}
		
			}
	}.runTaskAsynchronously(Main.get());

	}

	
	public void payFee() {
		new BukkitRunnable() {
			public void run() {
		for (Guild guild : getGuilds()) {
			for (UUID u : guild.getMembers()) {
				OfflinePlayer pa = Bukkit.getOfflinePlayer(u);
				SpleefPlayer temp = SpleefPlayer.getSpleefPlayer(pa);
				if (temp==null) {
					 new SpleefPlayer(pa.getUniqueId());
					HikariAPI.getManager().loadData(pa.getUniqueId());
				}
					new BukkitRunnable() {
						public void run() {		
							SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(pa);
							if (sp.getCoins()>=guild.getCoins()) {
							sp.removeCoins(guild.getMemberFee());
							guild.setCoins(guild.getCoins()+guild.getMemberFee());
							} else {
								guild.getMembers().remove(u);
								guild.broadcast("§7The member §c" + pa.getName() + " §7has been kicked from the Guild due to not being able to pay the daily fee.");
							}
				}
						}.runTaskLaterAsynchronously(Main.get(), 5L);
				
			
			}
			
			guild.addEarning("FEE PAYMENT", guild.getMemberFee()*guild.getMembers().size());
		}
			}
	}.runTaskAsynchronously(Main.get());
	}
	
	private String getMonth(int i) {
		switch (i) {
		case 0: return "Jan";
		case 1: return "Feb";
		case 2: return "March";
		case 3: return "Apr";
		case 4: return "May";
		case 5: return "Jun";
		case 6: return "Jul";
		case 7: return "Aug";
		case 8: return "Sept";
		case 9: return "Oct";
		case 10: return "Nov";
		case 11: return "Dec";		
		}
		return "";
	}
	
	@SuppressWarnings("deprecation")
	public void bankruptcy(Guild guild) {
		Date date = guild.getFoundationDate();
		Date now = new Date();
		String month1 = getMonth(date.getMonth());
		String month2 = getMonth(now.getMonth());
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		String year1 = format.format(date);
		String year2 = format.format(now);
		guildLog(":x:The guild **"+ guild.getName().toUpperCase()+"** went to bankruptcy! All players are now free agents again. ("+month1+"/"+year1+" - "+month2+"/"+year2+" :x:");
		GuildsManager.getManager().getGuilds().remove(guild);
		OfflinePlayer pa = Bukkit.getOfflinePlayer(guild.getLeader());
		SpleefPlayer temp = SpleefPlayer.getSpleefPlayer(pa);
		if (temp==null) {
			 new SpleefPlayer(pa.getUniqueId());
			HikariAPI.getManager().loadData(pa.getUniqueId());
		}
			new BukkitRunnable() {
				public void run() {		
					SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(pa);
					if (sp.getCoins()>=guild.getAllPlayersValue()/4) {
						for (GuildPlayer gp : guild.getPlayers()) {
					sp.removeCoins(gp.getValue()/4);
					OfflinePlayer pa2 = Bukkit.getOfflinePlayer(guild.getLeader());
					SpleefPlayer temp2 = SpleefPlayer.getSpleefPlayer(pa2);
					if (temp2==null) {
						 new SpleefPlayer(pa2.getUniqueId());
						HikariAPI.getManager().loadData(pa2.getUniqueId());
					}
					
					new BukkitRunnable() {
						public void run() {	
							SpleefPlayer sp2 = SpleefPlayer.getSpleefPlayer(pa2);
							sp2.addCoins(gp.getValue()/4);
						}
						
						}.runTaskLater(Main.get(), 5L);
						}
					} else {
						sp.removeCoins(sp.getCoins());
					}
		}
				}.runTaskLaterAsynchronously(Main.get(), 5L);
	}
	 
	
	public String getPrefix() {
		return "§e§lSplin§b§lDux§6§lGuilds ";
	}

	public void joinGuild(UUID leader, SpleefPlayer player,int salary) {		
		Guild guild = GuildsManager.getManager().getGuild(leader);
		
		if (guild.getMembers().contains(player.getUUID())) guild.getMembers().remove(player.getUUID());
		 guild.buyPlayer(player,null,salary);
		guild.broadcast("§aThe Guild has bought the player §b" + player.getOfflinePlayer().getName()+"§a!");	
		
	}
	
	public void buyPlayer(UUID leader, SpleefPlayer player,int salary) {		
		Guild guild = GuildsManager.getManager().getGuild(leader);
		Guild otherGuild = GuildsManager.getManager().getGuild(player);	
		guild.buyPlayer(player,otherGuild,salary);
		guild.broadcast("§aThe Guild has bought the player §b" + player.getOfflinePlayer().getName()+"§a!");	
		
	}

	public List<Guild> getDueleableGuilds() {
		List<Guild> guilds = new ArrayList<Guild>();
		
		for (Guild guild : this.guilds) {
			/*if (guild.isAbleToDuel())*/ guilds.add(guild);
		}

		return guilds;
		
	}

	public void startDuel(GuildDuel duel) {
	
		RelationshipRequest request = GuildsManager.getManager().getRequest(RelationshipRequestType.GUILD_DUEL,duel.getReceiver(), duel.getCreator());
		GuildsManager.getManager().getDuelRequests().remove(request);
		
		
		for (UUID u : duel.getPlayers1()) {
			
			
			
			if (!Bukkit.getOfflinePlayer(u).isOnline() && !SpleefPlayer.getSpleefPlayer(u).isInGame()) {
				for (UUID uu : duel.getGuild().getMods(false)) {
					OfflinePlayer p = Bukkit.getOfflinePlayer(uu);
					if (p.isOnline()) p.getPlayer().sendMessage(GuildsManager.getManager().getPrefix()+"§cThe player §b" + Bukkit.getOfflinePlayer(u).getName() + "§c is not able to play, duel cancelled.");
				}
				for (UUID uu : duel.getDuelGuild().getMods(false) ) {
					OfflinePlayer p = Bukkit.getOfflinePlayer(uu);
					if (p.isOnline()) p.getPlayer().sendMessage(GuildsManager.getManager().getPrefix()+"§cThe player §b" + Bukkit.getOfflinePlayer(u).getName() + "§c is not able to play, duel cancelled.");
				}
				return;
			}
		}
		
		for (UUID u : duel.getPlayers2()) {
			if (!Bukkit.getOfflinePlayer(u).isOnline() && !SpleefPlayer.getSpleefPlayer(u).isInGame()) {
				for (UUID uu : duel.getGuild().getMods(false)) {
					OfflinePlayer p = Bukkit.getOfflinePlayer(uu);
					if (p.isOnline()) p.getPlayer().sendMessage(GuildsManager.getManager().getPrefix()+"§cThe player §b" + Bukkit.getOfflinePlayer(u).getName() + "§c is not able to play, duel cancelled.");
				}
				for (UUID uu : duel.getDuelGuild().getMods(false)) {
					OfflinePlayer p = Bukkit.getOfflinePlayer(uu);
					if (p.isOnline()) p.getPlayer().sendMessage(GuildsManager.getManager().getPrefix()+"§cThe player §b" + Bukkit.getOfflinePlayer(u).getName() + "§c is not able to play, duel cancelled.");
				}
				return;
			}
		}
		
		
		List<SpleefPlayer> players1 = new ArrayList<SpleefPlayer>();
		SpleefPlayer splayer1 = SpleefPlayer.getSpleefPlayer(duel.getPlayers2().get(0));
		players1.add(splayer1);		
		
		List<SpleefPlayer> players2 = new ArrayList<SpleefPlayer>();
		SpleefPlayer splayer2 = SpleefPlayer.getSpleefPlayer(duel.getPlayers1().get(2));
		SpleefPlayer splayer3 = SpleefPlayer.getSpleefPlayer(duel.getPlayers2().get(1));
		SpleefPlayer splayer4 = SpleefPlayer.getSpleefPlayer(duel.getPlayers2().get(2));
		players2.add(splayer2);	
		players2.add(splayer3);		
		players2.add(splayer4);		
		
	
		
		List<SpleefPlayer> players3 = new ArrayList<SpleefPlayer>();
		SpleefPlayer splayer5 = SpleefPlayer.getSpleefPlayer(duel.getPlayers2().get(3));
		players3.add(splayer5);		
		
	DataManager.getManager().broadcast(GameManager.getManager().getGuildDuelPrefix(duel.getType())+ "§bA Guild Duel between §6§l" + duel.getGuild().getName() + " §band §6§l" + duel.getDuelGuild().getName() + " §bhas started !", true);
	
	new BukkitRunnable() {
		public void run() {
	Arena a1 = GameManager.getManager().duelGame(SpleefPlayer.getSpleefPlayer(duel.getPlayers1().get(0)), players1, null, duel.getType(), 1, false,true,-1);
	Arena a2 =GameManager.getManager().duelGame(SpleefPlayer.getSpleefPlayer(duel.getPlayers1().get(1)), players2, null, duel.getType(), 2, false,true,-1);
	Arena a3 =GameManager.getManager().duelGame(SpleefPlayer.getSpleefPlayer(duel.getPlayers1().get(3)), players3, null, duel.getType(), 1, false,true,-1);
	 
	duel.getArenas().add(a1);
	duel.getArenas().add(a2);
	duel.getArenas().add(a3);
		}
	}.runTaskAsynchronously(Main.get());
	
	}

	public void acceptDuel(SpleefPlayer sp,GuildDuel duel) {
		duel.setReceiver(sp.getUUID());
		new GuildAcceptDuelMenu(duel,1).o(sp.getPlayer());
	}

	public GuildDuel getGuildDuelByArena(Arena arena) {
		for (GuildDuel duel : this.guildDuels) {
			if (duel.getArenas().contains(arena)) return duel;
		}
		return null;
	}

	public void guildLog(String string) {
		new BukkitRunnable() {
			public void run() {
		Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "discord broadcast #guilds-log "+ string);
			}
		}.runTask(Main.get());	
	}

	public void pay() {
		payFee();
		payPlayers();	
	}


	public void joinGuildAsMember(UUID leader, UUID player) {
		Guild guild = GuildsManager.getManager().getGuild(leader);
		if (guild.getMembers().size()<=guild.getMaxMembers()) {
			guild.getMembers().add(player);
		}
		
		guild.broadcast("§a"+Bukkit.getOfflinePlayer(player).getName()+" §7Has joined the guild as a member!");
	}

	public void renegociate(UUID leader, UUID player, Integer value) {
		Guild guild = GuildsManager.getManager().getGuild(leader);
		guild.getPlayer(player).setSalary(value);
	String name=	Bukkit.getOfflinePlayer(player).getName();
		guildLog(":yellow_circle: The player **"+name+ "** from the guild **" +guild.getName().toUpperCase()+ "** has renegociated their contract with a new salary of **"+value+ " Coins** and a new value of **"+guild.getPlayer(player).getValue()+" Coins**! :yellow_circle:");
	}

	public HashMap<UUID, Integer> getPlayersValueRanking() {
		
		HashMap<UUID,Integer> hashmap = new HashMap<UUID,Integer>();
		for (Guild guild : this.guilds) {
			for (GuildPlayer gp : guild.getPlayers()) {
				hashmap.put(gp.getUUID(), gp.getValue());
			}
		}
		hashmap = StatsManager.getManager().sortByValue(hashmap);
		return hashmap;
	}

	
	public int getFutureMinValue(Guild guild,UUID player) {
		int value = 0;
			int ranking = RankingManager.getManager().getRanking().getPosition(player);
			double playerValue = 0;
			if (ranking!=-1) {
				 playerValue = (1500/Math.sqrt(ranking));
			} 
			
			double guildValue = Math.sqrt(guild.getValueWithoutPlayers());
			value = (int) ((playerValue+guildValue)*50);
			value = (int) (value/50*0.85);
			return value;
	}
	
	
}
