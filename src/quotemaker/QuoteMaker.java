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
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                
                MainFrame frame = new MainFrame();
                QuoteManager manager = new QuoteManager(frame);
                frame.setManager(manager);
                
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}
