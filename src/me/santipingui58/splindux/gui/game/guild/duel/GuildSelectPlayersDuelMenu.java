package me.santipingui58.splindux.gui.game.guild.duel;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.gui.MenuBuilder;
import me.santipingui58.splindux.relationships.RelationshipRequest;
import me.santipingui58.splindux.relationships.RelationshipRequestType;
import me.santipingui58.splindux.relationships.guilds.Guild;
import me.santipingui58.splindux.relationships.guilds.GuildDuel;
import me.santipingui58.splindux.relationships.guilds.GuildPlayer;
import me.santipingui58.splindux.relationships.guilds.GuildsManager;
import me.santipingui58.splindux.utils.ItemBuilder;



public class GuildSelectPlayersDuelMenu extends MenuBuilder {
	
	
	public GuildSelectPlayersDuelMenu(GuildDuel duel,int game) {
		super("§eSelect a player for "+ game+"v"+ game +" "+ duel.getType().getName(),2);
		
		new BukkitRunnable() {
		public void run() {
			
		
		int slot = 0;
		List<UUID> playerss = new ArrayList<UUID>();
		
		for (GuildPlayer gp : duel.getGuild().getPlayers()) {
			playerss.add(gp.getUUID());
		}
		playerss.add(duel.getGuild().getLeader());
		for (UUID uuid : playerss) {
			OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
			if (duel.getPlayers1().contains(uuid)) continue;
			if (player.isOnline()) {
				SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(player);
				ItemStack head = new ItemStack(Material.SKULL_ITEM,1,(short) 3);
				SkullMeta meta = (SkullMeta) head.getItemMeta();
				meta.setDisplayName("§a"+player.getName());
				meta.setOwningPlayer(player);
				List<String> lore = new ArrayList<String>();
				if (sp.isInGame()) {
					lore.add("§cPlayer In Game");
				}
				meta.setLore(lore);
				head.setItemMeta(meta);
				s(slot,head);		
				slot++;
			}
		}
		
		
		s(12,new ItemBuilder(Material.REDSTONE).setTitle("§cGo back").build());
		s(13,new ItemBuilder(Material.REDSTONE_BLOCK).setTitle("§cClean All").build());
		s(17,new ItemBuilder(Material.BARRIER).setTitle("§cCancel Duel").build());
		
		List<UUID> players = duel.getPlayers1();
		
		String notsel = "§cNot selected";
		String _1v1_1 =  players.size()<1  ? notsel : "§b"+Bukkit.getOfflinePlayer(players.get(0)).getName();
		String _2v2_1 = players.size()<2  ? notsel : "§b"+Bukkit.getOfflinePlayer(players.get(1)).getName();
		String _2v2_2 = players.size()<3  ? notsel : "§b"+Bukkit.getOfflinePlayer(players.get(2)).getName();
		String _1v1_2 = players.size()<4  ? notsel : "§b"+Bukkit.getOfflinePlayer(players.get(3)).getName();
		
		s(14,new ItemBuilder(Material.DIAMOND).setTitle("§aSend Duel to §6§l" + duel.getDuelGuild().getName())
				.addLore("§a1v1: " + _1v1_1)
				.addLore("§a2v2: " + _2v2_1+ "§7-"+ _2v2_2)
				.addLore("§a1v1: " + _1v1_2)
				.build());
		
		new BukkitRunnable() {
		public void run() {
			buildInventory();
		}
		}.runTask(Main.get());
		
		}
		}.runTaskAsynchronously(Main.get());
	}

	

	@Override
	public void onClick(SpleefPlayer sp, ItemStack stack, int slot) {
		Player p = sp.getPlayer();
		Guild guild = GuildsManager.getManager().getGuild(sp);
		GuildDuel duel = GuildsManager.getManager().getGuildDuelByCreator(p.getUniqueId());
		if (guild==null) {
			p.closeInventory();
			GuildsManager.getManager().getGuildDuels().remove(duel);
			return;
		}
	
		
		if (slot==13) {
			duel.getPlayers1().clear();
			new GuildSelectPlayersDuelMenu(duel,1).o(sp.getPlayer());
		} else if (slot==12) {
			if (duel.getPlayers1().size()==0) return;
			int game = duel.getPlayers1().size()==4 || duel.getPlayers1().size()==2 ? 1: 2;
			duel.getPlayers1().remove(duel.getPlayers1().size()-1);
			new GuildSelectPlayersDuelMenu(duel,game).o(p);
		} else if (slot==14) {
			if (duel.getPlayers1().size()==4) {
			
				List<UUID> list = new ArrayList<UUID>();
				list.add(p.getUniqueId());
				for (UUID u : duel.getGuild().getMods(false)) {
					if (p.getUniqueId().compareTo(u)==0) continue;
					list.add(u);
				}
				String args[] = {duel.getDuelGuild().getName(),duel.getGuild().getName(),duel.getType().toString()};
				new RelationshipRequest(list,duel.getDuelGuild().getMods(false), RelationshipRequestType.GUILD_DUEL, args);
				p.closeInventory();
			}
		} else if (slot==17) {
			GuildsManager.getManager().getGuildDuels().remove(duel);
			p.closeInventory();
		} else {
			if (stack!=null) {
				SkullMeta meta = (SkullMeta) stack.getItemMeta();
				if (meta.getLore() !=null && meta.getLore().size()>0) return;			
				OfflinePlayer player = meta.getOwningPlayer();
				duel.getPlayers1().add(player.getUniqueId());
				int game = duel.getPlayers1().size()==1 || duel.getPlayers1().size()==2 ? 2: 1;
				new GuildSelectPlayersDuelMenu(duel,game).o(p);
			}
		}
	}
	
	}



