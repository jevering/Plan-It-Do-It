package application;
	
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


public class Main extends Application {
	
	Stage window;
	Scene scene1, scene2;
	Control controller;
	
	@Override
	public void start(Stage primaryStage) {
		window = primaryStage;
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("UserInterface.fxml"));
			Parent root = (Parent)loader.load();
			controller = (Control)loader.getController();
			controller.primaryStage = window;
					
			window.setTitle("Plan It Do It");
			Scene scene2 = new Scene(root,400,400);
			
			GridPane grid = new GridPane();
			grid.setAlignment(Pos.CENTER);
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(25, 25, 25, 25));

			Scene scene1 = new Scene(grid, 400, 400);
			window.setScene(scene1);
			Text scenetitle = new Text("Welcome");
			scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
			grid.add(scenetitle, 0, 0, 2, 1);

			Label userName = new Label("User Name:");
			grid.add(userName, 0, 1);
			
			// Get users name from here
			TextField userTextField = new TextField();
			grid.add(userTextField, 1, 1);
			
			Button btn = new Button("Sign in");
			HBox hbBtn = new HBox(10);
			hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
			hbBtn.getChildren().add(btn);
			grid.add(hbBtn, 1, 4);
			btn.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					window.setScene(scene2);
					controller.user = new User(userTextField.getText());
					controller.usernameLabel.setText(userTextField.getText());
				}
				
			});
			//btn.setOnAction(e -> window.setScene(scene2));
			
			scene2.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			window.setScene(scene1);
			window.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
