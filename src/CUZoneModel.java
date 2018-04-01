import java.util.Random;
@SuppressWarnings("Duplicates")
public class CUZoneModel {
    private String[] Shapes = {"Circle", "Diamond", "Oval", "Parallelogram", "Pentagon", "Rectangle", "Square", "Triangle"};

    private Boolean emailPasswordSet;
    private String[] emailPassword;

    private Boolean shoppingPasswordSet;
    private String[] shoppingPassword;

    private Boolean bankingPasswordSet;
    private String[] bankingPassword;

    public CUZoneModel () {
        emailPasswordSet = false;
        shoppingPasswordSet = false;
        bankingPasswordSet = false;
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
        for (int i=0; i<numOfShapes; i++)
            randomPassword[i] = Shapes[rand.nextInt(7)];
        return randomPassword;
    }


}
