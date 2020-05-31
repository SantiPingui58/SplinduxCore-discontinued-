package me.santipingui58.splindux.particles;

import java.util.ArrayList;
import java.util.List;

import me.santipingui58.splindux.particles.effect.ParticleEffect;
import me.santipingui58.splindux.particles.effect.ParticleEffectType;
import me.santipingui58.splindux.particles.type.ParticleType;
import me.santipingui58.splindux.particles.type.ParticleTypeSubType;

public class ParticleManager {

	private static ParticleManager manager;	
	 public static ParticleManager getManager() {
	        if (manager == null)
	        	manager = new ParticleManager();
	        return manager;
	    }
	
	 
	 private List<ParticleEffect> effects = new ArrayList<ParticleEffect>();
	 private List<ParticleType> types = new ArrayList<ParticleType>();
	 
	 
	 public List<ParticleEffect> getEffects() {
		 return this.effects;
	 }
	 
	 public List<ParticleType> getTypes() {
		 return this.types;
	 }
	 
	 public ParticleEffect getEffectByType(ParticleEffectType type) {
		 for (ParticleEffect effect : this.effects) {
			 if (type.equals(effect.getType())) {
				 return effect;
			 }
		 }
		 return null;
	 }
	 
	 public ParticleType getTypeBySubType(ParticleTypeSubType subtype) {
		 for (ParticleType type : this.types) {
			 if (subtype.equals(type.getType())) {
				 return type;
			 }
		 }
		 return null;
	 }
	 
	 public void loadEffectsAndTypes() {
		 
		 for (ParticleEffectType type : ParticleEffectType.values()) {
			 boolean vip = false;
			 boolean epic = false;
			 boolean ext = false;
			 if (type.equals(ParticleEffectType.BATMAN) || type.equals(ParticleEffectType.INVOCATION)) {
				 ext =true;
			 } else if (type.equals(ParticleEffectType.TELEPORT)) {
				 epic =true;
			 } else if (type.equals(ParticleEffectType.BLOCKBREAK) || type.equals(ParticleEffectType.TRAIL) || type.equals(ParticleEffectType.WINGS)) {
				 vip = true;
			 }
			 
			 ParticleEffect effect = new ParticleEffect(type, vip,epic,ext);
			 this.effects.add(effect);
		 }
		 
		 for (ParticleTypeSubType stype : ParticleTypeSubType.values()) {
			 boolean vip = false;
			 if (stype.equals(ParticleTypeSubType.ANGRY_VILLAGER) || stype.equals(ParticleTypeSubType.DAMAGE_INDICATOR) || stype.equals(ParticleTypeSubType.END_ROD) || 
					 stype.equals(ParticleTypeSubType.FLAME) || stype.equals(ParticleTypeSubType.LARGE_SMOKE)) {
				 vip = true;
			 }
			 ParticleType type = new ParticleType(stype, vip,false,false);
			 this.types.add(type);
		 }
	 }


}
