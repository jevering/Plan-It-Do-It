package application;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.scene.chart.*;

import javafx.stage.Stage;

public class Control {
	Stage primaryStage;
	User user;
	
	@FXML TextField usernameField;
	@FXML Label usernameLabel;
	@FXML VBox taskListVBox;
	
	@FXML
	public void clickLogin() {
		user = new User(usernameField.getText());
		usernameLabel.setText(usernameField.getText());
		usernameField.setPromptText("username");
		updateTaskList();
	}
	
	@FXML
	public void clickNewTask() {
		Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        VBox dialogVbox = new VBox(20);
        
        Label newTaskLabel = new Label("New Task");
        newTaskLabel.setFont(Font.font ("Verdana", 20));
        TextField newTaskName = new TextField();
        newTaskName.setPromptText("Task Name");
        newTaskName.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				createNewTask(newTaskName.getText());
				dialog.close();
				updateTaskList();
			}
        });
        Button newTaskButton = new Button("Create");
        newTaskButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				createNewTask(newTaskName.getText());
				dialog.close();
				updateTaskList();
			}
        	
        });
        dialogVbox.getChildren().addAll(newTaskLabel, newTaskName, newTaskButton);
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();
	}
	
	private void createNewTask(String name) {
		Task task = new Task(name);
		user.newTask(task);
	}
	
	public void updateTaskList() {
		user.update();
		taskListVBox.getChildren().clear();
		Iterator<Task> tasks = user.getActiveTasks().iterator();
		while (tasks.hasNext()) {
			Task task = tasks.next();
			BorderPane taskPane = new BorderPane();
			Button finishTask = new Button("Finish Task");
			finishTask.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					user.finishTask(task);
					updateTaskList();
				}
				
			});
			Label taskName = new Label(task.getName());
			Button editTask = new Button("Edit Task");
			editTask.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					editTask(task);
					updateTaskList();
				}
				
			});
			
			
			taskPane.setLeft(finishTask);
			taskPane.setCenter(taskName);
			taskPane.setRight(editTask);
			taskPane.setPadding(new Insets(8,8,8,8));
			
			taskListVBox.getChildren().add(taskPane);
		}
	}
	
	private void editTask(Task task) {
		Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        VBox dialogVbox = new VBox(20);
        
        Label taskLabel = new Label(task.getName());
        taskLabel.setFont(Font.font ("Verdana", 20));
        TextArea description = new TextArea();
        description.setPromptText("Description...");
        description.setText(task.getDetails());
        
        ToggleGroup group = new ToggleGroup();

        RadioButton pLow = new RadioButton("Priority Low");
        pLow.setToggleGroup(group);
        pLow.setSelected(task.getPriority() == Task.LOW);

        RadioButton pMedium = new RadioButton("Priority Medium");
        pMedium.setToggleGroup(group);
        pMedium.setSelected(task.getPriority() == Task.MEDIUM);
         
        RadioButton pHigh = new RadioButton("Priority High");
        pHigh.setToggleGroup(group);
        pHigh.setSelected(task.getPriority() == Task.HIGH);
        
        TextField category = new TextField();
        category.setPromptText("Category");
        category.setText(task.getCategory());
        
        HBox dueDateBox = new HBox();
        dueDateBox.setPadding(new Insets(2,2,2,2));
        Label dateLabel = new Label("Deadline");
        TextField monthField = new TextField();
        monthField.setPrefWidth(36);
        monthField.setPromptText("MM");
        Label slash1 = new Label("/");
        TextField dayField = new TextField();
        dayField.setPrefWidth(36);
        dayField.setPromptText("DD");
        Label slash2 = new Label("/");
        TextField yearField = new TextField();
        yearField.setPrefWidth(48);
        yearField.setPromptText("YYYY");
        
        if (task.getDeadline() != null) {
            monthField.setText(task.getDeadline().getMonthValue()+"");
            dayField.setText(task.getDeadline().getDayOfMonth()+"");
            yearField.setText(task.getDeadline().getYear()+"");
        }
        
        dueDateBox.getChildren().addAll(dateLabel, monthField, slash1, dayField, slash2, yearField);
        
        
        Button edit = new Button("Done");
        edit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (description.getText() != null) {
					task.setDetails(description.getText());
				}
				
				if (pLow.isSelected()) {
					task.setPriority(Task.LOW);
				} else if (pMedium.isSelected()) {
					task.setPriority(Task.MEDIUM);
				} else {
					task.setPriority(Task.HIGH);
				}
				
				if (category.getText() != null) {
					task.setCategory(category.getText());
				}
				
				if (monthField.getText() != null && dayField.getText() != null && yearField.getText() != null) {
					try {
						int month = Integer.parseInt(monthField.getText());
						int day = Integer.parseInt(dayField.getText());
						int year = Integer.parseInt(yearField.getText());
						LocalDate deadline = LocalDate.of(year, month, day);
						task.setDeadline(deadline);
					} catch (Exception e) {
						// unable to parse the date
					}
				}
				
				dialog.close();
				user.resort(task);
				updateTaskList();
			}
        	
        });
        dialogVbox.getChildren().addAll(taskLabel, description, pLow, pMedium, pHigh, category, dueDateBox, edit);
        Scene dialogScene = new Scene(dialogVbox, 300, 600);
        dialog.setScene(dialogScene);
        dialog.show();
	}
	
	/**
	 * This function provides a bar graph of the amount of tasks completed the past seven days
	 * The X-Axis is how many days prior, with the max being seven days
	 * The Y-Axis is the number of tasks completed
	 */
	
	public void getBarGraph() {
		int i = 0;
		ArrayList<Integer> workDist = (ArrayList<Integer>) user.getWorkHistory(2);
		
		Stage dialog = new Stage();
		CategoryAxis xAxis = new CategoryAxis();  
	      xAxis.setCategories(FXCollections.<String>
	      observableArrayList(Arrays.asList("Yesterday", "2 Days Ago", "3 Days Ago", "4 Days Ago", "5 Days Ago", "6 Days Ago", "7 Days Ago")));
	      xAxis.setLabel("category");
	      
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> bc = new BarChart<>(xAxis,yAxis);
        bc.setTitle("Tasks Completed Over the Past Seven Days");
        xAxis.setLabel("Past Days");
        yAxis.setLabel("Tasks Completed");
        
        
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Past 7 Days");
        for (i = 0; i < workDist.size(); i++) {
        	series1.getData().add(new XYChart.Data<>(Integer.toString(i), workDist.get(i)));
        }
        
        bc.getData().add(series1);
        VBox vbox = new VBox(bc);
        Scene scene = new Scene(vbox, 700, 400);
        
        dialog.setScene(scene);
        dialog.show();
		
	}
	
	/**
	 * This is just an example of what the pie chart would look like, given more time
	 * It would show the different categories of the completed tasks and the 
	 * distribution between them
	 */
	public void getPieChart() {
		
//		ArrayList<Integer> workDist = (ArrayList<Integer>) user.getWorkDistribution(user.getCategories());
		Stage dialog = new Stage();
		ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                new PieChart.Data("Homework", 5),
                new PieChart.Data("Exercise", 10),
                new PieChart.Data("Get Groceries", 3),
                new PieChart.Data("Grab Mail", 6));
        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Task Completed in Different Categories");


        Group root = new Group(chart); 
        Scene scene = new Scene(root, 600, 500);
        
        dialog.setScene(scene);
        dialog.show();
	}
	
	
}
