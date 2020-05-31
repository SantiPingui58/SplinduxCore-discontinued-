package me.santipingui58.splindux.task;


public enum TaskType {
	ARENA,MINUTE,ONLINE,MOVE,SCOREBOARD,RANKING,TAB;
	
	
	
	public int getTicks() {		
		switch(this) {
		default:return 0;
		case ARENA: return 4;
		case MINUTE: return 240;
		case MOVE: return 1;
		case ONLINE: return 4;
		case SCOREBOARD: return 4;
		case RANKING: return 480;
		case TAB: return 12;
		}
		 
	}
	
	
	public boolean async() {
		switch(this) {
		default:break;
		case ARENA: return false;
		case MINUTE: return false;
		case MOVE: return false;
		case ONLINE: return false;
		case SCOREBOARD: return false;
		case RANKING: return true;
		case TAB: return false;
		}
		
		return false;	
	}
	
}
