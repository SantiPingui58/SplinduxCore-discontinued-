package me.santipingui58.splindux.relationships;

import me.santipingui58.splindux.relationships.guilds.GuildsManager;

public enum RelationshipRequestType {
	FRIENDS, JOIN_GUILD_AS_PLAYER,RENEGOCIATE_GUILD,JOIN_GUILD_AS_MEMBER, GUILD_DUEL, BUY_PLAYER;
	
	
	public String getAcceptText() {
		return "ACCEPT";
	}
	
	public String getDenyText() {
		return "DENY";
	}
	
	public String getHoverAccept() {
		switch(this) {
		case FRIENDS: return "friendaccept";
		case JOIN_GUILD_AS_PLAYER: return "joinguildplayeraccept";
		case JOIN_GUILD_AS_MEMBER:
			return "joinguildmemberaccept";
		case RENEGOCIATE_GUILD:
			return "renegociateguildaccept";
		case GUILD_DUEL:
			return "guildduelaccept";
		case BUY_PLAYER:
			return "buyplayeraccept";
		}
		return null;
	}
	
	public String getHoverDeny() {
		switch(this) {
		case FRIENDS: return "frienddeny";
		case JOIN_GUILD_AS_PLAYER: return "joinguildplayerdeny";
		case JOIN_GUILD_AS_MEMBER:
			return "joinguildmemberdeny";
		case RENEGOCIATE_GUILD:
			return "renegociateguilddeny";
		case GUILD_DUEL:
			return "guilddueldeny";
		case BUY_PLAYER:
			return "buyplayerdeny";
		}
		return null;
	}
	
	public String getAcceptDescription(String[] args) {
		switch(this) {
		case FRIENDS: return "Accept friend request";
		case JOIN_GUILD_AS_PLAYER: return "Accept to become a player of the "+ args[0] + " guild";
		case JOIN_GUILD_AS_MEMBER:
			 return "Accept to become a member of the "+ args[0] + " guild";
		case RENEGOCIATE_GUILD:
			return "Accept renegotiation";
		case GUILD_DUEL:
			return "Accept Guild Duel";
		case BUY_PLAYER:
			return "Accept to leave your current guild and become a player of the"+ args[0] + " guild";
		}
		
		return null;
	}
	
	
	public String getDenyDescription(String[] args) {
		switch(this) {
		case FRIENDS: return "Deny guild request";
		case JOIN_GUILD_AS_PLAYER: return "Deny to become a player of the "+ args[0] + " guild";
		case JOIN_GUILD_AS_MEMBER:
			return "Deny to become a member of the "+ args[0] + " guild";
		case RENEGOCIATE_GUILD:
			return "Deny renegotiation";
		case GUILD_DUEL:
			return "Deny Guild Duel";
		case BUY_PLAYER:
			return "Deny to leave your current guild and become a player of the"+ args[0] + " guild";
		}
		return null;
	}
	
	public String getSenderMessage(String[] args) {
		switch(this) {
		case FRIENDS: return "§aYou have sent a friend request to §b" + args[1] + "§a!";
		case JOIN_GUILD_AS_PLAYER: return GuildsManager.getManager().getPrefix()+"§aYou have sent a request to §b" + args[2] + " §ato join as a player!";
		case JOIN_GUILD_AS_MEMBER:
			 return GuildsManager.getManager().getPrefix()+"§aYou have sent a request to §b" + args[1] + "§ato join as a member!";
		case RENEGOCIATE_GUILD:
			 return GuildsManager.getManager().getPrefix()+"§aYou have sent a request to §b" + args[0] + "§ato renegociate!";
		case GUILD_DUEL:
			return GuildsManager.getManager().getPrefix()+"§aYou have sent a Guild Duel request to §6§l" + args[0]+ "§a at " + args[2]+"§a!";
		case BUY_PLAYER:
			return  GuildsManager.getManager().getPrefix() +"§aYou have sent a request to" + args[2]+" to join your guild as a player!";
		}
		
		return null;
	}
	
	
	public String getReceptorMessage(String[] args) {
		switch(this) {
		case FRIENDS: return "§aThe player §b " +args[0] + "§a has sent you a friend request §7(Request will expire in 5 minutes)";
		case JOIN_GUILD_AS_PLAYER:  return GuildsManager.getManager().getPrefix()+"§aThe player§b " +args[1] + "§a has sent you a request to join §6§l"+args[0]+  " Guild §aas a Player with a salary of §6" +args[3] + " Coins§a! §7(Request will expire in 1 hour)";
		case JOIN_GUILD_AS_MEMBER:
			return GuildsManager.getManager().getPrefix()+"§aThe player§b " +args[2] + "§a has sent you a request to join §6§l"+args[0]+  " Guild §aas a Member §7(Request will expire in 1 hour)";
		case RENEGOCIATE_GUILD:
			return GuildsManager.getManager().getPrefix()+"§aThe Leader §b " +args[2] + "§a has sent you a request to renegociate your salary. New salary:  §6§l"+args[1]+  " Coins per day§7(Request will expire in 1 hour)";
		case GUILD_DUEL:
			return GuildsManager.getManager().getPrefix()+"§6§l" + args[1] + " §ahas sent you a Guild Duel request at "+ args[2]+"§a! §7(This request expires in 1 minute.)";
		case BUY_PLAYER:  return GuildsManager.getManager().getPrefix()+"§aThe player§b " +args[1] + "§a has sent you a request to join §6§l"+args[0]+  " Guild §aas a Player with a new salary of §6" +args[3] + " Coins§a! §7(Request will expire in 1 hour)";
		
		}
		
		return null;
	}
	
	public long getExpirationTime() {
		switch(this) {
		case FRIENDS: return 20L*60*5;
		case JOIN_GUILD_AS_PLAYER: return 20L*60*60;
		case JOIN_GUILD_AS_MEMBER:
			return 20L*60*60;
		case RENEGOCIATE_GUILD:
			return 20L*60*60;
		case GUILD_DUEL:
			return 20L*60;
		case BUY_PLAYER:
			return 20L*60;
		}
		return 0;
	}
	
}
