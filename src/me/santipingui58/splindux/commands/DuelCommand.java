package me.santipingui58.splindux.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;



public class DuelCommand implements CommandExecutor{

	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
} else if(cmd.getName().equalsIgnoreCase("duel")){
	final Player p = (Player) sender;
	 SpleefPlayer sp = SpleefPlayer.getSpleefPlayer(p);
	
	if (args.length==0) {
		sender.sendMessage("§aUso of command: /duel <Player>");
	} else {
		Player op = Bukkit.getPlayer(args[0]);
		 SpleefPlayer dueled = SpleefPlayer.getSpleefPlayer(op);
		if (Bukkit.getOnlinePlayers().contains(op)) {
			if (op!=p) {
				if (!sp.getDueledPlayers().contains(dueled)) {
			sp.getDueledPlayers().add(dueled);	
			p.sendMessage("§aYou have sent a duel request to §b" + op.getName()+ "§a!");
			op.sendMessage("§aThe Player §b" + p.getName() + " §ahas sent you a duel request! §7(This request expires in 1 minute.)");
			op.spigot().sendMessage(getInvitation(sp));			
			new BukkitRunnable() {
				public void run() {
					sp.getDueledPlayers().remove(dueled);
				}
			}.runTaskLater(Main.get(), 20*60L);
				} else {
					sender.sendMessage("§cYou have already sent a duel request to this player!");
				}
		} 	else {
			sender.sendMessage("§cYou cant duel yourself...");
		}
		} else {
			sender.sendMessage("§cThe player §b" + args[0] + "§c does not exist or is not online.");
		}
	}
}
		return false;
	}
	
	
	private BaseComponent[] getInvitation(SpleefPlayer dueler) {
		TextComponent msg1 = new TextComponent("[ACCEPT]");
		msg1.setColor( ChatColor.GREEN );
		msg1.setBold( true );
		msg1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hover accept " + dueler.getPlayer().getName()));
		
		
		TextComponent msg2 = new TextComponent("[DENY]");
		msg2.setColor( ChatColor.RED );
		msg2.setBold( true );
		msg2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hover deny "  + dueler.getPlayer().getName()));
		
		ComponentBuilder cb = new ComponentBuilder(msg1);
		cb.append(" ");
		cb.append(msg2);
		return cb.create();
	}
}