import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Toolkit;
import java.awt.Image;
import javax.swing.*;

import javax.sound.sampled.AudioInputStream;

/*
*Amazon Echo frame and constructor - 20//2017.
*/

public class Echo extends JFrame {
    
    private final static String turnOnSound = "resources/hellotune.wav";
    private final static String turnOffSound = "resources/goodbyetune.aiff";
    
    private Image sideBackground = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sideview/background.jpg") );
    private Image topBackground = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/topview/background2.jpg") );
    private Image sideEcho = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sideview/echo.png"));
    private Image topEcho = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/topview/echo.png"));
    private Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sideview/buttonon.png") );
    
    private final int OFFMODE = 1;
    private final int LISTENINGMODE = 2;
    private final int ANSWERMODE = 3;
    private final int MUTEMODE = 4;
    
    private int currentMode = OFFMODE;
    
    /*ADDED FOR VIEW CHANGE*/
    private final int SIDEVIEW = 1;
    private final int TOPVIEW = 2;
    
    /*ATTRIBUTE USED FOR SWAPPING BACKGROUND WHEN CHANGING VIEW*/
    private BackgroundReplacer backgroundReplacer = new BackgroundReplacer();
    
    private int currentView = SIDEVIEW;
    
    private JPanel contentPane = (JPanel) getContentPane();
    private JLayeredPane layeredPane = getLayeredPane();
    
    /*ATTRIBUTES FOR SIDE VIEW*/
    private Button button = new Button();
    private Light light = new Light();
    private ChangeModeButton changeModeButton = new ChangeModeButton();
    private JLabel side = new JLabel( new ImageIcon( sideEcho ) );
    
