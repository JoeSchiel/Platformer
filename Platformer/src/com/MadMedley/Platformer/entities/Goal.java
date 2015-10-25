package com.MadMedley.Platformer.entities;

import com.MadMedley.Platformer.main.Platformer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class Goal extends B2DSprite {

	public Goal(Body body){
		
		super(body);
		
		Texture tex = Platformer.res.getTexture("goal");
		TextureRegion[] sprites = TextureRegion.split(tex, 16, 128)[0];
		
		setAnimation(sprites, 1 / 18f); 
	}
	
}
