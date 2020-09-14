package ics.co.ke.businessaccount;
import android.app.Application;
import android.graphics.Typeface;

import com.facebook.stetho.Stetho;

import ics.co.ke.businessaccount.model.User;
import ics.co.ke.businessaccount.settings.Settings;
import ics.co.ke.businessaccount.ui.widgets.TypeFactory;

public class BUZNESAPI extends Application {
    private static BUZNESAPI mInstance;
    public Settings settings;
    private TypeFactory mFontFactory;

    public static synchronized BUZNESAPI getApp() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        settings = new Settings(getApplicationContext());

        Stetho.initializeWithDefaults(this);

        mInstance = this;
    }

    public Typeface getTypeFace(int type) {
        if (mFontFactory == null)
            mFontFactory = new TypeFactory(this);

        switch (type) {
            case Constants.REGULAR:
                return mFontFactory.getRegular();

            case Constants.BOLD:
                return mFontFactory.getBold();

            default:
                return mFontFactory.getRegular();
        }
    }

    public interface Constants {
        int REGULAR = 1,
                BOLD = 2;
    }

    private User user;



    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }





}
