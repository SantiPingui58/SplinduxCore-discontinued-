package me.santipingui58.splindux.hologram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import me.santipingui58.splindux.stats.SpleefRankingPeriod;
import me.santipingui58.splindux.stats.SpleefRankingType;
import net.minecraft.server.v1_12_R1.EntityArmorStand;

public class HologramViewer {

	private UUID player;
	private HashMap<HologramType,Set<HologramID>> hologram_ids;
	
	private HologramSubType hologramSubType;
	private SpleefRankingPeriod srp;
	private SpleefRankingType type;
	
	public HologramViewer(HologramType type,UUID player) {
		this.player = player;
		hologramSubType = type.equals(HologramType.GAME_STATS) ? HologramSubType.FFA : HologramSubType.RANKING;
		this.hologram_ids = new HashMap<HologramType, Set<HologramID>>();
		this.srp = SpleefRankingPeriod.WEEKLY;
		this.type = hologramSubType.equals(HologramSubType.FFA) ? SpleefRankingType.WINS : hologramSubType.equals(HologramSubType.DUELS) ? SpleefRankingType.ELO : null;
	}
	
	public static HologramViewer getHologramViewer(UUID sp,HologramType h) {
		RankingHologram hologram = HologramManager.getManager().getHologram(h);
		if (hologram.getViewersCache().containsKey(sp)) {
			return hologram.getViewersCache().get(sp);
		} else {
			HologramViewer viewer = new HologramViewer(h, sp);
			hologram.getViewersCache().put(sp, viewer);
			return viewer;
		}
	}
	
	public void setHologramSubType(HologramSubType t) {
		this.hologramSubType = t;	
	}
	
	public SpleefRankingType getSpleefRankingType() {
		return this.type;
	}
	
	public void setSpleefRankingType(SpleefRankingType type) {
		this.type = type;
	}
	
	public SpleefRankingPeriod getPeriod() {
		return this.srp;
	}
	
	public HologramSubType getHologramSubType() {
		return this.hologramSubType;
	}
	
	public UUID getPlayer() {
		return this.player;
	}
	
	public Set<HologramID> getHologramIdsOf(HologramType h) {
		return this.hologram_ids.get(h);
	}

	public HashMap<HologramType, Set<HologramID>> getHologramIds() {
		return this.hologram_ids;
	}
	
	public List<HologramID> getHologramsIdPrimaryButton(HologramType h) {
		List<HologramID> set = new ArrayList<HologramID>();
		for (HologramID id : this.hologram_ids.get(h)) {
			if (id.isPrimaryChangeButton()) set.add(id);
		}
		return set;
	}
	
	public void cleanHologramsId(HologramType h) {
		this.hologram_ids.get(h).clear();
	}
	

	
	
	public Set<HologramID> getHologramsIdSecondaryButton(HologramType h) {
		Set<HologramID> set = new HashSet<HologramID>();
		for (HologramID id : this.hologram_ids.get(h)) {
			if (id.isSecondaryChangeButton()) set.add(id);
		}
		return set;
	}
	
	public EntityArmorStand getArmorStand(int id,HologramType h) {
		for (HologramID hid: getHologramIds().get(h)) {
			if (hid.getValue().getId()==id) {
				return hid.getValue();
			}
		}
		return null;
	}
	
	public boolean isPrimaryButton(int id,HologramType h) {
		for (HologramID hid : getHologramsIdPrimaryButton(h)) {
			
			if (hid.getValue().getId()==id) {
				return hid.isPrimaryChangeButton();
			}
		}
		return false;
	}
	
	public boolean isSecondaryButton(int id,HologramType h) {
		for (HologramID hid : getHologramsIdSecondaryButton(h)) {
	
			if (hid.getValue().getId()==id) {
				return hid.isSecondaryChangeButton();
			}
		}
		return false;
	}

	public void setPeriod(SpleefRankingPeriod type2) {
		this.srp = type2;
	}

	
}
