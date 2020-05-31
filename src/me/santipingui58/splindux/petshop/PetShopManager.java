package me.santipingui58.splindux.petshop;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


import me.santipingui58.splindux.Main;

public class PetShopManager {
	private static PetShopManager manager;
	 public static PetShopManager getManager() {
	        if (manager == null)
	        	manager = new PetShopManager();
	        return manager;
	    }
	 
	 
	 private List<SplinduxPet> pets = new ArrayList<SplinduxPet>();
	 private List<String> boughtPets = new ArrayList<String>();

	 public List<SplinduxPet> getPets() {
		 return this.pets;
	 }
	 
	 public List<String> getBoughPets() {
		 return this.boughtPets;
	 }
	 
	 
	 
	 public void loadPets() {
		 if (Main.petshop.getConfig().contains("pets")) {
			 Set<String> pets = Main.petshop.getConfig().getConfigurationSection("pets").getKeys(false);
			 for (String s : pets) {
				 
				 String name = Main.petshop.getConfig().getString("pets."+s+".name");
				 int price = Main.petshop.getConfig().getInt("pets."+s+".price");
				 PetSpawnType type  = PetSpawnType.valueOf(Main.petshop.getConfig().getString("pets."+s+".type"));
				 String spawn = Main.petshop.getConfig().getString("pets."+s+".spawn");
				 boolean vip = false;
				 boolean epic = false;
				 boolean extreme = false;
				 if (Main.petshop.getConfig().contains("pets."+s+".vip")) vip = true;
				 if (Main.petshop.getConfig().contains("pets."+s+".epic")) epic = true;
				 if (Main.petshop.getConfig().contains("pets."+s+".extreme")) extreme = true;
				 SplinduxPet pet = new SplinduxPet(s,name,type,price,vip,epic,extreme,spawn);
				 this.pets.add(pet);
				 
				 
	 }
		 }
	 
		 
		 if (Main.petshop.getConfig().contains("boughtpets")) {
			 this.boughtPets = Main.petshop.getConfig().getStringList("boughtpets");
		 }
		 
	 }

	 
	 public String getCommunityGoalPercentage() {
			 int current = this.boughtPets.size();
			 int max = this.pets.size();		
			 double p = (double)current/ (double) max;
			 int p2 = (int) (p*100);
			 return "§6§l"+String.valueOf(p2)+"%";
			 
		 }
	 
	 public String getBar() {
		 String string = "§6[";
		 
		 double d = this.pets.size()/4.5;
		 double d2 = this.boughtPets.size()/4.5;
		 for (int i = 0; i<=d;i++) {
			 if (d2<=i) {
			 string = string + "§7|";		 
		 } else {
			 string = string + "§b|";		 
		 }
			 }
		 
		 string = string + "§6]";
		 
		 return string;
	 }
	 
	 
	 public void boughtPet(String s) {
		 if (!this.boughtPets.contains(s)) {
			 this.boughtPets.add(s);
		 }
	 }
	 
	 public void savePets() {
			 Main.petshop.getConfig().set("boughtpets", this.boughtPets);
			 Main.petshop.saveConfig();
		 
	 }
	 
	public SplinduxPet getPetByName(String displayName) {
		for (SplinduxPet pet : this.pets) {
			if (pet.getName().equalsIgnoreCase(displayName)) {
				return pet;
			}
		}
		
		return null;
	}
}
