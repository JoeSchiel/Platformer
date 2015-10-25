package com.MadMedley.Platformer.handlers;

import java.util.Stack;

import com.MadMedley.Platformer.main.Platformer;
import com.MadMedley.Platformer.states.GameState;
import com.MadMedley.Platformer.states.LevelSelect;
import com.MadMedley.Platformer.states.Menu;
import com.MadMedley.Platformer.states.Play;

public class GameStateManager {

	private Platformer game;
	
	private Stack<GameState> gameStates;
	
	public static final int play = 5045;
	public static final int menu = 6516;
	public static final int level_select = -1514;
	
	public GameStateManager (Platformer game){
		this.game = game;
		gameStates = new Stack<GameState>();
		pushState(menu);
	}
	
	public void update(float dt){
		gameStates.peek().update(dt);
	}
	
	public void render(){
		gameStates.peek().render();
	}
	
	public Platformer game(){
		return game;
	}
	
	private GameState getState(int state){
		if(state == play) return new Play(this);
		if(state == menu) return new Menu(this);
		if(state == level_select) return new LevelSelect(this);
		return null;
	}
	
	public void setState(int state){
		popState();
		pushState(state);
	}
	
	public void popState(){
		GameState g = gameStates.pop();
		g.dispose();
	}
	
	public void pushState(int state){
		gameStates.push(getState(state));
	}
	
}
