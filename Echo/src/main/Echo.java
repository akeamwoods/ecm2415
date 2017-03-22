package main;

import answer.*;
import speech.*;
import sound.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Toolkit;
import java.awt.Image;
import javax.swing.*;
import java.awt.Font;
import java.awt.Color;
import java.util.TimerTask;
import java.util.Timer;

import javax.sound.sampled.AudioInputStream;
/*
*Amazon Echo frame and constructor - 20//2017.
*/
public class Echo extends JFrame {
    
    /*SOUNDS.*/
    private final static String turnOnSound = "resources/audio/hellotune.wav";
    private final static String turnOffSound = "resources/audio/goodbyetune.aiff";
    private final static String swapSound = "resources/audio/woosh.wav";
    private final static String muteSound = "resources/audio/mute.wav";
    
    /*ACESS TOKEN AND KEY ATTRIBUTES.*/
    private final static String KEY1 = "256a4ccc19dc41d7a75857c7dfd24825";
    String token;
    static Timer timer = new Timer();
    static int seconds = 0;
    
    /*JFRAME ATTRIBUTES*/
    private JPanel contentPane = (JPanel) getContentPane();
    private JLayeredPane layeredPane = getLayeredPane();
    
    private final ImageIcon topBackground = new ImageIcon( "resources/topview/background2.jpg" );
    private final ImageIcon sideBackground1 = new ImageIcon ( "resources/sideview/backgrounds/background1.jpg" );
    private final ImageIcon sideBackground2 = new ImageIcon ( "resources/sideview/backgrounds/background2.jpg" );
    private final ImageIcon sideBackground3 = new ImageIcon ( "resources/sideview/backgrounds/background3.jpg" );
 
    private Image sideEcho = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sideview/echo.png"));
    private Image topEcho = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/topview/echo.png"));
    
    /*ECHO STATUS ATTRIBUTES*/
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
    private CurrentViewLabel currentLabel = new CurrentViewLabel();
    
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
    
    private MuteIconTop muteIconTop = new MuteIconTop();
    private MuteIconSide muteIconSide = new MuteIconSide();
    
    RecordSound recorder = new RecordSound();
    PlaySound player = new PlaySound();
    
    /*ATTRIBUTES FOR SIDE VIEW*/
    private ImageIcon icon = new ImageIcon( "/sideview/buttonon.png" );
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
    
    private Notification topview = new Notification();
    private SideNotification sideview = new SideNotification();
    private CloseNotification closeButton = new CloseNotification();
    private SideCloseNotification sideCloseButton = new SideCloseNotification();
    private CloseInternetNotification closeInternetButton = new CloseInternetNotification();
    ImageIcon closeBut = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/notifications/closebutton.png") ) );
    private InternetCheck internet = new InternetCheck();
    private boolean disconnected = false;
    
