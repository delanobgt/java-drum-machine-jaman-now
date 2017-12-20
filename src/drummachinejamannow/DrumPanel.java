package drummachinejamannow;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import javax.swing.JPanel;

import javax.swing.JOptionPane;

public class DrumPanel extends JPanel {    
    private static Color[] rainbowColors = {
        new Color(0x78c5d6),
        new Color(0x459ba8),
        new Color(0x79c267),
        new Color(0xc5d647),
        new Color(0xf5d63d),
        new Color(0xf28c33),
        new Color(0xe868a2)
    };
    private int id;
    private DrumDashboard parentFrame;
    private int yAxis;
    private Button bigBtn;
    private Button[] btnArray = new Button[16];
    private boolean[] keyActive = new boolean[16];
    private String songFilepath;   
    private String fullSongName;
    private String songName;
    
    public DrumPanel(DrumDashboard parentFrame, int id, int yAxis, String filepath) {
        this.parentFrame = parentFrame;
        this.id = id;
        this.yAxis = yAxis;
        songFilepath = filepath;
        
        //create display name of the song
        File songFile = new File(songFilepath);
        fullSongName = songFile.getName();
        fullSongName = fullSongName.substring(0, fullSongName.length()-4);
        if (fullSongName.length() > 12)
            songName = fullSongName.substring(0, 12)+"..";
        else
            songName = fullSongName;
        
        initPanel();
    }
    
    private void initPanel() {
        setLayout(null);
        setBounds(0, yAxis, 900, 50);
        
        //create bigBtn
        bigBtn = new Button(songName);
        bigBtn.setBounds(0, 0, 100, 50);
        bigBtn.setFont(new Font("Arial", Font.BOLD, 12));
        bigBtn.setForeground(Color.WHITE);
        bigBtn.setBackground(rainbowColors[id]);
        add(bigBtn);
        bigBtn.addActionListener((e) -> {
            int resp = JOptionPane.showConfirmDialog(null, 
                                                    "Delete "+fullSongName+"?", 
                                                    "Delete Panel",
                                                    JOptionPane.YES_NO_OPTION);
            if (resp == JOptionPane.YES_OPTION) {
                parentFrame.removePanel(this);
            }
        });
        
        //create smallBtn
        for (int i = 0; i < 16; i++) {
            Button newBtn = new Button();
            if (i/4 == 0 || i/4 == 2)
                newBtn.setBackground(Color.LIGHT_GRAY);
            else
                newBtn.setBackground(new Color(0xadadad));
            newBtn.setBounds((i*50)+100, 0, 50, 50);
            add(newBtn);
            btnArray[i] = newBtn;
            
            final int btnIdx = i;
            newBtn.addActionListener((e) -> {
                if (keyActive[btnIdx]) { //key is on
                    if (btnIdx/4 == 0 || btnIdx/4 == 2)
                        btnArray[btnIdx].setBackground(Color.LIGHT_GRAY);
                    else
                        btnArray[btnIdx].setBackground(new Color(0xadadad));
                } else {    //key is off
                    btnArray[btnIdx].setBackground(Color.YELLOW);
                }
                keyActive[btnIdx] = !keyActive[btnIdx];
            });
        }
    }

    public void playNoteAt(int idx) {
        if (keyActive[idx]) {
            AudioSamplePlayer.play(songFilepath);
        }
    }
    
    public void reposition(int newId, int y) {
        id = newId;
        bigBtn.setBackground(rainbowColors[newId]);
        yAxis = y;
        setLocation(0, y);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DrumPanel)) return false;
        DrumPanel drumPanel = (DrumPanel)obj;
        return this.getyAxis() == drumPanel.getyAxis();
    }
    public int getyAxis() { return yAxis; }
}
