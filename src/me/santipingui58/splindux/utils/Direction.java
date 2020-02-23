package me.santipingui58.splindux.utils;

import org.bukkit.Location;

public enum Direction {
NORTH,WEST,SOUTH,EAST;

	private static final Direction[] directions = {SOUTH, WEST, NORTH, EAST};
	
	
	public static Direction getDirection(Location location) {
        return getDirection(location.getYaw());
    }
	
	 public static Direction getDirection(float yaw) {
         return directions[((int)(yaw+45F)%360)/90];
     }

}
