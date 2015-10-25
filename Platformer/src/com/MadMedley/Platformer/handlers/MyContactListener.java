package com.MadMedley.Platformer.handlers;

import com.MadMedley.Platformer.states.Play;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;

public class MyContactListener implements ContactListener{
	
	private int numFootContacts;
	private int numGoalContacts;
	private int numPlatContacts;
	private Array<Body> cBodiesToRemove;
	private Array<Body> hBodiesToRemove;
	//private boolean playerDead;
	private boolean bottleTouch;
	
	public MyContactListener(){
		super();
		cBodiesToRemove = new Array<Body>();
		hBodiesToRemove = new Array<Body>();
		bottleTouch = false;
		
		
	}
	
	//called when two fixtures start to collide
	public void beginContact(Contact c){
		
		Fixture fa = c.getFixtureA();
		Fixture fb = c.getFixtureB();
		
		if (fa == null || fb == null){
			return;
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("foot")){
			numFootContacts++;
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("foot")){
			numFootContacts++;
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("goal")){
			numGoalContacts++;
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("goal")){
			numGoalContacts++;
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("platform")){
			numPlatContacts++;
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("platform")){
			numPlatContacts++;
		}

		if(fa.getUserData() != null && fa.getUserData().equals("player") &&
		   fb.getUserData() != null && fb.getUserData().equals("crystal")){
				cBodiesToRemove.add(fb.getBody());
				Play.health--;
				}
		
		if(fa.getUserData() != null && fa.getUserData().equals("bottle") &&
		   fb.getUserData() != null && fb.getUserData().equals("crystal")){
				cBodiesToRemove.add(fb.getBody());
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("bottle")){
				bottleTouch = true;
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("bottle")){
				bottleTouch = true;
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("player") &&
		   fb.getUserData() != null && fb.getUserData().equals("spikes")){
				Play.health--;
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("health")){
				Play.health++;
				hBodiesToRemove.add(fa.getBody());
		}
		
		
		if(fb.getUserData() != null && fb.getUserData().equals("health")){
				Play.health++;
				hBodiesToRemove.add(fb.getBody());
		}
		
	}
	
	//called when two fixtures no longer collide
	public void endContact(Contact c){
		
		Fixture fa = c.getFixtureA();
		Fixture fb = c.getFixtureB();
		
		if (fa == null || fb == null){
			return;
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("foot")){
			numFootContacts--;
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("foot")){
			numFootContacts--;
		}
		
		if(fa.getUserData() != null && fa.getUserData().equals("platform")){
			numPlatContacts--;
		}
		
		if(fb.getUserData() != null && fb.getUserData().equals("platform")){
			numPlatContacts--;
		}
	}
	
	public boolean isPlayerOnGround(){
		return numFootContacts > 0;
	}
	
	public boolean PlayerTouchGoal(){
		return numGoalContacts > 0;
	}
	
	public boolean playerTouchPlatform(){
		return numPlatContacts > 0;
	}
	
	public Array<Body> getCBodiesToRemove(){
		return cBodiesToRemove;
	}
	
	public Array<Body> getHBodiesToRemove(){
		return hBodiesToRemove;
	}
	
	public boolean bottleTouched(){
		return bottleTouch;
	}
	
	public boolean bottleDestroyed(){
		return bottleTouch = false;
	}
	
	/*
	public boolean isPlayerDead() {
		return playerDead;
	}
	*/
	public void preSolve(Contact c, Manifold m){
		
	}
	
	public void postSolve(Contact c, ContactImpulse ci){
		
	}

}
