package me.santipingui58.splindux.game.mutation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.fawe.FAWESplinduxAPI;
import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.game.spleef.SpleefArena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.translate.Main;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class GameMutation {

	private UUID uuid;
	private MutationType type;
	private SpleefPlayer owner;
	private SpleefArena arena;
	private MutationState state;
	private List<SpleefPlayer> voted = new ArrayList<SpleefPlayer>();
	private int votes;
	private List<Location> tnt = new ArrayList<Location>();
	
	public GameMutation(SpleefPlayer owner,MutationType type) {
		this.type=type;
		this.owner=owner;
		this.state= MutationState.VOTING;
		this.uuid = UUID.randomUUID();
	
		this.voted = new ArrayList<SpleefPlayer>();
		int value = 0;
		
		if (owner.getPlayer().hasPermission("splindux.extreme")) {
			value = 3;
		} else if (owner.getPlayer().hasPermission("splindux.epic")) {
			value = 2;
		} else {
			value = 1;
		}
		
		votes = votes+value;
	}
	
	public UUID getUUID() {
		return this.uuid;
	}
	
	public MutationState getState() {	
		return this.state;
	}
	
	public void setState(MutationState state) {
		this.state = state;
	
	}
	public SpleefArena getArena() {
		return this.arena;
	}
	
	public SpleefPlayer getOwner() {
		return this.owner;
	}
	
	public void setArena(SpleefArena arena) {
		this.arena = arena;
		this.arena.getAllMutations().add(this);
	}
	
	public MutationType getType() {
		return this.type;
	}
	
	public List<Location> getTNT() {
		return this.tnt;
	}
	
	
	public void clearTNT() {
		for (Location l : this.tnt) {
			if (l.getBlock().getType().equals(Material.TNT)) {
				l.getBlock().setType(Material.AIR);
			}
		}
	}
	public void jumpSpleef() {
		arena.crumbleArena(90);
	}
	
	public void crumbleSpleef() {
		this.arena.crumbleArena(70);
		for (SpleefPlayer sp : arena.getFFAPlayers()) sp.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP,100,5));
	}
	
	public void miniSpleef() {
		Location arena1 = this.arena.getArena1();
		Location arena2 = this.arena.getArena2();
		Location a = new Location(arena1.getWorld(), arena1.getX(), arena1.getY(), arena1.getZ());
		Location b = new Location(arena2.getWorld(), arena2.getX(), arena2.getY(), arena2.getZ());
		FAWESplinduxAPI.getAPI().placeBlocks(a, b, Material.AIR);
		a = a.add(15,0,15);
		b = b.add(-15,0,-15);
		FAWESplinduxAPI.getAPI().placeBlocks(a, b, Material.SNOW_BLOCK);
		for (SpleefPlayer sp : arena.getFFAPlayers()) {
			sp.getPlayer().teleport(arena.getMainSpawn());
			sp.getPlayer().getInventory().clear();
		}

	}
	
	
	public void levitationSpleef() {
		new BukkitRunnable() {
		int i = 0;
            @Override
            public void run() {
                if(!getState().equals(MutationState.INGAME)) {
                    cancel();
                } else {
                	i++;
                	if (i>=40) {
                		i = 0;
                	for (SpleefPlayer sp : getArena().getFFAPlayers()) sp.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,60,getType().getLevel()));
                	}
                }

            }
        }.runTaskTimer(Main.get(), 0L, 4);
	}
	
	
	public void voteMutation(SpleefPlayer sp) {
		Player p = sp.getPlayer();
		if (this.voted.contains(sp)) {
			p.sendMessage("§cYou already voted for this mutation.");
			return;
		}
		this.voted.add(sp);
		int value = 0;
		if (p.hasPermission("splindux.extreme")) {
			value = 3;
		} else if (p.hasPermission("splindux.epic")) {
			value = 2;
		} else {
			value = 1;
		}
		
		votes = votes+value;
		
		for (SpleefPlayer viewers : this.arena.getViewers()) viewers.getPlayer().sendMessage("§b" + p.getName()+" §ahas added §6"+value+ " §a vote(s) to add §b" +this.type.getTitle() 
		+ "§a! §e("+this.votes+"/"+this.type.getRequiredVotes()+")");
				
		if (votes>=this.type.getRequiredVotes()) {
			for (SpleefPlayer viewers : this.arena.getViewers()) viewers.getPlayer().sendMessage("§b"+ this.type.getTitle()+ " §5added to the next round!");
			this.state=MutationState.QUEUE;
		} else {
			for (SpleefPlayer viewers : this.arena.getViewers()) {
			if(!viewers.equals(this.owner)) {
				viewers.getPlayer().spigot().sendMessage(getInvitation());
			}
		}
		}
	}
	
	
	
	public void sendMutationRequest(SpleefArena arena) {
		List<GameMutation> list = new ArrayList<GameMutation>();
		list.addAll(arena.getVotingMutations());
		list.addAll(arena.getQueuedMutations());
		
	
		for (GameMutation mutation : list) {
			if (mutation.getType().equals(this.type)) {			
				this.owner.getPlayer().sendMessage("§cThis mutation has already been choosed for this round.");
				return;
			}
		}
		
		setArena(arena);
		
		for (SpleefPlayer sp : this.arena.getViewers()) {
		mutationMessage(sp);		
		}
		
		mutationMessageTask();
		
		this.owner.setMutationTokens(this.owner.getMutationTokens()-1);
		GameMutation g = this;
		new BukkitRunnable() {
			public void run() {
				if (arena.getVotingMutations().contains(g)) {
					arena.getAllMutations().remove(g);
					state= MutationState.FINISHED;
					owner.getPlayer().sendMessage("§cThe voting has ended and the Mutation did not reach enough votes! You get back your Mutation Token.");
					owner.setMutationTokens(owner.getMutationTokens()+1);
				}
			}
		}.runTaskLaterAsynchronously(Main.get(), 20L*60);
	}
	
	public void mutationMessage(SpleefPlayer sp) {
			sp.getPlayer().sendMessage("§b"+this.owner.getPlayer().getName()+" §ahas activated a Mutation Token for §b" + this.type.getTitle() + "§a! You need §e" 
					+ this.type.getRequiredVotes() + " §avotes to activate this Mutation for the next game! §7(Mutation request will end in 1 minute) §e("+this.votes+"/"+this.type.getRequiredVotes()+")");			
			if(!sp.equals(this.owner)) {
				sp.getPlayer().spigot().sendMessage(getInvitation());
			}		
	}
	
	public void mutationMessageTask() {
		new BukkitRunnable() {
			public void run() {
				if (!state.equals(MutationState.VOTING)) cancel();
				for (SpleefPlayer sp : arena.getViewers()) {
					if (sp.equals(owner) || voted.contains(sp)) continue;
				sp.getPlayer().sendMessage("§aA mutation is still in voting for: §b" + type.getTitle() + " §e("+votes+"/"+type.getRequiredVotes()+")");			
					sp.getPlayer().spigot().sendMessage(getInvitation());
					
			}
			}
		}.runTaskTimer(Main.get(), 13*20, 13*20);
		
	
	}
	
	private BaseComponent[] getInvitation() {
		TextComponent msg1 = new TextComponent("[Vote]");
		msg1.setColor(net.md_5.bungee.api.ChatColor.YELLOW );
		msg1.setBold(true);
		msg1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hover mutationaccept " + this.uuid.toString()));	
		msg1.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aVote to add a " + this.type.getTitle() +" §afor the next game").create()));
		ComponentBuilder cb = new ComponentBuilder(msg1);
		return cb.create();
	}

		@SuppressWarnings("deprecation")
		public void giveMutationItems(SpleefPlayer sp) {
			Player p = sp.getPlayer();
			if (this.type.equals(MutationType.TNT_SPLEEF)) {
				p.getInventory().removeItem(DataManager.getManager().gameitems());
				p.getInventory().addItem(new ItemStack(Material.FLINT_AND_STEEL));
				ItemStack item = new ItemStack(Material.TNT);
				item.setAmount(576);
				p.getInventory().addItem(item);			
				
			} else if (this.type.equals(MutationType.BOW_SPLEEF)) {
				p.getInventory().removeItem(DataManager.getManager().gameitems());
				ItemStack bow = new ItemStack(Material.BOW);
				bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
				ItemMeta meta = bow.getItemMeta();
				meta.spigot().setUnbreakable(true);
				bow.setItemMeta(meta);
				bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
				p.getInventory().addItem(bow);
				p.getInventory().addItem(new ItemStack(Material.ARROW));
				
	 	} else if (this.type.equals(MutationType.BLINDNESS)) {
	 		sp.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,Integer.MAX_VALUE,0));
	 	} else if (this.type.equals(MutationType.CREEPY_SPLEEF)) {	 		
	 		p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,Integer.MAX_VALUE,0));
	 		p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,Integer.MAX_VALUE,0));
	 		ItemStack item = new ItemStack(Material.SKULL_ITEM, 1 , (short) 3);
	 		SkullMeta meta = (SkullMeta) item.getItemMeta();
	 		meta.setOwner("MHF_Herobrine");
	 		item.setItemMeta(meta);
	 		p.getInventory().setHelmet(item);
	 	} else if (this.type.equals(MutationType.ENDER_SPLEEF)) {
	 		p.getInventory().removeItem(DataManager.getManager().gameitems());
	 		ItemStack item = new ItemStack(Material.ENDER_PEARL);
			item.setAmount(288);
	 		p.getInventory().addItem(item);
	 	} else if (this.type.equals(MutationType.EXPERIENCE_SPLEEF)) {
	 		p.getInventory().removeItem(DataManager.getManager().gameitems());
	 		ItemStack item = new ItemStack(Material.EXP_BOTTLE);
			item.setAmount(576);
			p.getInventory().addItem(item);	
	 	} else if (this.type.equals(MutationType.FISHING_ROD)) {
	 		p.getInventory().removeItem(DataManager.getManager().gameitems());
			ItemStack fishing_rod = new ItemStack(Material.FISHING_ROD);
			ItemMeta meta = fishing_rod.getItemMeta();
			meta.spigot().setUnbreakable(true);
			fishing_rod.setItemMeta(meta);
			p.getInventory().addItem(fishing_rod);
	 	} else if (this.type.equals(MutationType.GLOWING)) {
	 		p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,Integer.MAX_VALUE,0));
	 	} else if (this.type.equals(MutationType.INVISIBILITY)) {
	 		p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,Integer.MAX_VALUE,0));
	 	} else if (this.type.equals(MutationType.JUMP_I)) {
	 		p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,Integer.MAX_VALUE,0));
	 	} else if (this.type.equals(MutationType.JUMP_II)) {
	 		p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,Integer.MAX_VALUE,1));
	 	} else if (this.type.equals(MutationType.JUMP_V)) {
	 		p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,Integer.MAX_VALUE,4));
	 	} else if (this.type.equals(MutationType.JUMP_X)) {
	 		p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,Integer.MAX_VALUE,9));
	 	} else if (this.type.equals(MutationType.JUMP_SPLEEF)) {
	 		p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,Integer.MAX_VALUE,8));
	 	} else if (this.type.equals(MutationType.NAUSEA)) {
	 		p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,Integer.MAX_VALUE,0));
	 	} else if (this.type.equals(MutationType.POT_SPLEEF)) {
	 		p.getInventory().removeItem(DataManager.getManager().gameitems());
	 		ItemStack splash = new ItemStack(Material.SPLASH_POTION);
	 		splash.setAmount(128);
	 		ItemStack lingering = new ItemStack(Material.LINGERING_POTION);
	 		lingering.setAmount(32);
	 		p.getInventory().addItem(splash);
	 		p.getInventory().addItem(lingering);
	 	} else if (this.type.equals(MutationType.SLOWNESS_I)) {
	 		p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,Integer.MAX_VALUE,0));
	 	} else if (this.type.equals(MutationType.SLOWNESS_II)) {
	 		p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,Integer.MAX_VALUE,1));
	 	} else if (this.type.equals(MutationType.SLOWNESS_V)) {
	 		p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,Integer.MAX_VALUE,4));
	 	} else if (this.type.equals(MutationType.SLOWNESS_X)) {
	 		p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,Integer.MAX_VALUE,9));
	 	} else if (this.type.equals(MutationType.SPEED_I)) {
	 		p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,Integer.MAX_VALUE,0));
	 	} else if (this.type.equals(MutationType.SPEED_II)) {
	 		p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,Integer.MAX_VALUE,1));
	 	} else if (this.type.equals(MutationType.SPEED_V)) {
	 		p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,Integer.MAX_VALUE,4));
	 	} else if (this.type.equals(MutationType.SPEED_X)) {
	 		p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,Integer.MAX_VALUE,9));
	 	} else if (this.type.equals(MutationType.SPLEFF)) {
	 		p.getInventory().removeItem(DataManager.getManager().gameitems());
	 		ItemStack snowballs = new ItemStack(Material.SNOW_BALL);
	 		snowballs.setAmount(288);
	 		p.getInventory().addItem(snowballs);
	 	}
		
	}
	
	
	
	
}
