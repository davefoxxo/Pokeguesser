/* 
    CREATED BY DAVID KOCHANSKI, 2022
    Pokeguesser - a Java Swing application where you guess Pokemon, based on a given image!
    This game features different modes, a score recording system, and an image grabber from PokeAPI

    Full code repository can be found at https://github.com/davefoxxo/Pokeguesser/
*/

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import javax.sound.sampled.*;

import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class Pokeguesser extends JFrame implements WindowListener {
    public static void main(String[] args) {
        Pokeguesser game = new Pokeguesser();
        game.titleScreen(); // Display title screen on startup
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
    
    // Variables

    // Game panel
    JPanel gamePanel = new JPanel();

    // Game sub panels
    JPanel subPanel = new JPanel();
    JPanel buttonPanel = new JPanel();

    // Pokemon object and it's image to go along with it
    Pokemon pk = new Pokemon(1);
    JLabel image = new JLabel();
    ImageIcon img = new ImageIcon();

    JLabel ptsText = new JLabel("Points: 0 | Time Left: -1"); // this text will be at the top of the screen and will be updated with points and time left
    JLabel text = new JLabel(); // This text will be updated on the screen on a player's guess
    JTextField textField = new JTextField(20); // Player input field
    
    // 3 Buttons at the bottom of the game panel
    JButton nextButton = new JButton("Pass");
    JButton submitButton = new JButton("Submit");
    JButton hintButton = new JButton("Hint");

    // Title panel
    JPanel titlePanel = new JPanel();

    // Title sub panels
    JLabel mainTitle = new JLabel("Pokeguesser"); // Main title at the top of the screen
    JPanel settingsPanel = new JPanel();
    JPanel subSettingsPanel = new JPanel();

    // Settings panel sub panels
    JPanel startButtonPanel = new JPanel();
    JPanel radioButtonPanel = new JPanel();

    // Difficulty selector: Radio Buttons on the title panel, and their group
    JRadioButton rbEasy = new JRadioButton("Easy");
    JRadioButton rbNormal = new JRadioButton("Normal");
    JRadioButton rbHard = new JRadioButton("Hard");
    ButtonGroup difficulties = new ButtonGroup();

    // Radio button labels
    JLabel timeLabel = new JLabel("Time");
    JLabel difLabel = new JLabel("Difficulty");

    // Settings panel buttons
    JButton startButton = new JButton("Start"); // Starts game
    JButton highScoreButton = new JButton("High Scores"); // Takes user to high scores

    // Time selector (drop down menu)
    String[] times = {"30s", "1 min", "3 min", "5 min", "Zen mode (unlimited)"};
    JComboBox<String> timeComboBox = new JComboBox<>(times);

    // Rules paragraph, using HTML!
    JLabel rules = new JLabel("<html>Welcome to Pokeguesser!<br>Guess the Pokemon's name and move onto the next one.<br>If you can't think of the name, either get a hint or pass!<br>Select your time interval & difficulty, and press START!<br>5 points awarded per Pokemon guessed.<br><br><strong>Easy Mode:</strong> Smaller selection of the first 151 Pokemon. Great for teachers! ;)<br><strong>Normal Mode:</strong> All 898 Pokemon available!<br><strong>Hard mode:</strong> All 898 Pokemon, AND 1 point is deducted per hint and per pass. Also, no hint blanks will be filled for partially correct guesses!<br>You can use <strong>ENTER</strong> to submit answers. If you're correct, press <strong>ENTER</strong> again to go to the next Pokemon! To pass, press <strong>ENTER</strong> with an empty textbox.</html>");

    // High Score panel
    JPanel highScorePanel = new JPanel();

    // High Score sub panels
    JPanel highScoreTitlePanel = new JPanel();
    JPanel highScoreTable = new JPanel();
    JPanel highScoreBackPanel = new JPanel();

    JLabel highScoreTitle = new JLabel("High Scores"); // High score title at the top of the screen

    // High score radio buttons and their button group
    JPanel rbHiPanel = new JPanel();
    JRadioButton rbHiEasy = new JRadioButton("Easy");
    JRadioButton rbHiNormal = new JRadioButton("Normal");
    JRadioButton rbHiHard = new JRadioButton("Hard");
    ButtonGroup hiGroup = new ButtonGroup();

    JButton highScoreBack = new JButton("Back"); // Takes user back to the home title panel

    int points = 0; // Stores player points globally
    int sec = 0;    // Stores time left globally
    int difficulty = 0; // 0 easy, 1 normal, 2 hard

    File file; // File object (placed in global scope since it's used in many places)

    // Global booleans
    boolean canGetPoints = true;
    boolean hasStarted = false;

    Timer timer = new Timer(); // Timer object
    StringBuilder sb = new StringBuilder(); // StringBuilder object in global score since it's again used in many places

    // Final defined font sizes that save having to type "new Font(.....)" every time a font is set
    final Font ARIAL24 = new Font("Arial", Font.PLAIN, 24);
    final Font ARIAL20 = new Font("Arial", Font.PLAIN, 20);
    final Font ARIAL18_BOLD = new Font("Arial", Font.BOLD, 18);
    final Font ARIAL18 = new Font("Arial", Font.PLAIN, 18);
    final Font ARIAL14_BOLD = new Font("Arial", Font.PLAIN, 14);

    // Same thing with colours
    final Color SPACE_GRAY = new Color(54, 52, 59);
    final Color DARK_SPACE_GRAY = new Color(29, 28, 32);
    final Color BRIGHT_RED = new Color(255,44,44);


    public void titleScreen() {
        /*
            This is the title screen panel, having the title, settings, rules, and buttons to start the game.
            It is called as soon as the program is run for the first time, and when the game is over
        */

        // Title panel initialization
        titlePanel.removeAll();
        setContentPane(titlePanel);

        // Title panel will have 3 sections: The title, the buttons (and where you set the difficulty and time), and the rules paragraph
        titlePanel.setLayout(new GridLayout(3,1));
        titlePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(10,10,10,10, BRIGHT_RED), BorderFactory.createEmptyBorder(20,20,20,20)));

        // Main title styles
        mainTitle.setFont(new Font("Arial", Font.PLAIN, 92));
        mainTitle.setHorizontalAlignment(JLabel.CENTER);
        mainTitle.setForeground(Color.WHITE);

        titlePanel.setBackground(DARK_SPACE_GRAY);
        titlePanel.add(mainTitle);
        
        // Add all 3 Radio buttons that control the selected difficulty to a button group `difficulties`
        difficulties.add(rbEasy);
        difficulties.add(rbNormal);
        difficulties.add(rbHard);

        // Styles the radio buttons
        rbEasy.setHorizontalAlignment(JRadioButton.CENTER);
        rbEasy.setFont(ARIAL14_BOLD);

        rbNormal.setHorizontalAlignment(JRadioButton.CENTER);
        rbNormal.setFont(ARIAL14_BOLD);
        rbNormal.setSelected(true); // By default, the normal difficulty will be selected

        rbHard.setHorizontalAlignment(JRadioButton.CENTER);
        rbHard.setFont(ARIAL14_BOLD);

        // The settings panel has two halves: one has two buttons: to start game and to take to high scores screen
        // The other half has settings for difficulty and time
        settingsPanel.setLayout(new GridLayout(1,2,0,0));
        settingsPanel.setBackground(SPACE_GRAY);
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(55,20,65,20));

        // `startButtonPanel` is the left half and has 2 buttons
        startButtonPanel.setLayout(new GridLayout(2,1, 20, 20));
        startButtonPanel.setBorder(BorderFactory.createEmptyBorder(40, 15, 0, 15));
        startButtonPanel.setBackground(SPACE_GRAY);

        // Style the two buttons
        highScoreButton.addActionListener(new ButtonListener());
        highScoreButton.setFont(ARIAL24);

        startButton.addActionListener(new ButtonListener());
        startButton.setFont(ARIAL24);
        
        // Add the buttons to the half panel, and add the panel to the main panel
        startButtonPanel.add(startButton);
        startButtonPanel.add(highScoreButton);
        settingsPanel.add(startButtonPanel);

        // `subSettingsPanel` is the right half, and has a label, a jcombobox (for time), another label, and a radio array (for difficulties)
        // Thus, a grid that's 4 rows
        subSettingsPanel.setLayout(new GridLayout(4,1));
        subSettingsPanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 25));
        subSettingsPanel.setBackground(SPACE_GRAY);

        // Style the time combo box, and it's label
        timeLabel.setHorizontalAlignment(JLabel.CENTER);
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(ARIAL20);
        
        timeComboBox.setFont(ARIAL18_BOLD);
        timeComboBox.setSelectedIndex(1); // Default selected is index 1, which corresponds to one minute

        // Add the top half of the sub settings
        subSettingsPanel.add(timeLabel);
        subSettingsPanel.add(timeComboBox);

        // Style the radio button array, and it's label
        radioButtonPanel.setLayout(new GridLayout(1,3)); // 3 radio buttons arranged inline
        radioButtonPanel.add(rbEasy);
        radioButtonPanel.add(rbNormal);
        radioButtonPanel.add(rbHard);

        difLabel.setHorizontalAlignment(JLabel.CENTER);
        difLabel.setForeground(Color.WHITE);
        difLabel.setFont(ARIAL20);

        // Add the bottom half of the sub settings
        subSettingsPanel.add(difLabel);
        subSettingsPanel.add(radioButtonPanel);

        // Add the right half of the settings panel to the middle panel
        settingsPanel.add(subSettingsPanel);
        
        // Add this entire middle settings panel to the main panel
        titlePanel.add(settingsPanel);

        // Style the rules paragraph which takes up the bottom third
        rules.setFont(ARIAL18);
        rules.setForeground(Color.WHITE);
        rules.setBackground(SPACE_GRAY);
        
        // Add the rules to the panel
        titlePanel.add(rules);

        titlePanel.updateUI();
    }


    public void highScoreScreen(int _dif) {
        /*
            This method will call and display high scores of users, accessible through the title screen panel
            It requires an argument _dif, since the High Scores are sorted based on difficulty - different panels for each of the 3 difficulties
        */

        // High score initialization
        highScorePanel.removeAll();
        setContentPane(highScorePanel);

        // The whole high score panel will have a Border layout:
        // The top will have a label and radio buttons that transition between the difficulties
        // The middle (most of the area) will have the actual high score table
        // The bottom will have a button that takes user back to the title panel
        highScorePanel.setLayout(new BorderLayout());
        highScoreTitle.setForeground(Color.WHITE);
        highScorePanel.setBackground(SPACE_GRAY);
        
        // The top of the panel has the label, and a radio button array with 3 buttons.
        highScoreTitlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 0));
        highScoreTitlePanel.setBackground(BRIGHT_RED);
        highScoreTitlePanel.add(highScoreTitle);

        rbHiPanel.setLayout(new GridLayout(1,3)); // 3 Radio buttons arranged inline

        // Styling and adding all 3 radio buttons
        final JRadioButton[] TABLE_RADIO_BUTTONS = {rbHiEasy, rbHiNormal, rbHiHard};

        for(JRadioButton btn : TABLE_RADIO_BUTTONS) {
            hiGroup.add(btn);
            btn.setFont(ARIAL24);
            btn.setHorizontalAlignment(JRadioButton.CENTER);
            if(btn.getActionListeners().length == 0) btn.addActionListener(new ButtonListener());
            rbHiPanel.add(btn);
        }
        highScoreTitlePanel.add(rbHiPanel);

        // Add `highScoreTitlePanel` to the top of the panel
        highScorePanel.add(highScoreTitlePanel, BorderLayout.PAGE_START);

        // The `highScoreTable` will hold the 10 most recent names, scores, and times of a given difficulty
        // 11x3 because another row is needed for the column headers
        highScoreTable.removeAll();
        highScoreTable.setLayout(new GridLayout(11,3));
        highScoreTitle.setFont(new Font("Arial", Font.PLAIN, 40));
        highScoreTable.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
        highScoreTable.setBackground(SPACE_GRAY);

        // Determine which file to read from depending on the difficulty set in the arguments of this highScoreScreen() method
        file = new File(String.format("HISCORES\\%d.txt", _dif));

        // 2D ArrayList: Inner Arraylists will have a length of 3, and store the name, score, and time of a given play-through (in that order)
        // Outer Arraylist contains all these inner ArrayLists so that we can manipulate them later
        ArrayList<ArrayList<String>> allPlayers = new ArrayList<>();

        try {
            // High score table with display the 10 MOST RECENT names, scores, and times for each of the 3 difficulties.
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line = reader.readLine();
            String[] data;

            // Read all the data, and store it in an ArrayList which holds ArrayLists that hold names, scores, times.
            while(line != null) {
                ArrayList<String> temp = new ArrayList<>(3);
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

            // Creating and styling JLabels on the top row of the table (they label each of the 3 columns)
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

            // JLabels to be used for each cell of the table to be constructed
            JLabel name, pts, time;

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
                    // In the case that there's not yet 10 attempts in a difficulty, put placeholder "-" dashes
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

        // Add the generated `highScoreTable`, all of its rows and columns, to the center of the highScorePanel
        highScorePanel.add(highScoreTable, BorderLayout.CENTER);
        
        // `highScoreBackPanel` is the footer of the active panel, it will simply house one button that takes user back to the title
        highScoreBackPanel.setBackground(BRIGHT_RED);

        // Style this back button
        highScoreBack.setPreferredSize(new Dimension(150, 50));
        highScoreBack.addActionListener(new ButtonListener());
        highScoreBack.setFont(ARIAL18_BOLD);

        // Add the footer to the bottom of the highScorePanel
        highScoreBackPanel.add(highScoreBack);
        highScorePanel.add(highScoreBackPanel, BorderLayout.PAGE_END);

        highScorePanel.updateUI();
    }


    public void gameScreen() {
        /*
            This method will show the game screen, which is accessible through the title screen.
            It will display points, the Pokemon's image, and the user's input fields and buttons.
            It will keep track of time and write to the file names, scores and times once the time has elapsed.
        */

        // Game Screen initialization
        sec = 0; // Keeps track of time
        gamePanel.removeAll();
        setContentPane(gamePanel);
        
        // This panel will use a GridBagLayout, since the image should have a bigger area, front and center
        GridBagLayout gb = new GridBagLayout();
        gamePanel.setLayout(gb);    
        GridBagConstraints con = new GridBagConstraints();

        // Ignore this entire block if the user selected zen mode (then we don't worry about timers)
        if(!timeComboBox.getSelectedItem().toString().equals("Zen mode (unlimited)")) {
            timer = new Timer();

            // Get the time the user selected
            String selection = timeComboBox.getSelectedItem().toString();
            
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
                    if(sec == 9) playAudio(new File("Media\\tick.wav"));

                    if (sec < 0) {
                        // Cancel timer, and play game over audio.
                        timer.cancel();
                        sec = -1;
                        playAudio(new File("Media\\over.wav"));

                        // Prompt user to enter name
                        String name = JOptionPane.showInputDialog(gamePanel, "Points: " + points + "\nPlease enter your name:", "Time's up!", JOptionPane.INFORMATION_MESSAGE);

                        // If person didn't enter a name, default to "Player"
                        if(name == null || name.isEmpty()) name = "Player";

                        // Writing to the file
                        try {
                            file = new File(String.format("HISCORES\\%d.txt", difficulty));
                            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));

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
            }, 0, 1000); // Every 1000 ms (1 sec.)
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
        if(difficulty == 0) toSet = BorderFactory.createMatteBorder(10, 10, 10, 10, new Color(30, 181, 120));
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

        // GridBag Layout constraint shenanigans
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
        subPanel.setBackground(SPACE_GRAY);

        // Button panel will be a 1x3 grid for all 3 buttons at the very bottom of the parent grid
        buttonPanel.setLayout(new GridLayout(1,3,10,0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        buttonPanel.setBackground(SPACE_GRAY);
        
        // Three bottom buttons in the UI
        final JButton[] BOTTOM_BUTTONS = {hintButton, submitButton, nextButton};

        // All three buttons have identical styles
        for(JButton btn : BOTTOM_BUTTONS) {
            btn.setPreferredSize(new Dimension(200,32));
            btn.addActionListener(new ButtonListener());
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setFont(ARIAL20);
            buttonPanel.add(btn);
        }

        // Textfield styles, setting default text to nothing
        textField.setText(null);
        textField.setPreferredSize(new Dimension(32,16));
        textField.setFont(new Font("Arial", Font.PLAIN, 32));
        textField.setHorizontalAlignment(JTextField.CENTER);
        // Weird bug was happening where the textfield would get multiple ActionListeners, heavily lagging the program
        // So only add an actionlistener if there isn't one already
        if(textField.getActionListeners().length == 0) textField.addActionListener(new ButtonListener());

        // Add the text field, and the three buttons in the button panel
        subPanel.add(textField);
        subPanel.add(buttonPanel);
        gamePanel.add(subPanel);

        gamePanel.updateUI();
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
                    playAudio(new File("Media\\right.wav"));

                // If guess is incorrect
                } else {
                    // Play incorrect audio
                    playAudio(new File("Media\\wrong.wav"));

                    // If playing on easy or normal difficulty,
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
            
            // Player presses hint button
            else if(event.getSource() == hintButton) {
                // If playing on hard difficulty, deduct a point for taking a hint
                if(points > 0 && difficulty == 2) points -= 1;
                ptsText.setText("Points: " + points + " | Time Left: " + sec);
                
                // The hint will reveal the first and last char of the Pokemon's name
                // For example, if the Pokemon is PIKACHU, requesting a hint will return P _ _ _ _ _ U
                sb = new StringBuilder();
                sb.append(pk.name.toCharArray()[0]); // Add first char
                sb.append(" ");

                // Fill in underscores for the Pokemon's length minus 2 (everything but the first and last char)
                for(int i = 0; i < pk.name.length()-2; i++) sb.append("_ ");
                    
                sb.append(pk.name.toCharArray()[pk.name.length()-1]); // Add last char

                // Update UI text
                text.setText(sb.toString());
            }

            // Player presses start button on title screen
            else if(event.getSource() == startButton) {
                // Update global difficulty variable based on the selected radio button on the title screen panel
                if(rbHard.isSelected()) difficulty = 2;
                else if(rbNormal.isSelected()) difficulty = 1;
                else difficulty = 0;

                // Call the gameScreen() method, which initiates the game.
                if(!hasStarted) gameScreen();
                hasStarted = true;   
            }

            // Player presses High Scores button on title screen
            else if(event.getSource() == highScoreButton) {
                // Call the highScoreScreen() method, which displays all the high scores.
                // By default show the high scores of the difficulty set by the user
                highScoreScreen(difficulty);
                rbHiNormal.setSelected(true);

                // highScorePanel() has a parameter that determines which difficulty high scores to display
            }

            // Player presses EASY radio button in the high score panel
            else if(event.getSource() == rbHiEasy) {
                // Update the highScorePanel to easy mode (_dif = 0)
                highScorePanel.removeAll();
                highScoreScreen(0);
            }   

            // Player presses NORMAL radio button in the high score panel
            else if(event.getSource() == rbHiNormal) {
                // Update the highScorePanel to normal mode (_dif = 1)
                highScorePanel.removeAll();
                highScoreScreen(1);
            }

            // Player presses HARD radio button in the high score panel
            else if(event.getSource() == rbHiHard) {
                // Update the highScorePanel to hard mode (_dif = 2)
                highScorePanel.removeAll();
                highScoreScreen(2);
            }

            // Player presses BACK button in the high score panel
            else if(event.getSource() == highScoreBack) {
                // Take player back to the title screen.
                titleScreen();
            }  
        }
    }

    // Player tries to close window
    public void windowClosing(WindowEvent e) {
        // If user is currently in a game, prompt them if they're sure they want to quit
        if(getContentPane() == gamePanel) {
            int c = JOptionPane.showConfirmDialog(getContentPane(), "Are you sure you want to exit? \n(Time's still ticking!)", "Quit?", JOptionPane.INFORMATION_MESSAGE);
            
            // Quit if user is sure
            if(c == 0) System.exit(0);

        } else {
            // If user isn't currently in game, just exit the program
            System.exit(0);
        }
    }

    public void windowOpened(WindowEvent e) {} 
    public void windowActivated(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}

    // This method will play a file given a file name
    // Taken from online
    public void playAudio(File file) {
        try {        
            AudioInputStream stream = AudioSystem.getAudioInputStream(file);
            AudioFormat format = stream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(stream);
            clip.start();
        }
        catch (Exception e) {}
    }
}
