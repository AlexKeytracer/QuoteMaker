package quotemaker;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Alex
 */
public class QuoteManager {
    
    public final MainFrame frame;
    public String proxSafeCabinet = "";
    
    public Map<String, Integer> proxSafePanels = new HashMap<>();
    public Map<String, Integer> proxSafeTerminals = new HashMap<>();
    public Map<String, Integer> proxSafeParts = new HashMap<>();
    public Map<String, Integer> proxSafeModules = new HashMap<>();
    public Map<String, Integer> proxSafeAdditionalLicenses = new HashMap<>();
    public Map<String, Integer> proxSafeInterfaces = new HashMap<>();
    public Map<String, Integer> proxSafeLockers = new HashMap<>();
    boolean b = false;
    
    public QuoteManager (final MainFrame frame) {
        
        this.frame = frame;
        
    }
    
    public void finishProxSafe() {
        
        //CHECKS ?
        
        DefaultTableModel model = (DefaultTableModel) frame.finalQuoteTable.getModel();
        final NumberFormat formatter = NumberFormat.getCurrencyInstance();
        
        //Remove current final quote row
        if(frame.finalQuoteTable.getRowCount() > 0) {
            model.removeRow(frame.finalQuoteTable.getRowCount() - 1);
        }
        
        model.addRow(new Object[]{null, null, "<html><b>" + proxSafeCabinet + "</b></html>", null, formatter.format(Info.lookupPrice(proxSafeCabinet))});
        
        addItemsFromMap(proxSafePanels);
        addItemsFromMap(proxSafeTerminals);
        addItemsFromMap(proxSafeParts);
        addItemsFromMap(proxSafeModules);
        addItemsFromMap(proxSafeAdditionalLicenses);
        addItemsFromMap(proxSafeInterfaces);
        addItemsFromMap(proxSafeLockers);
        
        
        model.addRow(new Object[]{null, null, null, null, null});
        
        //Add the new final quote row
        addTotalRow();
        
    }
    
    private void addItemsFromMap(Map<String, Integer> m) {
        
        final NumberFormat formatter = NumberFormat.getCurrencyInstance();
        DefaultTableModel model = (DefaultTableModel) frame.finalQuoteTable.getModel();
        
        for(Entry<String, Integer> entry : m.entrySet()) {
            
            String id = Info.lookupId(entry.getKey());
            Integer price = Info.lookupPrice(entry.getKey());
            
            if(id == null){
                System.err.println("Error finding product ID for item: " + entry.getKey());
                continue;
            }
            
            if(price == null){
                System.err.println("Error finding price for item: " + entry.getKey());
                continue;
            }
            
            String unitPrice = formatter.format(price);
            String extPrice = formatter.format(price * entry.getValue());
            
            model.addRow(new Object[]{id, String.valueOf(entry.getValue()), entry.getKey(), unitPrice, extPrice});
            
        }
        
    }
    
    private void addTotalRow() {
        
        Integer total = 0;
        final NumberFormat formatter = NumberFormat.getCurrencyInstance();
        DefaultTableModel model = (DefaultTableModel) frame.finalQuoteTable.getModel();
        
        for(int row = 0; row < frame.finalQuoteTable.getRowCount(); row++) {
            
            try{
                
                if(frame.finalQuoteTable.getValueAt(row, 4) != null)
                    total = total + formatter.parse(String.valueOf(frame.finalQuoteTable.getValueAt(row, 4))).intValue();
                
            } catch (NumberFormatException nfe) {
                //Do nothing
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
            
        }
        
        model.addRow(new Object[]{null, null, null, "<html><b>TOTAL QUOTE</b></html>", "<html><b>" + formatter.format(total) + "</b></html>"});
        
    }
    
}
