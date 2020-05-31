package me.santipingui58.splindux.game;

import me.santipingui58.splindux.DataManager;
import me.santipingui58.splindux.game.spleef.SpleefPlayer;
import me.santipingui58.translate.Language;

public class PlayerOptions {

	private boolean night_vision;
	private boolean translate;
	private Language language;
	private boolean ads;
	public PlayerOptions() {
		this.translate = true;
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
