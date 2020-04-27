package me.santipingui58.splindux.game.mutation;

import java.util.UUID;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.game.SpleefPlayer;
import me.santipingui58.splindux.game.spleef.SpleefArena;
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
	public GameMutation(SpleefPlayer owner,MutationType type,SpleefArena arena) {
		this.type=type;
		this.owner=owner;
		this.arena = arena;
		this.state= MutationState.VOTING;
		this.uuid = UUID.randomUUID();
	}
	
	public UUID getUUID() {
		return this.uuid;
	}
	
	public MutationState getState() {
		return this.state;
	}
	
	public SpleefArena getArena() {
		return this.arena;
	}
	
	public SpleefPlayer getOwner() {
		return this.owner;
	}
	
	public MutationType getType() {
		return this.type;
	}
	
	
	public void sendMutationRequest() {

		for (SpleefPlayer sp : this.arena.getViewers()) {
			sp.getPlayer().sendMessage("§b"+this.owner.getPlayer().getName()+" §ahas activated a Mutation Token for §b" + this.type.getTitle() + "§a! You need §e" 
					+ this.type.getRequiredVotes() + " §avotes to activate this Mutation for the next game! §7(Mutation request will end in 1 minute)");			
			if(!sp.equals(this.owner)) {
				sp.getPlayer().spigot().sendMessage(getInvitation(this.owner));
			}
		}		
		GameMutation g = this;
		new BukkitRunnable() {
			public void run() {
				if (arena.getVotingMutations().contains(g)) {
					arena.getAllMutations().remove(g);
				}
			}
		}.runTaskLaterAsynchronously(Main.get(), 20L*60);
	}
	
	
	private BaseComponent[] getInvitation(SpleefPlayer dueler) {
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
				
	 	} else if (this.type.equals(MutationType.BLINDNESS)) {
	 		sp.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,Integer.MAX_VALUE,0));
	 	} else if (this.type.equals(MutationType.CREEPY_SPLEEF)) {	 		
	 		p.playEffect(p.getLocation(), Effect.RECORD_PLAY, Material.RECORD_11.getId());
	 		p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,Integer.MAX_VALUE,0));
	 		p.getInventory().setHelmet(new ItemStack(Material.PUMPKIN));
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
	 		p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,Integer.MAX_VALUE,6));
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
