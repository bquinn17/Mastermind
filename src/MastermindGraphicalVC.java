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
    private Rectangle[][] guesses;
    private ArrayList<Rectangle> answer;
    private Circle[][] hints;
    private Button peek;

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

        answer = new ArrayList<>(MastermindModel.CODE_LENGTH);
        guesses = new Rectangle[MastermindModel.CODE_LENGTH][MastermindModel.MAX_GUESSES];
        hints = new Circle[MastermindModel.CODE_LENGTH][MastermindModel.MAX_GUESSES];
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        message = new Label(getMessage());
        message.setPadding(new Insets(20,20,20,20));

        GridPane hintsHolder = new GridPane();
        hintsHolder.setPadding(new Insets(68,10,10,0));
        hintsHolder.setHgap(2);
        hintsHolder.setVgap(35);

        for (int col = 0; col < MastermindModel.CODE_LENGTH ; col++) {
            hintsHolder.addColumn(col);
            for (int row = 0; row < MastermindModel.MAX_GUESSES ; row++){
                Circle circle = new Circle(10, Color.GRAY);
                hintsHolder.add(circle,col,row);
                hints[col][row] = circle;
            }
        }

        HBox answerHolder = new HBox();

        answerHolder.setSpacing(5);
        for (int k = 0; k < MastermindModel.CODE_LENGTH ; k++) {
            Rectangle rect = new Rectangle(50, 50, Color.GRAY);
            answer.add(rect);
            answerHolder.getChildren().add(k,rect);
        }

        GridPane guessesHolder = new GridPane();
        guessesHolder.setHgap(5);
        guessesHolder.setVgap(5);
        for (int col = 0; col < MastermindModel.CODE_LENGTH ; col++) {
            guessesHolder.addColumn(col);
            for (int row = 0; row < MastermindModel.MAX_GUESSES; row++) {
                Rectangle rect = new Rectangle(50, 50, Color.GRAY);
                int finalRow = model.getRemainingGuesses() - row;
                int finalCol = col + 1;
                rect.setOnMouseClicked(event -> {
                    model.choose(finalRow, finalCol);
                });
                guessesHolder.add(rect,col,row);
                guesses[col][row] = rect;
            }
        }

        VBox center = new VBox();
        center.setSpacing(5);
        center.getChildren().addAll(answerHolder,guessesHolder);
        Button newGame = new Button("New Game");
        newGame.setOnAction(event -> newGame());

        peek = new Button("Peek");
        peek.setOnAction(event -> {
            if (peek.getText().equals("Peek")){
                model.peek();
                peek.setText("Hide");
                showAnswer(true);
            } else {
                model.peek();
                peek.setText("Peek");
                showAnswer(false);
            }
        });

        Button guess = new Button("Guess");
        guess.setOnAction(event -> checkGuess());

        VBox buttons = new VBox();
        buttons.getChildren().addAll(newGame,peek,guess);
        buttons.setSpacing(20);
        buttons.setPadding(new Insets(10,10,10,10));

        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10,10,10,10));
        borderPane.setTop(message);
        borderPane.setCenter(center);
        borderPane.setLeft(hintsHolder);
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
        ArrayList<Character> clueData = model.getClueData();
        for (int k = 0; k < clueData.size() ; k++) {
            int curRow = model.getRemainingGuesses();
            if (clueData.get(k) == 'B') {
                hints[k][curRow].setFill(Color.BLACK);
            } else if (clueData.get(k) == 'W'){
                hints[k][curRow].setFill(Color.WHITE);
            }
        }
    }

    private void showAnswer(boolean b) {
        if (b){
            ArrayList solution = model.getSolution();
            for (int i = 0; i < solution.size(); i++) {
                answer.get(i).setFill(colors.get((int)solution.get(i)));
            }
        } else {
            ArrayList solution = model.getSolution();
            for (int i = 0; i < solution.size(); i++) {
                answer.get(i).setFill(Color.GRAY);
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        message.setText(getMessage());
        if (model.getVictoryStatus()) {
            peek.setText("Hide");
            showAnswer(true);
        } else if (model.getRemainingGuesses() == 0){
            peek.setText("Hide");
            showAnswer(true);
        } else {
            for (int col = 0; col < MastermindModel.CODE_LENGTH; col++) {
                int curRow = model.getRemainingGuesses() - 1;
                guesses[col][curRow].setFill(colors.get(model.getGuessData().get(col)));
            }
        }
    }

    private void newGame() {
        model.reset();
        //answer, guesses, hits
        for (int i = 0; i < MastermindModel.CODE_LENGTH; i++) {
            for (int j = 0; j < MastermindModel.MAX_GUESSES; j++) {
                guesses[i][j].setFill(Color.GRAY);
                hints[i][j].setFill(Color.GRAY);

            }
        }
        peek.setText("Peek");
        showAnswer(false);
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
    }
}
