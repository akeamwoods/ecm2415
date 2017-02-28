import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Toolkit;
import java.awt.Image;
import javax.swing.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/*
*Amazon Echo frame and constructor - 20//2017.
*/

public class Echo extends JFrame {
    
    private final static String turnOnSound = "/hellotune.wav";
    private final static String turnOffSound = "/goodbyetune.aiff";
    
    private Image sideBackground = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sideview/background.jpg") );
    private Image topBackground = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/topview/background2.jpg") );
    private Image sideEcho = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sideview/echo.png"));
    private Image topEcho = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/topview/echo.png"));
    private Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sideview/buttonon.png") );
    
    private final int OFFMODE = 1;
    private final int LISTENINGMODE = 2;
    private final int ANSWERMODE = 3;
    
    private int currentMode = OFFMODE;
    
    /*ADDED FOR VIEW CHANGE*/
    private final int SIDEVIEW = 1;
    private final int TOPVIEW = 2;
    
    private int currentView = SIDEVIEW;
    
    private JPanel contentPane = (JPanel) getContentPane();
    private JLayeredPane layeredPane = getLayeredPane();
    
    /*ATTRIBUTES FOR SIDE VIEW*/
    private Button button = new Button();
    private Light light = new Light();
    private ChangeModeButton changeModeButton = new ChangeModeButton();
    private JLabel side = new JLabel( new ImageIcon( sideEcho ) );
    
    /*ATTRIBUTES FOR TOP VIEW*/
    private JLabel top = new JLabel( new ImageIcon( topEcho ) );
    private TopLight topLight = new TopLight();
    
    /*ATTRIBUTES FOR LISTENING*/
    private final static String KEY1 = "256a4ccc19dc41d7a75857c7dfd24825";
    
    
    public Echo() {
        
        /**
         * Method to create an Amazon Echo
         */
        
        setupGUI();
        speak("Akeam is a cunt");
    }
    
    
    public void setupGUI() {
        
        /**
         * Method to set up the GUI
         */
        
        setTitle( "Amazon Echo Simulator" );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setContentPane( new JLabel( new ImageIcon ( sideBackground ) ) );
	setIconImage( (new ImageIcon ( icon ).getImage() ) );
	setLayout( null );
        pack();
        

        top.setBounds( 100, 101, 500, 500);
        side.setBounds( 250, 110, 200, 500);
        layeredPane.add(side, 0, -1);
        layeredPane.add(button, 0, 0);
        layeredPane.add(light, 0, 0);
        
        add(changeModeButton);
    }
    
    
    public void switchView() {
    /**
     * Method to switch between top and side view.
     */
        switch(currentView){
            case SIDEVIEW:
                layeredPane.remove(0);
                layeredPane.remove(0);
                layeredPane.remove(0);
                
                layeredPane.add(top, 0, -1);
                layeredPane.add(topLight, 0, 0);
                if (light.getStatus() == 1) {
                    topLight.turnOn();
                }
                layeredPane.repaint();
                currentView = TOPVIEW;
                break;
            case TOPVIEW:
                topLight.turnOff();
            
                layeredPane.remove(0);
                layeredPane.remove(0);
                
                layeredPane.add(side, 0, -1);
                layeredPane.add(button, 0, 0);
                layeredPane.add(light, 0, 0);
                layeredPane.repaint();
                currentView = SIDEVIEW;
                break;
        }
    }

    

    public static synchronized void PlaySound( final String url ) {
        
        new Thread(new Runnable() {
            public void run() {
                AudioInputStream stream = PlaySound.setupStream( url );
                PlaySound.playStream( stream, PlaySound.readStream( stream ) );
            }
        }).start();
    }

    public static synchronized void speak(String speech) {

        /**
         * Method to speak an input String
         */
        final String token = SpeechToText.renewAccessToken(KEY1);
        final byte[] byteArray = TextToSpeech.synthesizeSpeech(token, speech, "en-US", "Male", "riff-16khz-16bit-mono-pcm");
        TextToSpeech.writeData(byteArray, "output.wav");

        PlaySound("resources/output.wav");
    }
    
