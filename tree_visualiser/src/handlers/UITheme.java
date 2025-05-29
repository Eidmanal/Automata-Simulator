package handlers;

import javax.swing.LookAndFeel;
import javax.swing.UIManager;

public class UITheme {
    static LookAndFeel old = UIManager.getLookAndFeel();
    
	public static void windows() {
        try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void crossPlatform() {
        try {
			UIManager.setLookAndFeel(old);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
