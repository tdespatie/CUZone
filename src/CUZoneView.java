import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CUZoneView extends JPanel {
    private File[] fileList;
    private ActionListener handler = new Controller();

    private JPanel mainScreen = new JPanel();
    private JPanel mainControlsPanel = new JPanel();
    private JPanel logPanel = new JPanel();
    private JTextArea logText = new JTextArea();

    private JPanel testPanel = new JPanel();
    private JPanel passwordPanel = new JPanel();


    public CUZoneView() {
        setSize(400,200);
        setLayout(new BorderLayout());

        mainScreen.setLayout(new BorderLayout());
        mainControlsPanel.setLayout(new GridLayout(4,2));

        JLabel emailLabel = new JLabel("Email:");
        JButton buttonCreate = new JButton("Create Password");
        buttonCreate.setActionCommand("Email");
        buttonCreate.addActionListener(handler);

        JLabel shopLabel = new JLabel("Shopping:");
        JButton shopBtn = new JButton("Create Password");
        shopBtn.setActionCommand("Shopping");
        shopBtn.addActionListener(handler);
        shopBtn.setEnabled(false);

        JLabel bankLabel = new JLabel("Banking:");
        JButton bankBtn = new JButton("Create Password");
        bankBtn.setActionCommand("Banking");
        bankBtn.addActionListener(handler);
        bankBtn.setEnabled(false);

        JLabel testLabel = new JLabel("Test Your Memory");
        JButton testButton = new JButton("Launch Test");
        testButton.setActionCommand("Test");
        testButton.addActionListener(handler);
        testButton.setEnabled(false);

        mainControlsPanel.add(emailLabel);
        mainControlsPanel.add(buttonCreate);
        mainControlsPanel.add(shopLabel);
        mainControlsPanel.add(shopBtn);
        mainControlsPanel.add(bankLabel);
        mainControlsPanel.add(bankBtn);
        mainControlsPanel.add(testLabel);
        mainControlsPanel.add(testButton);
        mainControlsPanel.setVisible(true);

        mainScreen.add(mainControlsPanel, BorderLayout.NORTH);

        logText.setText("=========================== LOG FILE ============================\n");
        logText.setEditable(false);
        JButton saveLogText = new JButton("Save Log");
        saveLogText.setActionCommand("Save");
        saveLogText.addActionListener(handler);

        logPanel.setLayout(new BorderLayout());
        logPanel.add(logText, BorderLayout.CENTER);
        logPanel.add(saveLogText, BorderLayout.SOUTH);
        logPanel.setVisible(true);

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
        JTextArea passwordLabel = new JTextArea("Your password is: " + password);

        passwordLabel.setEditable(false);
        passwordLabel.setLineWrap(true);

        passwordPanel.setVisible(true);
        testPanel.add(passwordPanel, BorderLayout.CENTER); // Add the shape panel to the center of the Test panel
        testPanel.add(passwordLabel, BorderLayout.NORTH); // Add the provided the password to the top of the panel
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
    }

    private class Controller implements ActionListener {
        final CUZoneModel model = new CUZoneModel();

        @Override
        public void actionPerformed(ActionEvent e) {
            String str = e.getActionCommand();

            if (str.equals("Email")) {
                writeToLog("Email password has been created.");
                model.setEmailPassword(); // Create a random shape password

                String[] emailPassword = model.getEmailPassword(); // Get the created password
                String strEmailPassword = ""; // Turn the password from an array to a string
                for (String i : emailPassword)
                    strEmailPassword += i + " "; // I know...this leaves an extra comma

                randomizeShapePanel(model.randomizeFileList());
                createTestPanel(strEmailPassword); // Call the function to display the Test Panel for user input
                model.setCurrentPassword(emailPassword); // Sets the expected Password for user to test input
            } else { // Shape must've been clicked
                if (str.contains(".PNG")) {
                    str = str.replace(".PNG", "").toLowerCase();
                    writeToLog(str + " has been selected.");


                    if (!str.equals(model.getNextExpectedShape())) {
                        model.incNumberOfFailures();
                        JOptionPane.showMessageDialog(testPanel,"You've selected an invalid shape!");
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
            }

        }
    }
}


