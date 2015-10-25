package com.MadMedley.Platformer.entities;

import com.MadMedley.Platformer.handlers.Animation;
import com.MadMedley.Platformer.handlers.B2DVars;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class B2DSprite {
	
	protected Body body;
	protected Animation animation;
	protected float width;
	protected float height;
	
	public B2DSprite(Body body){
		this.body = body;
		animation = new Animation();
	}
	
	public void setAnimation(TextureRegion reg, float delay) {
		setAnimation(new TextureRegion[] { reg }, delay);
	}
	
	public void setAnimation(TextureRegion[] reg, float delay){
		animation.setFrames(reg, delay);
		width = reg[0].getRegionWidth();
		height = reg[0].getRegionHeight();
	}
	
	public void update(float dt){
		animation.update(dt);
	}
	
	public void render(SpriteBatch batch){
		batch.begin();
		batch.draw(	animation.getFrame(),
					body.getPosition().x * B2DVars.ppm - width / 2,
					body.getPosition().y * B2DVars.ppm - height / 2);
		batch.end();
	}
	
	public Body getBody(){
		return body;
	}
	
	
	public Vector2 getPosition(){
		return body.getPosition();
	}
	
	public float getWidth(){
		return width;
	}
	
	public float getHeight(){
		return height;
	}

}
