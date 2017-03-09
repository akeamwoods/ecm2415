package speech;

import main.HttpConnect;
import java.io.File;
import java.io.FileOutputStream;
import java.io.DataOutputStream;
/*import java.io.File;
import java.io.FileOutputStream;
import java.io.DataOutputStream;
/*
 * Text to speech conversion using Microsoft Cognitive Services.
 *
 * See http://www.microsoft.com/cognitive-services/en-us/speech-api
 *
 * David Wakeling, 2017.
 */
public class TextToSpeech {

  final static String KEY1   = "256a4ccc19dc41d7a75857c7dfd24825";
  /* final static String KEY2   = "ea072146f15446ed89d1c9f2498c0d87"; */

  /*
   * Synthesize speech.
   */
  public static byte[] synthesizeSpeech( String token, String text
                                , String lang,  String gender
                                , String format ) {
    final String method = "POST";
    final String url = "https://speech.platform.bing.com/synthesize";
    final byte[] body
      = ( "<speak version='1.0' xml:lang='en-us'>"
        + "<voice xml:lang='" + lang   + "' "
        + "xml:gender='"      + gender + "' "
        + "name='Microsoft Server Speech Text to Speech Voice"
        + " (en-US, ZiraRUS)'>"
        + text
        + "</voice></speak>" ).getBytes(); 
    final String[][] headers
      = { { "Content-Type"             , "application/ssml+xml"        }
        , { "Content-Length"           , String.valueOf( body.length ) }
        , { "Authorization"            , "Bearer " + token             }
        , { "X-Microsoft-OutputFormat" , format                        }
        };
    byte[] response = HttpConnect.httpConnect( method, url, headers, body );
    return response;
  } 

  /*
   * Write data to file.
   */
  public static void writeData( byte[] buffer, String name ) {
    try {
      File             file = new File( name );
      FileOutputStream fos  = new FileOutputStream( file );
      DataOutputStream dos  = new DataOutputStream( fos ); 
      dos.write( buffer );
      dos.flush();
      dos.close();
    } catch ( Exception ex ) {
      System.out.println( ex ); System.exit( 1 ); return;
    }
  }
}
