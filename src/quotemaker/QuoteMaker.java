package quotemaker;

/**
 *
 * @author alex.c
 */
import java.awt.Color;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class QuoteMaker {

    public static void main(String[] args) {
        Info.initialize();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                try {
                    // select Look and Feel
                    UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel");
                    UIManager.put("TabbedPane.focus", new Color(0, 0, 0, 0));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                MainFrame frame = new MainFrame();
                QuoteManager manager = new QuoteManager(frame);
                frame.setManager(manager);

                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}
