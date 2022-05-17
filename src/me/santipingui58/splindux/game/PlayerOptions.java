package me.santipingui58.splindux.game;

import org.bukkit.ChatColor;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.translate.Language;

public class PlayerOptions {

	private boolean night_vision;
	private boolean translate;
	private Language language;
	private boolean ads;
	private boolean onJoinMessage;
	private ChatColor defaultColorChat;
	private String rankedArena;
	private boolean chat = true;
	private int duelPermission;
	private int duelNotification;
	private int msgPermission;
	
	public PlayerOptions() {
		this.translate = true;
		this.onJoinMessage = true;
		this.defaultColorChat = ChatColor.AQUA;
		this.ads = true;
		this.chat = true;
	}
	
	public PlayerOptions(boolean night_vision, boolean translate, Language language, boolean ads, boolean onJoinMessage, ChatColor defaultColorChat,String rankedArena,int duelPermission,
			int duelNotification, int msgPermission) {
		this.night_vision = night_vision;
		this.translate = translate;
		this.language = language;
		this.ads = ads;
		this.onJoinMessage = onJoinMessage;
		this.defaultColorChat = defaultColorChat;
		this.rankedArena = rankedArena;
		this.duelNotification = duelNotification;
		this.msgPermission = msgPermission;
		this.duelPermission = duelPermission;
	}
	
	public int getDuelPermission() {
		return this.duelPermission;
	}
	
	public int getMsgPermission() {
		return this.msgPermission;
	}
	
	public int getDuelNotification() {
		return this.duelNotification;
	}
	
	public void setDuelPermission(int i) {
	duelPermission = i;
	}
	
	public void setMsgPermission(int i) {
		this.msgPermission = i;
	}
	
	public void setDuelNotification(int i) {
		this.duelNotification = i;
	}
	

	public boolean hasChat() {
		return this.chat;
	}
	
	public void chat(boolean b) {
		this.chat = b;
	}
	
	public String getRankedArena() {
		return this.rankedArena;
	}
	
	public void setRankedArena(String s) {
		this.rankedArena = s;
	}
	
	public boolean joinMessageEnabled() {
		return this.onJoinMessage;
	}
	
	public void joinMessage(boolean b) {
		this.onJoinMessage = b;
	}
	
	public ChatColor getDefaultColorChat() {
		if (this.defaultColorChat==null) this.defaultColorChat= ChatColor.BLUE;
		return this.defaultColorChat;
	}
	
	public void setDefaultColorChat(ChatColor c) {
		this.defaultColorChat = c;
	}
	
	public boolean hasAds() {
		return this.ads;
	}
	
	public void ads(boolean b) {
		this.ads = b;
	}
	
	public boolean hasNightVision() {
		return this.night_vision;
	}
	
	public void nightVision(boolean b) {
		this.night_vision = b;
	}
	
	public boolean hasTranslate() {
		return this.translate;
	}
	
	public void translate(boolean b) {
		this.translate = b;
	}
	
	public Language getLanguage() {
		if (this.language==null) {
			this.language = DataManager.getManager().languageFromCountry(getPlayer().getCountry());
		}
		return this.language;
	}
	
	public void setLanguage(Language l) {
		this.language = l;
	}
	
	public SpleefPlayer getPlayer() {
		for (SpleefPlayer sp : DataManager.getManager().getPlayers()) {
			if (sp.getOptions().equals(this)) {
				return sp;
			}
		}
		return null;
	}
}
