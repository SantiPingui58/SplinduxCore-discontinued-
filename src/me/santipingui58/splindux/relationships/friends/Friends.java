package me.santipingui58.splindux.relationships.friends;

import java.util.UUID;

public class Friends {

	private UUID uuid;
	private UUID friend1;
	private UUID friend2;
	
	public Friends(UUID friend1, UUID friend2) {
		this.uuid = UUID.randomUUID();
		this.friend1 = friend1;
		this.friend2 = friend2;
	}
	
	public Friends(UUID uuid, UUID friend1, UUID friend2) {
		this.uuid = uuid;
		this.friend1 = friend1;
		this.friend2 = friend2;
	}
	
	public UUID getUUID() {
		return this.uuid;
	}
	
	public UUID getFriend1() {
		return this.friend1;
	}
	
	public UUID getFriend2() {
		return this.friend2;
	}
	 
	
}
