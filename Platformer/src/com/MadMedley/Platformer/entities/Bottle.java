package com.MadMedley.Platformer.entities;

import com.MadMedley.Platformer.main.Platformer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class Bottle extends B2DSprite {

	public Bottle(Body body){
		
		super(body);
		
		Texture tex = Platformer.res.getTexture("bottle");
		TextureRegion[] sprites = TextureRegion.split(tex, 8, 16)[0];
		
		setAnimation(sprites, 1 / 18f); 
	}
	
}