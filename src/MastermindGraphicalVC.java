import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Bryan Quinn
 * file: MastermindGraphicalVC.java
 *
 */
public class MastermindGraphicalVC extends Application implements Observer{

    private MastermindModel model;

    private ArrayList<Color> colors;
    private Label message;
    private GridPane guesses;
    private HBox answer;
    private GridPane hints;

    @Override
    public void init(){
        this.model = new MastermindModel();
        this.model.addObserver(this);
        this.colors = new ArrayList<>(7);
        this.colors.add(Color.GRAY);
        this.colors.add(Color.BLACK);
        this.colors.add(Color.WHITE);
        this.colors.add(Color.BLUE);
        this.colors.add(Color.YELLOW);
        this.colors.add(Color.RED);
        this.colors.add(Color.GREEN);


    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        BorderPane borderPane = new BorderPane();
        message = new Label(getMessage());
        message.setPadding(new Insets(20,20,20,20));

        hints = new GridPane();
        hints.setPadding(new Insets(68,10,10,0));
        hints.setHgap(2);
        hints.setVgap(35);

        for (int col = 0; col < MastermindModel.CODE_LENGTH ; col++) {
            hints.addColumn(col);
            for (int row = 0; row < MastermindModel.MAX_GUESSES ; row++) {
                if (row > col) {
                    hints.add(new Circle(10, Color.GRAY),col,row);
                } else {
                    hints.addRow(row);
                    hints.add(new Circle(10, Color.GRAY),col,row);
                }

            }

        }

        answer = new HBox();
        answer.setSpacing(5);
        for (int k = 0; k < MastermindModel.CODE_LENGTH ; k++) {
            Rectangle rect = new Rectangle(50, 50, Color.GRAY);
            answer.getChildren().add(k,rect);
        }

        guesses = new GridPane();
        guesses.setHgap(5);
        guesses.setVgap(5);
        for (int col = 0; col < MastermindModel.CODE_LENGTH ; col++) {
            guesses.addColumn(col);
            for (int row = 0; row < MastermindModel.MAX_GUESSES; row++) {
                    Rectangle rect = new Rectangle(50, 50, Color.GRAY);
                    rect.setOnMouseClicked(event -> rect.setFill(nextColor((Color)rect.getFill())));
                    guesses.add(rect,col,row);
            }
        }

        VBox center = new VBox();
        center.setSpacing(5);
        center.getChildren().addAll(answer,guesses);

        VBox buttons = new VBox();
        Button newGame = new Button("New Game");
        newGame.setOnAction(event -> newGame());
        Button peek = new Button("Peek");
        peek.setOnAction(event -> {
            if (peek.getText().equals("Peek")){
                model.peek();
                peek.setText("Hide");
            } else {
                model.peek();
                peek.setText("Peek");
            }
        });
        Button guess = new Button("Guess");
        guess.setOnAction(event -> checkGuess());

        buttons.getChildren().addAll(newGame,peek,guess);
        buttons.setSpacing(20);
        buttons.setPadding(new Insets(10,10,10,10));

        borderPane.setPadding(new Insets(10,10,10,10));
        borderPane.setTop(message);
        borderPane.setCenter(center);
        borderPane.setLeft(hints);
        borderPane.setRight(buttons);

        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Mastermind");

        primaryStage.setMinHeight(Screen.getPrimary().getVisualBounds().getHeight() * (2));
        primaryStage.setMinWidth(Screen.getPrimary().getVisualBounds().getWidth() * (.75));
        primaryStage.setResizable(false);

        primaryStage.show();

    }

    private void checkGuess() {
        model.makeGuess();
        message.setText(getMessage());
    }

    private Color nextColor(Color fill) {
        int i = colors.indexOf(fill);
        if (i + 1 < colors.size()){
            return colors.get(++i);
        } else {
            return colors.get(0);
        }
        /*if (fill.equals(Color.GREY)){
            return Color.BLACK;
        } else if (fill.equals(Color.BLACK)){
            return Color.WHITE;
        } else if (fill.equals(Color.WHITE)){
            return Color.BLUE;
        } else if (fill.equals(Color.BLUE)){
            return Color.YELLOW;
        } else if (fill.equals(Color.YELLOW)){
            return Color.RED;
        } else if (fill.equals(Color.RED)){
            return Color.GREEN;
        } else if (fill.equals(Color.GREEN)){
            return Color.BLACK;
        } else {
            return Color.GRAY;
        }*/
    }

    private void showAnswer(boolean b) {
        model.peek();
    }

    @Override
    public void update(Observable o, Object arg) {
        getMessage();

    }

    private void newGame(){

    }

    private void Guess(){

    }

    /**
     * updates a single component of a guess. This type of modification corresponds to a user clicking on a single
     * component of a guess to cycle through the possible choices for that component.
     */
    private void choose(){

    }

    private String getMessage(){
        if (this.model.getVictoryStatus()) {
            return "You won the game!!";
        } else if (this.model.getRemainingGuesses() == 0) {
            return "You lost the game!!";
        } else {
            return "You have " + this.model.getRemainingGuesses() + " guesses remaining.";
        }
    }


    public static void main(String[] args) {
        Application.launch(args);
        /*MastermindGraphicalVC game = new MastermindGraphicalVC();
        Stage stage = new Stage(Application.Parameters);
        game.start(game);*/

    }
}
