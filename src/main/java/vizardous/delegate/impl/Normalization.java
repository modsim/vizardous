/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vizardous.delegate.impl;

import java.util.Locale;


/**
 * TODO
 *
 * @author Charaf E. Azzouzi <c.azzouzi@fz-juelich.de>
 * @version 1.0
 * 
 */
 
public class Normalization {
    
    public static double normalize(double input, double min, double max)
    {
        return (input-min)/(max-min);
    }
    public static double scale(double input, double min, double max)
    {
        return min + input * (max-min);
    }
    public static void main(String[] args)
    {
        double minIn = 0.0;
        double maxIn = 21.0;
        double minOut = 0.0;
        double maxOut = 1.0;
       
        for (double input=minIn; input<=maxIn; input+=0.1)
        {
            double alpha = normalize(input, minIn, maxIn);
            double output = scale(alpha, minOut, maxOut);
            System.out.println(
                "Input: "+String.format(Locale.ENGLISH, "%5.3f", input)+
                " normalized: "+String.format(Locale.ENGLISH, "%5.3f", alpha)+
                " output: "+String.format(Locale.ENGLISH, "%5.3f", output));
        }
    }    
}
