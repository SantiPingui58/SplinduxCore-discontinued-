package me.santipingui58.splindux.game.spleef;

public class QueueSize {

	private SpleefType spleefType;
	private GameType gameType;
	private boolean isranked;
	
	private int playingSize;
	private int queueSize;
	
	public QueueSize(SpleefType spleefType, GameType gameType, boolean isRanked) {
		this.spleefType= spleefType;
		this.gameType = gameType;
		this.isranked = isRanked;
		this.playingSize = 0;
		this.queueSize = 0;
	}
	
	public int getPlayingSize() {
		return this.playingSize;
	}
	
	public void setPlayingSize(int i ) {
		this.playingSize = i;
	}
	
	
	public int getQueueSize() {
		return this.queueSize;
	}
	
	public void setQueueSize(int i ) {
		this.queueSize = i;
	}
	
	public SpleefType getSpleefType() {
		return this.spleefType;
		
	}
	
	public GameType getGameType() {
		return this.gameType;
	}
	
	public boolean isRanked() {
		return this.isranked;
	}
}