    public static synchronized void playSound(final String url) {
       
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(Echo.class.getResourceAsStream(url));
            clip.open(inputStream);
            clip.start(); 
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    
    public String listen() {
        
        /**
         * Method listens for speech then returns it in String format
         */
        
        AudioInputStream stream = RecordSound.setupStream();
        RecordSound.recordSound( "input.wav", RecordSound.readStream( stream ) );   //This needs to be replaced by an automatic process
        
        final String token  = SpeechToText.renewAccessToken( KEY1 );
        final byte[] speech = SpeechToText.readData( "input.wav" );
        final String text   = SpeechToText.recognizeSpeech( token, speech );
        
        int startIndex = text.indexOf("name") + 7;
        int endIndex = startIndex;
        while (text.charAt(endIndex) != '\"') {
            endIndex++;
        }
        
        String finalText = text.substring( startIndex, endIndex );
        if (finalText.contains("profanity")) {
            finalText = "Don't be so rude!";
        }
        
        return finalText;
    }

    public class Button extends JButton {
        
        ImageIcon buttonOff = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sideview/buttonoff.png") ) );
        ImageIcon buttonOn = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sideview/buttonon.png") ) );
    
        Button() {
            setBounds( 325, 424, 50, 50);
            setBorderPainted(false);
            setContentAreaFilled(false); 
            setFocusPainted(false); 
            setBorder( null );
            setOpaque(false);
            setIcon( buttonOff );
            addActionListener( new ActionListener() {
                public void actionPerformed( ActionEvent e ) {
                    switch(currentMode){
                        case OFFMODE:
                            setIcon( buttonOn );
                            light.turnOn();
                            playSound( turnOnSound );
                            currentMode = LISTENINGMODE;
                            break;
                        case LISTENINGMODE:
                            setIcon( buttonOff );
                            light.turnOff();
                            playSound( turnOffSound);
                            currentMode = OFFMODE;
                            break;
                        case ANSWERMODE:
                            break;
                    }
                    
                    
                }
            });
        }
    }
    
    
    public class Light extends JLabel {
    
        ImageIcon lightOn = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sideview/lighton.png") ) );
        ImageIcon lightOff = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sideview/lightoff.png") ) );
        ImageIcon topLightOn = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/topview/lighton.png") ) );
        ImageIcon topLightOff = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/topview/lightoff.png") ) );
        int status = 0;
        
        Light() {
            setBounds(267, 101, 300, 100);
            setIcon( lightOff );
        }
        
        void turnOn() {
            setIcon( lightOn );
            status = 1;
        }
        
        void turnOff() {
            setIcon( lightOff );
            status = 0;
        }
        
        int getStatus() {
            return status;
        }
    }
    
    
    public class TopLight extends JLabel {
    
        ImageIcon topLightOn = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/topview/lighton.png") ) );
        ImageIcon topLightOff = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/topview/lightoff.png") ) );
        
        TopLight() {

            setBounds(92, -49, 800, 800);
            setIcon( topLightOff );
        }
        
        void turnOn() {
            setIcon( topLightOn );
        }
        
        void turnOff() {
            setIcon( topLightOff );
        }
    }
    
    
    public class ChangeModeButton extends JButton {
    
        ImageIcon sideView = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sideview.png") ) );
        ImageIcon topView = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/topview.png") ) );
        
        ChangeModeButton() {
            setBorderPainted(false);
            setContentAreaFilled(false); 
            setFocusPainted(false); 
            setBorder( null );
            setOpaque(false);
            setBounds(500, 107, 300, 100);
            setIcon( topView );
            addActionListener( new ActionListener() {
                public void actionPerformed( ActionEvent e ) {
                    switch(currentView){
                        case SIDEVIEW:
                            switchView();
                            setIcon( sideView );
                            break;
                        case TOPVIEW:
                            switchView();
                            setIcon( topView );
                            break;
                    }
                    
                    
                }
            });
        }   
    }
    
    public static void main( String[] argv ){
	JFrame frame = new Echo();
	frame.setResizable( false );
	frame.setVisible( true );
        
    }
}