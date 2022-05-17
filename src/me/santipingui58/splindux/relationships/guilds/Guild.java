package me.santipingui58.splindux.relationships.guilds;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.hikari.HikariAPI;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.relationships.RelationshipRequest;
import me.santipingui58.splindux.relationships.RelationshipRequestType;

public class Guild {

	private UUID uuid;
	private String name;
	private String achronym;
	private int coins;
	private UUID leader;
	private List<GuildPlayer> players;
	private List<UUID> members;
	private List<UUID> transferables;
	private List<UUID> banned;
	private List<UUID> mods;
	private List<UUID> admins;
	
	private List<String> earnings    = new ArrayList<String>();
	private List<String> loss = new ArrayList<String>();
	
	private int level;	
	private Date foundationDate;
	
	
	public Guild(String name, String achronym, UUID leader,int coins) {
		this.uuid = UUID.randomUUID();
		this.name = name;
		this.achronym = achronym;
		this.leader = leader;
		this.coins = coins;
		this.players = new ArrayList<GuildPlayer>();
		this.members = new ArrayList<UUID>();
		this.transferables = new ArrayList<UUID>();
		this.banned = new ArrayList<UUID>();
		this.level = 1;
		this.mods = new ArrayList<UUID>();
		this.admins = new ArrayList<UUID>();
		this.foundationDate = new Date();
	}
	public Guild(UUID uuid,String name, String achronym, int gCoins, UUID leader, List<GuildPlayer> players, List<UUID> members,int level,List<UUID> transferables, List<UUID> banned, List<UUID> mods, List<UUID> admins,Date foundationDate) {
		this.uuid = uuid;
		this.leader = leader;
		this.name = name;
		this.achronym = achronym;
		this.coins = gCoins;
		this.players = players;
		this.members = members;
		this.level = level;
		this.transferables = transferables;
		this.banned = banned;
		this.mods = mods;
		this.admins = admins;
		this.foundationDate = foundationDate;		
		for (GuildPlayer gp : this.players) gp.setGuild(this);
		
	} 
	
	public void addEarning(String string, int i) {
		if (i>0)this.earnings.add("§b"+string +"§7- §a+"+i+" §6Coins" );
	}
	
	public void addLoss(String string, int i) {
		if (i>0) this.loss.add("§b"+string +"§7- §c-"+i+" §6Coins" );
	}
	
	
	public List<String> getEarnings() {
		return this.earnings;
	}
	
	public List<String> getLoss() {
		return this.loss;
	}
	
	public UUID getUUID() {
		return this.uuid;
	}
	
	public Date getFoundationDate() {
		return this.foundationDate;
	}
	
	public boolean isMod(UUID u,boolean onlyMod) {
		if (onlyMod) return mods.contains(u);
		
		return mods.contains(u)|| u.compareTo(leader)==0 || admins.contains(u);
	}
	
	public boolean isAdmin(UUID u,boolean onlyAdmin) {
		if (onlyAdmin) return admins.contains(u);
		return admins.contains(u) || u.compareTo(leader)==0;
	}
	
	public int getMaxMembers() {
		return 5*level;
	}
	
	public List<UUID> getBannedPlayers() {
		return this.banned;
	}
	
	public int getMemberFee() {
		return 10*level;
	}
	
	public void addAdmin(UUID u) {
		this.admins.add(u);
	}
	
	public void removeAdmin(UUID u) {
		this.admins.remove(u);
	}
	
	public void addMod(UUID u) {
		this.mods.add(u);
	}
	
	public void removeMod(UUID u) {
		this.mods.remove(u);
	}
	
	
	public List<UUID> getAdmins(boolean onlyAdmins) {
		List<UUID> list = new ArrayList<UUID>();
		for (UUID u : this.getAllMembers()) {
			if (isAdmin(u,onlyAdmins)) list.add(u);		
		}
		
		return list;
	}
	
	
	public List<UUID> getMods(boolean onlyMods) {
		List<UUID> list = new ArrayList<UUID>();
		for (UUID u : this.getAllMembers()) {
			if (isMod(u,onlyMods)) list.add(u);
		}
		return list;
	}
	
	public boolean isTransferable(GuildPlayer gp) {
		for (UUID u : transferables) {
			if (gp.getUUID().compareTo(u)==0) return true;
		}
		return false;
	}
	
	public List<UUID> getTransferablePlayers() {
		return this.transferables;
	}
	
	
	public UUID getLeader() {
		return this.leader;
	}
	
