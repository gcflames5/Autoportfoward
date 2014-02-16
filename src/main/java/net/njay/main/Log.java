package net.njay.main;

public class Log {

	private static boolean debug = false;

	public static void debug(String msg){
		if (debug) System.out.println("[DEBUG] " + msg);
	}


}
