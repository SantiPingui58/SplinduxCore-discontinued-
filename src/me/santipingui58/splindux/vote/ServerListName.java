package me.santipingui58.splindux.vote;


public enum ServerListName {
	MINECRAFTSERVERS_DOT_ORG,
	PLANETMINECRAFT_DOT_COM,
	MINECRAFT_HYPHEN_MP_DOT_COM,
	TOPG_DOT_ORG,
	MINECRAFT_HYPHEN_SERVER_DOT_NET;
	
	
	public String getService() {
		String s = this.name().replace("_DOT_", ".");
		s = s.replace("_HYPHEN_", "-");
		return s;
	}
	

	public static ServerListName fromString(String string) {
		for (ServerListName sln : ServerListName.values()) {
			if (sln.getService().equalsIgnoreCase(string)) {
				return sln;
			}
		}
		return null;
	}
	
	
	public String getName() {
		switch(this) {
		default:break;
		case MINECRAFTSERVERS_DOT_ORG: return "MinecraftServers";
		case PLANETMINECRAFT_DOT_COM: return "PlanetMinecraft";
		case MINECRAFT_HYPHEN_MP_DOT_COM: return "Minecraft-MP";
		case TOPG_DOT_ORG: return "TopG";
		case MINECRAFT_HYPHEN_SERVER_DOT_NET: return "Minecraft-Server";
		}
		return null;
	}
	
}