	public String getName() {
		return this.name;
	}
	
	
	
	public String getAchronym() {
		return this.achronym;
	}
	
	public int getCoins() {
		return this.coins;
	}
	
	public void setCoins(int i) {
		this.coins = i;
	}
	
	public List<GuildPlayer> getPlayers() {
		return this.players;
	}
	
	public List<UUID> getPlayersUUID() {
		List<UUID> list = new ArrayList<UUID>();
		for (GuildPlayer gp : this.players) {
			list.add(gp.getUUID());
		}
		return list;
	}
	
	
	public List<UUID> getMembers() {
		return this.members;
	}
	
	
	public GuildPlayer getPlayer(UUID uuid) {
		for (GuildPlayer gp : this.players) {
			if (gp.getUUID().compareTo(uuid)==0) return gp;
		}
		return null;
	}
	
	
	public List<UUID> getAllMembers() {
		List<UUID> members = new ArrayList<UUID>();
		members.addAll(getMembers());
		members.addAll(getPlayersUUID());
		members.add(leader);
		return members;
	}
	
	public List<Player> getAllOnlineMembers() {
		List<Player> members = new ArrayList<Player>();
		for (UUID u : getAllMembers()) {
			Player p = Bukkit.getPlayer(u);
			if (Bukkit.getOnlinePlayers().contains(p)) members.add(p);
		}
		return members;
	}
	
