import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;

import java.io.*;
import java.awt.Font;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class Pokeguesser extends JFrame implements WindowListener {
    public static void main(String[] args) {
        Pokeguesser game = new Pokeguesser();
        game.titleScreen();
    }
    
    JPanel gamePanel = new JPanel();
    JPanel subPanel = new JPanel();
    JPanel buttonPanel = new JPanel();
    JLabel ptsText = new JLabel("Points: 0");

    Pokemon pk = new Pokemon(1);
    JLabel image = new JLabel();
    ImageIcon img = new ImageIcon();

    JLabel text = new JLabel();
    JTextField textField = new JTextField(20);
    JButton nextButton = new JButton("Pass");
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

    String[] times = {"30s", "1 min", "3 min", "5 min", "Zen mode (unlimited)"};
    JComboBox<String> cb = new JComboBox<>(times);
    JLabel rules = new JLabel("<html>Welcome to Pokeguesser!<br>Guess the Pokemon's name and move onto the next one.<br>If you can't think of the name, either get a hint or pass!<br>Select your time interval & difficulty, and press START!<br>5 points awarded per Pokemon guessed.<br><br><strong>Easy Mode:</strong> Smaller selection of the first 151 Pokemon. Great for teachers! ;)<br><strong>Normal Mode:</strong> All 898 Pokemon available!<br><strong>Hard mode:</strong> All 898 Pokemon, AND 1 point is deducted per hint and per pass. Also, no hint blanks will be filled for partially correct guesses!<br>You can use <strong>ENTER</strong> to submit answers. If you're correct, press <strong>ENTER</strong> again to go to the next Pokemon! To pass, press <strong>ENTER</strong> with an empty textbox.</html>");

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

    int points = 0;
    int sec = 0;
    int difficulty = 0; // 0 easy, 1 normal, 2 hard

    BufferedReader reader;
    BufferedWriter writer;
    File file;

    boolean canGetPoints = true;
    boolean hasStarted = false;
    Timer timer = new Timer();
    StringBuilder sb = new StringBuilder();

    final Font ARIAL24 = new Font("Arial", Font.PLAIN, 24);
    final Font ARIAL20 = new Font("Arial", Font.PLAIN, 20);
    final Font ARIAL18_BOLD = new Font("Arial", Font.BOLD, 18);
    final Font ARIAL18 = new Font("Arial", Font.PLAIN, 18);
    final Font ARIAL14_BOLD = new Font("Arial", Font.PLAIN, 14);

    final Color SPACE_GRAY = new Color(54, 52, 59);
    final Color DARK_SPACE_GRAY = new Color(29, 28, 32);
    final Color BRIGHT_RED = new Color(255,44,44);


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
        cb.setSelectedIndex(1);
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

        ArrayList<ArrayList<String>> allPlayers = new ArrayList<>();

        try {
            // High score table with display the 10 MOST RECENT names, scores, and times for each of the 3 difficulties.
            reader = new BufferedReader(new FileReader(file));

            String line = reader.readLine();
            String[] data;

            // Read all the data, and store it in an ArrayList which holds ArrayLists that hold names, scores, times.
            while(line != null) {
                ArrayList<String> temp = new ArrayList<>();
                data = line.split(";");

                temp.add(data[0]);
                temp.add(data[1]);
                temp.add(data[2]);

                allPlayers.add(temp);
                line = reader.readLine();
            }

            // Reverse the list so that calling indexes 0 to 9 means the 10 most recent scores
            // Since new lines in a file are written at the bottom
            Collections.reverse(allPlayers);

            // Creating and styling JLabels on the top row of the table (they label the columns)
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

            // Loop that goes from 0-9, taking the most recent
            for(int x = 0; x < 10; x++) {
                // Reset the labels after each loop
                name = new JLabel("-");
                pts = new JLabel("-");
                time = new JLabel("-");

                try {
                    // Set each row to be a player's name, score, and time
                    // ArrayList that holds ArrayLists that holds the name, points, and time of a player in that order
                    name.setText(allPlayers.get(x).get(0));
                    pts.setText(allPlayers.get(x).get(1));
                    time.setText(allPlayers.get(x).get(2));
                } catch(IndexOutOfBoundsException e) { 
                    // In the case that there's not yet 10 attemps in a difficulty, put placeholder "-" dashes
                    // (this is already declared above)
                }
                    
                // Setting all the styles of the rows
                name.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(3, 6, 3, 0, DARK_SPACE_GRAY), BorderFactory.createEmptyBorder(10,10,10,10)));
                pts.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(3, 6, 3, 6, DARK_SPACE_GRAY), BorderFactory.createEmptyBorder(10,10,10,10)));
                time.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(3, 0, 3, 6, DARK_SPACE_GRAY), BorderFactory.createEmptyBorder(10,10,10,10)));

                final JLabel[] ROW = {name, pts, time};

                for(JLabel row : ROW) {
                    row.setFont(ARIAL24);
                    row.setBackground(Color.LIGHT_GRAY);
                    row.setOpaque(true);
                    row.setForeground(Color.BLACK);
                    highScoreTable.add(row);
                }
                

            }
            reader.close();

        } catch(IOException e) {}

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
        // Game Screen initialization
        sec = 0; // Keeps track of time
        gamePanel.removeAll();
        setContentPane(gamePanel);
        
        GridBagLayout gb = new GridBagLayout();
        gamePanel.setLayout(gb);    
        GridBagConstraints con = new GridBagConstraints();

        // Ignore this entire block if the user selected zen mode (then we don't worry about timers)
        if(!cb.getSelectedItem().toString().equals("Zen mode (unlimited)")) {
            timer = new Timer();

            // Get the time the user selected
            String selection = cb.getSelectedItem().toString();
            
            // Set the initial time keeping variable to the appropriate selection
            // (2 more seconds because I noticed it usually takes ~2 seconds for the panel to load)
            if(selection.equals("30s")) sec = 32;
            else if(selection.equals("1 min")) sec = 62;
            else if(selection.equals("3 min")) sec = 182;
            else if(selection.equals("5 min")) sec = 302;

            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    // Every time the clock ticks:
                    // Update the timer text at the top of the screen
                    ptsText.setText("Points: " + points + " | Time Left: " + sec);
                    sec--;

                    // If below 10 seconds are remaining, flash the text red and white, changing every second
                    if(sec < 10 && sec % 2 == 1) {
                        ptsText.setForeground(BRIGHT_RED);
                    } else if(sec < 10 && sec % 2 == 0) {
                        ptsText.setForeground(Color.WHITE);
                    }   

                    // At 10 seconds remaining, play a "time's running out" sound
                    if(sec == 9) playAudio(new File("tick.wav"));

                    if (sec < 0) {
                        // Cancel timer, and play game over audio.
                        timer.cancel();
                        sec = -1;
                        playAudio(new File("over.wav"));

                        // Prompt user to enter name
                        String name = JOptionPane.showInputDialog(gamePanel, "Points: " + points + "\nPlease enter your name:", "Time's up!", JOptionPane.INFORMATION_MESSAGE);

                        // If person didn't enter a name, default to "Player"
                        if(name == null || name.isEmpty()) name = "Player";

                        try {
                            file = new File(String.format("HISCORES\\%d.txt", difficulty));
                            writer = new BufferedWriter(new FileWriter(file, true));

                            // Split character is a semicolon
                            writer.write(name);
                            writer.write(';');
                            writer.write(String.valueOf(points));
                            writer.write(';');
                            writer.write(selection);
                            writer.write(';');
                            writer.newLine();

                            writer.close();

                        } catch(IOException e) {}
                        
                        // Prompt user if they want to play again or not
                        int yn = JOptionPane.showConfirmDialog(gamePanel,"Play again?", "Time's up!", JOptionPane.INFORMATION_MESSAGE);
                        
                        // Yes button takes back to title screen
                        if(yn == 0 || yn == 2) {
                            canGetPoints = true;
                            points = 0;
                            hasStarted = false;
                            titleScreen();
                        
                        // No button exits the program.
                        } else if(yn == 1) {
                            System.exit(0);
                        }
                    }
                }
            }, 0, 1000);
        }

        gamePanel.setBackground(SPACE_GRAY);
        gamePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Text at the top of the screen
        ptsText.setText("Points: " + points + " | Time Left: " + sec);
        ptsText.setForeground(Color.WHITE);

        // The panel at the bottom which houses the _ _ _ _ text, textfield, and 3 buttons has a grid layout
        subPanel.setLayout(new GridLayout(3, 1, 50, 20));

        // Colour of border changes depending on what difficulty you're playing on
        MatteBorder toSet = BorderFactory.createMatteBorder(10, 10, 10, 10, Color.BLACK);   

        // Green = easy, White = normal, Red = hard
        if(difficulty == 0) toSet = BorderFactory.createMatteBorder(10, 10, 10, 10, new Color(100, 208, 152));
        else if (difficulty == 1) toSet = BorderFactory.createMatteBorder(10, 10, 10, 10, Color.WHITE);
        else if(difficulty == 2) toSet = BorderFactory.createMatteBorder(10, 10, 10, 10, BRIGHT_RED);
            
        subPanel.setBorder(BorderFactory.createCompoundBorder(toSet, BorderFactory.createEmptyBorder(10, 10, 10, 10)));


        // Generate new Pokemon (difficulty is passed because in the case of easy more, less Pokemon to choose from)
        pk = new Pokemon(difficulty);
        // Download an image of this new Pokemon
        pk.getImage();

        // Set the main image to the Pokemon image we just downloaded
        Image mon = null;
        try {
            mon = ImageIO.read(new File("mon.png"));
        } catch(IOException e) {}

        img = new ImageIcon(new ImageIcon(mon).getImage().getScaledInstance(480, 480, java.awt.Image.SCALE_SMOOTH));

        image.setIcon(img);
        image.setHorizontalAlignment(JLabel.CENTER);
        image.setSize(256,256);

        // Set the default text in the UI (A bunch of underscores depending on the Pokemon's name's length)
        sb = new StringBuilder();
        for(int i = 0; i < pk.name.length(); i++) sb.append("_ ");
        text.setText(sb.toString());
        
        // Style the text
        text.setFont(new Font("Arial", Font.PLAIN, 50));
        text.setForeground(new Color(255,255,255));
        text.setHorizontalAlignment(JLabel.CENTER);

        // GridBag Layout shenanegans
        con.fill = GridBagConstraints.BOTH;
        con.gridwidth = GridBagConstraints.REMAINDER;
        con.gridheight = 1;
        
        // Style the top text
        ptsText.setFont(new Font("Arial", Font.PLAIN, 40));
        ptsText.setHorizontalAlignment(JLabel.CENTER);

        // Update constraints to GridBag and add the top text to the panel
        gb.setConstraints(ptsText, con);
        gamePanel.add(ptsText);

        // Update constraints again and then add the image to the panel 
        // (which is below the top text, and takes up most of the panel)
        con.gridheight = 2;
        con.weighty = 1.0;
        gb.setConstraints(image, con);
        gamePanel.add(image);
        
        subPanel.add(text);
        subPanel.setBackground(new Color(54, 52, 59));

        // Button panel will be a 1x3 grid for all 3 buttons at the very bottom of the parent grid
        buttonPanel.setLayout(new GridLayout(1,3,10,0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        buttonPanel.setBackground(DARK_SPACE_GRAY);
        
        // Rightmost next button styles
        nextButton.setPreferredSize(new Dimension(200,32));
        nextButton.addActionListener(new ButtonListener());
        nextButton.setToolTipText("Generate a new Pokemon!");
        nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        nextButton.setFont(ARIAL20);

        // Middle submit button styles
        submitButton.setPreferredSize(new Dimension(200,32));
        submitButton.addActionListener(new ButtonListener());
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.setFont(ARIAL20);

        // Leftmost hit button styles
        hintButton.setPreferredSize(new Dimension(200, 32));
        hintButton.addActionListener(new ButtonListener());
        hintButton.setFont(ARIAL20);
        hintButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Textfield styles, setting default text to nothing
        textField.setText(null);
        textField.setPreferredSize(new Dimension(32,16));
        textField.setFont(new Font("Arial", Font.PLAIN, 32));
        textField.setHorizontalAlignment(JTextField.CENTER);
        // Weird bug was happening where the textfield would get multiple actionlisteners, lagging the program
        // So only add an actionlistener if there isn't one already
        if(textField.getActionListeners().length == 0) textField.addActionListener(new ButtonListener());

        // Add the buttons, and everything else to the gamePanel
        buttonPanel.add(hintButton);
        buttonPanel.add(submitButton);
        buttonPanel.add(nextButton);

        subPanel.add(textField);
        subPanel.add(buttonPanel);

        gamePanel.add(subPanel);
        gamePanel.updateUI();
    }

    public Pokeguesser() {
        // JFrame boilerplate code
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
            
            // Next Pokemon
            if(event.getSource() == nextButton || // Activated Clicking actual button...
              (event.getSource() == textField && (!canGetPoints || textField.getText().length() == 0))) { 
                // Or Pressing enter when textfield is focused, and is empty
                // When player presses enter on an empty field, assume they are SKIPPING
                
                // Remove points only if player can lose points and is playing hard mode
                if(points > 0 && canGetPoints && difficulty == 2) points -= 1;

                // Update text with the points
                ptsText.setText("Points: " + points + " | Time Left: " + sec);
                canGetPoints = true;
                nextButton.setText("Pass");

                // New random Pokemon
                pk = new Pokemon(difficulty);
                pk.getImage();

                // Parse image
                Image mon = null;
                try {
                    mon = ImageIO.read(new File("mon.png"));
                } catch(IOException e) {}

                // Update text and image
                img = new ImageIcon(mon);
                image.setIcon(img);
                
                // Make a new string of empty _ based on Pokemon's name's length
                sb = new StringBuilder();
                for(int i = 0; i < pk.name.length(); i++) {
                    sb.append("_ ");
                }

                // Update text
                text.setText(sb.toString());
                textField.setText("");

            }

            // Player guess
            else if((event.getSource() == submitButton && textField.getText() != "") || // Activated on clicking submit button...
                    (event.getSource() == textField && canGetPoints)) { // Or pressing enter when focusing on textfield, and there's actually a guess in the text field

                // If the guess is correct (compare to Pokemon name instance variable)
                if(textField.getText().toLowerCase().equals(pk.name.toLowerCase())) {
                    // Update text to the name of the Pokemon
                    text.setText("You got it! " + pk.name);

                    // Add 5 points to the player's score
                    // If block here because no points should be awarded if the player presses Enter just like that
                    if(canGetPoints) {
                        points += 5;
                        ptsText.setText("Points: " + points + " | Time Left: " + sec);
                        canGetPoints = false; // Set can get points to false so that player can't spam enter for infinite points
                        nextButton.setText("NEXT");
                    }

                    // Play correct audio!
                    playAudio(new File("right.wav"));

                } else {
                    
                    // Play incorrect audio
                    playAudio(new File("wrong.wav"));

                    // If playing on easy or normal diffuculty,
                    // The player gets a hint
                    if(difficulty != 2) {
                        sb = new StringBuilder();

                        // Build a new string, going char by char of the players guess and the actual Pokemon's name.
                        // If a char matches, add that correct char to the string. Otherwise, put an _.
                        // For example, if the actual name is PIKACHU and the guess is PEKACHU
                        // Set the text to P _ K A C H U
                        for(int i = 0; i < pk.name.length(); i++) {
                            try {
                                if(textField.getText().toLowerCase().charAt(i) == pk.name.toLowerCase().charAt(i)) {
                                    sb.append(pk.name.charAt(i) + " "); // Correct char at this index
                                } else {
                                    sb.append("_ "); // Incorrect char at this index
                                }
                            }  catch(IndexOutOfBoundsException e) { // In case guess is longer or shorter than the actual name
                                sb.append("_ ");
                            }
                        }

                        text.setText(sb.toString());

                    // If playing on hard mode, player does not get a hint.
                    } else {
                        // Just put underscores.
                        sb = new StringBuilder();
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

                sb = new StringBuilder();
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
