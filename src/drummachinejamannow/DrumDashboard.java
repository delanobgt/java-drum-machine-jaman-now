package drummachinejamannow;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class DrumDashboard extends JFrame {
    
    private int curYAxisPanel = 50;
    private int frameHeight = 130;
    private List<DrumPanel> panelList = new ArrayList<>();
    private JSpinner jSpinner;
    private JButton playBtn;
    private Button addBtn;
    private DrumLight drumLight;
    private DrumPlayer drumPlayer;
    private int bpm = 128;
    
    public DrumDashboard() {
        initUI();
        
        addPanel("./Kick Basic.wav");
        addPanel("./Hat Basic.wav");
        addPanel("./Snare Basic.wav");
        addPanel("./Clap Basic.wav");
        drumPlayer = new DrumPlayer(this);
    }
    
    private void initUI() {
        //window properties
        setTitle("Drum Machine Jaman Now");
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(908, 130);
        setLocationRelativeTo(null);
        
        //create titleLabel
        JLabel titleLabel = new JLabel("DRUM MACHINE JAMAN NOW");
        titleLabel.setLocation(20, 0);
        titleLabel.setSize(500, 50);
        titleLabel.setForeground(Color.BLUE);
        titleLabel.setBackground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        add(titleLabel);
        
        //create bpmLabel
        JLabel bpmLabel = new JLabel("BPM");
        bpmLabel.setLocation(675, 0);
        bpmLabel.setSize(50, 50);
        bpmLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(bpmLabel);
        
        //create jSpinner
        SpinnerModel spinnerModel = new SpinnerNumberModel(bpm, //initial value  
                                                        50, //minimum value  
                                                        200, //maximum value  
                                                        1); //step  
        jSpinner = new JSpinner(spinnerModel);   
        jSpinner.setBounds(725, 5, 50, 40);    
        jSpinner.setFont(new Font("Arial", Font.BOLD, 18));
        ((DefaultEditor)jSpinner.getEditor()).getTextField().setEditable(false);
        add(jSpinner);
        jSpinner.addChangeListener(new ChangeListener() {  
            public void stateChanged(ChangeEvent e) {  
                bpm = (Integer)((JSpinner)e.getSource()).getValue();
            }  
        });  
        
        //create playBtn
        playBtn = new JButton("Play");
        playBtn.setBounds(800, 0, 100, 50);
        playBtn.setFont(new Font("Arial", Font.BOLD, 24));
        playBtn.setFocusPainted(false);
        playBtn.setForeground(Color.WHITE);
        playBtn.setBackground(Color.GREEN);
        add(playBtn);
        playBtn.addActionListener((e) -> {
            if (drumPlayer.isAlive()) { //drumPlayer is running
                playBtn.setText("Play");
                playBtn.setBackground(Color.GREEN);
                drumPlayer.stop();
            } else {        //drumPlayer is idle
                playBtn.setText("Stop");
                playBtn.setBackground(Color.RED);
                drumPlayer.start();
            }
            
        });
        
        //create addBtn
        addBtn = new Button("+");
        addBtn.setFont(new Font("Arial", Font.BOLD, 32));
        addBtn.setBounds(0, curYAxisPanel, 100, 50);
        addBtn.setForeground(Color.GRAY);
        addBtn.setBackground(Color.LIGHT_GRAY);
        add(addBtn);
        addBtn.addActionListener((e) -> {
            String filepath = askAudioClip();
            if (!filepath.isEmpty() && new File(filepath).exists()) 
                addPanel(filepath);
        });
        
        //create drumLight
        drumLight = new DrumLight(100, curYAxisPanel);
        add(drumLight);
    }
    
    private String askAudioClip() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Audio Clip (wav)", "wav", "song");
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            return selectedFile.getAbsolutePath();
        }
        return "";
    }
    
    private void addPanel(String filepath) {
        //create newDrumPanel
        DrumPanel newDrumPanel = new DrumPanel(this, panelList.size(), curYAxisPanel, filepath);
        add(newDrumPanel);
        panelList.add(newDrumPanel);

        //change addBtnPosition, windowSize, drumLightPosition
        addBtn.setBounds(0, curYAxisPanel+=50, 100, 50); //change addBtn pos
        drumLight.setLocation(100, curYAxisPanel);
        if (panelList.size() == 8) //disable addBtn once there are 8 panels
            addBtn.setEnabled(false);
        setSize(getSize().width, frameHeight+=50);  //change window size
    }
    
    public void removePanel(JPanel remPanel) {
        //enable addBtn if disabled
        if (!addBtn.isEnabled()) addBtn.setEnabled(true);
        
        //find the panel to remove
        int remIndex = -1;
        for (int i = 0; i < panelList.size(); i++) {
            if (panelList.get(i).equals(remPanel)) {
                remIndex = i;
                break;
            }
        }
        //remove panel
        remove(remPanel);
        panelList.remove(remIndex);
        
        //reposition all panel
        curYAxisPanel = 50;
        for (int i = 0; i < panelList.size(); i++) {
            DrumPanel curPanel = panelList.get(i);
            curPanel.reposition(i, curYAxisPanel);
            curYAxisPanel += 50;
        }
        
        //reposition addBtn and resize the window
        addBtn.setLocation(0, curYAxisPanel);
        drumLight.setLocation(100, curYAxisPanel);
        setSize(getSize().width, frameHeight=curYAxisPanel+80);
    }
    
    public List<DrumPanel> getPanelList() { return panelList; }
    public DrumLight getDrumLight() { return drumLight; }
    public int getBpm() { return bpm; }
}
