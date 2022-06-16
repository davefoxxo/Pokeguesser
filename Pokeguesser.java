import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;

import java.io.File;
import java.io.*;
import java.awt.Font;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

public class Pokeguesser extends JFrame implements WindowListener {
    public static void main(String[] args) {
        Pokeguesser game = new Pokeguesser();
        game.titleScreen();
    }
    
    int points = 0;
    JPanel gamePanel = new JPanel();
    JPanel subPanel = new JPanel();
    JPanel buttonPanel = new JPanel();
    JLabel ptsText = new JLabel("Points: 0");
    Pokemon pk = new Pokemon(1);
    JLabel image = new JLabel();
    ImageIcon img = new ImageIcon();
    JLabel text = new JLabel();
    JTextField textField = new JTextField(20);
    JButton btn = new JButton("Pass");
    JButton submitButton = new JButton("Submit");
    JButton hintButton = new JButton("Hint");

    JRadioButton rbEasy = new JRadioButton("Easy");
    JRadioButton rbNormal = new JRadioButton("Normal");
    JRadioButton rbHard = new JRadioButton("Hard");

    ButtonGroup difficulties = new ButtonGroup();
    JPanel startButtonPanel = new JPanel();
    JPanel radioButtonPanel = new JPanel();

    JLabel timeLabel = new JLabel("Time");
    JLabel difLabel = new JLabel("Difficulty");

    JPanel titlePanel = new JPanel();
    JLabel mainTitle = new JLabel("Pokeguesser");
    JButton startButton = new JButton("Start");
    JPanel settingsPanel = new JPanel();
    JPanel subSettingsPanel = new JPanel();
    JLabel rules = new JLabel("<html>Welcome to Pokeguesser!<br>Guess the Pokemon's name and move onto the next one.<br>If you can't think of the name, either get a hint or pass!<br>Select your time interval & difficulty, and press START!<br>5 points awarded per Pokemon guessed.<br><br><strong>Easy Mode:</strong> Smaller selection of the first 151 Pokemon. Great for teachers! ;)<br><strong>Normal Mode:</strong> All 898 Pokemon available!<br><strong>Hard mode:</strong> All 898 Pokemon, AND 1 point is deducted per hint and per pass. Also, no hint blanks will be filled for partially correct guesses!<br>You can use <strong>ENTER</strong> to submit answers. If you're correct, press <strong>ENTER</strong> again to go to the next Pokemon! To pass, press <strong>ENTER</strong> with an empty textbox.</html>");

    String[] times = {"30s", "1 min", "3 min", "Zen mode (unlimited)"};
    JComboBox<String> cb = new JComboBox<>(times);
    boolean canGetPoints = true;
    boolean hasStarted = false;
    Timer timer = new Timer();
    int sec = 0;
    int difficulty = 0;// 0 easy, 1 normal, 2 hard

    JPanel highScorePanel = new JPanel();
    JButton highScoreButton = new JButton("High Scores");
    JLabel highScoreTitle = new JLabel("High Scores");

    JRadioButton rbHiEasy = new JRadioButton("Easy");
    JRadioButton rbHiNormal = new JRadioButton("Normal");
    JRadioButton rbHiHard = new JRadioButton("Hard");

    JPanel rbHiPanel = new JPanel();
    ButtonGroup hiGroup = new ButtonGroup();
    JPanel highScoreTitlePanel = new JPanel();

    JPanel highScoreTable = new JPanel();

    JPanel highScoreBackPanel = new JPanel();
    JButton highScoreBack = new JButton("Back");

    JLabel name, pts, time;

    final Font ARIAL24 = new Font("Arial", Font.PLAIN, 24);
    final Font ARIAL20 = new Font("Arial", Font.PLAIN, 20);
    final Font ARIAL18_BOLD = new Font("Arial", Font.BOLD, 18);
    final Font ARIAL18 = new Font("Arial", Font.PLAIN, 18);
    final Font ARIAL14_BOLD = new Font("Arial", Font.PLAIN, 14);

    final Color SPACE_GRAY = new Color(54, 52, 59);
    final Color DARK_SPACE_GRAY = new Color(29, 28, 32);
    final Color BRIGHT_RED = new Color(255,44,44);

    BufferedReader reader;
    BufferedWriter writer;
    File file;

