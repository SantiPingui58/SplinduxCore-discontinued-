package me.santipingui58.splindux.task.tasks;

import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.Arena;
import me.santipingui58.splindux.game.spleef.GameType;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.splindux.utils.ActionBarAPI;

public class ActionBarTask {
	

	public ActionBarTask() {
		new BukkitRunnable() {
			public void run() {
				for (Arena arena : DataManager.getManager().getArenas()) {
					if (!arena.getGameType().equals(GameType.DUEL)) continue;
					if (arena.getPlayTo()==7) continue;
					if (arena.getViewers()==null) continue;
					for (SpleefPlayer sp : arena.getViewers()) {
						if (sp.getOfflinePlayer().isOnline()) {
							new BukkitRunnable() {
								public void run() {
						ActionBarAPI.sendActionBar(sp.getPlayer(), "§6§lPlaying to: §a§l"+arena.getPlayTo());
							}
							}.runTask(Main.get());
						}
					}
				}
			}
		}.runTaskTimerAsynchronously(Main.get(), 0L, 40L);
	}
}
