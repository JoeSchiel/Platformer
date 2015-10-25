package com.MadMedley.Platformer.states;

import com.MadMedley.Platformer.handlers.GameButton;
import com.MadMedley.Platformer.handlers.GameStateManager;
import com.MadMedley.Platformer.main.Platformer;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class LevelSelect extends GameState {
	
	private TextureRegion reg;
	
	private GameButton[][] buttons;
	
	public LevelSelect(GameStateManager gsm) {
		
		super(gsm);
		
		reg = new TextureRegion(Platformer.res.getTexture("background"), 0, 0, 320, 240);
		
		TextureRegion buttonReg = new TextureRegion(Platformer.res.getTexture("container"), 0, 0, 32, 32);
		buttons = new GameButton[5][5];
		for(int row = 0; row < buttons.length; row++) {
			for(int col = 0; col < buttons[0].length; col++) {
				buttons[row][col] = new GameButton(buttonReg, 80 + col * 40, 200 - row * 40, camera);
				buttons[row][col].setText(row * buttons[0].length + col + 1 + "");
			}
		}
		
		camera.setToOrtho(false, Platformer.v_width, Platformer.v_height);
		
	}
	
	public void handleInput() {
	}
	
	public void update(float dt) {
		
		handleInput();
		
		for(int row = 0; row < buttons.length; row++) {
			for(int col = 0; col < buttons[0].length; col++) {
				buttons[row][col].update(dt);
				if(buttons[row][col].isClicked()) {
					Play.level = row * buttons[0].length + col + 1;
					//Platformer.res.getSound("levelselect").play();
					gsm.setState(GameStateManager.play);
				}
			}
		}
		
	}
	
	public void render() {
		
		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
		batch.draw(reg, 0, 0);
		batch.draw(Platformer.res.getTexture("clouds"), 0, 0);
		batch.draw(Platformer.res.getTexture("trees"), 0, 0);
		batch.draw(Platformer.res.getTexture("grass"), 0, 0);
		batch.end();
		
		for(int row = 0; row < buttons.length; row++) {
			for(int col = 0; col < buttons[0].length; col++) {
				buttons[row][col].render(batch);
			}
		}
		
	}
	
	public void dispose() {
		// everything is in the resource manager com.neet.blockbunny.handlers.Content
	}
	
}

