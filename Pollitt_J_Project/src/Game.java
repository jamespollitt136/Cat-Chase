import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * A simple mathematics learning game using the composition design pattern.
 * @author James Pollitt <J.Polllitt@edu.salford.ac.uk>
 * @version 1.0
 */
public class Game extends Application 
{
	//fields for style
	private Scene scene;
	private TabPane root;
	private Tab welcomeTab, gameTab, scoreTab;
	private Pane userInput, mathInput, scorePane, game;
	private Canvas canvas;
	private GraphicsContext graphicsContext;
	private TextField inputName, inputInitials, inputAge, inputNumber1, inputNumber2, sumField;
	private Label showNameLabel, showScoreLabel, operatorLabel;
	private Label nameScoreLabel, ageScoreLabel, scoreScoreLabel;
	private Button rollDiceButton, finishGameButton;
	private Image image;
	private AudioClip sound;
	//fields for game play
	private Player player;
	private static final int LIMIT = 3;
	private String playerName, playerInitials;
	private int playerAge, playerScore, sumOfDice;
	private Boolean correct, caught;
	private Random diceOne = new Random(); //dice
	private Random diceTwo = new Random(); //dice
	private ArrayList<GameObject> board = new ArrayList<GameObject>(); //game board
	private Factory factory = new Factory(); //use of the factory design pattern
	private final TableView scoreboard = new TableView();
	private ObservableList<Player> scoreData;
	private Database database = new Database();
	
	/**
	 * Launches application.
	 * @param args
	 */
	public static void main(String[] args) 
	{
		launch(args);
	}
	
