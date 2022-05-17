package me.santipingui58.splindux.npc;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.GameManager;
import me.santipingui58.splindux.game.parkour.ParkourManager;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.SpleefType;
import net.citizensnpcs.api.npc.NPC;


public class SplinduxNPC {

	private NPCType type;
	private SpleefType spleefType;
	private Hologram hologram;
	private NPC npc;
	private int queue;
	private int playing;
	public SplinduxNPC(NPC npc,NPCType type,SpleefType spleefType) {
		this.type = type;
		this.spleefType = spleefType;
		this.npc = npc;
		 createHologram();		 
	}
	
	public SpleefType getSpleefType() {
		return this.spleefType;
	}	
	
	public void createHologram() {
		double x = npc.getStoredLocation().getBlockX()+0.5;	
		double y =  npc.getStoredLocation().getBlockY()+2.7;
		double z = npc.getStoredLocation().getBlockZ()+0.5;	
		Location l = new Location (npc.getStoredLocation().getWorld(),x,y,z);
		this.hologram = loadHologram(l);
		hologram.clearLines();
		
		if (this.spleefType!=null || this.type.equals(NPCType.PARKOUR)) 
		hologram.appendTextLine("§e§l0 Players");
		
		
	}
	
	
	public void updateHologram() {		
			int queue = 0;
			int playing = 0;
			if (this.spleefType!=null) {
				for (int i = 1; i <= 3;i++) {
					queue = queue + GameManager.getManager().getQueueSize(spleefType,GameType.DUEL,i);
					playing = playing + GameManager.getManager().getPlayingSize(spleefType, GameType.DUEL,i);
				}		

				playing = playing + GameManager.getManager().getPlayingSize(spleefType, GameType.FFA, 0);
			} else if (this.type.equals(NPCType.PARKOUR)){
				playing = ParkourManager.getManager().getArenas().size();
			} else if (this.type.equals(NPCType.THETOWERS)) {
				playing = Main.thetowers;
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
		if (this.type==null) {
		this.hologram.delete();
		}
	}
	

}
