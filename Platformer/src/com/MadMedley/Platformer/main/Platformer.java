package com.MadMedley.Platformer.main;

import com.MadMedley.Platformer.handlers.BoundedCamera;
import com.MadMedley.Platformer.handlers.Content;
import com.MadMedley.Platformer.handlers.GameStateManager;
import com.MadMedley.Platformer.handlers.MyInput;
import com.MadMedley.Platformer.handlers.MyInputProcessor;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Platformer implements ApplicationListener {
	
	public static final String TITLE = "Platformer";
	public static final int v_width = 320;
	public static final int v_height = 240;
	public static final int scale = 2;
	
	public static final float step = 1 / 60f;
	//private float accum;
	
	private SpriteBatch batch;
	private BoundedCamera camera;
	private OrthographicCamera hudCamera;

	private GameStateManager gsm;
	
	public static Content res;
	
	@Override
	public void create() {	
		
		//setEnforcepotimages
		Gdx.input.setInputProcessor(new MyInputProcessor());
		
		res = new Content();
		res.loadTexture("res/images/menu.png", "menu");
		//res.loadTexture("res/images/victory.png", "victory");
		res.loadTexture("res/images/hud.png", "hud");
		res.loadTexture("res/images/bgs.png" , "bg");
		res.loadTexture("res/images/background.png" , "background");
		res.loadTexture("res/images/clouds.png" , "clouds");
		res.loadTexture("res/images/runnersp.png", "runnersp");
		res.loadTexture("res/images/runnerjump32.png", "runnerjump");
		res.loadTexture("res/images/firesprite.png", "fire");
		res.loadTexture("res/images/healthsp2.png", "health");
		res.loadTexture("res/images/goaltrigger.png", "goal");
		res.loadTexture("res/images/container.png", "container");
		res.loadTexture("res/images/bottle.png", "bottle");
		res.loadTexture("res/images/platformW9.png", "platform");
		res.loadTexture("res/images/spikes.png", "spikes");
		res.loadTexture("res/images/x.png", "x");
		res.loadTexture("res/images/splash.png", "splash");
		res.loadTexture("res/images/trees.png", "trees");
		res.loadTexture("res/images/grass.png", "grass");
		
		camera = new BoundedCamera();
		camera.setToOrtho(false, v_width, v_height);
		hudCamera = new OrthographicCamera();
		hudCamera.setToOrtho(false, v_width, v_height);
		
		batch = new SpriteBatch();
		gsm = new GameStateManager(this);

	}

	public void render() {		
		
		//accum += Gdx.graphics.getDeltaTime();
		//while (accum >= step){
			//accum -= step;
			gsm.update(Gdx.graphics.getDeltaTime());
			gsm.render();
			MyInput.update();
		//}	
	}
	
	public void dispose() {
		//res.removeAll();
	}
	
	public SpriteBatch getSpriteBatch() {
		return batch;
	}
	
	public BoundedCamera getCamera(){
		return camera;
	}
	
	public OrthographicCamera getHudCamera(){
		return hudCamera;
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
