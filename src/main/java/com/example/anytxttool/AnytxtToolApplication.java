package com.example.anytxttool;

import com.example.anytxttool.view.MainView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AnytxtToolApplication extends AbstractJavaFxApplicationSupport {

    private static final Logger logger = LoggerFactory.getLogger(AnytxtToolApplication.class);

    public static void main(String[] args) {
        launch(AnytxtToolApplication.class, MainView.class, new LaunchSplash(), args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);
    }

    public void relaunch() {
        Platform.runLater(() -> {
            getStage().close();
            try {
                this.stop();
                this.init();
                this.start(new Stage());
            } catch (Exception ex) {
                logger.error("{}", ex);
            }
        });
    }

}
