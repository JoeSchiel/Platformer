package com.MadMedley.Platformer.states;


import com.MadMedley.Platformer.main.Platformer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.MadMedley.Platformer.handlers.BoundedCamera;
import com.MadMedley.Platformer.handlers.GameStateManager;

public abstract class GameState {
	
	protected GameStateManager gsm;
	protected Platformer game;
	
	protected SpriteBatch batch;
	protected BoundedCamera camera;
	protected OrthographicCamera hudCamera;
	
	protected GameState(GameStateManager gsm){
		this.gsm = gsm;
		game = gsm.game();
		batch = game.getSpriteBatch();
		camera = game.getCamera();
		hudCamera = game.getHudCamera();
	}
	
	public abstract void handleInput();
	public abstract void update(float dt);
	public abstract void render();
	public abstract void dispose();
}