    public void titleScreen() {
        titlePanel.removeAll();

        gamePanel.removeAll();
        setContentPane(titlePanel);

        titlePanel.setLayout(new GridLayout(3,1));
        titlePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(10,10,10,10, BRIGHT_RED), BorderFactory.createEmptyBorder(20,20,20,20)));
        mainTitle.setFont(new Font("Arial", Font.PLAIN, 92));
        mainTitle.setHorizontalAlignment(JLabel.CENTER);
        mainTitle.setForeground(Color.WHITE);

        titlePanel.setBackground(DARK_SPACE_GRAY);
        titlePanel.add(mainTitle);
        
        difficulties.add(rbEasy);
        difficulties.add(rbNormal);
        difficulties.add(rbHard);

        rbEasy.setHorizontalAlignment(JRadioButton.CENTER);
        rbEasy.setFont(ARIAL14_BOLD);

        rbNormal.setHorizontalAlignment(JRadioButton.CENTER);
        rbNormal.setFont(ARIAL14_BOLD);
        rbNormal.setSelected(true);

        rbHard.setHorizontalAlignment(JRadioButton.CENTER);
        rbHard.setFont(ARIAL14_BOLD);


        settingsPanel.setLayout(new GridLayout(1,2,0,0));
        settingsPanel.setBackground(new Color(54, 52, 59));
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(55,20,65,20));

        startButton.setFont(ARIAL24);
        startButton.addActionListener(new ButtonListener());

        startButtonPanel.add(startButton);
        startButtonPanel.setLayout(new GridLayout(2,1, 20, 20));
        startButtonPanel.setBorder(BorderFactory.createEmptyBorder(40, 15, 0, 15));
        startButtonPanel.setBackground(new Color(54, 52, 59));
        startButtonPanel.add(highScoreButton);

        highScoreButton.addActionListener(new ButtonListener());
        highScoreButton.setFont(ARIAL24);

        settingsPanel.add(startButtonPanel);

        timeLabel.setHorizontalAlignment(JLabel.CENTER);
        timeLabel.setBackground(new Color(54, 52, 59));
        timeLabel.setFont(ARIAL20);
        timeLabel.setForeground(Color.WHITE);

        subSettingsPanel.setLayout(new GridLayout(4,1));
        subSettingsPanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 25));
        subSettingsPanel.setBackground(new Color(54, 52, 59));
        subSettingsPanel.add(timeLabel);

        cb.setFont(ARIAL18_BOLD);
        subSettingsPanel.add(cb);

        difLabel.setHorizontalAlignment(JLabel.CENTER);
        difLabel.setForeground(Color.WHITE);
        difLabel.setFont(ARIAL20);
        subSettingsPanel.add(difLabel);
    
        radioButtonPanel.setLayout(new GridLayout(1,3));
        radioButtonPanel.add(rbEasy);
        radioButtonPanel.add(rbNormal);
        radioButtonPanel.add(rbHard);
        subSettingsPanel.add(radioButtonPanel);
        settingsPanel.add(subSettingsPanel);

        rules.setFont(ARIAL18);
        rules.setForeground(Color.WHITE);
        rules.setBackground(SPACE_GRAY);

        titlePanel.add(settingsPanel);
        titlePanel.add(rules);

        titlePanel.updateUI();
    }

    public void highScoreScreen(int _dif) {
        highScorePanel.removeAll();
        setContentPane(highScorePanel);
        highScoreTitle.setForeground(Color.WHITE);
        highScorePanel.setBackground(SPACE_GRAY);
        highScorePanel.setLayout(new BorderLayout());

        highScoreTitlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 0));
        highScoreTitlePanel.setBackground(BRIGHT_RED);
        highScoreTitlePanel.add(highScoreTitle);

        rbHiPanel.setLayout(new GridLayout(1,3));

        final JRadioButton[] TABLE_RADIO_BUTTONS = {rbHiEasy, rbHiNormal, rbHiHard};

        for(JRadioButton btn : TABLE_RADIO_BUTTONS) {
            hiGroup.add(btn);
            btn.setFont(ARIAL24);
            btn.setHorizontalAlignment(JRadioButton.CENTER);
            if(btn.getActionListeners().length == 0) btn.addActionListener(new ButtonListener());
            rbHiPanel.add(btn);
        }


        highScoreTitlePanel.add(rbHiPanel);
        highScorePanel.add(highScoreTitlePanel, BorderLayout.PAGE_START);

        highScoreTable.removeAll();
        highScoreTitle.setFont(new Font("Arial", Font.PLAIN, 40));
        highScoreTable.setLayout(new GridLayout(11,3));
        highScoreTable.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
        highScoreTable.setBackground(SPACE_GRAY);

        file = new File(String.format("HISCORES\\%d.txt", _dif));


        try {
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            String[] data;

            JLabel nameTop = new JLabel("NAME");
            JLabel pointsTop = new JLabel("SCORE");
            JLabel timeTop = new JLabel("TIME");

            final JLabel[] TABLE_HEADERS = {nameTop, pointsTop, timeTop};

            for(JLabel lbl : TABLE_HEADERS) {
                lbl.setHorizontalAlignment(JLabel.CENTER);
                lbl.setFont(ARIAL24);
                lbl.setForeground(Color.WHITE);
                highScoreTable.add(lbl);
            }

            int x = 0;
            while(x < 10) {
                name = new JLabel("-");
                pts = new JLabel("-");
                time = new JLabel("-");

                final JLabel[] ROW = {name, pts, time};

                if(line != null) {
                    data = line.split(";");

                    name.setText(data[0]);
                    pts.setText(data[1]);
                    time.setText(data[2]);
                }

                name.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(3, 6, 3, 0, DARK_SPACE_GRAY), BorderFactory.createEmptyBorder(10,10,10,10)));
                pts.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(3, 6, 3, 6, DARK_SPACE_GRAY), BorderFactory.createEmptyBorder(10,10,10,10)));
                time.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(3, 0, 3, 6, DARK_SPACE_GRAY), BorderFactory.createEmptyBorder(10,10,10,10)));

                for(JLabel row : ROW) {
                    row.setFont(ARIAL24);
                    row.setBackground(Color.LIGHT_GRAY);
                    row.setOpaque(true);
                    row.setForeground(Color.BLACK);
                    highScoreTable.add(row);
                }

                line = reader.readLine();
                x++;
            }
            reader.close();

        } catch(IOException e) {
            System.err.print(e);
        }

        highScorePanel.add(highScoreTable, BorderLayout.CENTER);
        
        highScoreBackPanel.add(highScoreBack);
        highScoreBackPanel.setBackground(BRIGHT_RED);

        highScoreBack.setPreferredSize(new Dimension(150, 50));
        highScoreBack.setFont(ARIAL18_BOLD);
        highScoreBack.addActionListener(new ButtonListener());

        highScorePanel.add(highScoreBackPanel, BorderLayout.PAGE_END);
        highScorePanel.updateUI();
    }


    public void gameScreen() {
        sec = 0;
        gamePanel.removeAll();
        setContentPane(gamePanel);
        
        GridBagLayout gb = new GridBagLayout();
        gamePanel.setLayout(gb);    
        GridBagConstraints con = new GridBagConstraints();

        if(!cb.getSelectedItem().toString().equals("Zen mode (unlimited)")) {
            timer = new Timer();
            String selection = cb.getSelectedItem().toString();

            if(selection.equals("30s")) sec = 32;
            else if(selection.equals("1 min")) sec = 62;
            else if(selection.equals("3 min")) sec = 182;

            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    ptsText.setText("Points: " + points + " | Time Left: " + sec);
                    sec--;

                    if(sec < 10 && sec % 2 == 1) {
                        ptsText.setForeground(BRIGHT_RED);
                    } else if(sec < 10 && sec % 2 == 0) {
                        ptsText.setForeground(Color.WHITE);
                    }   

                    if(sec == 9) playAudio(new File("tick.wav"));

                    if (sec < 0) {
                        timer.cancel();
                        sec = -1;
                        
                        playAudio(new File("over.wav"));

                        String name = JOptionPane.showInputDialog(gamePanel, "Points: " + points + "\nPlease enter your name:", "Time's up!", JOptionPane.INFORMATION_MESSAGE);
                        
                        System.out.println(name);

                        file = new File(String.format("HISCORES\\%d.txt", difficulty));
                        if(name != null) {
                            try {
                                writer = new BufferedWriter(new FileWriter(file, true));

                                writer.write(name);
                                writer.write(';');
                                writer.write(String.valueOf(points));
                                writer.write(';');
                                writer.write(selection);
                                writer.write(';');
                                writer.newLine();

                                writer.close();

                            } catch(IOException e) {
                                System.err.println(e);
                            }
                        }

                        int yn = JOptionPane.showConfirmDialog(gamePanel,"Play again?", "Time's up!", JOptionPane.INFORMATION_MESSAGE);


                        if(yn == 0 || yn == 2) {
                            canGetPoints = true;
                            points = 0;
                            hasStarted = false;
                            titleScreen();

                        } else if(yn == 1) {
                            System.exit(0);
                        }
                    }
                }
            }, 0, 1000);
        }

        gamePanel.setBackground(SPACE_GRAY);
        gamePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        ptsText.setText("Points: " + points + " | Time Left: " + sec);
        ptsText.setForeground(Color.WHITE);

        subPanel.setLayout(new GridLayout(3, 1, 50, 20));

        // Colour of border changes depending on what difficulty you're playing on
        MatteBorder toSet = BorderFactory.createMatteBorder(10, 10, 10, 10, Color.BLACK);
        if(difficulty == 0) {
            toSet = BorderFactory.createMatteBorder(10, 10, 10, 10, Color.GREEN);
        } else if (difficulty == 1) {
            toSet = BorderFactory.createMatteBorder(10, 10, 10, 10, Color.WHITE);
        } else if(difficulty == 2) {
            toSet = BorderFactory.createMatteBorder(10, 10, 10, 10, BRIGHT_RED);
            
        }
        subPanel.setBorder(BorderFactory.createCompoundBorder(toSet, BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        buttonPanel.setLayout(new GridLayout(1,3,10,0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        buttonPanel.setBackground(DARK_SPACE_GRAY);

        pk = new Pokemon(difficulty);
        pk.getImage();

        Image mon = null;
        try {
            mon = ImageIO.read(new File("mon.png"));
        } catch(IOException e) {
            e.printStackTrace();
        }
        img = new ImageIcon(new ImageIcon(mon).getImage().getScaledInstance(480, 480, java.awt.Image.SCALE_SMOOTH));

        image.setIcon(img);
        image.setHorizontalAlignment(JLabel.CENTER);
        image.setSize(256,256);

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < pk.name.length(); i++) {
            sb.append("_ ");
        }
        text.setText(sb.toString());

        text.setFont(new Font("Arial", Font.PLAIN, 50));
        text.setForeground(new Color(255,255,255));
        text.setHorizontalAlignment(JLabel.CENTER);

        con.fill = GridBagConstraints.BOTH;
        con.gridwidth = GridBagConstraints.REMAINDER;

        con.gridheight = 1;
        ptsText.setFont(new Font("Arial", Font.PLAIN, 40));
        ptsText.setHorizontalAlignment(JLabel.CENTER);
        gb.setConstraints(ptsText, con);
        gamePanel.add(ptsText);

        con.gridheight = 2;
        con.weighty = 1.0;
        gb.setConstraints(image, con);
    
        gamePanel.add(image);
        subPanel.add(text);
        subPanel.setBackground(new Color(54, 52, 59));

        
        btn.setPreferredSize(new Dimension(200,32));
        btn.addActionListener(new ButtonListener());
        btn.setToolTipText("Generate a new Pokemon!");
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFont(ARIAL20);

        
        submitButton.setPreferredSize(new Dimension(200,32));
        submitButton.addActionListener(new ButtonListener());
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.setFont(ARIAL20);

        textField.setText(null);
        
        textField.setPreferredSize(new Dimension(32,16));
        textField.setFont(new Font("Arial", Font.PLAIN, 32));
        textField.setHorizontalAlignment(JTextField.CENTER);
        if(textField.getActionListeners().length == 0) textField.addActionListener(new ButtonListener());

        hintButton.setPreferredSize(new Dimension(200, 32));
        hintButton.addActionListener(new ButtonListener());
        hintButton.setFont(ARIAL20);
        hintButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.setBackground(new Color(54, 52, 59));
        buttonPanel.add(hintButton);
        buttonPanel.add(submitButton);
        buttonPanel.add(btn);

        subPanel.add(textField);
        subPanel.add(buttonPanel);

        gamePanel.add(subPanel);
        gamePanel.updateUI();
    }

    public Pokeguesser() {
        setContentPane(titlePanel);
        setTitle("Pokeguesser");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(720,980);
        setResizable(false);
        setLocation(64,32);
        addWindowListener(this);
        setVisible(true);
    }

    

    public class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent event) {
            if(event.getSource() == btn || 
              (event.getSource() == textField && (!canGetPoints || textField.getText().length() == 0))) {
                if(points > 0 && canGetPoints && difficulty == 2) points -= 1;
                ptsText.setText("Points: " + points + " | Time Left: " + sec);
                canGetPoints = true;
                btn.setText("Pass");

                // New random Pokemon
                pk = new Pokemon(difficulty);
                pk.getImage();

                // Parse image
                Image mon = null;
                try {
                    mon = ImageIO.read(new File("mon.png"));
                } catch(IOException e) {
                    e.printStackTrace();
                }

                // Update text and image
                img = new ImageIcon(mon);
                image.setIcon(img);
                
                StringBuilder sb = new StringBuilder();
                for(int i = 0; i < pk.name.length(); i++) {
                    sb.append("_ ");
                }

                text.setText(sb.toString());
                textField.setText("");

            }

            else if((event.getSource() == submitButton && textField.getText() != "") || (event.getSource() == textField && canGetPoints)) {
                if(textField.getText().toLowerCase().equals(pk.name.toLowerCase())) {
                    text.setText("You got it! " + pk.name);
                    if(canGetPoints) {
                        points += 5;
                        ptsText.setText("Points: " + points + " | Time Left: " + sec);
                        canGetPoints = false;
                        btn.setText("NEXT");
                    }

                    playAudio(new File("right.wav"));

                } else {
                    
                    playAudio(new File("wrong.wav"));

                    if(difficulty != 2) {
                        StringBuilder sb = new StringBuilder();
                        for(int i = 0; i < pk.name.length(); i++) {
                            try {
                                if(textField.getText().toLowerCase().charAt(i) == pk.name.toLowerCase().charAt(i)) {
                                    sb.append(pk.name.charAt(i) + " ");
                                } else {
                                    sb.append("_ ");
                                }
                            }  catch(IndexOutOfBoundsException e) {
                                sb.append("_ ");

                            }
                        }

                        text.setText(sb.toString());
                    } else {
                        StringBuilder sb = new StringBuilder();
                        for(int i = 0; i < pk.name.length(); i++) {
                            sb.append("_ ");
                        }
                        text.setText(sb.toString());
                    }
                } 
            }
            

            else if(event.getSource() == hintButton) {
                if(points > 0 && difficulty == 2) points -= 1;
                ptsText.setText("Points: " + points + " | Time Left: " + sec);

                StringBuilder sb = new StringBuilder();
                sb.append(pk.name.toCharArray()[0]);
                sb.append(" ");

                for(int i = 0; i < pk.name.length()-2; i++) {
                    sb.append("_ ");
                }

                sb.append(pk.name.toCharArray()[pk.name.length()-1]);

                text.setText(sb.toString());
            }

            else if(event.getSource() == startButton) {

                if(rbHard.isSelected()) difficulty = 2;
                else if(rbNormal.isSelected()) difficulty = 1;
                else difficulty = 0;

                if(!hasStarted) gameScreen();
                hasStarted = true;   
            }

            else if(event.getSource() == highScoreButton) {
                highScoreScreen(1);
                rbHiNormal.setSelected(true);

            }

            else if(event.getSource() == rbHiEasy) {
                highScorePanel.removeAll();
                highScoreScreen(0);
            }   

            else if(event.getSource() == rbHiNormal) {
                highScorePanel.removeAll();
                highScoreScreen(1);
            }

            else if(event.getSource() == rbHiHard) {
                highScorePanel.removeAll();
                highScoreScreen(2);
            }

            else if(event.getSource() == highScoreBack) {
                titleScreen();
            }  
        }
    }

    public void windowClosing(WindowEvent e) {
        // First message
        if(getContentPane() == gamePanel) {
            int c = JOptionPane.showConfirmDialog(getContentPane(), "Are you sure you want to exit? \n(Time's still ticking!)", "Quit?", JOptionPane.INFORMATION_MESSAGE);
            
            if(c == 0) System.exit(0);

        } else {
            System.exit(0);
        }
    }

    public void windowOpened(WindowEvent e) {} 
    public void windowActivated(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}

    public void playAudio(File file) {
        try {
            javax.sound.sampled.AudioInputStream stream;
            AudioFormat format;
            DataLine.Info info;
            Clip clip;
        
            stream = AudioSystem.getAudioInputStream(file);
            format = stream.getFormat();
            info = new DataLine.Info(Clip.class, format);
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(stream);
            clip.start();
        }
        catch (Exception e) {}

    }
}
