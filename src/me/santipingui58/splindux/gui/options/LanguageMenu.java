package me.santipingui58.splindux.gui.options;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.gui.MenuBuilder;
import me.santipingui58.splindux.utils.ItemBuilder;
import me.santipingui58.splindux.utils.Utils;
import me.santipingui58.translate.Language;

public class LanguageMenu extends MenuBuilder {

	
	public LanguageMenu(SpleefPlayer sp) {
		super("§e§lSelect your Language",3);
		
		new BukkitRunnable() {
		public void run() {
			
		Utils utils = Utils.getUtils();
		s(11,utils.getSkull("http://textures.minecraft.net/texture/a9f5799dfb7de65350337e735651d4c831f1c2a827d584b02d8e875ff8eaa2","§e§lEspañol"));
	    s(13, utils.getSkull("http://textures.minecraft.net/texture/5d263199c737676d72cdf446d73119b58f8fe70a39bd5ab8c82724da6b4744","§9§lEnglish"));
	    s(15, utils.getSkull("http://textures.minecraft.net/texture/6b66dbc44a0caa458dcf5ad0ae5263583de8680213cbb6ceda3dbd3910b70abe","§f§lрусский"));
		s(26, new ItemBuilder(Material.ARROW).setTitle("§cGo back").build());
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
		if (slot==11) {
			sp.getOptions().setLanguage(Language.SPANISH);
			p.sendMessage("§aLenguaje cambiado a: §b" + Language.SPANISH+"§a!");
			p.closeInventory();
		} else if (slot==13) {
			sp.getOptions().setLanguage(Language.ENGLISH);
			p.sendMessage("§aLanguage set to: §b" + Language.ENGLISH+"§a!");
			p.closeInventory();
		} else if (slot==15) {
			sp.getOptions().setLanguage(Language.RUSSIAN);
			p.sendMessage("§aЯзык изменился наo: §b" + Language.RUSSIAN+"§a!");
			p.closeInventory();
		} 
		
		if (slot==26) {
			new OptionsMenu(sp).o(sp.getPlayer());
			return;
		}
		
		
		
	}
	
	
	}



