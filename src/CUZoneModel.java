import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

@SuppressWarnings("Duplicates")
public class CUZoneModel {
    private String[] Shapes = {"circle", "diamond", "oval", "parallelogram", "pentagon", "rectangle", "square", "triangle"};
    private File[] fileList;

    private Boolean emailPasswordSet, shoppingPasswordSet, bankingPasswordSet;
    private String[] emailPassword, bankingPassword, shoppingPassword, currentPassword;

    private String nextExpectedShape;
    private int currentShapeIndex;

    private int numberOfFailures, numOfSuccesses;

    public CUZoneModel() {
        emailPasswordSet = shoppingPasswordSet = bankingPasswordSet = false;
        numberOfFailures = numOfSuccesses = currentShapeIndex = 0;

        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();

        File directory;
        if (!s.endsWith("src")) {
            directory = new File("src"); // Source directory of the shape
        } else
            directory = new File("."); // Source directory of the shape

        fileList = directory.listFiles((d, name) -> name.endsWith(".PNG"));
    }

    public File[] getFileList() {
        return fileList;
    }

    public String getNextExpectedShape() {
        return nextExpectedShape;
    }

    public boolean setNextExpectedShape() {
        String[] current_Password = getCurrentPassword();

        if (current_Password.length > 0 && currentShapeIndex < current_Password.length) {
            nextExpectedShape = current_Password[currentShapeIndex++];
            return true;
        }

        return false;
    }

    public String[] getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String[] password) {
        currentPassword = password;
        setNextExpectedShape();
    }

    public File[] randomizeFileList() {
        int index;
        File temp;
        Random random = new Random();
        for (int i = fileList.length - 1; i > 0; i--) {
            index = random.nextInt(i + 1);
            temp = fileList[index];
            fileList[index] = fileList[i];
            fileList[i] = temp;
        }
        return fileList;
    }

    public String[] getEmailPassword() {
        if (emailPasswordSet)
            return emailPassword;
        return null;
    }

    public String[] getShoppingPassword() {
        if (shoppingPasswordSet)
            return shoppingPassword;
        return null;
    }

    public String[] getBankingPassword() {
        if (bankingPasswordSet)
            return bankingPassword;
        return null;
    }

    public void setEmailPassword() {
        int length = getRandomLength();
        emailPassword = new String[length];
        emailPassword = getRandomPassword(length);
        emailPasswordSet = true;
    }

    public void setShoppingPassword() {
        int length = getRandomLength();
        shoppingPassword = new String[length];
        shoppingPassword = getRandomPassword(length);
        shoppingPasswordSet = true;
    }

    public void setBankingPassword() {
        int length = getRandomLength();
        bankingPassword = new String[length];
        bankingPassword = getRandomPassword(length);
        bankingPasswordSet = true;
    }

    /* This is how many screens we will iterate through (Max is 7, Min 5)  */
    private int getRandomLength() {
        Random rand = new Random();
        return rand.nextInt(4) + 4;
    }

    /* This is generating a random number of shape combinations for a given password length */
    private String[] getRandomPassword(int numOfShapes) {
        Random rand = new Random();
        String[] randomPassword = new String[numOfShapes];
        for (int i = 0; i < numOfShapes; i++)
            randomPassword[i] = Shapes[rand.nextInt(7)];
        return randomPassword;
    }


}
