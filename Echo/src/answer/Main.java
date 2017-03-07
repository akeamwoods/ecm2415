package answer;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ClientLopez
 */
public class Main {
    
    public static void main(String[] args){
    
    String question = "What is the answer to life, the universe, and everything?";

    /*
    String result = Wolfram.solve(question);
    
    int plaintextIndex = result.indexOf("plaintext", result.indexOf("plaintext") + 1);
    
    String x = result.substring(plaintextIndex+14, (result.length()-1));
    
    int quotationmarkIndex = result.indexOf("\"", (plaintextIndex+14));
    
    System.out.println(plaintextIndex);
    System.out.println(quotationmarkIndex);
    
    String finalanswer = result.substring((plaintextIndex+14), quotationmarkIndex);
    finalanswer = finalanswer.replace("\\n", " ");
    finalanswer = finalanswer.replace("\\", "");
    
    //int finalquotationmarkIndex = result.indexOf()
    
*/
    
    System.out.println(Wolfram.solve(question));
    
    
        
        
//    System.out.print(Wolfram.solve("What is the distance between London and New York?").indexOf("plaintext"));
//    System.out.println(Wolfram.solve(input);
    }
    
}
