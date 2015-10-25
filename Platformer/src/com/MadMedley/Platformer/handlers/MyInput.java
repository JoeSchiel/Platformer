package com.MadMedley.Platformer.handlers;

public class MyInput {
	
	public static int x;
	public static int y;
	public static boolean down;
	public static boolean pdown;
	
	public static boolean[] keys;
	public static boolean[] previous_keys;
	
	public static final int num_keys = 3;
	public static final int button1 = 0;
	public static final int button2 = 1;
	public static final int button3 = 2;
	
	static {
		keys = new boolean[num_keys];
		previous_keys = new boolean[num_keys];
	}
	
	public static void update(){
		pdown = down;
		for(int i = 0; i < num_keys; i++){
			previous_keys[i] = keys[i];
		}
	}
	
	public static boolean isDown() {
		return down;
	}
	
	public static boolean isPressed() {
		return down && !pdown;
	}
	public static boolean isReleased() {
		return !down && pdown;
	}
	
	public static void setKey(int i, boolean b){
		keys[i] = b;
	}
	
	public static boolean isDown(int i){
		return keys[i];
	}
	
	public static boolean isPressed(int i){
		return keys[i] && !previous_keys[i];
	}
	
}
