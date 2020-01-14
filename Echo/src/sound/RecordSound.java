package sound;

import java.io.File;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
/*
 * Record sound. David Wakeling, 2017.
 */
public class RecordSound {
  private static final String  FILENAME        = "output.wav";
  private static final int     TIMER           = 10;     /* secs */
  private static final int     SAMPLE_RATE     = 16000; /* MHz  */
  private static final int     SAMPLE_SIZE     = 16;    /* bits */
  private static final int     SAMPLE_CHANNELS = 1;     /* mono */
  static volatile boolean      PLAY            = true;
  

  /*
   * Set up stream.
   */
  static AudioInputStream setupStream() {
    try {
      AudioFormat af =
        new AudioFormat( SAMPLE_RATE
                       , SAMPLE_SIZE
                       , SAMPLE_CHANNELS
                       , true /* signed */
                       , true /* little-endian */
                       );
      DataLine.Info    info = new DataLine.Info( TargetDataLine.class, af );
      TargetDataLine   line = (TargetDataLine) AudioSystem.getLine( info );
      AudioInputStream stm  = new AudioInputStream( line );
      line.open( af );
      line.start();
      return stm;
    } catch ( Exception ex ) {
      System.out.println( ex ); System.exit( 1 ); return null;
    }
  }

  /*
   * Read stream.
   */
  static ByteArrayOutputStream readStream( AudioInputStream stm ) {
      
    try {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();

      int  bufferSize = SAMPLE_RATE * stm.getFormat().getFrameSize();
      byte buffer[]   = new byte[ bufferSize ];

      PLAY = true;
      for ( int counter = TIMER; counter > 0; counter-- ) {
        if (PLAY == true) {
            int sum = 0;
            int n = stm.read( buffer, 0, buffer.length );

            if ( n > 0 ) {
              for ( int i = 0; i < buffer.length; i++) {
                sum += buffer[i];
            }

            double average = (double)sum/buffer.length;

            double sumMeanSquare = 0;;
            for (int i = 0; i < buffer.length; i++) {
                double f = buffer[i] - average;
                sumMeanSquare += f * f;
            }

            double averageMeanSquare = sumMeanSquare/buffer.length;
            double rootMeanSquare = Math.sqrt(averageMeanSquare);

            bos.write( buffer, 0, n );

            if ( counter < 10 && rootMeanSquare < 30 ){
                break;
            }
            } else {
              break;
            }
        }
        else {
            return null;
        }
      }

      return bos;
    } catch ( Exception ex ) {
      System.out.println( ex ); System.exit( 1 ); return null;
    }
  }

  /*
   * Record sound. 
   */
  static void recordSound( String name, ByteArrayOutputStream bos ) {
    try {
      AudioFormat af =
        new AudioFormat( SAMPLE_RATE
                       , SAMPLE_SIZE
                       , SAMPLE_CHANNELS
                       , true /* signed */
                       , true /* little-endian */
                       );
      byte[]           ba   = bos.toByteArray();
      InputStream      is   = new ByteArrayInputStream( ba );
      AudioInputStream ais  = new AudioInputStream( is, af, ba.length );
      File             file = new File( name );
      AudioSystem.write( ais, AudioFileFormat.Type.WAVE, file );
    } catch ( Exception ex ) {
      System.out.println( ex ); System.exit( 1 );
    }
  }
  
  
  /*
   * Stop recording. 
   */
  public static void stop() {
      PLAY = false;
  }

  /*
   * Record sound.
   */
  public static void record() {
      
    AudioInputStream stm = setupStream();
    ByteArrayOutputStream bos = readStream(stm);
    if (bos != null) {
      recordSound( FILENAME, bos );
    }
  }
}