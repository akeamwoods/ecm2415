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
  final static String TEXT   = "Frankly, Lotti, I think your wet, soggy, mouldy and extremely raw pussy could do with a good pounding from Robin, your step dad, who has a massive twenty inch cock, that hasn't stopped throbbing for hours! Also I will spaff on your knee space if you don't suck my dick right now.";
  final static String LANG   = "en-US";
  final static String GENDER = "Male";
  final static String OUTPUT = "output.wav";
  final static String FORMAT = "riff-16khz-16bit-mono-pcm";

  final static String KEY1   = "256a4ccc19dc41d7a75857c7dfd24825";
  /* final static String KEY2   = "ea072146f15446ed89d1c9f2498c0d87"; */

  /*
   * Renew an access token --- they expire after 10 minutes.
   */
  static String renewAccessToken( String key1 ) {
    final String method = "POST";
    final String url = 
      "https://api.cognitive.microsoft.com/sts/v1.0/issueToken";
    final byte[] body = {}; 
    final String[][] headers
      = { { "Ocp-Apim-Subscription-Key", key1                          }
        , { "Content-Length"           , String.valueOf( body.length ) }
        };
    byte[] response = HttpConnect.httpConnect( method, url, headers, body );
    return new String( response ); 
  }

  /*
   * Synthesize speech.
   */
  static byte[] synthesizeSpeech( String token, String text
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
  static void writeData( byte[] buffer, String name ) {
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

  /*
   * Convert text to speech.
   */
  public static void main( String[] argv ) {
    final String token  = renewAccessToken( KEY1 );
    final byte[] speech = synthesizeSpeech( token, TEXT, LANG, GENDER, FORMAT );
    writeData( speech, OUTPUT );
  }
}
