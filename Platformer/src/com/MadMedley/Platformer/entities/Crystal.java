package com.MadMedley.Platformer.entities;

import com.MadMedley.Platformer.main.Platformer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class Crystal extends B2DSprite {

	public Crystal(Body body){
		
		super(body);
		
		Texture tex = Platformer.res.getTexture("fire");
		TextureRegion[] sprites = TextureRegion.split(tex, 32, 32)[0];
		
		setAnimation(sprites, 1 / 18f); 
	}
	
}
