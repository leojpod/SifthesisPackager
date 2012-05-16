/*
 * Master-Thesis work: see https://sites.google.com/site/sifthesis/
 */
package sifthesis.runner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author leo
 */
public class SifthesisRunner {
	
	public static void main(String[] args) {
		Process tApiProc, siftDriverProc = null, spheroDriverProc = null;
		
		//For tAPI
		try {
			//first start the TangibleAPI 
			tApiProc = Runtime.getRuntime().exec(
					new String[]{"java", "-jar", "./TangibleAPI/TangibleAPI.jar",
						"./TangibleAPI/resources"});
			System.out.println("Starting TangibleAPI");
			//now let's listen to the stdout until we read the good line
			BufferedReader reader = 
					new BufferedReader(new InputStreamReader(tApiProc.getInputStream()));
			String line;
			BufferedReader errReader = 
					new BufferedReader(new InputStreamReader(tApiProc.getErrorStream()));
			
			while( (line = reader.readLine()) != null){
				if(line.equals("TANGIBLE_API_READY")){
					System.out.println("TangibleAPI is ready, let's start the drivers now");
					break;
				}else if(line.equals("TANGIBLE_API_FAIL")){
					System.out.println("Couldn't start the TangibleAPI ...");
					tApiProc.destroy();
					return;
				}
			}
		} catch (IOException e) {
			System.out.println("Couldn't start the TangibleAPI ...");
			return;
		}
		//For SiftDriver
		try {
			//let's init the API now
			siftDriverProc = Runtime.getRuntime().exec(new String[]{"mono", "SiftDriver/SiftDriver.exe"});
		} catch (IOException ex) {
			System.out.println("Couldn't start the SiftDriver");
		}
		try {
			spheroDriverProc = Runtime.getRuntime().exec(
					new String[]{"java", "-jar", "SpheroDriver/SpheroTangibleDriver.jar"});
		} catch (IOException ex) {
			System.out.println("Couldn't start the SpheroDriver");
		}
		
		System.out.println("Everything is ready, "
				+ "press enter to turn the whole thing off");
		BufferedReader sysIn = new BufferedReader( new InputStreamReader(System.in));
		try {
			sysIn.readLine();
		} catch (IOException ex) {
			//Wooops
		}
		tApiProc.destroy();
		if(siftDriverProc != null) {
			siftDriverProc.destroy();
		}
		if(spheroDriverProc != null) {
			spheroDriverProc.destroy();
		}
		System.out.println("Everything is turned off, press enter to quit");
		try{
			sysIn.readLine();
		} catch (IOException ex) {
			// woops again
		}
		System.out.println("Bye, bye");
	}
}
