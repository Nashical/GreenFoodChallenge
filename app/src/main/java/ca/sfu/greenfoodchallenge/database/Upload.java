package ca.sfu.greenfoodchallenge.database;

import ca.sfu.greenfoodchallenge.ui.SecondMainActivity;

public class Upload {
    private String mName;
    private String mImageUrl;
    private String mKey;
    private String emailKey;

    public Upload() {
        //empty constructor needed
    }

    public Upload(String name, String imageUrl) {
        if (name.trim().equals("")) {
            name = "No name";
        }

        mName = name;
        mImageUrl = imageUrl;

        setEmailKey();
        emailKey = getEmailKey();
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public String getEmailKey() {
        return emailKey;
    }

    public void setEmailKey() {
        this.emailKey = GreenFoodChallengeDatabase.getcurrentUserEmailKey();
        System.out.println(emailKey);
    }
}
