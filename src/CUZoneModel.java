import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

@SuppressWarnings("Duplicates")
public class CUZoneModel {
    private Random random = new Random();
    private String[] Shapes = {"circle", "diamond", "oval", "parallelogram", "pentagon", "rectangle", "square", "triangle"};
    private File[] fileList;

    private Boolean emailPasswordSet, shoppingPasswordSet, bankingPasswordSet;
    private String[] emailPassword, bankingPassword, shoppingPassword, currentPassword;

    private String nextExpectedShape;
    private int currentShapeIndex;

    private int numberOfFailures, numOfSuccesses;

    CUZoneModel() {
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

    String getNextExpectedShape() {
        return nextExpectedShape;
    }

    boolean setNextExpectedShape() {
        String[] current_Password = getCurrentPassword();

        if (current_Password.length > 0 && currentShapeIndex < current_Password.length) {
            nextExpectedShape = current_Password[currentShapeIndex++];
            return true;
        }

        return false;
    }
    boolean getEmailPasswordSet() {
        return emailPasswordSet;
    }
    boolean getShopPasswordSet() {
        return shoppingPasswordSet;
    }
    boolean getBankPasswordSet() {
        return bankingPasswordSet;
    }

    private String[] getCurrentPassword() {
        return currentPassword;
    }

    void setCurrentPassword(String[] password) {
        currentPassword = password;
        currentShapeIndex = 0;
        setNextExpectedShape();
    }

    File[] randomizeFileList() { // Refactor this
        int index;
        File temp;
        for (int i = fileList.length - 1; i > 0; i--) {
            index = random.nextInt(i + 1);
            temp = fileList[index];
            fileList[index] = fileList[i];
            fileList[i] = temp;
        }
        return fileList;
    }

    String[] getEmailPassword() {
        if (emailPasswordSet)
            return emailPassword;
        return null;
    }

    String[] getShoppingPassword() {
        if (shoppingPasswordSet)
            return shoppingPassword;
        return null;
    }

    String[] getBankingPassword() {
        if (bankingPasswordSet)
            return bankingPassword;
        return null;
    }

    void setEmailPassword() {
        int length = getRandomLength();
        emailPassword = new String[length];
        emailPassword = getRandomPassword(length);
        emailPasswordSet = true;
    }

    void setShoppingPassword() {
        int length = getRandomLength();
        shoppingPassword = new String[length];
        shoppingPassword = getRandomPassword(length);
        shoppingPasswordSet = true;
    }

    void setBankingPassword() {
        int length = getRandomLength();
        bankingPassword = new String[length];
        bankingPassword = getRandomPassword(length);
        bankingPasswordSet = true;
    }

    int getNumOfSuccesses() {
        return numOfSuccesses;
    }

    void incNumOfSuccesses() {
        numOfSuccesses++;
    }

    int getNumberOfFailures() {
        return numberOfFailures;
    }

    void incNumberOfFailures() {
        numberOfFailures++;
    }

    void resetNumberOfFailures() { numberOfFailures = 0; }


    /* This is how many screens we will iterate through (Max is 7, Min 5)  */
    private int getRandomLength() {
        return random.nextInt(4) + 4;
    }

    /* This is generating a random number of shape combinations for a given password length */
    private String[] getRandomPassword(int numOfShapes) {
        String[] randomPassword = new String[numOfShapes];
        for (int i = 0; i < numOfShapes; i++)
            randomPassword[i] = Shapes[random.nextInt(7)];
        return randomPassword;
    }


}
