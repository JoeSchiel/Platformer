package com.MadMedley.Platformer.entities;

import com.MadMedley.Platformer.main.Platformer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class Splash extends B2DSprite {
	
	public Splash(Body body){
		
		super(body);
		
		Texture tex = Platformer.res.getTexture("splash");
		TextureRegion[] sprites = TextureRegion.split(tex, 32, 32)[0];
		
		setAnimation(sprites, 1 / 22f); 
		
	}
	
	public int getSplashTimesPLayed(){
		return animation.getTimesPlayed();
	}
}
