package me.santipingui58.splindux.npc;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.spleef.GameType;
import net.citizensnpcs.api.npc.NPC;


public class SplinduxNPC {

	private NPCType type;
	private Hologram hologram;
	private NPC npc;
	private int queue;
	private int playing;
	public SplinduxNPC(NPC npc,NPCType type) {
		this.type = type;
		this.npc = npc;
		 createHologram();		 
	}
	
	
	public void createHologram() {
		double x = npc.getStoredLocation().getBlockX()+0.5;	
		double y =  npc.getStoredLocation().getBlockY()+2.7;
		double z = npc.getStoredLocation().getBlockZ()+0.5;	
		Location l = new Location (npc.getStoredLocation().getWorld(),x,y,z);
		this.hologram = loadHologram(l);
		hologram.clearLines();
		
		if (this.type.getGameType()!=null) {
		 if (this.type.getGameType().equals(GameType.DUEL) || this.type.getGameType().equals(GameType.FFA)) {
				hologram.appendTextLine("§e§l0 Players");
		} else if (this.type.equals(NPCType.FISHING)) {
			hologram.appendTextLine("§eNPC");
		}
		}
	}
	
	
	public void updateHologram() {	
		if (this.type.equals(NPCType.VOTES) || this.type.equals(NPCType.FISHING) || this.type.equals(NPCType.PARKOUR)) return;	
		
		
			int ranked = this.type.equals(NPCType.RANKED) ? 1 : 0;
			int queue = 0;
			int playing = 0;
				int min = this.type.equals(NPCType.TEAMS) ? 2 : 1;
				int max = this.type.equals(NPCType.TEAMS) ? 3 : 1;
				for (int i = min; i <= max;i++) {
					queue = queue + GameManager.getManager().getQueueSize(null,type.getGameType(),i, ranked);
					playing = playing + GameManager.getManager().getPlayingSize(null, type.getGameType(),i,ranked);
				}		
			
			if (this.queue!= queue || 
					this.playing!=playing) {
				this.playing = playing;
				this.queue = queue;			
				hologram.clearLines();
				int i = this.playing+this.queue;
				new BukkitRunnable() {
					public void run() {
				hologram.appendTextLine("§e§l" + i + " Players");
				}
				}.runTask(Main.get());
			}
		
	}
	
	public Hologram loadHologram(Location l) {
		for (Hologram h : HologramsAPI.getHolograms(Main.get())) {
			if (h.getLocation().distance(l)<1) {
				return h;
			}
		}	

		return HologramsAPI.createHologram(Main.get(),l);
	}
	
	public NPCType getType() {
		return this.type;
	}
	
	public NPC getNPC() {
		return this.npc;
	}
	
	public void deleteHologram() {
		if (!this.type.equals(NPCType.VOTES)) {
		this.hologram.delete();
		}
	}
	

}
