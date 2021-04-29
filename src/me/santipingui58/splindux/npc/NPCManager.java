package me.santipingui58.splindux.npc;

import java.util.ArrayList;
import java.util.List;

import net.citizensnpcs.api.CitizensAPI;


public class NPCManager {
	private static NPCManager manager;	
	 public static NPCManager getManager() {
	        if (manager == null)
	        	manager = new NPCManager();
	        return manager;
	    }
	 
	 private List<SplinduxNPC> npcs = new ArrayList<SplinduxNPC>();
	 
	 public List<SplinduxNPC> getNPCList() {
		 return this.npcs;
	 }
	 


	 
	 public SplinduxNPC getSplinduxNPCBy(int id) {
		 for (SplinduxNPC npc : this.npcs) {
			 if (npc.getNPC().getId()==id) {
				 return npc;
			 }
		 }
		 return null;
	 }
	 
	 
	 public void loadNPCs() {
		 for (NPCType type : NPCType.values()) {
			 SplinduxNPC npc = new SplinduxNPC(CitizensAPI.getNPCRegistry().getById(type.getId()),type);
			 this.npcs.add(npc);
		 }
		  
		 updateNPCs();
	 }
	 
	 public void removeNPCs() {
		 for (SplinduxNPC npc : this.npcs) {
			 npc.deleteHologram();
		 }
	 }
	 
	 
	 public void updateNPCs() {
		 for (SplinduxNPC npc : this.npcs) {
			 npc.updateHologram();
		 }
	 }
	 
	 
}
