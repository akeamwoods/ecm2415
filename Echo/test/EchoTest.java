public class EchoTest extends junit.framework.TestCase{

    @org.junit.BeforeClass
    public static void setUpClass() throws Exception {
    }

    @org.junit.AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of playSound method, of class Echo.
     */
    @org.junit.Test
    public void testPlaySound() {
        System.out.println("playSound");
        String url = "goodbyetune.wav";
        Echo.playSound(url);
    }

    /**
     * Test of component visibility of class Echo.
     */
    @org.junit.Test
    public void testMainVisible() {
        System.out.println("button and light are visible");
        Echo echo = new Echo();
        
        assertTrue(echo.button.isVisible());   
        assertTrue(echo.light.isVisible());
        
    }
    
        /**
     * Test of Echo OnOff and Listening Transitions.
     */
    @org.junit.Test
    public void testOnOffAndListeningTransitions() {
        System.out.println("OnOffAndListeningTransitions");
        Echo echo = new Echo();
        
        assertTrue(echo.button.getIcon() == echo.button.buttonOff); 
        assertTrue(echo.light.getIcon() == echo.light.lightOff);
        
        echo.button.doClick();
        
        
        assertTrue(echo.button.getIcon() == echo.button.buttonOn); 
        assertTrue(echo.light.getIcon() == echo.light.lightOn);
        
        echo.button.doClick();
        
        assertTrue(echo.button.getIcon() == echo.button.buttonOff); 
        assertTrue(echo.light.getIcon() == echo.light.lightOff);
        
    }
    
    

    
}