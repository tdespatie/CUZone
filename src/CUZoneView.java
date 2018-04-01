import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;


public class CUZoneView extends JPanel {
    private ActionListener handler = new Controller();
    private JPanel testPanel = new JPanel();
    private JPanel mainScreen = new JPanel();

    public CUZoneView() {
        setSize(400,200);
        //setLayout(new BorderLayout());

        mainScreen.setLayout(new GridLayout(4,2));

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

        mainScreen.add(emailLabel);
        mainScreen.add(buttonCreate);
        mainScreen.add(shopLabel);
        mainScreen.add(shopBtn);
        mainScreen.add(bankLabel);
        mainScreen.add(bankBtn);

        JLabel testLabel = new JLabel("Test Your Memory");
        JButton testButton = new JButton("Launch Test");
        testButton.setActionCommand("Test");
        testButton.addActionListener(handler);
        testButton.setEnabled(false);

        mainScreen.add(testLabel);
        mainScreen.add(testButton);

        add(mainScreen);

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
        JPanel testPanel = new JPanel();
        testPanel.setLayout(new BorderLayout());

        // Display the random password to the user, make sure they can't edit it
        JTextArea passwordLabel = new JTextArea("Your password is: " + password);
        passwordLabel.setEditable(false);
        passwordLabel.setLineWrap(true);

        JPanel passwordPanel = new JPanel(); // This is the panel responsible for displaying the shapes
        passwordPanel.setLayout(new GridLayout(4,2));

        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();

        File directory;
        if (!s.endsWith("src")) {
            directory = new File("src"); // Source directory of the shape
        } else
            directory = new File("."); // Source directory of the shape

        final File[] fileList = directory.listFiles((d, name) -> name.endsWith(".PNG"));

        // Iterate through each file in the directory to find all the shapes
        for (final File file : fileList)
            passwordPanel.add(createButton(file.getName(), file, handler)); // Create a button per shape

        passwordPanel.setVisible(true);
        testPanel.add(passwordPanel, BorderLayout.CENTER); // Add the shape panel to the center of the Test panel
        testPanel.add(passwordLabel, BorderLayout.NORTH); // Add the provided the password to the top of the panel
        testPanel.setVisible(true);

        mainScreen.setVisible(false); // Make the main menu not visible
        this.add(testPanel); // Add the test panel to the Main Panel
        this.revalidate(); // Revalidate (A.K.A Reload the panel)
    }

    private class Controller implements ActionListener {
        final CUZoneModel model = new CUZoneModel();

        @Override
        public void actionPerformed(ActionEvent e) {
            String str = e.getActionCommand();

            if (str.equals("Email")) {
                System.out.println("Email: Create Password!");
                model.setEmailPassword(); // Create a random shape password

                String[] emailPassword = model.getEmailPassword(); // Get the created password
                String strEmailPassword = ""; // Turn the password from an array to a string
                for (String i : emailPassword)
                    strEmailPassword += i + ", "; // I know...this leaves an extra comma
                createTestPanel(strEmailPassword); // Call the function to display the Test Panel for user input
            }

        }
    }
}


