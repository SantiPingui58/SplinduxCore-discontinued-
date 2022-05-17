package me.santipingui58.splindux.hologram;

import net.minecraft.server.v1_12_R1.EntityArmorStand;

public class HologramID {

	private EntityArmorStand value;
	private boolean isPrimaryChangeButton;
	private boolean isSecondChangeButton;
	
	public HologramID(EntityArmorStand value, boolean isPrimaryChangeButton, boolean isSecondChangeButton) {
		this.value = value;

		this.isPrimaryChangeButton = isPrimaryChangeButton;
		this.isSecondChangeButton = isSecondChangeButton;
	}
	
	public EntityArmorStand getValue() {
		return this.value;
	}
	
	public boolean isPrimaryChangeButton() {
		return this.isPrimaryChangeButton;
	}
	
	public boolean isSecondaryChangeButton() {
		return this.isSecondChangeButton;
	}
 	
}