    public Echo() {
        
        /**
         * Method to create an Amazon Echo
         */
       
        setupGUI();
        
        try {
            token = SpeechToText.renewAccessToken( KEY1 );
        } catch (Exception ex) {
            internet.disconnected();
        }
        autoRenewToken(KEY1);
    }
    
    
    public void setupGUI() {
        
        /**
         * Method to set up the GUI
         */
        
        setTitle( "Amazon Echo Simulator" );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setContentPane( new JLabel(sideBackground1) );
	setIconImage( icon.getImage() );
	setLayout( null );
        pack();
        
        
        top.setBounds( 137, 139, 426, 425);
        side.setBounds( 250, 110, 200, 500);
        
        label1a.setBounds(16, 379, 500, 500);
        label1a.setFont(new Font("Helvetica", Font.BOLD, 12));
        label1a.setForeground(Color.BLACK);
        label1b.setBounds(75, 380, 610, 500);
        label1b.setFont(new Font("Helvetica", Font.ITALIC, 11));
        label1b.setForeground(Color.BLACK);
        label2a.setBounds(23, 409, 500, 500);
        label2a.setFont(new Font("Helvetica", Font.BOLD, 12));
        label2a.setForeground(Color.BLACK);
        label2b.setBounds(75, 410, 610, 500);
        label2b.setFont(new Font("Helvetica", Font.ITALIC, 11));
        label2b.setForeground(Color.BLACK);
        layeredPane.add(closeInternetButton, 101, 0);
        layeredPane.add(internet, 100, 0);
        
        layeredPane.add(sideview, 10, 0);
        layeredPane.add(sideCloseButton, 11, 0);
        layeredPane.add(currentLabel, 6, 0);
        currentLabel.SetSide();
        layeredPane.add(label1a, 0, 0);
        layeredPane.add(label2a, 0, 0);
        layeredPane.add(label1b, 0, 0);
        layeredPane.add(label2b, 0, 0);
        layeredPane.add(side, 0, -1);
        layeredPane.add(button, 0, 0);
        layeredPane.add(muteIconTop, 0, 0);
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
                layeredPane.remove(0);
                layeredPane.remove(0);
                layeredPane.remove(0);
                layeredPane.remove(0);
                layeredPane.remove(0);
                
                background.setTop();
                
                layeredPane.add(closeInternetButton, 101, 0);
                layeredPane.add(internet, 100, 0);
                layeredPane.add(closeButton, 11, 0);
                layeredPane.add(topview, 10, 0);
                layeredPane.add(label1a, 6, 0);
                layeredPane.add(label2a, 6, 0);
                layeredPane.add(label1b, 6, 0);
                layeredPane.add(label2b, 6, 0);
                layeredPane.add(currentLabel, 6, 0);
                currentLabel.SetTop();
                layeredPane.add(changeModeButton, 5, 1);
                layeredPane.add(changeModeLabel, 5, 2);
                layeredPane.add(muteIconTop, 4, 0);
                layeredPane.add(muteButton, 3, 0);
                layeredPane.add(topButton, 3, 0);
                layeredPane.add(topLight, 2, 0);
                layeredPane.add(top, 1, 0);
                layeredPane.add(background, 0, 0 );
                if (light.getStatus() != 0) {
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
                
                layeredPane.add(closeInternetButton, 101, 0);
                layeredPane.add(internet, 100, 0);
                layeredPane.add(sideview, 10, 0);
                layeredPane.add(sideCloseButton, 11, 0);
                layeredPane.add(currentLabel, 6, 0);
                currentLabel.SetSide();
                layeredPane.add(label1a, 0, 0);
                layeredPane.add(label2a, 0, 0);
                layeredPane.add(label1b, 0, 0);
                layeredPane.add(label2b, 0, 0);
                layeredPane.add(muteIconTop, 10, 0);
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
                    case 0:
                        light.turnOff();
                        break;
                    case 1:
                        light.turnOn();
                        break;
                    case 2:
                        light.turnOn();
                        break;
                }
                layeredPane.repaint();
                currentView = SIDEVIEW;
                break;
        }
    }

    
    public synchronized void playSound( final String url ) {
        
        new Thread(new Runnable() {
            public void run() {
                AudioInputStream stream = player.setupStream( url );
                player.playStream( stream, player.readStream( stream ) );
            }
        }).start();
    }

    
    public void autoRenewToken(String key) {
        
        TimerTask task;
        
        task = new TimerTask() {
            
            public void run() {
                if (seconds > 540) {
                    try {
                        token = SpeechToText.renewAccessToken( KEY1 );
                        System.out.println("Renewed key!");
                    } catch (Exception ex) {
                        System.out.print("Could not renew access key!");
                        internet.disconnected();
                    }
                    seconds = 0;
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

            recorder.record();
        
        if( currentMode == LISTENINGMODE ) {
            label1b.setText("Please wait.");
            final byte[] speech;
            final String text;

            speech = SpeechToText.readData( "output.wav" );
            try {
                text = SpeechToText.recognizeSpeech( token, speech );
            } catch (Exception ex) {
                internet.disconnected();
                return "";
            }
            
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

            label1b.setText(finalText);
            return finalText;
        }
        else {
            label1b.setText("");
            return null;
        }
    }
    
    
    public void answer(String question){
    
        if (currentMode == LISTENINGMODE && disconnected == false) {
            switchModeTo(ANSWERMODE);
            label2b.setText("Answering...");
            String response = Wolfram.solve(question);

            label2b.setText(response);
            speak(response);
            topLight.turnStill();
        }
    }    
    
    
    public void switchModeTo(int nextMode){
        
        switch(nextMode){
            case OFFMODE:
                
                switch(currentMode){
                    case LISTENINGMODE:
                        recorder.stop();
                        break;
                        
                    case ANSWERMODE:
                        player.stop();
                        break;
                }
                
                button.turnOff();
                topButton.turnOff();
                light.turnOff();
                topLight.turnOff();
                muteButton.turnOff();
                muteIconTop.turnOff();
                muteIconSide.turnOff();
                
                label1b.setText("");
                label2b.setText("");
                
                currentMode = OFFMODE;
                break;
                
            case LISTENINGMODE:
                
                button.turnOn();
                topButton.turnOn();
                topLight.turnSpinning();
                light.turnOn();
                
                Thread t = new Thread( new Activity() );
                t.start();
                
                currentMode = LISTENINGMODE;
                break;
                
            case ANSWERMODE:
                
                if( currentMode == MUTEMODE) {
                    label1b.setText("");
                }
                
                button.turnOn();
                topButton.turnOn();
                
                light.turnOn();
                //microphone is disabled
                //on/off button is disabled
                
                currentMode = ANSWERMODE;
                break;
                
            case MUTEMODE:
                
                if (currentMode == LISTENINGMODE) {
                    player.stop();
                }
                label1b.setText("Microphone is currently muted.");
                muteIconSide.turnOn();
                muteIconTop.turnOn();
                muteButton.turnOn();
                topLight.turnMute();
                light.turnMute();
                button.turnMute();
                topButton.turnMute();
                
                currentMode = MUTEMODE;
                break;
        }
        
        
    }
    
    
    private class Activity implements Runnable {
    
        public void run() {
            String question = listen();
            answer(question);
            
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
            currentLabel.SetTop();
        }
    }

//WORKS
    public class Button extends JButton {
        
        ImageIcon buttonOff = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sideview/buttonoff.png") ) );
        ImageIcon buttonOn = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sideview/buttonon.png") ) );
        ImageIcon buttonMute = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sideview/buttonmute.png") ) );

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
                            playSound( turnOnSound );
                            switchModeTo(ANSWERMODE);
                            break;
                        case LISTENINGMODE:
                            playSound( turnOffSound);
                            switchModeTo(OFFMODE);
                            break;
                        case ANSWERMODE:
                            playSound( turnOffSound);
                            switchModeTo(OFFMODE);
                            break;
                        case MUTEMODE:
                            playSound( turnOffSound );
                            switchModeTo(OFFMODE);
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
        
        void turnMute() {
            setIcon( buttonMute );
            status = -1;
        }
        
        int getStatus() {
            return status;
        }
    }
    

    public class ActionButton extends JButton {
        
        ImageIcon topButtonOff = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/topview/actionbuttonoff.png") ) );
        ImageIcon topButtonOn = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/topview/actionbuttonon.png") ) );
        ImageIcon topButtonMute = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/topview/actionbuttonmuted.png") ) );

        int status = 0;
        
        ActionButton() {
            setBounds( 388, 267, 153, 163);
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
                            break;
                        case LISTENINGMODE:
                            break;
                        case ANSWERMODE:
                            playSound( turnOnSound );
                            switchModeTo(LISTENINGMODE);
                            break;
                        case MUTEMODE:
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
        
        void turnMute(){
            setIcon(topButtonMute);
            status = -1;
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
                            break;
                        case LISTENINGMODE:
                            playSound( muteSound);
                            switchModeTo( MUTEMODE );
                            break;
                        case ANSWERMODE:
                            playSound( muteSound );
                            switchModeTo( MUTEMODE );
                            break;
                        case MUTEMODE:
                            playSound( muteSound);
                            muteIconSide.turnOff();
                            muteIconTop.turnOff();
                            muteButton.turnOff();
                            topLight.turnStill();
                            light.turnOn();
                            switchModeTo( ANSWERMODE );
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
        ImageIcon topLightStill = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/topview/lighton.png") ) );
        ImageIcon topLightOff = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/topview/lightoff2.png") ) );
        ImageIcon topLightMute = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/topview/slowmute.gif") ) );
        
        int status = 0;
        
        TopLight() {

            setBounds(0, 0, 700, 677);
            setIcon( topLightOff );
        }
        
        void turnOn() {
            switch (status) {
                case -1:
                    setIcon (topLightMute);
                    break;
                case 0:
                    setIcon (topLightStill);
                    status = 1;
                    break;
                case 1:
                    setIcon (topLightStill);
                    status = 1;
                    break;
                case 2:
                    setIcon (topLightOn);
                    status = 2;
                    break;
            }
            
        }
        
        void turnOff() {
            setIcon( topLightOff );
            status = 0;
        }
        
        void turnMute() {
            setIcon( topLightMute );
            status = -1;
        }
        
        void turnSpinning() {
            setIcon (topLightOn);
            status = 2;
        }
        
        void turnStill() {
            setIcon (topLightStill);
            status = 1;
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
            setBounds(616, 146, 90, 101);
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
    
   public class MuteIconTop extends JLabel {
    
        ImageIcon muteIconTop = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/topview/woodmute.png") ) );
        ImageIcon muteIconOff = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/mutenoicon.png") ) );

        MuteIconTop() {
            setBounds(10, 527, 300, 100);
            setIcon( muteIconOff );
        }
 
        
        void turnOn() {
            setIcon( muteIconTop );
        }
        
        void turnOff() {
            setIcon( muteIconOff );
        }
        
    }
   
   public class MuteIconSide extends JLabel {
    
        ImageIcon muteIconOff = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/mutenoicon.png") ) );
        ImageIcon muteIconSide = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sideview/muteicon.png") ) );

        MuteIconSide() {
            setBounds(10, 527, 300, 100);
            setIcon( muteIconOff );
        }
 
        
        void turnOn() {
            setIcon( muteIconSide );
        }
        
        void turnOff() {
            setIcon( muteIconOff );
        }
        
    }
   
   
   public class Notification extends JLabel {
       
       ImageIcon topview = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/notifications/topview.png") ) );
       ImageIcon close = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/notifications/close.png") ) );

        Notification() {
            setIcon( topview );
            setBounds(290, 120, 318, 90);
        }
        
        void closeNotification() {
            setIcon( close );
        }
           
   }
   
   
   public class CloseNotification extends JButton {
        
        ImageIcon closeBut = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/notifications/closebutton.png") ) );
        ImageIcon closeInvis = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/notifications/noclosebutton.png") ) );
        
        CloseNotification() {
            setBounds( 295, 125, 14, 14);
            setBorderPainted(false);
            setContentAreaFilled(false); 
            setFocusPainted(false); 
            setBorder( null );
            setOpaque(false);
            setIcon( closeBut );
            addActionListener( new ActionListener() {
                public void actionPerformed( ActionEvent e ) {
                    setIcon(closeInvis);
                    closeButton.setEnabled(false);
                    topview.closeNotification();
                }
            }
            );
        }
   }
           
   
    public class CurrentViewLabel extends JLabel{
        private ImageIcon top = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/topview/currenttop.png")));
        private ImageIcon side = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sideview/currentside1.png")));
        
       CurrentViewLabel(){
           setBounds(10, 110, 192, 71);
           setIcon( side );
       }
       
       void SetTop(){
           setIcon( top );
       }
       
       void SetSide(){
           setIcon( side );
       }
          
    }
    
    public class CloseInternetNotification extends JButton {
        
        ImageIcon closeInvis = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/notifications/noclosebutton.png") ) );
        
        CloseInternetNotification() {
            setBounds( 207, 299, 14, 14);
            setBorderPainted(false);
            setContentAreaFilled(false); 
            setFocusPainted(false); 
            setBorder( null );
            setOpaque(false);
            setIcon( closeBut );
            addActionListener( new ActionListener() {
                public void actionPerformed( ActionEvent e ) {
                    setIcon(closeInvis);
                    closeInternetButton.setEnabled(false);
                    internet.connected();
                }
            }
            );
        }
   }
    
    public class InternetCheck extends JLabel{
        
        private ImageIcon internetDisconnected = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/nointernet.png")));
        private ImageIcon internetConnected = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/internet.png")));

        InternetCheck(){
            setBounds(0, 0, 700, 677);
            setIcon(internetConnected);
        }
        
        void connected(){
            disconnected = false;
            setIcon( internetConnected );
        }
        
        void disconnected(){
            disconnected = true;
            closeInternetButton.setIcon(closeBut);
            closeInternetButton.setEnabled(true);
            setIcon( internetDisconnected );
            if (topLight.getStatus() == 2){
                topLight.turnStill();
            }
            label1b.setText("");
            label2b.setText("");
        }
    }
    
    public class SideNotification extends JLabel {
       
       ImageIcon sideview = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/notifications/sideview.png") ) );
       ImageIcon close = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/notifications/close.png") ) );

        SideNotification() {
            setIcon( sideview );
            setBounds(290, 120, 318, 90);
        }
        
        void closeNotification() {
            setIcon( close );
        }
           
   }
   
   
   public class SideCloseNotification extends JButton {
        
        ImageIcon closeBut = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/notifications/closebutton.png") ) );
        ImageIcon closeInvis = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/notifications/noclosebutton.png") ) );
        
        SideCloseNotification() {
            setBounds( 295, 125, 14, 14);
            setBorderPainted(false);
            setContentAreaFilled(false); 
            setFocusPainted(false); 
            setBorder( null );
            setOpaque(false);
            setIcon( closeBut );
            addActionListener( new ActionListener() {
                public void actionPerformed( ActionEvent e ) {
                    setIcon(closeInvis);
                    sideCloseButton.setEnabled(false);
                    sideview.closeNotification();
                }
            }
            );
        }
   }
   
    
    public static void main( String[] argv ){
	JFrame frame = new Echo();
	frame.setResizable( false );
	frame.setVisible( true );
    }
    
}