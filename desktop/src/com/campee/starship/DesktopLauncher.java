package com.campee.starship;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import com.campee.starship.screens.*;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument2
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.useVsync(true);
		config.setWindowedMode(800, 600);
		config.setTitle("Starship Game!");
		new Lwjgl3Application(new MoonshipGame(), config);
	}
}