package drummachinejamannow;

import java.util.List;

public class DrumPlayer {
    
    private DrumDashboard drumDashboard;
    private List<DrumPanel> drumPanels;
    private DrumLight drumLight;
    private volatile boolean alive;
    
    public DrumPlayer(DrumDashboard drumDashboard) {
        this.drumDashboard = drumDashboard;
        this.drumPanels = drumDashboard.getPanelList();
        this.drumLight = drumDashboard.getDrumLight();
    }
    
    public void start() {
        alive = true;
        Thread thread = new Thread(() -> {
            int i = 0;
            while (true) {
                drumLight.turnOnAt(i);
                int delay = calculateDelay(drumDashboard.getBpm());
                long beginTime = System.currentTimeMillis();
                for (DrumPanel drumPanel : drumPanels) {
                    drumPanel.playNoteAt(i);
                }
                long deltaTime = System.currentTimeMillis()-beginTime;
                long realDelay = Math.max(0, delay-deltaTime);
                i = (i+1)%16;
                sleep(realDelay);
                if (!alive) break;
            }
            drumLight.turnOffAll();
        });
        thread.start();
    }
    
    public void stop() {
        alive = false;
    }

    private static int calculateDelay(int bpm) {
        return (int)Math.round(60_000.0/(bpm*4));
    }
    
    private static void sleep(long delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException ex) {}
    }
    
    public boolean isAlive() { return alive; }
}
