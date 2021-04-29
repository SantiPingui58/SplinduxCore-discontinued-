package me.santipingui58.splindux.relationships.friends;

import java.util.UUID;



public class Friendship {

	private UUID player1;
	private UUID player2;
	
	public Friendship(UUID player1, UUID player2) {
		this.player1 = player1;
		this.player2 = player2;
		FriendsManager.getManager().getFriendships().add(this);
	}
	
	public UUID getPlayer1() {
		return this.player1;
	}
	
	public UUID getPlayer2() {
		return this.player2;
	}
	
}
