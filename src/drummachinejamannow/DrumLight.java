package drummachinejamannow;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

public class DrumLight extends JPanel {
    
    private JLabel[] labels = new JLabel[16];
    
    public DrumLight(int xPos, int yPos) {
        setLayout(null);
        setBounds(xPos, yPos, 808, 100);
        
        //create 16 labels
        for (int i = 0; i < 16; i++) {
            JLabel newLabel = new JLabel();
            newLabel.setBackground(Color.LIGHT_GRAY);
            newLabel.setOpaque(true);
            newLabel.setSize(50, 25);
            newLabel.setLocation(i*50, 12);
            Border border = BorderFactory.createLineBorder(new Color(0xeeeeee), 9);
	    newLabel.setBorder(border);
            add(newLabel);
            labels[i] = newLabel;
        }
    }
    
    public void turnOnAt(int idx) {
        SwingUtilities.invokeLater(() -> {
            labels[(idx-1+16)%16].setBackground(Color.LIGHT_GRAY);
            labels[idx].setBackground(Color.MAGENTA);            
        });
    }
    
    public void turnOffAll() {
        SwingUtilities.invokeLater(() -> {
            for (JLabel label : labels)
                label.setBackground(Color.LIGHT_GRAY);
        });
    }
}
