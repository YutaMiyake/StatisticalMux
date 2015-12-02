// Program: Driver.java
// Purpose of this project is to demonstrate the concept and visualize
// statistical multiplexer in routing calculation for CPE400 class
// Author: Yuta Miyake, Dat Luu

import java.io.IOException;

public class Driver{
  public static void main(String[] args){

    try
        {
            new WindowApplication("Statistical Multiplexer");
        }
        catch (IOException exc)
        {
            System.out.println (exc.toString());
            System.out.println("Unexpected error!");
        }

  }
}