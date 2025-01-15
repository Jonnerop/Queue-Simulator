package simu.framework;

/**
 * The Trace class is a simple logging utility that outputs messages based on a specified trace level.
 * It allows filtering of messages based on the importance of the message's level.
 */
public class Trace {
	public enum Level{INFO, WAR, ERR}
	
	private static Level traceLevel;
	
	public static void setTraceLevel(Level lvl){
		traceLevel = lvl;
	}
	public static void out(Level lvl, String txt){
		if (lvl.ordinal() >= traceLevel.ordinal()){
			System.out.println(txt);
		}
	}
}