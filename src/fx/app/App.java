package fx.app;
import fx.view.Controller;
import fx.view.Song;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/fx/view/sample.fxml"));
        primaryStage.setTitle("Song Library");
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
        	
        	public void handle(WindowEvent event) {
        		try {
        			ObservableList<String> currSongs = Controller.getObsList();
        			ArrayList<Song> songs = Controller.getSongs();
            		File oldList = new File("src/Songs.txt");
            		if (oldList.exists()) {
            			oldList.delete();
            		}
            		if (!songs.isEmpty()) {
            			File newList = new File("src/Songs.txt");
                		FileWriter writer = new FileWriter(newList);
                		for (String curr : currSongs) {
                			writer.write(curr + "\n");
                		}
                		writer.close();
            		}
        		} catch (Exception e) {
        			System.out.println(e);
        		}
        	}
        	
        });
        try {
    		File input = new File("src/Songs.txt");
        	if (input.exists()) {
        		Scanner fileScan = new Scanner(input);
        		ObservableList<String> namesAndSongs = FXCollections.observableArrayList();
        		ArrayList<Song> songs = new ArrayList<Song>();
        		while (fileScan.hasNextLine()) {
        			String currLine = fileScan.nextLine();
        			namesAndSongs.add(currLine);
        			String[] details = currLine.split("\\s+");
        			Song newSong = new Song(details[0], details[1], Integer.parseInt(details[2]), details[3]);
        			songs.add(newSong);
        		}
        		Controller.setSongs(songs);
        		Controller.setObsList(namesAndSongs);
        		fileScan.close();
        	}
    	} catch (Exception e) {
    		System.out.println(e);
    	}
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}