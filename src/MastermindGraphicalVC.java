import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.Observable;
import java.util.Observer;

/**
 * @author Bryan Quinn
 * file: MastermindGraphicalVC.java
 *
 */
public class MastermindGraphicalVC extends Application implements Observer{

    private MastermindModel model;

    @Override
    public void init(){
        this.model = new MastermindModel();
        this.model.addObserver(this);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        BorderPane borderPane = new BorderPane();

        primaryStage.setScene(new Scene(borderPane));
        primaryStage.setTitle("Mastermind");

        primaryStage.setResizable(false);
        primaryStage.show();

    }

    @Override
    public void update(Observable o, Object arg) {

    }

    /**
     * updates a single component of a guess. This type of modification corresponds to a user clicking on a single
     * component of a guess to cycle through the possible choices for that component.
     */
    private void choose(){

    }


    public static void main(String[] args) {
        Application.launch(args);
        /*MastermindGraphicalVC game = new MastermindGraphicalVC();
        Stage stage = new Stage(Application.Parameters);
        game.start(game);*/

    }
}
