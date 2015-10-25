package com.MadMedley.Platformer;

import com.MadMedley.Platformer.main.Platformer;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = Platformer.TITLE;
		cfg.width = Platformer.v_width * Platformer.scale;
		cfg.height = Platformer.v_height * Platformer.scale;
		
		new LwjglApplication(new Platformer(), cfg);
		
	}
}
