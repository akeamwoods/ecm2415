import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Toolkit;
import java.awt.Image;
import javax.swing.*;
import answer.*;
import java.awt.Font;
import java.awt.Color;
import java.util.TimerTask;
import java.util.Timer;

import javax.sound.sampled.AudioInputStream;
/*
*Amazon Echo frame and constructor - 20//2017.
*/
public class Echo extends JFrame {
    // Sound Variables
    private final static String turnOnSound = "resources/audio/hellotune.wav";
    private final static String turnOffSound = "resources/audio/goodbyetune.aiff";
    private final static String swapSound = "resources/audio/woosh.wav";
    private final static String muteSound = "resources/audio/mute.wav";
    
    /*ATTRIBUTES FOR LISTENING*/
    private final static String KEY1 = "256a4ccc19dc41d7a75857c7dfd24825";
    String token  = SpeechToText.renewAccessToken( KEY1 );
    static Timer timer = new Timer();
    static int seconds = 0;
    
    // JPanel Attributes
    private JPanel contentPane = (JPanel) getContentPane();
    private JLayeredPane layeredPane = getLayeredPane();
    
    // Background Images
    private final ImageIcon topBackground = new ImageIcon( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/topview/background2.jpg") ) );
    private final ImageIcon sideBackground1 = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sideview/backgrounds/background1.jpg") ) );;
    private final ImageIcon sideBackground2 = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sideview/backgrounds/background2.jpg") ) );
    private final ImageIcon sideBackground3 = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sideview/backgrounds/background3.jpg") ) );
 
    // Echo Images
    private Image sideEcho = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sideview/echo.png"));
    private Image topEcho = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/topview/echo.png"));
    
    // Mode Variables
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
    
    private int currentView = SIDEVIEW;
    
    // Attributes for Background
    private final int FIRSTBG = 1;
    private final int SECONDBG = 2;
    private final int THIRDBG = 3;

    private int currentBackground = 1;
    
    // Change Background Icons
    private ChangeBackgroundButton changeBackgroundButton = new ChangeBackgroundButton();
    private ChangeBackgroundLabel changeBackgroundLabel = new ChangeBackgroundLabel();
    
    /*ATTRIBUTE USED FOR SWAPPING BACKGROUND WHEN CHANGING VIEW*/
    private Background background = new Background();
    
    private MuteIcon muteIcon = new MuteIcon();
    
    /*ATTRIBUTES FOR SIDE VIEW*/
    private Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sideview/buttonon.png") );
    private Button button = new Button();
    private Light light = new Light();
    private JLabel side = new JLabel( new ImageIcon( sideEcho ) );
    
    /*ATTRIBUTES FOR TOP VIEW*/
    private ActionButton topButton = new ActionButton();
    private MuteButton muteButton = new MuteButton();
    private JLabel top = new JLabel( new ImageIcon( topEcho ) );
    private TopLight topLight = new TopLight();

    private JLabel label1a = new JLabel("Question: ");
    private JLabel label1b = new JLabel();
    private JLabel label2a = new JLabel("Answer: ");
    private JLabel label2b = new JLabel();
    
    public Echo() {
        
        /**
         * Method to create an Amazon Echo
         */
       
        setupGUI();
        autoRenewToken(KEY1);
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
        
        label1a.setBounds(16, 379, 500, 500);
        label1a.setFont(new Font("Helvetica", Font.BOLD, 12));
        label1a.setForeground(Color.BLACK);
        label1b.setBounds(75, 380, 500, 500);
        label1b.setFont(new Font("Helvetica", Font.ITALIC, 11));
        label1b.setForeground(Color.BLACK);
        label2a.setBounds(23, 409, 500, 500);
        label2a.setFont(new Font("Helvetica", Font.BOLD, 12));
        label2a.setForeground(Color.BLACK);
        label2b.setBounds(75, 410, 500, 500);
        label2b.setFont(new Font("Helvetica", Font.ITALIC, 11));
        label2b.setForeground(Color.BLACK);
        
        layeredPane.add(label1a, 0, 0);
        layeredPane.add(label2a, 0, 0);
        layeredPane.add(label1b, 0, 0);
        layeredPane.add(label2b, 0, 0);
        layeredPane.add(side, 0, -1);
        layeredPane.add(button, 0, 0);
        layeredPane.add(muteIcon, 0, 0);
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
                layeredPane.remove(0);
                layeredPane.remove(0);
                layeredPane.remove(0);
                layeredPane.remove(0);
                layeredPane.remove(0);
                
                background.setTop();
                layeredPane.add(label1a, 10, 0);
                layeredPane.add(label2a, 9, 0);
                layeredPane.add(label1b, 8, 0);
                layeredPane.add(label2b, 7, 0);
                layeredPane.add(background, 0, 0 );
                layeredPane.add(changeModeButton, 6, 0);
                layeredPane.add(changeModeLabel, 5, 0);
                layeredPane.add(top, 1, 0);
                layeredPane.add(muteIcon, 10, 0);
                layeredPane.add(muteButton, 4, 0);
                layeredPane.add(topButton, 3, 0);
                layeredPane.add(topLight, 2, 0);
                if (light.getStatus() == 1) {
                    topLight.turnOn();
                }
           
                layeredPane.repaint();
                currentView = TOPVIEW;
                break;
                
            case TOPVIEW:
                playSound( swapSound );
                layeredPane.remove(0);
                layeredPane.remove(0);
                layeredPane.remove(0);
                layeredPane.remove(0);
                layeredPane.remove(0);
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
                layeredPane.add(label1a, 0, 0);
                layeredPane.add(label2a, 0, 0);
                layeredPane.add(label1b, 0, 0);
                layeredPane.add(label2b, 0, 0);
                layeredPane.add(muteIcon, 10, 0);
                layeredPane.add(background, 0, -1 );
                layeredPane.add(changeModeButton, 0, 0);
                layeredPane.add(changeModeLabel, 0, 0);
                layeredPane.add(changeBackgroundButton, 0, 0);
                layeredPane.add(changeBackgroundLabel, 0, 0);
                layeredPane.add(side, 0, 0);
                layeredPane.add(button, 0, 0);
                layeredPane.add(light, 0, 0);
                switch(topLight.getStatus()){
                    case -1:
                        light.turnMute();
                        break;
                    case 1:
                        light.turnOn();
                        break;
                    case 0:
                        light.turnOff();
                        break;
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

    
    public void autoRenewToken(String key) {
        
        TimerTask task;
        
        task = new TimerTask() {
            
            public void run() {
                if (seconds > 540) {
                    token = SpeechToText.renewAccessToken( KEY1 );
                    seconds = 0;
                    System.out.println("Renewed key!");
                }
                else {
                    seconds++;
                }
            }
        };
        timer.schedule(task, 0, 1000);
    }
    
    
    public synchronized void speak(String speech) {

        /**
         * Method to speak an input String
         */
        
        final byte[] byteArray = TextToSpeech.synthesizeSpeech(token, speech, "en-US", "Male", "riff-16khz-16bit-mono-pcm");
        TextToSpeech.writeData(byteArray, "resources/output.wav");

        playSound("resources/output.wav");
    }
    

    public String listen() {
        
        /**
         * Method listens for speech then returns it in String format
         */
        label1b.setText("Listening...");
        RecordSound.record();   //This needs to be replaced by an automatic process
        label1b.setText("Please wait.");
        
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
        
        label1b.setText(finalText);
        return finalText;
    }
    
    
    public void answer(String question){
    
        label2b.setText("Answering...");
        String response = Wolfram.solve(question);
        
        switchModeTo(ANSWERMODE);
        
        label2b.setText(response);
        speak(response);
        
        switchModeTo(LISTENINGMODE);
        
        
    }    
    
    
    Thread activityThread = new Thread( new Runnable() {
                                public void run() {
                                    String question = listen();
                                    answer(question);
                                }
                            });
    
    
    public void switchModeTo(int nextMode){
        
        switch(nextMode){
            case OFFMODE:
                button.turnOff();
                topButton.turnOff();
                light.turnOff();
                topLight.turnOff();
                muteButton.turnOff();
                
                label1b.setText("");
                label2b.setText("");
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
            setBounds(0, -112, 900, 900);
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
                            
                            Thread activityThread = new Thread( new Runnable() {
                                public void run() {
                                    String question = listen();
                                    answer(question);
                                }
                            });
                            activityThread.start();                          
                            
                            break;
                        case LISTENINGMODE:
                            switchModeTo(OFFMODE);
                            break;
                        case ANSWERMODE:
                            switchModeTo(OFFMODE);
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
                            muteIcon.turnOn();
                            muteButton.turnOn();
                            topLight.turnMute();
                            light.turnMute();
                            topButton.turnOff();
                            currentMode = MUTEMODE;
                            break;
                        case LISTENINGMODE:
                            playSound( muteSound);
                            muteIcon.turnOn();
                            muteButton.turnOn();
                            topLight.turnMute();
                            light.turnMute();
                            button.turnOff();
                            topButton.turnOff(); 
                            currentMode = MUTEMODE;
                            break;
                        case MUTEMODE:
                            playSound( muteSound);
                            muteIcon.turnOff();
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
        ImageIcon lightMute = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sideview/lightmute.png") ) );

        int status = 0;
        
        Light() {
            setBounds(267, 100, 300, 100);
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
        
        void turnMute() {
            setIcon( lightMute );
            status = -1;
        }
        
        
        int getStatus() {
            return status;
        }
    }
    
    
    public class TopLight extends JLabel {
    
        ImageIcon topLightOn = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/topview/lighton.gif") ) );
        ImageIcon topLightOff = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/topview/lightoff2.png") ) );
        ImageIcon topLightMute = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/topview/muteplain.png") ) );
        
        int status = 0;
        
        TopLight() {

            setBounds(0, -62, 800, 800);
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
            status = -1;
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
    
   public class MuteIcon extends JLabel {
    
        ImageIcon muteIconOff = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/mutenoicon.png") ) );
        ImageIcon muteIconOn = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/muteicon.png") ) );

        MuteIcon() {
            setBounds(10, 86, 300, 100);
            setIcon( muteIconOff );
        }
        
        void turnOn() {
            setIcon( muteIconOn );
        }
        
        void turnOff() {
            setIcon( muteIconOff );
        }
        
    }
    
    public static void main( String[] argv ){
	JFrame frame = new Echo();
	frame.setResizable( false );
	frame.setVisible( true );
        
    }
}