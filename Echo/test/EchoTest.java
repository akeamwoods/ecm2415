

public class EchoTest extends junit.framework.TestCase{

    @org.junit.BeforeClass
    public static void setUpClass() throws Exception {
        Echo echo = new Echo();
        echo.setResizable( false );
	echo.setVisible( true );
    }

    @org.junit.AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of elements that should always be visible.
     */
    
    @org.junit.Test
    public void testConstantVisibilities() {
        
	assertTrue(echo.getLabel1a().isVisible());
        assertTrue(echo.getLabel1b().isVisible());
        assertTrue(echo.getLabel2a().isVisible());
        assertTrue(echo.getLabel2b().isVisible());
        assertTrue(echo.getChangeModeButton().isVisible());
        assertTrue(echo.getChangeViewLabel().isVisible());
        
        
    }
    
    

    /**
     * Test of component visibility of class Echo in top view.
     */
    @org.junit.Test
    public void testTopViewVisibilities() {

        assertTrue(echo.getTopButton().isVisible());
        assertTrue(echo.getMuteButton().isVisible());
        assertTrue(echo.getTop().isVisible());
        assertTrue(echo.getTopLight().isVisible());
        
    }
    
        /**
     * Test of component visibility of class Echo in side view.
     */
    @org.junit.Test
    public void testSideViewVisibilities() {

        assertTrue(echo.getSide().isVisible());
        assertTrue(echo.getButton().isVisible());
        assertTrue(echo.getChangeBackgroundButton().isVisible());
        assertTrue(echo.getChangeBackgroundLabel().isVisible());
        
        assertFalse(echo.getTopButton().isVisible());
        assertFalse(echo.getMuteButton().isVisible());
        assertFalse(echo.getTop().isVisible());
        assertFalse(echo.getTopLight().isVisible());
        
    }
    
    
            /**
     * Test of component permissions of class Echo in OnOffMode.
     */
    @org.junit.Test
    public void testOnOffModePermissionsTop() {

        testConstantVisibilities();
        testSideViewVisibilities();
        
        assertFalse(echo.getTopLight().isEnabled());
        assertTrue(echo.getTopButton().isEnabled());
        
    }
    
                /**
     * Test of component permissions of class Echo in OnOffMode.
     */
    @org.junit.Test
    public void testOnOffModePermissionsSide() {

        testConstantVisibilities();
        testTopViewVisibilities();
        
        assertFalse(echo.getLight().isEnabled());
        assertTrue(echo.getButton().isEnabled());
        
    }
    
    
        /**
     * Test of Echo OnOff and Listening Transitions.
     */
    @org.junit.Test
    public void testOnOffAndListeningTransitions() {
        
        
        assertTrue(echo.getButton().getIcon() == echo.getButton().buttonOff); 
        assertTrue(echo.getLight().getIcon() == echo.getButton().lightOff);
        
        echo.getButton().doClick();
        
        
        assertTrue(echo.getButton().getIcon() == echo.getButton().buttonOn); 
        assertTrue(echo.getLight().getIcon() == echo.getButton().lightOn);
        
        echo.button.doClick();
        
        assertTrue(echo.getButton().getIcon() == echo.getButton().buttonOff); 
        assertTrue(echo.getLight().getIcon() == echo.getButton.lightOff);
        
    }
    
    

            /**
     * Test of component permissions of class Echo in Listening mode.
     */
    @org.junit.Test
    public void testListeningModePermissionsSide() {

        testConstantVisibilities();
        testSideViewVisibilities();
        
        assertTrue(echo.getLight().isEnabled());
        assertTrue(echo.getButton().isEnabled());
        
    }
    
                /**
     * Test of component permissions of class Echo in Listening mode.
     */
    @org.junit.Test
    public void testListeningModePermissionsTop() {

        testConstantVisibilities();
        testTopViewVisibilities();
        
        assertTrue(echo.getTopLight().isEnabled());
        assertTrue(echo.getTopButton().isEnabled());
        
    }
    
    
               /**
     * Test of component permissions of class Echo in Answering mode.
     */
    @org.junit.Test
    public void testAnsweringModePermissionsSide() {

        testConstantVisibilities();
        testSideViewVisibilities();
        
        assertTrue(echo.getLight().isEnabled());
        assertTrue(echo.getButton().isEnabled());
        
    }
    
                /**
     * Test of component permissions of class Echo in Answering mode.
     */
    @org.junit.Test
    public void testAnsweringModePermissionsTop() {

        testConstantVisibilities();
        testTopViewVisibilities();
        
        assertTrue(echo.getTopLight().isEnabled());
        assertTrue(echo.getTopButton().isEnabled());
        
    }
    

    
}