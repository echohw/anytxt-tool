package com.example.anytxttool;

import com.example.anytxttool.view.MainView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AnytxtToolApplication extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {
        launch(AnytxtToolApplication.class, MainView.class, new LaunchSplashScreen(), args);
    }
}
