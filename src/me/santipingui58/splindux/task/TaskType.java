package me.santipingui58.splindux.task;


public enum TaskType {
	ARENA,MINUTE,ONLINE,HIGH_MOVE,LOW_MOVE,SCOREBOARD,RANKING,TAB;
	
	
	
	public int getTicks() {		
		switch(this) {
		default:return 0;
		case ARENA: return 5; //10
		case MINUTE: return 240; //600
		case HIGH_MOVE: return 1;
		case LOW_MOVE: return 3;
		case ONLINE: return 5; //10
		case SCOREBOARD: return 5; //10
		case RANKING: return 3000; //1200
		case TAB: return 15; //30
		}
		 
	}
	
	
	public boolean async() {
		switch(this) {
		default:break;
		case ARENA: return false;
		case MINUTE: return false;
		case HIGH_MOVE: return false;
		case LOW_MOVE: return false;
		case ONLINE: return false;
		case SCOREBOARD: return false;
		case RANKING: return true;
		case TAB: return false;
		}
		
		return false;	
	}
	
}
