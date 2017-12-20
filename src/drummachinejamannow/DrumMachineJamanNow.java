package drummachinejamannow;

import javax.swing.SwingUtilities;

public class DrumMachineJamanNow {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DrumDashboard app = new DrumDashboard();
            app.setVisible(true);
        });
    }
    
}
