package client.gameGUI;

import java.applet.Applet;
import java.applet.AudioClip;

public class playSound extends Thread {

    private AudioClip clip;

    public playSound(String name) {
        clip = Applet.newAudioClip(getClass().getResource(name));
    }
    
    
    public void pause() {
    	clip.stop();
//    	Thread.interrupted();
    }
    
    public void play() {
		clip.loop();
	}

	public void run() {
		// TODO Auto-generated method stub
			this.play();
	}
}