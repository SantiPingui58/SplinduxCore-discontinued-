package me.santipingui58.splindux.npc;

import org.bukkit.Location;

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
		double y =  npc.getStoredLocation().getBlockY()+3;
		double z = npc.getStoredLocation().getBlockZ()+0.5;	
		if (this.type.equals(NPCType.FFA)) {
			 y = npc.getStoredLocation().getBlockY()+2.75;	
		} 
		Location l = new Location (npc.getStoredLocation().getWorld(),x,y,z);
		this.hologram = loadHologram(l);
		hologram.clearLines();
		
		if (this.type.getGameType()!=null) {
		if (this.type.equals(NPCType.FFA)) {		
			hologram.appendTextLine("§7Playing: §a" + this.playing/2);
		}  else if (this.type.getGameType().equals(GameType.DUEL)) {
			hologram.appendTextLine("§7Playing: §a" + this.playing);
			hologram.appendTextLine("§7In Queue: §a" + this.queue);
		} else if (this.type.equals(NPCType.FISHING)) {
			hologram.appendTextLine("§eNPC");
		}
		}
	}
	
	
	public void updateHologram() {	
		if (this.type.equals(NPCType.VOTES) || this.type.equals(NPCType.FISHING)) return;
		if (this.type.equals(NPCType.FFA)) {	
			if (this.playing!=GameManager.getManager().getPlayingSize(type.getSpleefType(), type.getGameType(), 0)) {
				this.playing = GameManager.getManager().getPlayingSize(type.getSpleefType(), type.getGameType(), 0);
			hologram.clearLines();
			hologram.appendTextLine("§7Playing: §a" + this.playing);
			}
		} else {		
			if (this.queue!= GameManager.getManager().getQueueSize(type.getSpleefType(), type.getGameType(), type.getAmount()) || 
					this.playing!=GameManager.getManager().getPlayingSize(type.getSpleefType(), type.getGameType(), type.getAmount())) {
				this.playing = GameManager.getManager().getPlayingSize(type.getSpleefType(), type.getGameType(), type.getAmount());
				this.queue = GameManager.getManager().getQueueSize(type.getSpleefType(), type.getGameType(), type.getAmount());				
				hologram.clearLines();
				hologram.appendTextLine("§7Playing: §a" + this.playing);
				hologram.appendTextLine("§7In Queue: §a" + this.queue);
			}
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
