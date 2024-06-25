

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.BufferedReader;
import java.io.BufferedWriter;

public class Assets {
	
	private static ArrayList<Clip> soundList= new ArrayList<Clip>();
	private static ArrayList<Integer> volumeList= new ArrayList<Integer>();
	public static HashMap<String, BufferedImage> imgs = new HashMap<String, BufferedImage>();
	public Assets() {
		
	}
	public static BufferedImage newImage(String name) {
		try {
			URL url = PoliceChase.class.getResource("images/" + name);
			BufferedImage img = ImageIO.read(url);
			System.out.println(url + " successfully loaded.");
			return img;
		} catch (Exception e) {
			if (name.equals("MissingTexture.png")) {
				System.exit(0);
			}
			System.out.println("Cannot find " + name);
			return newImage("MissingTexture.png");
		}
	}
	public static void loadImages() {
		Class[] refs = {Cannonball.class, Bomb.class, Shotgun.class, 
				Minigun.class, Missile.class, Mortar.class,
				Lazer.class};
		for (Class c: refs) {
			imgs.put("Red" + c.getSimpleName(), Assets.newImage("Red" + c.getSimpleName() + ".png"));
			imgs.put("Blue" + c.getSimpleName(), Assets.newImage("Blue" + c.getSimpleName() + ".png"));
		}
	}
	public static Clip newSound(String name) {
		try {
			URL url = PoliceChase.class.getResource("sounds/" + name);
			AudioInputStream in = AudioSystem.getAudioInputStream(url);
			Clip clip = AudioSystem.getClip(); 
			clip.open(in);
			return clip;
		} catch (NullPointerException e) {
			return newSound("gun.wav");
		} catch (UnsupportedAudioFileException e) {
			System.out.println(name + " is unsupported");
			System.exit(0);
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Cannot find " + name);
			System.exit(0);
			return null;
		} catch (LineUnavailableException e) {
			System.out.println(name + "'s line is unavailable");
			System.exit(0);
			return null;
		}
	}
	
	public static void playSound(Clip clip, int volume) {
		soundList.add(clip);
		volumeList.add(volume);
	}
	
	public static void playAll() {
		ArrayList<Clip> played = new ArrayList<Clip>();
		while (soundList.size() > 0) {
			if (!played.contains(soundList.get(0))) {
				play(soundList.get(0), volumeList.get(0));
				played.add(soundList.get(0));
			} 
			soundList.remove(0);
			volumeList.remove(0);
		}
		played.clear();
	}
	
	public static void play(Clip clip, int volume) {
		clip.stop();
		volume += 2;
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		float range = gainControl.getMaximum() - gainControl.getMinimum();
		float gain = 0;
		if ((range/3 * volume) + gainControl.getMinimum() <= gainControl.getMaximum()) {
			gain = (range/5f * volume) + gainControl.getMinimum();
		} else {
			gain = gainControl.getMaximum();
		}
		gainControl.setValue(gain);
		clip.setFramePosition(0);
		clip.setMicrosecondPosition(0);
		for (int i = 0; !clip.isActive(); i ++) {
			if (i > 300) {
				System.err.println("Unable to play sound");
				break;
			}
			clip.start();
		}
	}
}

