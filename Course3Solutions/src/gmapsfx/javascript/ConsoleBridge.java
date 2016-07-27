package gmapsfx.javascript;
/*
 * Class used to redirect Javascript console to standard out.
 */
public class ConsoleBridge {
	 public void log(String text)
	    {
	        System.out.println(text);
	    }

}