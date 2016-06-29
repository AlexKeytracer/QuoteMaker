package quotemaker;

/**
 *
 * @author alex.c
 */
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
