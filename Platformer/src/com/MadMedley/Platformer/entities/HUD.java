package com.MadMedley.Platformer.entities;

import com.MadMedley.Platformer.main.Platformer;
import com.MadMedley.Platformer.states.Play;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class HUD {
	
	//private Player player;
	
	private TextureRegion container;
	//private TextureRegion[] blocks;
	private TextureRegion health;
	private TextureRegion bottle;
	private TextureRegion platform;
	private TextureRegion textureX;
	private TextureRegion[] font;
	
	public HUD(Player player) {
		
		//this.player = player;
		
		Texture tex = Platformer.res.getTexture("hud");
		Texture tex2 = Platformer.res.getTexture("health");
		Texture tex3 = Platformer.res.getTexture("container");
		Texture tex4 = Platformer.res.getTexture("bottle");
		Texture tex5 = Platformer.res.getTexture("platform");
		Texture texX = Platformer.res.getTexture("x");
		
		
		container = new TextureRegion(tex3, 0, 0, 32, 32);
		
		/*
		blocks = new TextureRegion[3];
		for(int i = 0; i < blocks.length; i++) {
			blocks[i] = new TextureRegion(tex, 32 + i * 16, 0, 16, 16);
		}
		*/
		
		health = new TextureRegion(tex2, 16, 16);
		bottle = new TextureRegion(tex4, 8, 16);
		platform = new TextureRegion(tex5, 32, 9);
		textureX = new TextureRegion(texX, 7, 7);
		
		font = new TextureRegion[11];
		for(int i = 0; i < 6; i++) {
			font[i] = new TextureRegion(tex, 32 + i * 9, 16, 9, 9);
		}
		for(int i = 0; i < 5; i++) {
			font[i + 6] = new TextureRegion(tex, 32 + i * 9, 25, 9, 9);
		}
		
		
	}
	
	public void render(SpriteBatch batch) {
		
		batch.begin();
		
		// draw container
		batch.draw(container, 5, 200);
		batch.draw(container, 42, 200);
		batch.draw(textureX, 9, 190);
		batch.draw(textureX, 47, 190);
		
		// draw platform and bottle qty
		drawString(batch, Play.bottleInv + " ", 22 , 190);
		drawString(batch, Play.platformInv + " ", 60 , 190);
		
		//draw bottle
		if(Play.bottleInv > 0){
			batch.draw(bottle, 17, 208);
		}
		
		//draw platform
		if(Play.platformInv > 0){
			batch.draw(platform, 44, 210, 28, 9);
		}
		
		//draw health
		for(int i = 0; i < Play.health ; i++){
			batch.draw(health, 80 +(i*20), 208);
		}
		
		batch.end();
		
	}
	
	private void drawString(SpriteBatch sb, String s, float x, float y) {
		for(int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if(c == '/') c = 10;
			else if(c >= '0' && c <= '9') c -= '0';
			else continue;
			sb.draw(font[c], x + i * 9, y);
		}
	}
	
}
