package sound;

import java.io.File;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
/*
 * Play sound. David Wakeling, 2017.
 */
public class PlaySound {
    
  private final static String FILENAME = "input.wav";
  static SourceDataLine line;

  /*
   * Set up stream. 
   */
  public static AudioInputStream setupStream( String name ) {
    try {
      File             file = new File( name );
      AudioInputStream stm  = AudioSystem.getAudioInputStream( file );
      return stm;
    } catch ( Exception ex ) {
      System.out.println( ex ); System.exit( 1 ); return null;
    }
  }

  /*
   * Read stream.
   */
  public static ByteArrayOutputStream readStream( AudioInputStream stm ) {
    try {
      AudioFormat           af  = stm.getFormat();
      ByteArrayOutputStream bos = new ByteArrayOutputStream();

      int  bufferSize = (int) af.getSampleRate() * af.getFrameSize();
      byte buffer[]   = new byte[ bufferSize ];

      for (;;) {
        int n = stm.read( buffer, 0, buffer.length );
        if ( n > 0 ) {
           bos.write( buffer, 0, n );
        } else {
          break;
        }
      }

      return bos;
    } catch ( Exception ex ) {
      System.out.println( ex ); System.exit( 1 ); return null;
    }
  }

  /*
   * Play stream.
   */
  public static void playStream( AudioInputStream stm, ByteArrayOutputStream bos ) {
    try {
      AudioFormat    af   = stm.getFormat();
      byte[]         ba   = bos.toByteArray();
      DataLine.Info  info = new DataLine.Info( SourceDataLine.class, af );
      line = (SourceDataLine) AudioSystem.getLine( info );

      line.open( af );
      line.start();
      line.write( ba, 0, ba.length );
    } catch ( Exception ex ) {
      System.out.println( ex ); System.exit( 1 );
    }
  }
  

  /*
   * Stop stream.
   */
  public static void stop() {
      line.stop();
      line.flush();
  }
}
