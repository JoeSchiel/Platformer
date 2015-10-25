package com.MadMedley.Platformer.states;

import com.MadMedley.Platformer.entities.Bottle;
import com.MadMedley.Platformer.entities.Crystal;
import com.MadMedley.Platformer.entities.Goal;
import com.MadMedley.Platformer.entities.HUD;
import com.MadMedley.Platformer.entities.Health;
import com.MadMedley.Platformer.entities.Platform;
import com.MadMedley.Platformer.entities.Player;
import com.MadMedley.Platformer.entities.Spikes;
import com.MadMedley.Platformer.entities.Splash;
import com.MadMedley.Platformer.handlers.B2DVars;
import com.MadMedley.Platformer.handlers.Background;
import com.MadMedley.Platformer.handlers.GameStateManager;
import com.MadMedley.Platformer.handlers.MyContactListener;
import com.MadMedley.Platformer.handlers.MyInput;
import com.MadMedley.Platformer.handlers.BoundedCamera;
import com.MadMedley.Platformer.handlers.Vars;
import com.MadMedley.Platformer.main.Platformer;
import static com.MadMedley.Platformer.handlers.B2DVars.ppm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Play extends GameState {
	
	private boolean debug = false;
	
	private World world;
	private Box2DDebugRenderer b2dr;
	
	private BoundedCamera b2dCamera;
	
	private MyContactListener cl;
	
	private TiledMap tileMap;
	private float tileMapWidth;
	private float tileMapHeight;
	private float tileSize;
	private OrthogonalTiledMapRenderer tmr;
	
	private Player player;
	private Bottle bottle;
	private Platform platform;
	private Array<Crystal> crystals;
	private Array<Health> healthObj;
	private Array<Goal> goal;
	private Array<Spikes> spikes;
	
	private Array<Splash> splash;
	
	private Background cloudBackground;
	private Background treeBackground;
	private Background grassBackground;
	private HUD hud;
	
	public static int level;
	public static int bottleInv;
	public static int platformInv;
	public static int health;
	public static Double linVelocity;
	public static int firePass;
	
	private boolean animStarted;
	private boolean playerDead;
	private boolean bottleThrown;
	private boolean platformThrown;
	
	public Play(GameStateManager gsm){
		super(gsm);
		
		//setup box 2D stuff
		
		world = new World(new Vector2(0, -9.8f), true);
		cl = new MyContactListener();
		world.setContactListener(cl);
		b2dr = new Box2DDebugRenderer();
		
		animStarted = false; //Did the animation start? Without this switch, setAnimation in player.playerMove
							 //is called over and over again preventing the frames to progress
		
		playerDead = false;
		bottleThrown = false;
		platformThrown = false;
		firePass = 0;
		
		bottleInv = Vars.bottleInv.get("bottleInv" + level);
		platformInv = Vars.platformInv.get("platformInv" + level);
		health = Vars.healthInv.get("healthInv" + level);
		linVelocity = Vars.linVel.get("linVel" + level);
		
		
		
		createPlayer();
		
		// create walls
		createWalls();
		camera.setBounds(0, tileMapWidth * tileSize, 0, tileMapHeight * tileSize);
		
		// create backgrounds		
		
		Texture clouds_tex = Platformer.res.getTexture("clouds");
		cloudBackground = new Background(new TextureRegion(clouds_tex), camera, 1f);
		cloudBackground.setVector(-5, 0);
		
		Texture tree_tex = Platformer.res.getTexture("trees");
		treeBackground = new Background(new TextureRegion(tree_tex), camera, 1f);
		treeBackground.setVector(0, 0);
		
		Texture grass_tex = Platformer.res.getTexture("grass");
		grassBackground = new Background(new TextureRegion(grass_tex), camera, 1f);
		grassBackground.setVector(0, 0);
		
		createSpikes();
		
		createCrystals();
		
		createHealth();
		
		createGoal();
		
		// create hud
		hud = new HUD(player);
		
		//setup box2d camera
		b2dCamera = new BoundedCamera();
		b2dCamera.setToOrtho(false, Platformer.v_width / ppm, Platformer.v_height / ppm);
		b2dCamera.setBounds(0, (tileMapWidth * tileSize) / ppm, 0, (tileMapHeight * tileSize) / ppm);
		
		
	}
	
	public void handleInput(){
		
		//player jump
		if(MyInput.isPressed(MyInput.button1)){
			if(cl.isPlayerOnGround()){
				player.getBody().applyForceToCenter(0, 250, true);
			} 
		}
		
		if(MyInput.isPressed(MyInput.button2) && bottleThrown == false  && bottleInv > 0){
			createBottle();
			bottleInv--;
			bottleThrown = true;
			bottle.getBody().applyForceToCenter(100,200, true);
		}
		
		//throw platform
		if(MyInput.isPressed(MyInput.button3) && platformThrown == false  && platformInv > 0){
			if(platform != null){
				world.destroyBody(platform.getBody());
			}
			createPlatform();
			platformInv--;
			platformThrown = true;
			platform.getBody().applyForceToCenter(200,150, true);
		}
		
		/*
		if(MyInput.isPressed(MyInput.button1)){
			
		}
		if(MyInput.isDown(MyInput.button2)){
		
		}
		*/
	}
	
	public void update(float dt){
		
		//check input
		handleInput();
		
		//update box2D
		world.step(dt, 6, 2);
		
		cloudBackground.update(dt);
		treeBackground.update(dt);
		grassBackground.update(dt);
		
		//remove crystals
		Array<Body> cBodies =  cl.getCBodiesToRemove();
			for (int i = 0 ; i < cBodies.size ; i++){
				Body b = cBodies.get(i);
					crystals.removeValue((Crystal) b.getUserData(), true);
					world.destroyBody(b);
			}
		cBodies.clear();
		
		//remove health object
		Array<Body> healthBodies = cl.getHBodiesToRemove();
			for (int i = 0 ; i < healthBodies.size ; i++){
				Body b = healthBodies.get(i);
					healthObj.removeValue((Health) b.getUserData(), true);
					world.destroyBody(b);
			}
		healthBodies.clear();
		
		
		//remove splash 
		if(splash != null){
		Array<Body> splashBodies =  new Array<Body>();
		for(int i = 0 ; i < splash.size; i ++){
			if(splash.get(i).getSplashTimesPLayed() >=1){
				splashBodies.add(splash.get(i).getBody());
			}
		}
		for (int i = 0 ; i < splashBodies.size ; i++){
			Body b = splashBodies.get(i);
				splash.removeValue((Splash) b.getUserData(), true);
				world.destroyBody(b);
		}
		splashBodies.clear();
		}
		
		//remove bottles
		if(cl.bottleTouched()){
			cl.bottleDestroyed();
			bottleThrown = false;
			createSplash();
			world.destroyBody(bottle.getBody());
			
		}
		
		//Changing X velocity when player passes a fire
		//Only on endless level
		if(level == 25){
		Array<Body> passBodies =  new Array<Body>();
			for (int i = 0 ; i < crystals.size ; i++){
				Crystal c = crystals.get(i);
				    if (player.getPosition().x > c.getBody().getPosition().x){
				    	passBodies.add(crystals.get(i).getBody());
				    	firePass = passBodies.size;
				    	Double addSpeed = ((double)firePass/5);
				    	linVelocity = .6 + addSpeed;
				    	player.getBody().setLinearVelocity(linVelocity.floatValue(), player.getBody().getLinearVelocity().y);
				    	//System.out.println (linVelocity);
				    	
				    }
					
					
			}
		}
		
		//platform settle
		if( platform != null &&
			player.getPosition().x + 1 < platform.getPosition().x &&
			platform.getBody().getType() == BodyType.DynamicBody){
				platform.getBody().setLinearVelocity(0, 0);
				platform.getBody().setType(BodyType.StaticBody);
				platformThrown = false;
		}
		
		player.update(dt);
		
		if(bottleThrown){
		bottle.update(dt);
		}
		
		if(splash != null){
			for(int i = 0 ; i < splash.size; i ++){
				splash.get(i).update(dt);
			}
		}
		
		if(platform != null){
		platform.update(dt);
		}
		
		//fixing x velocity on side collision
		if (player.getBody().getLinearVelocity().x == 0 && cl.isPlayerOnGround()){
		player.getBody().setLinearVelocity(linVelocity.floatValue() , 0);
		}
		//re-position if stuck on in front of platform
		if (player.getBody().getLinearVelocity().x == 0 && cl.isPlayerOnGround() && cl.playerTouchPlatform()){
			player.getPosition().set(player.getPosition().x -5 , player.getPosition().y);
		}
		/*
		if(player.getBody().getLinearVelocity().x == 0 &&
		   player.getBody().getLinearVelocity().y == 0	){
			player.getPosition().set(player.getPosition().x +40 , player.getPosition().y + 2);
			player.getBody().setLinearVelocity(linVelocity.floatValue(), 0);
		}
		*/
		
		
		for(int i = 0 ; i < crystals.size; i ++){
			crystals.get(i).update(dt);
		}
		
		for(int i = 0 ; i < healthObj.size; i ++){
			healthObj.get(i).update(dt);
		}
		
		for(int i = 0 ; i < goal.size; i ++){
			goal.get(i).update(dt);
		}
		
		if(cl.PlayerTouchGoal()){
			gsm.setState(GameStateManager.level_select);
		}
		
		if(playerDead == true){
			sleep(2);
			gsm.setState(GameStateManager.menu);
		}
		
		if (health <= 0){
			playerDead = true;
		}
		
		//player dies if y < 0
		if(player.getPosition().y < 0){ 
			playerDead = true;
		}
		
	}
	
	public void render(){
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//set camera to follow player
		camera.position.set(player.getPosition().x * ppm + Platformer.v_width / 4, Platformer.v_height / 2, 0);
		camera.update();
		b2dCamera.position.set(player.getPosition().x * ppm + Platformer.v_width / 4, Platformer.v_height / 2, 0);
		//b2dCamera.update();
		
		//draw background
		batch.setProjectionMatrix(hudCamera.combined);
		batch.begin();
		batch.draw(Platformer.res.getTexture("background"), 0, 0);
		batch.end();
		cloudBackground.render(batch);
		grassBackground.render(batch);
		treeBackground.render(batch);
		
		//draw tile map
		tmr.setView(camera);
		tmr.render();
		
		//draw player
		batch.setProjectionMatrix(camera.combined);
		player.render(batch);
		if(cl.isPlayerOnGround() && animStarted == false ){
			player.playerMove();
			animStarted = true;
		} else if (cl.isPlayerOnGround() == false) {
			player.playerJump();
			animStarted = false;
		}
		
		//draw fire
		for(int i = 0 ; i < crystals.size; i ++){
			crystals.get(i).render(batch);
		}
		
		//draw bottle
		if(bottleThrown){
			bottle.render(batch);
		}
		
		//draw splash
		if (splash != null){
			for(int i = 0 ; i < splash.size; i ++){
				splash.get(i).render(batch);
			}
		}
		
		//draw goal
		for(int i = 0 ; i < goal.size; i ++){
			goal.get(i).render(batch);
		}
		
		//draw spikes
		for(int i = 0 ; i < spikes.size; i ++){
			spikes.get(i).render(batch);
		}
		
		//draw health
		for(int i = 0 ; i < healthObj.size; i ++){
			healthObj.get(i).render(batch);
		}
		
		//draw platform
		if(platform != null){
			platform.render(batch);
		}
		
		if(debug){
		b2dr.render(world, b2dCamera.combined);
		}
		
		batch.setProjectionMatrix(hudCamera.combined);
		hud.render(batch);
		
		//draw victory
		/*
		if(health == 0){
			batch.begin();
			batch.draw(Platformer.res.getTexture("victory"),  0, 0 );
			batch.end();
			playerDead = true;
		}
		*/
	}
	
	public void dispose(){
		
	}
	
	private void createPlayer(){

		BodyDef bodyDef = new BodyDef();
		FixtureDef fixDef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
			
		bodyDef.position.set(60 / ppm, 200 / ppm); //player's starting point
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.linearVelocity.set(linVelocity.floatValue(), 0);  //player's x-axis speed
		//bodyDef.linearDamping = 0.0f;
		//bodyDef.angularDamping = 0.01f;
		Body body = world.createBody(bodyDef);
		shape.setAsBox(9 / ppm, 13 / ppm);
		fixDef.shape = shape;
		fixDef.filter.categoryBits = B2DVars.bit_player;
		fixDef.filter.maskBits =   B2DVars.bit_ground |
								   B2DVars.bit_crystal |
								   B2DVars.bit_goal | 
								   B2DVars.bit_platform |
								   B2DVars.bit_health |
								   B2DVars.bit_spikes;
		body.createFixture(fixDef).setUserData("player");
		
		//create foot sensor
		shape.setAsBox(9 / ppm, 2/ ppm, new Vector2(0, -13 /ppm), 0);
		fixDef.shape = shape;
		fixDef.filter.categoryBits = B2DVars.bit_player;
		fixDef.filter.maskBits = B2DVars.bit_ground | B2DVars.bit_platform;
		fixDef.isSensor = true;
		body.createFixture(fixDef).setUserData("foot");
		
		//create player
		player = new Player(body);
		
		body.setUserData(player);
	}
	
private void createWalls() {
		
		// load tile map and map renderer
		try {
			tileMap = new TmxMapLoader().load("res/maps/level" + level + ".tmx");
		}
		catch(Exception e) {
			System.out.println("Cannot find file: res/maps/level" + level + ".tmx");
			Gdx.app.exit();
		}
		tileMapWidth = Float.parseFloat((tileMap.getProperties().get("width").toString()));
		//can not (cast) normally.
		tileMapHeight = Float.parseFloat((tileMap.getProperties().get("height").toString()));
		tileSize = Float.parseFloat((tileMap.getProperties().get("tilewidth").toString()));
		tmr = new OrthogonalTiledMapRenderer(tileMap);
		
		// read layers
		TiledMapTileLayer layer;

		layer = (TiledMapTileLayer) tileMap.getLayers().get("ground");
		createBlocks(layer, B2DVars.bit_ground);
	}

private void createBlocks(TiledMapTileLayer layer, short bits) {
	
	// tile size
	float ts = layer.getTileWidth();
	
	// go through all cells in layer
	for(int row = 0; row < layer.getHeight(); row++) {
		for(int col = 0; col < layer.getWidth(); col++) {
			
			// get cell
			Cell cell = layer.getCell(col, row);
			
			// check that there is a cell
			if(cell == null) continue;
			if(cell.getTile() == null) continue;
			
			// create body from cell
			BodyDef bdef = new BodyDef();
			bdef.type = BodyType.StaticBody;
			bdef.position.set((col + 0.5f) * ts / ppm, (row + 0.5f) * ts / ppm);
			ChainShape cs = new ChainShape();
			Vector2[] v = new Vector2[5];
			v[0] = new Vector2(ts / 2 / ppm, -ts / 2 / ppm);  //bottom right point 1
			v[1] = new Vector2(-ts / 2 / ppm, -ts / 2 / ppm); //bottom left point  2
			v[2] = new Vector2(-ts / 2 / ppm, ts / 2 / ppm);  //top left point     3
			v[3] = new Vector2(ts / 2 / ppm, ts / 2 / ppm);   //top right point    4
			v[4] = new Vector2(-(ts + 13) / 2 / ppm, ts / 2 / ppm);  //top left cliff     3

			
			cs.createChain(v);
			FixtureDef fd = new FixtureDef();
			fd.friction = 0;
			fd.shape = cs;
			fd.filter.categoryBits = bits;
			fd.filter.maskBits = B2DVars.bit_player | B2DVars.bit_bottle | B2DVars.bit_platform;
			world.createBody(bdef).createFixture(fd);
			cs.dispose();
		}
	}	
}

	
	private void createCrystals(){
		
		crystals = new Array<Crystal>();
		
		MapLayer layer = tileMap.getLayers().get("crystals");
		
		BodyDef bodyDef = new BodyDef();
		FixtureDef fixDef = new FixtureDef();
		
		for(MapObject mo : layer.getObjects()){
			
			bodyDef.type = BodyType.StaticBody;
			
			float x = Float.parseFloat((mo.getProperties().get("x").toString()))/ ppm;
			float y = Float.parseFloat((mo.getProperties().get("y").toString()))/ ppm;
		
			bodyDef.position.set(x, y);
			
			CircleShape cshape = new CircleShape();
			cshape.setRadius(8 / ppm);
			
			fixDef.shape = cshape;
			fixDef.isSensor = true;
			fixDef.filter.categoryBits = B2DVars.bit_crystal;
			fixDef.filter.maskBits = B2DVars.bit_player | B2DVars.bit_bottle;
			
			Body body = world.createBody(bodyDef);
			body.createFixture(fixDef).setUserData("crystal");
			
			Crystal c = new Crystal(body);
			crystals.add(c);
			
			body.setUserData(c);
		}
	}
	
	private void createHealth(){
		
		healthObj = new Array<Health>();
		
		if(tileMap.getLayers().get("health") != null){  //if health exists in the tile map
		MapLayer layer = tileMap.getLayers().get("health");
		
		BodyDef bodyDef = new BodyDef();
		FixtureDef fixDef = new FixtureDef();
		
		for(MapObject mo : layer.getObjects()){
			
			bodyDef.type = BodyType.StaticBody;
			
			float x = Float.parseFloat((mo.getProperties().get("x").toString()))/ ppm;
			float y = Float.parseFloat((mo.getProperties().get("y").toString()))/ ppm;
		
			bodyDef.position.set(x, y);
			
			CircleShape cshape = new CircleShape();
			cshape.setRadius(8 / ppm);
			
			fixDef.shape = cshape;
			fixDef.isSensor = true;
			fixDef.filter.categoryBits = B2DVars.bit_health;
			fixDef.filter.maskBits = B2DVars.bit_player;
			
			Body body = world.createBody(bodyDef);
			body.createFixture(fixDef).setUserData("health");
			
			Health h = new Health(body);
			healthObj.add(h);
			
			body.setUserData(h);
		}
		}
	}
	
	private void createGoal(){
		
		goal = new Array<Goal>();
		
		MapLayer layer = tileMap.getLayers().get("goal");
		
		BodyDef bodyDef = new BodyDef();
		FixtureDef fixDef = new FixtureDef();
		
		for(MapObject mo : layer.getObjects()){
			
			bodyDef.type = BodyType.StaticBody;
			
			float x = Float.parseFloat((mo.getProperties().get("x").toString()))/ ppm;
			float y = Float.parseFloat((mo.getProperties().get("y").toString()))/ ppm;
		
			bodyDef.position.set(x, y);
			
			PolygonShape shape = new PolygonShape();
			shape.setAsBox(3 / ppm, 42 / ppm);
			
			fixDef.shape = shape;
			fixDef.isSensor = true;
			fixDef.filter.categoryBits = B2DVars.bit_goal;
			fixDef.filter.maskBits = B2DVars.bit_player;
			
			Body body = world.createBody(bodyDef);
			body.createFixture(fixDef).setUserData("goal");
			
			Goal g = new Goal(body);
			goal.add(g);
			
			body.setUserData(g);	
		}
	}
	
	private void createBottle(){

		BodyDef bodyDef = new BodyDef();
		FixtureDef fixDef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
			
		bodyDef.position.set(player.getPosition().x, player.getPosition().y); //bottle's starting point
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.linearVelocity.set(linVelocity.floatValue(), 0);  //player's x-axis speed
		Body body = world.createBody(bodyDef);
		shape.setAsBox(3 / ppm, 6 / ppm);
		fixDef.shape = shape;
		fixDef.filter.categoryBits = B2DVars.bit_bottle;
		fixDef.filter.maskBits = B2DVars.bit_ground | B2DVars.bit_crystal;
		body.createFixture(fixDef).setUserData("bottle");
		
		//create bottle
		bottle = new Bottle(body);
		body.setUserData(bottle);
	}
	
	private void createSplash(){
		
		splash = new Array<Splash>();
		/*
		BodyDef bodyDef = new BodyDef();
		FixtureDef fixDef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
			
		bodyDef.position.set(bottle.getPosition().x, bottle.getPosition().y); //bottle's starting point
		bodyDef.type = BodyType.StaticBody;
		//bodyDef.linearVelocity.set(.6f, 0);  //player's x-axis speed
		Body body = world.createBody(bodyDef);
		shape.setAsBox(3 / ppm, 6 / ppm);
		fixDef.shape = shape;
		//fixDef.filter.categoryBits = B2DVars.bit_bottle;
		//fixDef.filter.maskBits = B2DVars.bit_ground | B2DVars.bit_crystal;
		body.createFixture(fixDef).setUserData("splash");
		
		//create splash
		Splash s = new Splash(body);
		splash.add(s);
		body.setUserData(s);
		*/
		splash = new Array<Splash>();
		
		BodyDef bodyDef = new BodyDef();
		FixtureDef fixDef = new FixtureDef();
			
			bodyDef.type = BodyType.StaticBody;
		
			bodyDef.position.set(bottle.getPosition().x, bottle.getPosition().y);
			
			CircleShape cshape = new CircleShape();
			cshape.setRadius(8 / ppm);
			
			fixDef.shape = cshape;
			fixDef.isSensor = false;
			
			Body body = world.createBody(bodyDef);
			body.createFixture(fixDef).setUserData("splash");
			
			Splash s = new Splash(body);
			splash.add(s);
			
			body.setUserData(s);
	}
	
	private void createPlatform(){

		BodyDef bodyDef = new BodyDef();
		FixtureDef fixDef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
			
		bodyDef.position.set(player.getPosition().x, (player.getPosition().y + (16/ppm))); //platform's starting point
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.linearVelocity.set(linVelocity.floatValue(), 0);  //player's x-axis speed
		
		Body body = world.createBody(bodyDef);
		
		shape.setAsBox(9 / ppm, 6 / ppm);
		fixDef.shape = shape;
		fixDef.friction = 0;
		fixDef.restitution = 2;
		fixDef.filter.categoryBits = B2DVars.bit_platform;
		fixDef.filter.maskBits = B2DVars.bit_ground | B2DVars.bit_player;
		body.createFixture(fixDef).setUserData("platform");
		
		//create platform
		platform = new Platform(body);
		body.setUserData(platform);
		
	}
	
private void createSpikes(){
		
		spikes = new Array<Spikes>();
		
		if(tileMap.getLayers().get("spikes") != null){
		MapLayer layer = tileMap.getLayers().get("spikes");
		
		BodyDef bodyDef = new BodyDef();
		FixtureDef fixDef = new FixtureDef();
		
		for(MapObject mo : layer.getObjects()){
			
			bodyDef.type = BodyType.StaticBody;
			
			float x = Float.parseFloat((mo.getProperties().get("x").toString()))/ ppm;
			float y = Float.parseFloat((mo.getProperties().get("y").toString()))/ ppm;
		
			bodyDef.position.set(x, y +(1/ppm));
			
			PolygonShape shape = new PolygonShape();
			shape.setAsBox(3 / ppm, 12 / ppm);

			fixDef.shape = shape;
			fixDef.isSensor = false;
			fixDef.filter.categoryBits = B2DVars.bit_spikes;
			fixDef.filter.maskBits = B2DVars.bit_player;
			
			Body body = world.createBody(bodyDef);
			body.createFixture(fixDef).setUserData("spikes");
			
			Spikes s = new Spikes(body);
			spikes.add(s);
			body.setUserData(s);
			
		}
		}
	}
	
	private void sleep(int sec){
		try {
		    Thread.sleep(sec * 1000);                 //1000 milliseconds is one second.
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}	
	}
}
