package me.santipingui58.splindux.gui;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.santipingui58.splindux.game.mutation.GameMutation;
import me.santipingui58.splindux.game.mutation.MutationType;
import me.santipingui58.splindux.game.spleef.SpleefArena;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.utils.Utils;






public class MutationTokenMenu extends MenuBuilder {

	public static HashMap<Player,Integer> page = new HashMap<Player,Integer>();
	
	
	public MutationTokenMenu(SpleefPlayer sp) {
		super("§5Select a Mutation:",6);
		for (MutationType type : MutationType.values()) {
			s(type.getSlot(), type.getItem());
		}
		
		ItemStack item = Utils.getUtils().getSkull("http://textures.minecraft.net/texture/35b116dc769d6d5726f12a24f3f186f839427321e82f4138775a4c40367a49");
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§aYou have §5§l" + sp.getMutationTokens() + " §aMutation Tokens!");
		item.setItemMeta(meta);
		s(53,item);
	}
	

	@Override
	public void onClick(SpleefPlayer sp, ItemStack stack, int slot) {
		if (sp.isInArena()) {
			SpleefArena arena = sp.getArena();
			for (MutationType type : MutationType.values()) {
				if (slot==type.getSlot()) {					
					GameMutation mutation = new GameMutation(sp,type);
					mutation.sendMutationRequest(arena);
					sp.getPlayer().closeInventory();
					break;
				}
			}
		} else {
			sp.getPlayer().closeInventory();
			sp.getPlayer().sendMessage("§cYou need to be in a FFA game to do this!");
		}
		
	}
	
	
	}



