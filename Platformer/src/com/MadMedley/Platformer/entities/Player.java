package com.MadMedley.Platformer.entities;

import com.MadMedley.Platformer.main.Platformer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class Player extends B2DSprite {

	private int numCrystals;
	private int totalCrystals;
	
	public Player(Body body){
		
		super(body);
		
		playerMove();
		
		
	}
	
	public void collectCrystal(){
		numCrystals++;
	}
	
	public int getNumCrystals(){
		return numCrystals;
	}
	
	public void setTotalCrystals(int i){
		totalCrystals = i;
	}
	
	public int getTotalCrystals(){
		return totalCrystals;
	}
	
	public void playerMove(){
		Texture tex = Platformer.res.getTexture("runnersp");
		TextureRegion[] sprites = TextureRegion.split(tex, 32 , 32)[0];
		setAnimation(sprites, 1 / 12f);
	}
	
	public void playerJump(){
		Texture tex = Platformer.res.getTexture("runnerjump");
		TextureRegion[] sprites = TextureRegion.split(tex, 32 , 32)[0];
		setAnimation(sprites, 1 / 12f);
	}
	
	
}
