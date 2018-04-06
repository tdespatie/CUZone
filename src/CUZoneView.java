/*
    File: CUZoneView.java
    COMP 3008 - Project 2
    Group: 11
    Date Modified: April 6, 2018
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

class CUZoneView extends JPanel {

    private ActionListener handler = new Controller();

    private JPanel mainScreen = new JPanel();

    private JPanel mainControlsPanel = new JPanel();
    private JButton emailBtn;
    private JButton shopBtn;
    private JButton bankBtn;
    private JButton testBtn;
    private JButton backBtn;

    private JTextArea logText = new JTextArea();

    private JPanel testPanel = new JPanel();
    private JPanel passwordPanel = new JPanel();
    private JTextArea passwordLabel = new JTextArea();

    private ArrayList<Integer> orderOfPassword = new ArrayList<>();

    CUZoneView() {
        setSize(400,200);
        setLayout(new BorderLayout());

        orderOfPassword.add(0);
        orderOfPassword.add(1);
        orderOfPassword.add(2);
        Collections.shuffle(orderOfPassword);

        mainScreen.setLayout(new BorderLayout());
        mainControlsPanel.setLayout(new GridLayout(4,2));

        JLabel emailLabel = new JLabel("Email:");
        emailBtn = new JButton("Create Password");
        emailBtn.setActionCommand("Email");
        emailBtn.addActionListener(handler);

        JLabel shopLabel = new JLabel("Shopping:");
        shopBtn = new JButton("Create Password");
        shopBtn.setActionCommand("Shopping");
        shopBtn.addActionListener(handler);
        shopBtn.setEnabled(false);

        JLabel bankLabel = new JLabel("Banking:");
        bankBtn = new JButton("Create Password");
        bankBtn.setActionCommand("Banking");
        bankBtn.addActionListener(handler);
        bankBtn.setEnabled(false);

        JLabel testLabel = new JLabel("Test Your Memory");
        testBtn = new JButton("Launch Test");
        testBtn.setActionCommand("Test");
        testBtn.addActionListener(handler);
        testBtn.setEnabled(false);

        mainControlsPanel.add(emailLabel);
        mainControlsPanel.add(emailBtn);
        mainControlsPanel.add(shopLabel);
        mainControlsPanel.add(shopBtn);
        mainControlsPanel.add(bankLabel);
        mainControlsPanel.add(bankBtn);
        mainControlsPanel.add(testLabel);
        mainControlsPanel.add(testBtn);
        mainControlsPanel.setVisible(true);

        mainScreen.add(mainControlsPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(logText);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        logText.setText("=========================== LOG FILE ===========================\n");
        logText.setEditable(false);

        JButton saveLogText = new JButton("Save Log");
        saveLogText.setActionCommand("Save");
        saveLogText.addActionListener(handler);

        JPanel logPanel = new JPanel();
        logPanel.setLayout(new BorderLayout());
        logPanel.add(scrollPane, BorderLayout.CENTER);
        logPanel.add(saveLogText, BorderLayout.SOUTH);
        logPanel.setVisible(true);
        writeToLog("Application started.");

        mainScreen.add(mainControlsPanel, BorderLayout.NORTH);
        mainScreen.add(logPanel, BorderLayout.CENTER);
        mainScreen.setVisible(true);

        add(mainScreen, BorderLayout.CENTER);
        setVisible(true);

    }

    /* This function is used to create the "Shapes" and allow them to be clickable
        and handled with the controller
     */

    private JButton createButton(String ac, File file, ActionListener handler) {
        final JButton button = new JButton();
        button.setActionCommand(ac);
        button.addActionListener(handler);
        button.setBackground(Color.white);
        button.setForeground(Color.white);

        //This is reading the image from the 'image' directory.
        try {
            String fileLocation;
            if (file.toString().contains("src"))
                fileLocation = file.toString().substring(4);
            else
                fileLocation = file.toString();
            Image img = ImageIO.read(getClass().getResource(fileLocation));

            button.setIcon(new ImageIcon(img));
        } catch (Exception ex) {
            System.out.println(ex);
        }

        return button;
    }

    /* This is the panel that will be shown to have the users test the graphical password
        system
     */
    private void createTestPanel (String password) {
        // Test Panel
        testPanel = new JPanel();
        testPanel.setLayout(new BorderLayout());

        // Display the random password to the user, make sure they can't edit it
        if (password != null) passwordLabel.setText("Your password is: " + password);

        passwordLabel.setEditable(false);
        passwordLabel.setLineWrap(true);
        passwordPanel.setVisible(true);

        backBtn = new JButton("Back");
        backBtn.setActionCommand("Back");
        backBtn.addActionListener(handler);

        testPanel.add(passwordLabel, BorderLayout.NORTH); // Add the provided the password to the top of the panel
        testPanel.add(passwordPanel, BorderLayout.CENTER); // Add the shape panel to the center of the Test panel
        testPanel.add(backBtn, BorderLayout.SOUTH);
        testPanel.setVisible(true);
        mainScreen.setVisible(false); // Make the main menu not visible

        this.add(testPanel); // Add the test panel to the Main Panel
        this.revalidate(); // Revalidate (A.K.A Reload the panel)
    }

    private void randomizeShapePanel(File[] fileList) {
        passwordPanel = new JPanel();
        passwordPanel.setLayout(new GridLayout(4,2));

        // Iterate through each file in the directory to find all the shapes
        for (final File file : fileList)
            passwordPanel.add(createButton(file.getName(), file, handler)); // Create a button per shape

    }

    private void writeToLog(String message) {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd - HH:mm:ss").format(new Date());
        logText.append(timeStamp + ": " + message + "\n");
        logText.setCaretPosition(logText.getDocument().getLength());
    }

    private void saveToFile() {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setApproveButtonText("Save");
        int actionDialog = fileChooser.showSaveDialog(this);
        if (actionDialog != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = fileChooser.getSelectedFile();
        if (!file.getName().endsWith(".txt")) {
            file = new File(file.getAbsolutePath() + ".txt");
        }

        BufferedWriter outFile = null;
        try {
            outFile = new BufferedWriter(new FileWriter(file));
            logText.write(outFile);
        } catch (IOException e) {
            writeToLog("Failed to save log to file.");
            e.printStackTrace();
        } finally {
            if (outFile != null) {
                try {
                    outFile.close();
                } catch (IOException ex) {
                    writeToLog("Failed to save log to file.");
                }
            }
        }
        writeToLog("Log was written to " + file.getAbsolutePath());
        JOptionPane.showMessageDialog(this,"Log was written to " + file.getAbsolutePath());
    }


    private class Controller implements ActionListener {
        final CUZoneModel model = new CUZoneModel();

        @Override
        public void actionPerformed(ActionEvent e) {
            String str = e.getActionCommand();

            switch (str) {
                case "Email":
                    writeToLog("Email password has been created.");
                    model.setEmailPassword(); // Create a random shape password

                    String[] emailPassword = model.getEmailPassword(); // Get the created password

                    StringBuilder strEmailPassword = new StringBuilder(); // Turn the password from an array to a string

                    for (String i : emailPassword)
                        strEmailPassword.append(i).append(" ");

                    randomizeShapePanel(model.randomizeFileList());
                    createTestPanel(strEmailPassword.toString()); // Call the function to display the Test Panel for user input

                    model.setCurrentPassword(emailPassword); // Sets the expected Password for user to test input

                    break;
                case "Shopping":
                    writeToLog("Shopping password has been created.");
                    model.setShoppingPassword(); // Create a random shape password

                    String[] shoppingPassword = model.getShoppingPassword(); // Get the created password

                    StringBuilder strShoppingPassword = new StringBuilder(); // Turn the password from an array to a string

                    for (String i : shoppingPassword)
                        strShoppingPassword.append(i).append(" ");

                    randomizeShapePanel(model.randomizeFileList());
                    createTestPanel(strShoppingPassword.toString()); // Call the function to display the Test Panel for user input

                    model.setCurrentPassword(shoppingPassword); // Sets the expected Password for user to test input

                    break;
                case "Banking":
                    writeToLog("Banking password has been created.");
                    model.setBankingPassword(); // Create a random shape password

                    String[] bankingPassword = model.getBankingPassword(); // Get the created password

                    StringBuilder strBankingPassword = new StringBuilder(); // Turn the password from an array to a string

                    for (String i : bankingPassword)
                        strBankingPassword.append(i).append(" ");

                    randomizeShapePanel(model.randomizeFileList());
                    createTestPanel(strBankingPassword.toString()); // Call the function to display the Test Panel for user input
                    model.setCurrentPassword(bankingPassword); // Sets the expected Password for user to test input
                    break;
                case "Test":
                    writeToLog("Testing passwords. You must click the test button until all tests are completed.");
                    emailBtn.setEnabled(false);
                    shopBtn.setEnabled(false);
                    bankBtn.setEnabled(false);
                    backBtn.setEnabled(false);

                    if (orderOfPassword.size() > 0) {
                        model.resetNumberOfFailures(); // TODO: figure out if this is needed here.
                        randomizeShapePanel(model.randomizeFileList());
                        createTestPanel(null);
                        switch (orderOfPassword.get(0)) {
                            case 0:
                                writeToLog("Randomized test - Email password.");
                                model.setCurrentPassword(model.getEmailPassword());
                                passwordLabel.setText("Please enter the Email password.");
                                break; // Email
                            case 1:
                                writeToLog("Randomized test - Shopping password.");
                                model.setCurrentPassword(model.getShoppingPassword());
                                passwordLabel.setText("Please enter the Shopping password.");
                                break; // Shopping
                            case 2:
                                writeToLog("Randomized test - Banking password.");
                                model.setCurrentPassword(model.getBankingPassword());
                                passwordLabel.setText("Please enter the Banking password.");
                                break; // Banking
                        }
                        orderOfPassword.remove(0);
                        if (orderOfPassword.size() == 0) {
                            mainControlsPanel.setVisible(false);
                            writeToLog("All tests completed!");
                        }
                    }
                    break;
                case "Save":
                    saveToFile();
                    break;
                case "Back":
                    writeToLog("User has chosen to return to main menu.");
                    testPanel.setVisible(false);
                    mainControlsPanel.setVisible(true);
                    mainScreen.setVisible(true);
                    break;
                default:  // Shape must've been clicked
                    if (str.contains(".PNG")) {
                        str = str.replace(".PNG", "").toLowerCase();
                        writeToLog(str + " has been selected.");

                        if (!str.equals(model.getNextExpectedShape())) {
                            model.incNumberOfFailures();
                            JOptionPane.showMessageDialog(testPanel, "You've selected an invalid shape! You have " + (3 - model.getNumberOfFailures()) + " tries remaining.");
                            writeToLog("User has entered an invalid shape! Number of failures: " + model.getNumberOfFailures());
                            if (model.getNumberOfFailures() >= 3) {
                                writeToLog("User has failed 3 times. Click \"Save Log\" below.");
                                testPanel.setVisible(false);
                                mainControlsPanel.setVisible(false);
                                mainScreen.setVisible(true);
                            }
                        } else {
                            if (!model.setNextExpectedShape()) {
                                writeToLog("User has been properly authenticated!");
                                model.resetNumberOfFailures();

                                if (model.getEmailPasswordSet()) {
                                    shopBtn.setEnabled(true);
                                }
                                if (model.getShopPasswordSet()) {
                                    bankBtn.setEnabled(true);
                                }
                                if (model.getBankPasswordSet()) {
                                    testBtn.setEnabled(true);
                                }

                                testPanel.setVisible(false);
                                mainScreen.setVisible(true);
                                return;
                            }
                        }

                        testPanel.remove(passwordPanel);
                        randomizeShapePanel(model.randomizeFileList());
                        testPanel.add(passwordPanel, BorderLayout.CENTER);
                        testPanel.revalidate();
                    }
                    break;
            }

        }
    }
}