	@SuppressWarnings("unchecked")
	public void start(Stage stage) throws Exception 
	{
	  	stage.setTitle("Software Architectures – James Pollitt");
	  	root = new TabPane();
	    scene = new Scene(root, 800, 600);
	  	
	  	game = new Pane();
	  	playerScore = 0;
	  	caught = false;
	  	
	  	//tabs
	  	welcomeTab = new Tab();
	  	welcomeTab.setText("Welcome");
	  	welcomeTab.setClosable(false);
	  	gameTab = new Tab();
	  	gameTab.setText("Game");
	  	gameTab.setClosable(false);
	  	scoreTab = new Tab();
	  	scoreTab.setText("Score");
	  	scoreTab.setClosable(false);
	  	root.getTabs().addAll(welcomeTab, gameTab, scoreTab);  	
	  	//welcome page
	  	userInput = new Pane();
		userInput.setPrefSize(800, 600);
		userInput.setStyle("-fx-background-color: #000040;");
		
		//creating an user interface for the welcome tab
		Label welcomeLabel = new Label();
		welcomeLabel.setText("Welcome to Cat-Chase");
		welcomeLabel.setLayoutX(275);
		welcomeLabel.setLayoutY(100);
		welcomeLabel.setFont(new Font("Rockwell", 18));
		welcomeLabel.setStyle("-fx-text-fill: #FFA500;");
		
		Label messageLabel = new Label();
		messageLabel.setText("The basic mathematics learning game for early learners!");
		messageLabel.setLayoutX(180);
		messageLabel.setLayoutY(130);
		messageLabel.setFont(new Font("Rockwell", 16));
		messageLabel.setStyle("-fx-text-fill: #ffffff;");
		
		Label playerNameLabel = new Label("Player Name:");
	  	playerNameLabel.setLayoutX(300);
	  	playerNameLabel.setLayoutY(190);
	  	playerNameLabel.setStyle("-fx-text-fill: #ffffff;");
	  	inputName = new TextField(); //where the user will input their name.
	  	inputName.setLayoutX(300);
	  	inputName.setLayoutY(210);
	  	
	  	Label initialsLabel = new Label("Abbreviation (ABC):");
	  	initialsLabel.setLayoutX(300);
	  	initialsLabel.setLayoutY(250);
	  	initialsLabel.setStyle("-fx-text-fill: #ffffff;");
	  	inputInitials = new TextField(); //where the user will input initials.
	  	inputInitials.setLayoutX(300);
	  	inputInitials.setLayoutY(270);
	  	
	  	Label playerAgeLabel = new Label("Age:");
	  	playerAgeLabel.setLayoutX(300);
	  	playerAgeLabel.setLayoutY(310);
	  	playerAgeLabel.setStyle("-fx-text-fill: #ffffff;");
	  	inputAge = new TextField(); //where the user will input age.
	  	inputAge.setLayoutX(300);
	  	inputAge.setLayoutY(330);
	  	
	  	Button submitDetailsButton = new Button("Submit Details");
	  	submitDetailsButton.setLayoutX(325);
	  	submitDetailsButton.setLayoutY(395);
	  	//event handler to pass the details to the fields upon clicking the submit button
	  	EventHandler<ActionEvent> detailsHandler = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) 
			{
				//check that the user is not skipping putting in their details
				if(inputName.getText().isEmpty() && inputInitials.getText().isEmpty())
				{
					Alert detailsWarning = new Alert(AlertType.WARNING); //dialog
					detailsWarning.setTitle("Cat-Chase: Missing Information");
					detailsWarning.setHeaderText(null);
					detailsWarning.setContentText("You must insert your name and abbreviation.");
					detailsWarning.showAndWait();
				}
				//check that the user is entering a 3 character abbreviation
				else if(inputInitials.getText().length() < LIMIT || inputInitials.getText().length() > LIMIT)
				{
					Alert initialsWarning = new Alert(AlertType.WARNING);
					initialsWarning.setTitle("Cat-Chase: Input Error");
					initialsWarning.setHeaderText(null);
					initialsWarning.setContentText("Your abbreviation must be 3 letters long.");
					initialsWarning.showAndWait();
				}
				//if the user has entered correct details
				else
				{
					playerName = inputName.getText();
					inputName.setEditable(false); //stop the user editing the text field
					playerInitials = inputInitials.getText();
					inputInitials.setEditable(false);
					playerAge = Integer.parseInt(inputAge.getText());
					inputAge.setEditable(false);
					root.getSelectionModel().select(gameTab); //change tab on button click
				}
				showNameLabel.setText("Player: " + playerName); //update the player name label on the game tab
			}  		
	  	};
	  	submitDetailsButton.setOnAction(detailsHandler);
	  	//adding elements to the userInput pane
	  	userInput.getChildren().addAll(welcomeLabel, inputName, playerNameLabel, inputInitials, 
	  			initialsLabel, playerAgeLabel, inputAge, submitDetailsButton, messageLabel);
		
		//creating an interface for the game tab
		mathInput = new Pane();
		mathInput.setStyle("-fx-background-color: #000040;");
		mathInput.setPrefSize(200, 600);
		mathInput.setLayoutX(600);
		
		canvas = new Canvas(800, 600);
		graphicsContext = canvas.getGraphicsContext2D();
		graphicsContext.setFill(Color.TRANSPARENT);
		graphicsContext.fillRect(0, 0, 800, 600);
		
		showNameLabel = new Label("Player: ");
	  	showNameLabel.setLayoutX(10);
	  	showNameLabel.setLayoutY(10);
	  	showNameLabel.setStyle("-fx-text-fill: #ffffff;");
	  	
	  	showScoreLabel = new Label("Score: 0");
	  	showScoreLabel.setLayoutX(10);
	  	showScoreLabel.setLayoutY(30);
	  	showScoreLabel.setStyle("-fx-text-fill: #ffffff;");
	  	
	  	Button instructionsButton = new Button("Instructions");
	  	instructionsButton.setLayoutX(10);
	  	instructionsButton.setLayoutY(70);
	  	EventHandler<ActionEvent> instructionsHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) 
			{
				if(sumField.getText().equals("") == true)
				{
					Alert dialogCorrect = new Alert(AlertType.INFORMATION);
					dialogCorrect.setTitle("Cat-Chase: Instructions");
					dialogCorrect.setHeaderText(null);
					dialogCorrect.setContentText("1: Click the 'Roll Dice!' button." + System.lineSeparator() 
							+ "2: Enter the sum of the two dice values and then submit your answer." 
							+ System.lineSeparator() + "3: Repeat until you catch the cat!" + System.lineSeparator() 
							+ "4: Click 'Finish Game' to end the game.");
					dialogCorrect.showAndWait();
				}
				else
				{
					checkAnswer();
				}
			}
	  	};
	  	instructionsButton.setOnAction(instructionsHandler);
	  	
		inputNumber1 = new TextField();
	  	inputNumber1.setLayoutX(10);
	  	inputNumber1.setLayoutY(130);
	  	inputNumber1.setEditable(false);
	  	
	  	operatorLabel = new Label("+");
	  	operatorLabel.setLayoutX(10);
	  	operatorLabel.setLayoutY(155);
	  	operatorLabel.setStyle("-fx-text-fill: #ffffff;");
	  	
	  	inputNumber2 = new TextField();
	  	inputNumber2.setLayoutX(10);
	  	inputNumber2.setLayoutY(180);
	  	inputNumber2.setEditable(false);
	  	
	  	Label equalLabel = new Label("=");
	  	equalLabel.setLayoutX(10);
	  	equalLabel.setLayoutY(220);
	  	equalLabel.setStyle("-fx-text-fill: #ffffff;");
	  	
	  	//answer section
	  	sumField = new TextField();
	  	sumField.setLayoutX(10);
	  	sumField.setLayoutY(250);
	  	
	    image = new Image(Game.class.getResource("res/square.png").toExternalForm());
	  	graphicsContext.drawImage(image, 200, 0);
	  	
	  	int x, y, i = 0;
	  	for(y = 0; y<500; y+=80)
	  	{
		  	for(x = 0; x<500; x+=80)
		  	{
		  		if(i == 8)
		  		{
		  			board.add(factory.createProduct("tennis", x, y, 5)); //add tennis ball image to board
		  		}
		  		else if(i == 27)
		  		{
		  			board.add(factory.createProduct("tennis", x, y, 3));
		  		}
		  		else if(i == 45)
		  		{
		  			board.add(factory.createProduct("tennis", x, y, 11));
		  		}
		  		else
		  		{
		  			board.add(new GameObject(x,y));
		  		}
		  		i++;
		  	}
	  	}
	  	board.add(factory.createProduct("player", 0, 0, 0)); //sets the dog starting point
	  	board.add(factory.createProduct("cat", 480, 83, 0)); //sets the cat starting point 

	  	for(GameObject player : board)
	  	{
	  		graphicsContext.drawImage(player.image, player.rectangle.getX(), player.rectangle.getY(), 
	  				player.rectangle.getWidth(), player.rectangle.getHeight());
	  	}
	  	
	  	//dice
	  	rollDiceButton = new Button("Roll Dice!");
	  	rollDiceButton.setLayoutX(10);
	  	rollDiceButton.setLayoutY(530);
	  	EventHandler<ActionEvent> diceHandler = new EventHandler<ActionEvent>() {
	  		public void handle(ActionEvent arg0)
	  		{
	  			playerRollDice();
	  		}
	  	};
	  	rollDiceButton.setOnAction(diceHandler);
	  	
	  	finishGameButton = new Button("Finish Game");
	  	finishGameButton.setLayoutX(100);
	  	finishGameButton.setLayoutY(530);
	  	EventHandler<ActionEvent> finishHandler = new EventHandler<ActionEvent>() {
	  		public void handle(ActionEvent arg0)
	  		{
	  			try 
	  			{
	  				Alert confirmEndDialog = new Alert(AlertType.CONFIRMATION);
	  				confirmEndDialog.setTitle("End Game?");
	  				confirmEndDialog.setContentText("Are you sure you wish to finish the game?");
	  				Optional<ButtonType> confirm = confirmEndDialog.showAndWait();
	  				if(confirm.get() == ButtonType.OK)
	  				{
	  					database.addToDatabase(playerName, playerInitials, playerAge, playerScore);
						database.showScoreBoard();
						nameScoreLabel.setText("Player: " + playerName);
						ageScoreLabel.setText("Age: " + playerAge);
						scoreScoreLabel.setText("Score: " + playerScore);
						root.getSelectionModel().select(scoreTab);
	  				}
				}
	  			catch (SQLException sqlException) 
	  			{
	  				sqlException.printStackTrace();
				}
	  			
	  		}
	  	};
	  	finishGameButton.setOnAction(finishHandler);
	  	
	    //submit math button
	  	Button submitMathButton = new Button("Submit Math");
	  	submitMathButton.setLayoutX(10);
	  	submitMathButton.setLayoutY(285);
	  	EventHandler<ActionEvent> sumHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) 
			{
				if(sumField.getText().equals("") == true)
				{
					Alert dialogCorrect = new Alert(AlertType.WARNING);
					dialogCorrect.setTitle("Cat-Chase: No Answer");
					dialogCorrect.setHeaderText(null);
					dialogCorrect.setContentText("No answer entered." + System.lineSeparator() 
												+ "Please try again.");
					dialogCorrect.showAndWait();
				}
				else
				{
					checkAnswer();
				}
			}
	  	};
	  	submitMathButton.setOnAction(sumHandler);
	  	
	  	mathInput.getChildren().addAll(showNameLabel, showScoreLabel, instructionsButton, inputNumber1, 
	  								   inputNumber2, sumField, equalLabel, operatorLabel, submitMathButton, 
	  								   rollDiceButton, finishGameButton);
	  	game.getChildren().addAll(canvas, mathInput);
	  	
	  	//score tab
	  	scorePane = new Pane();
	  	scorePane.setPrefSize(800, 600);
	  	scorePane.setStyle("-fx-background-color: #000040;");
	  	
	  	nameScoreLabel = new Label();
	  	nameScoreLabel.setLayoutX(500);
	  	nameScoreLabel.setLayoutY(10);
	  	nameScoreLabel.setStyle("-fx-text-fill: #ffffff;");
	  	
	  	ageScoreLabel = new Label();
	  	ageScoreLabel.setLayoutX(500);
	  	ageScoreLabel.setLayoutY(30);
	  	ageScoreLabel.setStyle("-fx-text-fill: #ffffff;");
	  	
	  	scoreScoreLabel = new Label();
	  	scoreScoreLabel.setLayoutX(500);
	  	scoreScoreLabel.setLayoutY(50);
	  	scoreScoreLabel.setStyle("-fx-text-fill: #ffffff;");
	  	
	  	scoreboard.setEditable(false);
	  	
	  	Label highScoreLabel = new Label("High Scores");
	  	highScoreLabel.setLayoutX(75);
	  	highScoreLabel.setLayoutY(10);
	  	highScoreLabel.setStyle("-fx-text-fill: #ffffff");
	  	highScoreLabel.setFont(new Font("Rockwell", 20));
	  	scoreboard.setLayoutX(10);
	  	scoreboard.setLayoutY(50);
	  	
	  	TableColumn rankColumn = new TableColumn("RANK");
	  	TableColumn initialsColumn = new TableColumn("PLAYER");
	  	TableColumn scoreColumn = new TableColumn("SCORE");
	  	
	  	//rankColumn.setCellValueFactory(new PropertyValueFactory<Database, int>(database.getColumns()));
	  	//initialsColumn.setCellValueFactory(new PropertyValueFactory<Database, String>(database.getInitials()));
	  	
	  	scoreboard.getColumns().addAll(rankColumn, initialsColumn, scoreColumn);
	  	
	  	Label creditLabel = new Label("Credits: 0");
	  	creditLabel.setLayoutX(10);
	  	creditLabel.setLayoutY(530);
	  	creditLabel.setStyle("-fx-text-fill: #FFA500;");
	  	creditLabel.setFont(new Font("Rockwell", 14));
	  	
	  	scorePane.getChildren().addAll(highScoreLabel, nameScoreLabel, ageScoreLabel, scoreScoreLabel, 
	  			scoreboard, creditLabel);
		//adding content to the tabs
		welcomeTab.setContent(userInput);
		gameTab.setContent(game);
		scoreTab.setContent(scorePane);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Display the player's name.
	 * @return Name of player
	 */
	public String getPlayerName()
	{
		return playerName;
	}
	
	/**
	 * Display the player's abbreviation/initials.
	 * @return Chosen abbreviation of player
	 */
	public String getPlayerInitials()
	{
		return playerInitials;
	}
	
	/**
	 * Display the player's age.
	 * @return Age of player
	 */
	public int getPlayerAge()
	{
		return playerAge;
	}
	
	/**
	 * Set player details in this class and in the Player class.
	 * @param name
	 * @param initials
	 */
	public void setDetails(String name, String initials, int age, int score)
	{
		this.playerName = name;
		this.playerInitials = initials;
		this.playerAge = age;
		this.playerScore = score;
		player.setDetails(name, initials, age, score);
	}
	
	/**
	 * Display the player's score.
	 * @return Player's score
	 */
	public int getScore()
	{
		return playerScore;
	}
	
	/**
	 * Show whether the correct variable is set to true or false.
	 * @return True or false depending on user answer
	 */
	public boolean getCorrect()
	{
		if(correct == true)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Check whether the user's input in the sum field is correct or not. 
	 * If it is not correct display dialogs informing the user and deduct points from the score accordingly.
	 * If it is correct play a sound and add points to the score.
	 */
	public void checkAnswer()
	{
		for(GameObject p : board)
		{
			if(p instanceof Player)
			{
				if(sumField.getText() != null)
				{
					int userSum = Integer.parseInt(sumField.getText());
					if(userSum == sumOfDice) //correct
					{
						sound = new AudioClip(Game.class.getResource("res/correct.wav").toExternalForm());
						sound.play(); //plays the above audio file
						playerScore += 5; //add 5 to the score
						
						Alert dialogCorrect = new Alert(AlertType.INFORMATION); //dialog
						dialogCorrect.setTitle("Cat-Chase: Correct Answer");
						dialogCorrect.setHeaderText(null);
						dialogCorrect.setContentText("Correct." + System.lineSeparator() + "5 points earned.");
						dialogCorrect.showAndWait();
						
						correct = true;
						showScoreLabel.setText("Score: " + playerScore);
						inputNumber1.setText("");
						inputNumber2.setText("");
						sumField.setText("");
						Player player = (Player)p;
						player.move(sumOfDice);
						if(board.get(player.getPosition()) instanceof Tennisball)
						{
							Tennisball tennis = (Tennisball)board.get(player.getPosition());
							
							Alert moveDialog = new Alert(AlertType.INFORMATION);
							moveDialog.setTitle("Cat-Chase: Distraction");
							moveDialog.setHeaderText(null);
							moveDialog.setContentText("Distracted by tennis ball, moving to " 
													 + tennis.moveToPosition + ".");
							moveDialog.showAndWait();		
							
							player.moveTo(tennis.moveToPosition);
						}
						if(board.get(player.getPosition()) instanceof Cat)
						{
							sound = new AudioClip(Game.class.getResource("res/chomp.wav").toExternalForm());
							sound.play();
							Alert caughtDialog = new Alert(AlertType.INFORMATION);
							caughtDialog.setTitle("Cat-Chase: Caught The Cat");
							caughtDialog.setHeaderText(null);
							caughtDialog.setContentText("Congratulations, you have caught the cat!");
							caughtDialog.showAndWait();
							caught = true;
						}
						graphicsContext.drawImage(p.image, p.rectangle.getX(), 
			  					p.rectangle.getY(), p.rectangle.getHeight(), 
			  					p.rectangle.getWidth());
						//make the cat move
						if(caught != true)
						{
							for(GameObject c : board)
							{
								if(c instanceof Cat)
								{
									if(caught == false)
									{
										int firstDice = diceOne.nextInt(4) + 1;
										int secondDice = diceTwo.nextInt(4) + 1;
										int catDice = firstDice + secondDice;
										
										Alert diceAlert = new Alert(AlertType.INFORMATION);
										diceAlert.setTitle("Cat Throw");
										diceAlert.setHeaderText(null);
										diceAlert.setContentText("Dice 1: " + firstDice 
												+ System.lineSeparator() + "Dice 2: " + secondDice);
										diceAlert.showAndWait();
										
										Cat cat = (Cat)c;
										cat.move(catDice);
									}
								}
								graphicsContext.drawImage(c.image, c.rectangle.getX(), 
					  					c.rectangle.getY(), c.rectangle.getHeight(), 
					  					c.rectangle.getWidth());
							}
						}
					}
					else //incorrect
					{
						sound = new AudioClip(Game.class.getResource("res/incorrect.wav").toExternalForm());
						sound.play(); //plays the above audio file
						playerScore -= 3; //deduct 3 from the score
						
						Alert warningSum = new Alert(AlertType.WARNING); //dialog
						warningSum.setTitle("Cat-Chase: Wrong Answer");
						warningSum.setHeaderText(null);
						warningSum.setContentText("3 points deducted." 
												  + System.lineSeparator() 
												  + "Please try again.");
						warningSum.showAndWait();
						
						correct = false;
						
						if(playerScore < 0) //if score is below 0 set it to 0
						{
							showScoreLabel.setText("Score: 0");
							playerScore = 0;
						}
						else
						{
							showScoreLabel.setText("Score: " + playerScore); //update the score label
						}
					}
				}
				else
				{
					Alert missingWarning = new Alert(AlertType.WARNING); //dialog
					missingWarning.setTitle("Cat-Chase: Missing Fields");
					missingWarning.setHeaderText(null);
					missingWarning.setContentText("Please fill in the sum field.");
					missingWarning.showAndWait();
				}
			}
		}
	}
	
	public void playerRollDice()
	{
		for(GameObject p : board)
		{
			if(p instanceof Player)
			{
				int firstDice = diceOne.nextInt(6) + 1;
				int secondDice = diceTwo.nextInt(6) + 1;
				sumOfDice = firstDice + secondDice;
				String diceValueOne = firstDice+"";
				String diceValueTwo = secondDice+"";
					
				Alert diceAlert = new Alert(AlertType.INFORMATION);
				diceAlert.setTitle("Dice Throw");
				diceAlert.setHeaderText(null);
				diceAlert.setContentText("Dice 1: " + firstDice 
										+ System.lineSeparator() + "Dice 2: " + secondDice);
				inputNumber1.setText(diceValueOne);
				inputNumber2.setText(diceValueTwo);
				diceAlert.showAndWait();
			}
		}
	}
}