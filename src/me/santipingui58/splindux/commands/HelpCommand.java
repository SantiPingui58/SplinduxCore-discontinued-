package me.santipingui58.splindux.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		
		if(!(sender instanceof Player)) {
			
			sender.sendMessage("Solo los jugadores pueden hacer esto!");
			return true;
			
		} else {
	
		if(cmd.getName().equalsIgnoreCase("help")){
			final Player p = (Player) sender;
			p.sendMessage("§e§lSplin§b§lDux §7is a Competitive §bSpleef §7Server. If you want to know more about Spleef you can read the Minecraft Wiki article: §ahttps://minecraft.gamepedia.com/Spleef");
			p.sendMessage("§7The server is under constant development, to see the news of the server and to connect with the community you should join our discord server: §9§lhttps://discord.gg/wjWX3hK");
			p.sendMessage("§6Here are a list of useful commands:");
			p.sendMessage("§a/duel <Player> Spleef §7- Play a 1vs1 Game");
			p.sendMessage("§a/endgame §7- Request to finish the current game to your opponent in a 1vs1");
			p.sendMessage("§a/crumble <percentage> §7- Request to crumble the arena in a 1vs1");
			p.sendMessage("§a/reset §7- Request to reset the arena in a 1vs1");
			p.sendMessage("§a/playto <number> §7- Request to play to to an specific number of points (1 to 99)");
			p.sendMessage("§a/matches §7- See the current 1vs1 games with information about the game (you can right click in chat to spectate it)");
			p.sendMessage("§a/spectate <Player> §7- Spectate a player who is in a 1vs1 game");
			p.sendMessage("§a/ping §7- See your ping");
			p.sendMessage("§a/ping all §7- See everyone ping");
			p.sendMessage("§a/stats §7- See your stats (for more information of how to use this command do: /stats help");
			p.sendMessage("§a/translate §7- Set your language and enable/disable automatic chat translate to translate english and spanish messages");
			p.sendMessage("§6Here are a list of useful information:");
			p.sendMessage("§5§lSplinbox §7Its a lootbox that containts gadgets such as pets, hats, etc. You can win one being online or in tournaments. If you are in a game, your chances of getting one increase. You can see all Gadgets with the item in your hotbar at Lobby.");
			p.sendMessage("§b§lSpleef Rank §7There are different ranks, you level up playing games, winning games, playing tournaments, etc. You can see your progress to the next level in the EXP bar, or with the command §a/stats");
			p.sendMessage("§6§lTournaments §7There are 2 type of tourneys, weekly tournaments and 'monthly' tournaments. The first ones are hosted every weekend, in the week we vote what day and hour will be the tournament. The tournament starts and ends the same day. Monthly tournaments"
					+ " are bigger and last more than a week most of time.");
		}
		}
		
		return false;
			}
	
	
	
}