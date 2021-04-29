package me.santipingui58.splindux.task;

public class Task {

	private int tick;
	private int currenttick;
	private TaskType type;
	
	public Task(TaskType type, int tick) {
		this.type = type;
		this.tick = tick;
	}
	
	public int getTick() {
		return this.tick;
	}
	
	public int getCurrentTick() {
		return this.currenttick;
	}
	
	public void addCurrentTick() {
		this.currenttick = this.currenttick+1;
	}
	
	public void resetCurrentTick() {
		this.currenttick = 0;
	}
	
	public TaskType getType() {
		return this.type;
	}
}
