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
		double x = npc.getStoredLocation().getBlockX()+0.5;				
		double z = npc.getStoredLocation().getBlockZ()+0.5;
		
	
		if (this.type.getGameType()!=null) {
		if (this.type.equals(NPCType.FFA)) {		
			double y = npc.getStoredLocation().getBlockY()+2.75;
			Location l = new Location (npc.getStoredLocation().getWorld(),x,y,z);
			this.hologram = HologramsAPI.createHologram(Main.get(),l);
			hologram.clearLines();
			hologram.appendTextLine("§7Playing: §a" + this.playing/2);
		} else if (this.type.getGameType().equals(GameType.DUEL)) {
			double y = npc.getStoredLocation().getBlockY()+3;
			Location l = new Location (npc.getStoredLocation().getWorld(),x,y,z);
			this.hologram = HologramsAPI.createHologram(Main.get(),l);
			hologram.clearLines();
			hologram.appendTextLine("§7Playing: §a" + this.playing);
			hologram.appendTextLine("§7In Queue: §a" + this.queue);
		}
		}
	}
	
	public void updateHologram() {	
		if (this.type.equals(NPCType.VOTES)) return;
		if (this.type.equals(NPCType.FFA)) {	
			if (this.playing!=GameManager.getManager().getPlayingSize(type.getSpleefType(), type.getGameType(), 0)) {
				this.playing = GameManager.getManager().getPlayingSize(type.getSpleefType(), type.getGameType(), 0);
			hologram.clearLines();
			hologram.appendTextLine("§7Playing: §a" + this.playing);
			}
		} else {		
			if (this.queue!= GameManager.getManager().getQueueSize(type.getSpleefType(), type.getGameType(), type.getAmount()) || 
					this.playing!=GameManager.getManager().getPlayingSize(type.getSpleefType(), type.getGameType(), type.getAmount())) {
				this.playing = GameManager.getManager().getPlayingSize(type.getSpleefType(), type.getGameType(), 0);
				this.queue = GameManager.getManager().getQueueSize(type.getSpleefType(), type.getGameType(), 0);				
				hologram.clearLines();
				hologram.appendTextLine("§7Playing: §a" + this.playing);
				hologram.appendTextLine("§7In Queue: §a" + this.queue);
			}
		}
	}
	
	public NPCType getType() {
		return this.type;
	}
	
	public NPC getNPC() {
		return this.npc;
	}
	
	public void deleteHologram() {
		this.hologram.delete();
	}
	

}
