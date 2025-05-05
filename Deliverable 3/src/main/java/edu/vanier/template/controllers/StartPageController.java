package edu.vanier.template.controllers;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import edu.vanier.template.ui.MainApp;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import javafx.application.Platform;
import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * FXML controller  for the start page.
 *
 * @author Joseph Josue Forestal
 */
public class StartPageController {
    @FXML
    private Label labelTitle;
    @FXML
    private Button buttonStart;
    @FXML
    private Button buttonExit;
    
    @FXML
    private Button logInButton;
    
    @FXML 
    private Button signUpButton;

    @FXML
    private Button signUpSubmit;
    
    @FXML
    private Button logInSubmit;
    
    @FXML
    private Button backButtonLogIn;
    
    @FXML
    private Button backButtonSignUp;
    
    @FXML
    private VBox logInWindow;
    
    @FXML
    private VBox signUpWindow;
    
    @FXML
    private TextField logInName;
    
    @FXML
    private PasswordField logInPassword;
    
    @FXML
    private TextField signUpName;
    
    @FXML
    private PasswordField signUpPassword;
    
    @FXML
    private Label guideLabel;
            
    @FXML
    private Label messageLabel;
            
    String username;
    String password;
    boolean loggedIn = false;
    
    @FXML
    public void initialize() {
        buttonStart.setOnAction(event -> handleStartButton());
        buttonExit.setOnAction(event -> handleExitButton());
        logInButton.setOnAction(event -> handleLogInButton());
        signUpButton.setOnAction(event -> handleSignUpButton());
        
        backButtonSignUp.setOnAction(event -> handleSignUpBack());
        backButtonLogIn.setOnAction(event -> handleLogInBack());
        
        signUpSubmit.setOnAction(event -> handleSignUpSubmit());
        logInSubmit.setOnAction(event -> handleLogInSubmit());
    }
    
    public void handleSignUpSubmit(){
        String name = signUpName.getText();
        String password = signUpPassword.getText();
        
        try{
            File file = new File("src/main/resources/save/users.csv");
            FileWriter outputFile = new FileWriter(file);         
            try (CSVWriter writer = new CSVWriter(outputFile)) {
                String[] userData = {name, password};
                
                writer.writeNext(userData);
            }
            signUpName.clear();
            signUpPassword.clear();
            logInButton.setDisable(false);
            signUpButton.setDisable(false);
            signUpWindow.toBack();
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
        
    }
    
    public void handleLogInSubmit(){
        String enteredName = logInName.getText();
        String enteredPassword = logInPassword.getText();

        System.out.println(enteredName + enteredPassword);
        try (FileInputStream stream = new FileInputStream("src/main/resources/save/users.csv");
            InputStreamReader isReader = new InputStreamReader(stream);
            CSVReader csvReader = new CSVReaderBuilder(isReader).build();) {
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                String name = line[0];
                String password = line[1];

                if(name.equals(enteredName) && password.equals(enteredPassword)){
                    username = enteredName;
                    this.password = enteredPassword;
                    loggedIn=true;
                    
                    guideLabel.setText("Welcome, " + username);
                    logInName.clear();
                    logInPassword.clear();
                    buttonStart.setDisable(false);
                    logInButton.setDisable(false);
                    signUpButton.setDisable(false);
                    logInWindow.toBack();
                    break;
                }
            }
            if(!loggedIn){
                messageLabel.setText("Your username or password\n is wrong, please try again.");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
    }
    }
    
    public void handleLogInButton(){
        logInButton.setDisable(true);
        signUpButton.setDisable(true);
        logInWindow.toFront();
    }
    public void handleSignUpButton(){
        logInButton.setDisable(true);
        signUpButton.setDisable(true);
        signUpWindow.toFront();
    }
    
    public void handleSignUpBack(){
        logInButton.setDisable(false);
        signUpButton.setDisable(false);
        signUpWindow.toBack();

    }
    
    public void handleLogInBack(){
        logInButton.setDisable(false);
        signUpButton.setDisable(false);
        logInWindow.toBack();
    }
    
    public void handleStartButton(){
        MainApp.switchScene(MainApp.TEMPLATE_SELECTION_LAYOUT);
    }

    public void handleExitButton() {
        Platform.exit();
    }

}