	public boolean isAbleToDuel() {
		int onlinePlayers = 0;
		boolean onlineMod = false;
		for (GuildPlayer gp : getPlayers()) {
			OfflinePlayer p = Bukkit.getOfflinePlayer(gp.getUUID());
			if (p.isOnline()) onlinePlayers++;
		}
		if (Bukkit.getOfflinePlayer(this.leader).isOnline()) onlinePlayers++;
		
		if (onlinePlayers<4) return false;
		
		for (UUID u : getMods(false)) {
			OfflinePlayer p = Bukkit.getOfflinePlayer(u);
			if (p.isOnline()) {
				onlineMod = true;
				break;
			}
		}
		
		return onlineMod;
	}
	
public boolean isLeader(SpleefPlayer sp) {
	return leader.compareTo(sp.getUUID())==0;
}


public void buyPlayer(SpleefPlayer splayer, Guild guildName,int salary) {
	GuildPlayer oldPlayer = null;
	if (guildName!=null) {
	 oldPlayer = guildName.getPlayer(splayer.getUUID());
	}
	
	boolean free = guildName==null;
	int fichaje = 0;
	if (free) {
		fichaje = salary*25;
	} else if (guildName!=null) {
		fichaje = oldPlayer.getValue();
	}
	if (free) {
		splayer.addCoins(fichaje);
		} else {
			guildName.setCoins(guildName.getCoins()+fichaje);	
			guildName.getPlayers().remove(oldPlayer);
		}

	
	setCoins(this.coins-fichaje);
	GuildPlayer gp = new GuildPlayer(splayer.getUUID(),salary);
	gp.setGuild(this);
	getPlayers().add(gp);
	
	
	
	
	GuildsManager.getManager().saveGuilds();
	
	String message = !free ? ":dollar: The Guild **" +this.getName().toUpperCase() +"** has bought the player **"+ splayer.getOfflinePlayer().getName()+"** from the guild **"+guildName.getName().toUpperCase()+ "** for **"+ salary*50+" Coins** and a new salary of **" 
	+salary+ " Coins**! :dollar:" : ":dollar: The Guild **" +this.getName().toUpperCase() +"** has added the free player **"+ splayer.getOfflinePlayer().getName() + "** with a salary of **" 
			+salary+ " Coins**! :dollar:";
	
	this.addLoss("BOUGHT PLAYER", fichaje);
	GuildsManager.getManager().guildLog(message);
	
	
}


public void firePlayer(UUID uuid, int value) {
	GuildPlayer gp = getPlayer(uuid);
	getPlayers().remove(gp);
	this.coins= this.coins-value;
	this.addLoss("FIRED PLAYER", value);
	OfflinePlayer pa = Bukkit.getOfflinePlayer(gp.getUUID());
	SpleefPlayer temp = SpleefPlayer.getSpleefPlayer(pa);
	if (temp==null) {
		 new SpleefPlayer(pa.getUniqueId());
		HikariAPI.getManager().loadData(pa.getUniqueId());
	}
		new BukkitRunnable() {
			public void run() {		
			SpleefPlayer splayer = SpleefPlayer.getSpleefPlayer(pa);
			splayer.addCoins(value);				
			GuildsManager.getManager().saveGuilds();
	}
			}.runTaskLaterAsynchronously(Main.get(), 3L);
	
			GuildsManager.getManager().guildLog(":exclamation: The player §b" +pa.getName() + " has become a free agent after leaving the guild **"+this.name.toUpperCase()+"**! :exclamation:");
}


public int getAllPlayersValue() {
	int value = 0;
	for (GuildPlayer gp : getPlayers()) {
		value = value + gp.getValue();
	}
	return value;
}

public void resign(SpleefPlayer sp,int value) {
	GuildPlayer gp = getPlayer(sp.getUUID());
	getPlayers().remove(gp);
	this.coins = this.coins+value;
	this.addEarning("RESIGNED PLAYER", value);
	sp.removeCoins(value);
	sp.sendMessage("§cYou have left your Guild");
	broadcast("§cThe player §b" +sp.getOfflinePlayer().getName() + " §chas left the guild!");
	GuildsManager.getManager().guildLog(":exclamation: The player §b" +sp.getOfflinePlayer().getName() + " has become a free agent after leaving the guild **"+this.name.toUpperCase()+"**! :exclamation:");
	
}

public void broadcast(String msg) {
	for (UUID u : getAllMembers()) {
		Player p = Bukkit.getPlayer(u);
		if (Bukkit.getOnlinePlayers().contains(p)) {
			p.sendMessage(GuildsManager.getManager().getPrefix()+msg);
		}
	}
}

public void renegociate(GuildPlayer gp, int salary) {
	String[] args = {Bukkit.getOfflinePlayer(gp.getUUID()).getName(),String.valueOf(salary),Bukkit.getOfflinePlayer(this.leader).getName()};
	List<UUID> se = new ArrayList<UUID>();
	se.add(this.leader);
	List<UUID> re = new ArrayList<UUID>();
	re.add(gp.getUUID());
	new RelationshipRequest(se,re,RelationshipRequestType.RENEGOCIATE_GUILD,args);
	
}
public void rename(String name2, String upperCase) {
	broadcast("§a§lThe guild has been renamed from §b§l" + this.name + "§a§lto §b§l" + name2 + "§a§l!");
	GuildsManager.getManager().guildLog(":warning: The guild **"+this.name+"** has been renamed to **"+name2.toUpperCase()+"**! :warning:");
	this.name = name2;
	this.achronym = upperCase;
	this.coins=this.coins-10000;
	this.addLoss("RENAME", 10000);

	
}
public int getLevel() {
	return this.level;
}


public int getValue() {
	int value = getValueWithoutPlayers();
	for (GuildPlayer gp : this.getPlayers()) {
		value = value + gp.getValue();
	}
	
	
return value;
}


public int getValueWithoutPlayers() {
	int value = this.coins;
	
for (int i = this.level; i>=1;i-- ) {
	value = value + getLevelPrice(i);
}
	
	return value;
}



public int getNextLevelPrice() {
	return getLevelPrice(this.level+1);
}

public int getLevelPrice(int level) {
	switch(level) {
	case 1: return 0;
	case 2: return 10000;
	case 3: return 25000;
	case 4: return 50000;
	case 5: return 100000;
	case 6: return 250000;
	case 7: return 500000;
	case 8: return 1000000;
	case 9: return 2500000;
	case 10: return 5000000;
	}
	return 0;
}
public boolean hasTabTag() {
	return this.level>=5;
}
public boolean hasHeadTag() {
	return this.level>=7;
}

public int getExpectedGains() {
	int gain =  this.getMemberFee()*this.getMembers().size()*30;
	gain = gain +12500;
	return gain;
}


public int getExpectedLoss() {
	int loss = getDailyPlayersSalary()*30;
	loss = loss +getDailyLeaderRevenue()*30;
	return loss-2500;
}

public int getDailyPlayersSalary() {
	int loss = 0;
	for (GuildPlayer gp : this.getPlayers()) {
		loss = loss +gp.getSalary();
	}
	return loss;
}
public int getDailyLeaderRevenue() {
	return getCoins()/100;
}
public void levelUp() {
	this.coins= this.coins-this.getNextLevelPrice();
	this.addLoss("LEVELED UP", this.getNextLevelPrice());
	
	if (this.level<10) this.level++;
	this.broadcast("§eThe Guild has leveled up to Level §d§l" + this.level + "§e!");
}

	
	}
