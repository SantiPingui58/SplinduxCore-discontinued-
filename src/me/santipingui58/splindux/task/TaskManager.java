package me.santipingui58.splindux.task;

import me.santipingui58.splindux.Main;
import me.santipingui58.splindux.task.tasks.ActionBarTask;
import me.santipingui58.splindux.task.tasks.ArenaTask;
import me.santipingui58.splindux.task.tasks.HighMoveTask;
import me.santipingui58.splindux.task.tasks.LowMoveTask;
import me.santipingui58.splindux.task.tasks.MinuteTask;
import me.santipingui58.splindux.task.tasks.OnlineTask;
import me.santipingui58.splindux.task.tasks.RankedTask;
import me.santipingui58.splindux.task.tasks.RankingTask;
import me.santipingui58.splindux.task.tasks.ScoreboardTask;
import me.santipingui58.splindux.task.tasks.SpleggTask;
import me.santipingui58.splindux.task.tasks.StressingTask;
import me.santipingui58.splindux.task.tasks.TNTRunTask;


public class TaskManager {

	private static TaskManager manager;	
	 public static TaskManager getManager() {
	        if (manager == null)
	        	manager = new TaskManager();
	        return manager;
	    }
	 
	 public void task() {
		 new ArenaTask();
		 new HighMoveTask();
		 new LowMoveTask();
		 new MinuteTask();
		 new OnlineTask();
		 new RankingTask();
		 new ScoreboardTask();
		 new ActionBarTask();
		 new SpleggTask();
		 new TNTRunTask();
		 new RankedTask();
		 if (Main.stressing) new StressingTask();
	 }
	 


}
