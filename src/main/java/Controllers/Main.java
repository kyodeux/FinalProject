package Controllers;

import Model.CircularQueue;
import Model.User;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main extends Application {
    private static Stage stage;
    private static Scene currentProgramUI;
    private static Program_Handler programHandler;
    public static final CircularQueue userList = new CircularQueue();
    public static final JSONObject userCatalog = new JSONObject();
    
    private static void saveUsers(){
            File usersFile = new File(System.getProperty("user.dir") + "\\users.txt");
            boolean fileExists = true;
            
            if (!usersFile.exists()){
                try{
                    fileExists = usersFile.createNewFile();
                }catch(IOException e){}      
            }
            
            if (!fileExists){return;}
            JSONArray usersJSON = userList.toJSON();
            
            try{
                PrintWriter pen;
                try (FileWriter writer = new FileWriter(usersFile)) {
                    pen = new PrintWriter(writer);
                    pen.write(usersJSON.toString(4));
                    pen.close();
                }
            }catch(IOException e){}
    }
    
    public static void loadUsers(){
        File backup = new File(System.getProperty("user.dir") + "\\users.txt");
        if (!backup.exists()){return;}
        
        StringBuilder builder = new StringBuilder();
        
        try{
            FileReader reader = new FileReader(backup);
            BufferedReader buff = new BufferedReader(reader);
            
            try{
                String currentLine = buff.readLine();
                while(currentLine != null){
                    builder.append(currentLine);
                    currentLine = buff.readLine();
                }
                
            }catch(IOException ex){}
        }catch(FileNotFoundException e){}
        
        JSONArray usersJSON = new JSONArray(builder.toString());
        Iterator<Object> iterator = usersJSON.iterator();
        
        while (iterator.hasNext()){
            User currentUser = new User((JSONObject) iterator.next());
            userList.push(currentUser);
            userCatalog.put(currentUser.getUserName(), currentUser);
        }
    }
    
    @Override
    public void start(Stage stage) {
        Main.stage = stage;
        
        try {
            Parent login_signup = FXMLLoader.load(getClass().getResource("/View/login_signup.fxml"));            
            Scene scene = new Scene(login_signup);
           
            scene.setFill(Color.TRANSPARENT);
            stage.setResizable(false);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {}
    }
    public static void setupProgram(User user){
        try{
            Program_Handler controller = new Program_Handler();
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/View/main.fxml"));
            loader.setController(controller);
            controller.setup(user);
            
            Parent decodedFXML = loader.load();
            Scene scene = new Scene(decodedFXML);
            scene.setFill(Color.TRANSPARENT);
            
            programHandler = controller;
            currentProgramUI = scene;
        }catch (IOException e){}  
    }
    public static void openProgram(){
        if (currentProgramUI == null){return;}
        double previusWidth = stage.getWidth();
        double previusHeight = stage.getHeight();
        stage.setScene(currentProgramUI);
        stage.setX(stage.getX() + previusWidth / 2 - stage.getWidth() / 2);
        stage.setY(stage.getY() + previusHeight / 2 - stage.getHeight() / 2);
        programHandler.init();
        programHandler.playWelcomeAnimation();
    }
    public static void backToLogin(){
        try{
            Parent login_signup = FXMLLoader.load(Main.class.getResource("/View/login_signup.fxml"));            
            Scene scene = new Scene(login_signup);
            scene.setFill(Color.TRANSPARENT);
            
            double previusWidth = stage.getWidth();
            double previusHeight = stage.getHeight();
            
            stage.setScene(scene);
            
            stage.setX(stage.getX() + previusWidth / 2 - stage.getWidth() / 2);
            stage.setY(stage.getY() + previusHeight / 2 - stage.getHeight()/ 2);
        }catch(IOException e){}   
    }
    public static Stage getStage(){
        return stage;
    }
    public static void main(String[] args) {
        loadUsers();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            saveUsers();
        }));
        launch(args);
    }
}
