package com.MadMedley.Platformer.entities;

import com.MadMedley.Platformer.main.Platformer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class Spikes extends B2DSprite {

	public Spikes(Body body){
		
		super(body);
		
		Texture tex = Platformer.res.getTexture("spikes");
		TextureRegion[] sprites = TextureRegion.split(tex, 8, 32)[0];
		
		setAnimation(sprites, 1 / 18f); 
	}
	
}