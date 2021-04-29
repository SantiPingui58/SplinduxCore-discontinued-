package me.santipingui58.splindux.vote;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.santipingui58.splindux.utils.Utils;

public enum Rewarded {
	NAMEMC,YOUTUBE,TWITTER,TWITCH,DISCORD,
	MINECRAFTSERVERS_DOT_ORG,
	PLANETMINECRAFT_DOT_COM,
	MINECRAFT_HYPHEN_MP_DOT_COM,
	MINECRAFT_HYPHEN_SERVER_DOT_NET,
	TOPG_DOT_ORG,
	YOUTUBE_VIDEO;
	
	
	public ServerListName toServerListName() {
		
		if (isServerList()) return ServerListName.valueOf(this.name());
		
		return null;
	}
	
	
	public String getSkullURL() {
		switch(this) {
		 default: break;
		case NAMEMC: return "http://textures.minecraft.net/texture/56cb657381ee96f5eade4c730ee1a1b14552765f1dee2bcfdae175792b016fb";
		case DISCORD: return "http://textures.minecraft.net/texture/7873c12bffb5251a0b88d5ae75c7247cb39a75ff1a81cbe4c8a39b311ddeda";
		case TWITTER: return "http://textures.minecraft.net/texture/dcb76166d1e1e449457b5c4436b3f48b7d768ac60f19e2c6b25ea42c4bad7c";
		case YOUTUBE: return "http://textures.minecraft.net/texture/b4353fd0f86314353876586075b9bdf0c484aab0331b872df11bd564fcb029ed";
		case YOUTUBE_VIDEO: return "http://textures.minecraft.net/texture/b4353fd0f86314353876586075b9bdf0c484aab0331b872df11bd564fcb029ed";
		case TWITCH: return "http://textures.minecraft.net/texture/2f18fa43d4d9378948b56b85b531979074119c125232e715ce4bd52780ac4d76";
		}
		return "";
	}

	
	public boolean isServerList() {
		if (this.equals(MINECRAFTSERVERS_DOT_ORG) || this.equals(PLANETMINECRAFT_DOT_COM) || this.equals(MINECRAFT_HYPHEN_MP_DOT_COM)
				|| this.equals(TOPG_DOT_ORG)  || this.equals(MINECRAFT_HYPHEN_SERVER_DOT_NET)) return true;
		return false;
	}
	
	public boolean isDailyClaimeable() {
		if (this.equals(NAMEMC) || this.equals(YOUTUBE) || this.equals(TWITCH) || this.equals(TWITTER) || this.equals(DISCORD)) return false;
		return true;
	}
	
	public VoteRewards getRewards() {
		
		if (isDailyClaimeable()) {
			return new VoteRewards(this,100,25,null);
		} else {
			HashMap<Integer,Integer> hashmap = new HashMap<Integer,Integer>();
			hashmap.put(1, 3);
		switch(this) {
		default: break;
		case NAMEMC: 
			return new VoteRewards(NAMEMC,300,50,hashmap);
		case DISCORD: 
			return new VoteRewards(DISCORD,400,50,hashmap);
		case YOUTUBE:
			return new VoteRewards(YOUTUBE,75,25,hashmap);			
		case TWITTER:
			return new VoteRewards(TWITTER,75,25,hashmap);
		case TWITCH:
			return new VoteRewards(TWITCH,75,25,hashmap);
		}
		}
		return null;
	}
	
	public ChatColor getColor() {
		
		if (isServerList()) {
			return ChatColor.YELLOW;
		} else {
		switch(this) {
		default: break;
		case YOUTUBE: return ChatColor.RED;
		case YOUTUBE_VIDEO: return ChatColor.RED;
		case TWITCH: return ChatColor.DARK_PURPLE;
		case TWITTER: return ChatColor.AQUA;
		case DISCORD: return ChatColor.BLUE;
		case NAMEMC: return ChatColor.WHITE;
		}
		}
		return null;
	}
	
	public int getSlotInMenu() {
		switch(this) {
		default:break;
		case TWITCH: return 20;
		case YOUTUBE: return 21;
		case DISCORD: return 22;
		case TWITTER: return 23;	
		case NAMEMC: return 24;
		case MINECRAFTSERVERS_DOT_ORG:return 29;
		case PLANETMINECRAFT_DOT_COM: return 30;
		case MINECRAFT_HYPHEN_MP_DOT_COM: return 31;
		case TOPG_DOT_ORG: return 32;		
		case MINECRAFT_HYPHEN_SERVER_DOT_NET: return 33;
		case  YOUTUBE_VIDEO: return 14;
		}
		return 0;
	}
	
	
	public static Rewarded getBySlot(int slot) {
		for (Rewarded r : Rewarded.values()) {
			if (r.getSlotInMenu()==slot) {
				return r;
			}
		}
		return null;
	}
	
	public String getClaimMessage() {
		if (isServerList()) {
			return "voting for our Server!";
		} if (this.equals(YOUTUBE_VIDEO)) {
			return "watching a video";
		}else {
		switch(this) {
		default: break;
		case YOUTUBE: return "visiting our Youtube Channel";
		case TWITCH: return "visiting our Twitch Channel";
		case TWITTER: return "visiting our Twitter";
		case DISCORD: return "joining our Discord Server";
		}
		}
		return null;
	}

	public String getURL() {
		if (isServerList()) {
			int number = 0;
			switch(this) {
			default:break;
			case MINECRAFTSERVERS_DOT_ORG:number = 1; break;
			case PLANETMINECRAFT_DOT_COM:number=2; break;
			case MINECRAFT_HYPHEN_MP_DOT_COM:number=3;break;
			case MINECRAFT_HYPHEN_SERVER_DOT_NET:number=5;break;
			case TOPG_DOT_ORG:number=4;break;
				}
			
			return "http://vote"+number+".splindux.com";
		} else {
		switch(this) {
		default: break;
		case NAMEMC: return "https://es.namemc.com/server/mc.splindux.com";
		case YOUTUBE: return "http://www.youtube.com/subscription_center?add_user=santipingui";
		case YOUTUBE_VIDEO: return "https://youtu.be/6OS_IqX32aU";
		case TWITCH: return "https://www.twitch.tv/SantiPingui58";
		case TWITTER: return "https://twitter.com/intent/user?screen_name=SplinduxServer";
		case DISCORD: return "http://discord.splindux.com";
		 
		}
		return null;
	}
	}


	public String getName() {
		if (isServerList()) {
			return toServerListName().getName();
		} else {
			return this.name();
		}
	}


	public ItemStack getItem() {
		ItemStack item = null;
		if (isServerList()) {
			item = new ItemStack(Material.DIAMOND);
			ItemMeta meta = item.getItemMeta();
			ServerListName sln = toServerListName();
			String name = sln.getName();
			meta.setDisplayName("§eLike us at §b"+ name);
			item.setItemMeta(meta);
		} else {
			String title = "";
			if (equals(DISCORD)) {
				title = "§eJoin our "+ getColor() + toString();
			} else if (equals(YOUTUBE_VIDEO)) {
				title = "§eWatch a video";
			}else {
				if (equals(NAMEMC)) {
					title = "§eLike us at "+ getColor() + toString();
				} else {
				title = "§eFollow us at "+ getColor() + toString();
				}
			}
			
			 item = Utils.getUtils().getSkull(getSkullURL(), title);
		}
		
		return item;
		
	}
}
