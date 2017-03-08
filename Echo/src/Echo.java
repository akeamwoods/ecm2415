import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Toolkit;
import java.awt.Image;
import javax.swing.*;
import answer.*;

import javax.sound.sampled.AudioInputStream;

/*
*Amazon Echo frame and constructor - 20//2017.
*/

public class Echo extends JFrame {
    
    private final static String turnOnSound = "resources/audio/hellotune.wav";
    private final static String turnOffSound = "resources/audio/goodbyetune.aiff";
    private final static String swapSound = "resources/audio/woosh.wav";
    private final static String muteSound = "resources/audio/mute.wav";
    
    private final ImageIcon topBackground = new ImageIcon( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/topview/background2.jpg") ) );
    private final ImageIcon sideBackground1 = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sideview/backgrounds/background1.jpg") ) );;
    private final ImageIcon sideBackground2 = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sideview/backgrounds/background2.jpg") ) );
    private final ImageIcon sideBackground3 = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sideview/backgrounds/background3.jpg") ) );
 
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
    private ChangeModeButton changeModeButton = new ChangeModeButton();
    private ChangeModeLabel changeModeLabel = new ChangeModeLabel();
    
    private final int FIRSTBG = 1;
    private final int SECONDBG = 2;
    private final int THIRDBG = 3;
    
    private int currentBackground = 1;
    
    
    private ChangeBackgroundButton changeBackgroundButton = new ChangeBackgroundButton();
    private ChangeBackgroundLabel changeBackgroundLabel = new ChangeBackgroundLabel();
    
    /*ATTRIBUTE USED FOR SWAPPING BACKGROUND WHEN CHANGING VIEW*/
    private Background background = new Background();
    
    private int currentView = SIDEVIEW;
    
    private JPanel contentPane = (JPanel) getContentPane();
    private JLayeredPane layeredPane = getLayeredPane();
    
    /*ATTRIBUTES FOR SIDE VIEW*/
    private Button button = new Button();
    private Light light = new Light();
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
    }
    
    
    public void setupGUI() {
        
        /**
         * Method to set up the GUI
         */
        
        setTitle( "Amazon Echo Simulator" );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setContentPane( new JLabel(sideBackground1) );
	setIconImage( (new ImageIcon ( icon ).getImage() ) );
	setLayout( null );
        pack();
        

        top.setBounds( 100, 101, 500, 500);
        side.setBounds( 250, 110, 200, 500);
        layeredPane.add(side, 0, -1);
        layeredPane.add(button, 0, 0);
        layeredPane.add(light, 0, 0);
        layeredPane.add(background, 0, -1 );
        layeredPane.add(changeModeButton, 0, 0);
        layeredPane.add(changeModeLabel, 0, 0);
        layeredPane.add(changeBackgroundButton,0, 0);
        layeredPane.add(changeBackgroundLabel,0, 0);
    }
    
    
    public void switchView() {
    /**
     * Method to switch between top and side view.
     */
        switch(currentView){
            case SIDEVIEW:
                playSound( swapSound);
                layeredPane.remove(0);
                layeredPane.remove(0);
                layeredPane.remove(0);
                layeredPane.remove(0);
                layeredPane.remove(0);
                layeredPane.remove(0);
                layeredPane.remove(0);
                layeredPane.remove(0);
                
                
                background.setTop();
                layeredPane.add(background, 0, -1 );
                layeredPane.add(changeModeButton, 0, 0);
                layeredPane.add(changeModeLabel, 0, 0);
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
                playSound( swapSound );
                topLight.turnOff();
                layeredPane.remove(0);
                layeredPane.remove(0);
                layeredPane.remove(0);
                layeredPane.remove(0);
                layeredPane.remove(0);
                layeredPane.remove(0);
                layeredPane.remove(0);
                
                switch(currentBackground){
                    case 1:
                        background.setOne();
                        break;
                    case 2:
                        background.setTwo();
                        break;
                    case 3:
                        background.setThree();
                        break;
                }
                layeredPane.add(background, 0, -1 );
                layeredPane.add(changeModeButton, 0, 0);
                layeredPane.add(changeModeLabel, 0, 0);
                layeredPane.add(changeBackgroundButton, 0, 0);
                layeredPane.add(changeBackgroundLabel, 0, 0);
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
        System.out.println("Listening...!");
        RecordSound.record();   //This needs to be replaced by an automatic process
        System.out.println("Done listening...!");
        SpeechToText.convert();
        
        final String token  = SpeechToText.renewAccessToken( KEY1 );
        final byte[] speech = SpeechToText.readData( "output.wav" );
        final String text   = SpeechToText.recognizeSpeech( token, speech );
        
        int startIndex = text.indexOf("name") + 7;
        int endIndex = startIndex;
        while (text.charAt(endIndex) != '\"') {
            endIndex++;
        }
        
        String finalText = text.substring( startIndex, endIndex );
        if (finalText.contains("profanity")) {
            speak("Don't be so rude!");
            return "why am I so rude";
        }
        
        System.out.println(finalText);
        
        currentMode = ANSWERMODE;
        
        return finalText;
    }
    
    
    public void answer(String question){
    
        String response = Wolfram.solve(question);
        
        switchModeTo(ANSWERMODE);
        
        speak(response);
        
        switchModeTo(LISTENINGMODE);
        
        
    }    
    
    
    public void switchModeTo(int nextMode){
        
        switch(nextMode){
            case OFFMODE:
                button.turnOff();
                topButton.turnOff();
                light.turnOff();
                topLight.turnOff();
                //microphone disable
                playSound( turnOffSound);
                currentMode = OFFMODE;
                break;
            case LISTENINGMODE:
                button.turnOn();
                topButton.turnOn();
                topLight.turnOn();
                light.turnOn();
                //microphone enable
                currentMode = LISTENINGMODE;
                playSound( turnOnSound );
                break;
            case ANSWERMODE:
                //all lights turn blue
                //microphone is disabled
                //on/off button is disabled
                break;
                
        }
        
        
    }
    
    
    public class Background extends JLabel {
        /**
         * Class to replace background image when switching views
         */
         
        Background(){
            setBounds(0, -125, 900, 900);
            setOne();
        }
         
        void setOne(){
            setIcon(sideBackground1);
            currentBackground = 1;
        }
         
        void setTwo(){
            setIcon(sideBackground2);
            currentBackground = 2;
        }
         
        void setThree(){
            setIcon(sideBackground3);
            currentBackground = 3;
        }  
        
        void setTop(){
            setIcon(topBackground);
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
                            switchModeTo(LISTENINGMODE);
                            
                            new Thread( new Runnable() {
                                public void run() {
                                    String question = listen();
                                    answer(question);
                                }
                            }).start();                          
                            
                            break;
                        case LISTENINGMODE:
                            switchModeTo(OFFMODE);
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
                            switchModeTo(LISTENINGMODE);
                            
                            String question = listen();
                            answer(question);
                            
                            break;
                        case LISTENINGMODE:
                            switchModeTo(OFFMODE);
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
                            playSound( muteSound);
                            muteButton.turnOn();
                            topLight.turnMute();
                            button.turnOff();
                            topButton.turnOff();
                            light.turnOff();
                            currentMode = MUTEMODE;
                            break;
                        case LISTENINGMODE:
                            playSound( muteSound);
                            muteButton.turnOn();
                            topLight.turnMute();
                            button.turnOff();
                            topButton.turnOff();
                            light.turnOff();
                            currentMode = MUTEMODE;
                            break;
                        case MUTEMODE:
                            playSound( muteSound);
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
            setBounds(606, 98, 90, 101);
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
    
    
    public class ChangeModeLabel extends JLabel {
    
        ImageIcon label = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/changeviewlabel.png") ) );
        
        ChangeModeLabel() {
            setIcon( label );
            setBounds(616, 144, 90, 101);
        }   
    }
    
    
    public class ChangeBackgroundButton extends JButton {
    
        ImageIcon changeBackground = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/changebackground.png") ) );
        
        ChangeBackgroundButton() {
            setBorderPainted(false);
            setContentAreaFilled(false); 
            setFocusPainted(false); 
            setBorder( null );
            setOpaque(false);
            setBounds(608, 196, 90, 101);
            setIcon( changeBackground );
            addActionListener( new ActionListener() {
                public void actionPerformed( ActionEvent e ) {
                    switch(currentBackground){
                        case FIRSTBG:
                            background.setTwo();
                            currentBackground = SECONDBG;
                            break;
                        case SECONDBG:
                            background.setThree();
                            currentBackground = THIRDBG;
                            break;
                        case THIRDBG:
                            background.setOne();
                            currentBackground = FIRSTBG;
                            break;
                    }
                    
                    
                }
            });
        }   
    }
    
    
    public class ChangeBackgroundLabel extends JLabel {
    
        ImageIcon bglabel = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/changebackgroundlabel.png") ) );
        
        ChangeBackgroundLabel() {
            setIcon( bglabel );
            setBounds(620, 240, 90, 101);
        }   
    }
    
    
    public static void main( String[] argv ){
	JFrame frame = new Echo();
	frame.setResizable( false );
	frame.setVisible( true );
        
    }
}