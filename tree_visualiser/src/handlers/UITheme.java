package handlers;

import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import com.formdev.flatlaf.FlatDarkLaf;

public class UITheme {
    static LookAndFeel old = UIManager.getLookAndFeel();
    
	public static void darkTheme() {
        try {
			UIManager.setLookAndFeel(new FlatDarkLaf());
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
