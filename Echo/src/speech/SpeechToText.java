package speech;

import main.HttpConnect;
import java.io.File;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.util.UUID;
/*
 * Speech to text conversion using Microsoft Cognitive Services.
 * 
 * See http://www.microsoft.com/cognitive-services/en-us/speech-api
 *
 * David Wakeling, 2017.
 */
public class SpeechToText {
  final static String LANG  = "en-GB";
  final static String INPUT = "output.wav";

  final static String KEY1 = "256a4ccc19dc41d7a75857c7dfd24825";
  final static String KEY2 = "ea072146f15446ed89d1c9f2498c0d87";

  /*
   * Renew an access token --- they expire after 10 minutes.
   */
  public static String renewAccessToken( String key1 ) {
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
   * Recognize speech.
   */
  public static String recognizeSpeech( String token, byte[] body ) { 
    final String method = "POST";
    final String url    
      = ( "https://speech.platform.bing.com/recognize"
        + "?" + "version"    + "=" + "3.0"
        + "&" + "format"     + "=" + "json"
        + "&" + "device.os"  + "=" + "wp7"
        + "&" + "scenarios"  + "=" + "smd"
        + "&" + "locale"     + "=" + LANG
        + "&" + "appid"      + "=" + "D4D52672-91D7-4C74-8AD8-42B1D98141A5"
        + "&" + "instanceid" + "=" + UUID.randomUUID().toString() 
        + "&" + "requestid"  + "=" + UUID.randomUUID().toString() 
        );
    final String[][] headers
      = { { "Content-Type"   , "audio/wav; samplerate=16000"  }
        , { "Content-Length" , String.valueOf( body.length )  }
        , { "Authorization"  , "Bearer " + token              }
        };
    byte[] response = HttpConnect.httpConnect( method, url, headers, body );
    return new String( response );
  } 

  /*
   * Read data from file.
   */
  public static byte[] readData( String name ) {
    try {
      File            file = new File( name );
      FileInputStream fis  = new FileInputStream( file );
      DataInputStream dis  = new DataInputStream( fis );
      byte[] buffer = new byte[ (int) file.length() ];
      dis.readFully( buffer );
      dis.close();
      return buffer;
    } catch ( Exception ex ) {
      System.out.println( ex ); System.exit( 1 ); return null;
    }
  }
}
