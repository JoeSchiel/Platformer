package com.MadMedley.Platformer.states;

import static com.MadMedley.Platformer.handlers.B2DVars.ppm;

import com.MadMedley.Platformer.handlers.Animation;
import com.MadMedley.Platformer.handlers.Background;
import com.MadMedley.Platformer.handlers.GameButton;
import com.MadMedley.Platformer.handlers.GameStateManager;
import com.MadMedley.Platformer.handlers.Vars;
import com.MadMedley.Platformer.main.Platformer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class Menu extends GameState {
	
	private boolean debug = false;
	
	private Background bg;
	private Animation animation;
	private GameButton playButton;
	
	private World world;
	private Box2DDebugRenderer b2dRenderer;
	
	public Menu(GameStateManager gsm) {
		
		
		super(gsm);
		
		Vars.putBottleInv(); //load variables on start up
		Vars.putPlatformInv();
		Vars.putHealthInv();
		Vars.putLinVel();
		
		Texture tex = Platformer.res.getTexture("menu");
		bg = new Background(new TextureRegion(tex), camera, 1f);
		bg.setVector(-20, 0);
		
		tex = Platformer.res.getTexture("runnersp");
		TextureRegion[] reg = new TextureRegion[5];
		for(int i = 0; i < reg.length; i++) {
			reg[i] = new TextureRegion(tex, i * 32, 0, 32, 32);
		}
		
		animation = new Animation(reg, 1 / 12f);
		
		tex = Platformer.res.getTexture("hud");
		playButton = new GameButton(new TextureRegion(tex, 0, 34, 58, 27), 160, 100, camera);
		
		camera.setToOrtho(false, Platformer.v_width, Platformer.v_height);
		
		world = new World(new Vector2(0, -9.8f * 5), true);
		//world = new World(new Vector2(0, 0), true);
		b2dRenderer = new Box2DDebugRenderer();
		
	}
		
	//}
	
	public void handleInput() {
		
		// mouse/touch input
		if(playButton.isClicked()) {
			//Platformer.res.getSound("crystal").play();
			gsm.setState(GameStateManager.level_select);
		}
		
	}
	
	public void update(float dt) {
		
		handleInput();
		
		world.step(dt / 5, 8, 3);
		
		bg.update(dt);
		animation.update(dt);
		
		playButton.update(dt);
		
	}
	
	public void render() {
		
		batch.setProjectionMatrix(camera.combined);
		
		// draw background
		bg.render(batch);
		
		// draw button
		playButton.render(batch);
		
		// draw player
		batch.begin();
		batch.draw(animation.getFrame(), 146, 31);
		batch.end();
		
		// debug draw box2d
		if(debug) {
			camera.setToOrtho(false, Platformer.v_width / ppm, Platformer.v_height / ppm);
			b2dRenderer.render(world, camera.combined);
			camera.setToOrtho(false, Platformer.v_width, Platformer.v_height);
		}
		
	}
	
	public void dispose() {
		// everything is in the resource manager com.neet.blockbunny.handlers.Content
	}
	
}