    /*ATTRIBUTES FOR TOP VIEW*/
    private ActionButton topButton = new ActionButton();
    private MuteButton muteButton = new MuteButton();
    private JLabel top = new JLabel( new ImageIcon( topEcho ) );
    private TopLight topLight = new TopLight();

    
    /*ATTRIBUTES FOR LISTENING*/
    private final static String KEY1 = "256a4ccc19dc41d7a75857c7dfd24825";
    
    
    public Echo() {
        
        /**
         * Method to create an Amazon Echo
         */
        
        setupGUI();
        speak("Welcome to the Amazon Echo Simulator!");
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
        layeredPane.add(changeModeButton);
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
                layeredPane.remove(0);
                layeredPane.remove(0);
                
                layeredPane.add(backgroundReplacer, 0, -1 );
                layeredPane.add(changeModeButton, 0, 0);
                layeredPane.add(top, 0, 0);
                layeredPane.add(muteButton, 0, 0);
                layeredPane.add(topButton, 0, 0);
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
                layeredPane.remove(0);
                layeredPane.remove(0);
                layeredPane.remove(0);
                layeredPane.remove(0);
                
                layeredPane.add(backgroundReplacer, 0, -1 );
                layeredPane.add(changeModeButton, 0, 0);
                layeredPane.add(side, 0, 0);
                layeredPane.add(button, 0, 0);
                layeredPane.add(light, 0, 0);
                if (topLight.getStatus() == 1) {
                    light.turnOn();
                }
                /* 
                TURNS OFF MUTEMODE WHEN SWITCHING VIEWS
                */
                muteButton.turnOff();
                if(currentMode == MUTEMODE){
                    playSound( turnOffSound);
                    currentMode = OFFMODE;
                }
                
                layeredPane.repaint();
                currentView = SIDEVIEW;
                break;
        }
    }

    
    public static synchronized void playSound( final String url ) {
        
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
        TextToSpeech.writeData(byteArray, "resources/output.wav");

        playSound("resources/output.wav");
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
    
    
    public class BackgroundReplacer extends JLabel {
        /**
         * Class to replace background image when switching views
         */
         ImageIcon replaceSide = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/topview/background2.jpg") ) );   
         ImageIcon replaceTop = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sideview/background.jpg") ) );   
         
         BackgroundReplacer(){
         setBounds(0, -125, 900, 900);
         }
         
         void replaceSideBg() {
            setIcon( replaceSide );
         }
         
         void replaceTopBg() {
            setIcon( replaceTop );
         }
         
    }

//WORKS
    public class Button extends JButton {
        
        ImageIcon buttonOff = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sideview/buttonoff.png") ) );
        ImageIcon buttonOn = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sideview/buttonon.png") ) );
        int status = 0;
        
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
                            button.turnOn();
                            topButton.turnOn();
                            topLight.turnOn();
                            light.turnOn();
                            playSound( turnOnSound );
                            currentMode = LISTENINGMODE;
                            break;
                        case LISTENINGMODE:
                            button.turnOff();
                            topButton.turnOff();
                            light.turnOff();
                            topLight.turnOff();
                            playSound( turnOffSound);
                            currentMode = OFFMODE;
                            break;
                        case ANSWERMODE:
                            break;
                    }
                }
            });
        }
    
        void turnOn() {
            setIcon( buttonOn );
            status = 1;
        }
        
        void turnOff() {
            setIcon( buttonOff );
            status = 0;
        }
        
        int getStatus() {
            return status;
        }
    }
    

    public class ActionButton extends JButton {
        
        ImageIcon topButtonOff = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/topview/actionbuttonoff.png") ) );
        ImageIcon topButtonOn = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/topview/actionbuttonon.png") ) );
        int status = 0;
        
        ActionButton() {
            setBounds( 388, 266, 153, 163);
            setBorderPainted(false);
            setContentAreaFilled(false); 
            setFocusPainted(false); 
            setBorder( null );
            setOpaque(false);
            setIcon( topButtonOff );
            addActionListener( new ActionListener() {
                public void actionPerformed( ActionEvent e ) {
                    switch(currentMode){
                        case OFFMODE:
                            button.turnOn();
                            topButton.turnOn();
                            topLight.turnOn();
                            light.turnOn();
                            playSound( turnOnSound );
                            currentMode = LISTENINGMODE;
                            break;
                        case LISTENINGMODE:
                            button.turnOff();
                            topButton.turnOff();
                            topLight.turnOff();
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
        
        void turnOn() {
            setIcon( topButtonOn );
            status = 1;
        }
        
        void turnOff() {
            setIcon( topButtonOff );
            status = 0;
        }
        
        int getStatus() {
            return status;
        }
    }
    
    public class MuteButton extends JButton {
        
        ImageIcon muteOff = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/topview/muteoff.png") ) );
        ImageIcon muteOn = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/topview/muteon.png") ) );
        
        MuteButton() {
            setBounds( 160, 272, 153, 163);
            setBorderPainted(false);
            setContentAreaFilled(false); 
            setFocusPainted(false); 
            setBorder( null );
            setOpaque(false);
            setIcon( muteOff );
            addActionListener( new ActionListener() {
                public void actionPerformed( ActionEvent e ) {
                    switch(currentMode){
                        case OFFMODE:
                            muteButton.turnOn();
                            topLight.turnMute();
                            button.turnOff();
                            topButton.turnOff();
                            light.turnOff();
                            currentMode = MUTEMODE;
                            break;
                        case LISTENINGMODE:
                            muteButton.turnOn();
                            topLight.turnMute();
                            button.turnOff();
                            topButton.turnOff();
                            light.turnOff();
                            currentMode = MUTEMODE;
                            break;
                        case MUTEMODE:
                            muteButton.turnOff();
                            topLight.turnOff();
                            currentMode = OFFMODE;
                            break;
                    }

                }
            });
        }
        
        void turnOn() {
            setIcon( muteOn );
        }
        
        void turnOff() {
            setIcon( muteOff );
        }
        
    }
    
    
    public class Light extends JLabel {
    
        ImageIcon lightOn = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sideview/lighton.png") ) );
        ImageIcon lightOff = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sideview/lightoff.png") ) );
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
        ImageIcon topLightMute = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/topview/lightmute.png") ) );
        
        int status = 1;
        
        TopLight() {

            setBounds(92, -49, 800, 800);
            setIcon( topLightOff );
        }
        
        void turnOn() {
            setIcon( topLightOn );
            status = 1;
        }
        
        void turnOff() {
            setIcon( topLightOff );
            status = 0;
        }
        
        void turnMute() {
            setIcon( topLightMute );
            status = 0;
        }
        
        int getStatus() {
            return status;
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
            setBounds(606, 107, 90, 101);
            setIcon( topView );
            addActionListener( new ActionListener() {
                public void actionPerformed( ActionEvent e ) {
                    switch(currentView){
                        case SIDEVIEW:
                            switchView();
                            setIcon( sideView );
                            backgroundReplacer.replaceSideBg();
                            break;
                        case TOPVIEW:
                            switchView();
                            setIcon( topView );
                            backgroundReplacer.replaceTopBg();
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