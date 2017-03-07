package answer;



import java.io.File;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.net.URLEncoder;

/*
 * Access the Wolfram Alpha computational knowledge engine. 
 * 
 * See http://products.wolframalpha.com/api/documentation.html
 *
 * David Wakeling, 2017.
 */
public class Wolfram {
  private String question = "DEFAULT QUESTION";
  final static String APPID   = "H46UG6-XKXYP5GH4R";


  
  public Wolfram(){
      
  }
    
    
  /*
   * Solve.
   */
  public static String solve( String input ) { 
    final String method = "POST";
    final String url    
      = ( "http://api.wolframalpha.com/v2/query"
        + "?" + "appid"  + "=" + APPID
        + "&" + "input"  + "=" + urlEncode( input )
        + "&" + "output" + "=" + "JSON"
        );
    final String[][] headers
      = { { "Content-Length", "0" }
        };
    final byte[] body = new byte[0];
    byte[] response   = HttpConnect.httpConnect( method, url, headers, body );
    String solution = new String( response );
    
    
    int plaintextIndex = solution.indexOf("plaintext", solution.indexOf("plaintext") + 1);
    
    String x = solution.substring(plaintextIndex+14, (solution.length()-1));
    
    int quotationmarkIndex = solution.indexOf("\"", (plaintextIndex+14));
    
    String finalanswer = solution.substring((plaintextIndex+14), quotationmarkIndex);
    finalanswer = finalanswer.replace("\\n", " ");
    finalanswer = finalanswer.replace("\\", "");
    
    return finalanswer;
  } 
  

  /*
   * URL encode string.
   */ 
  private static String urlEncode( String s ) {
    try {
      return URLEncoder.encode( s, "utf-8" );
    } catch ( Exception ex ) {
      System.out.println( ex ); System.exit( 1 ); return null;
    }
  }
  
  
}

  


