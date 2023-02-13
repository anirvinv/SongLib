package fx.view;
import java.util.ArrayList;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class Controller {
    
    @FXML private ListView<String> listView;    
    @FXML private TextField songField;
    @FXML private TextField artistField;
    @FXML private TextField yearField;
    @FXML private TextField albumField;
    private static ObservableList<String> namesAndSongs = FXCollections.observableArrayList();
    
    private static ArrayList<Song> songs = new ArrayList<>();
    
    public static ArrayList<Song> getSongs() {
    	return songs;
    }
    
    public static void setSongs(ArrayList<Song> newList) {
    	songs = newList;
    }
    
    public static ObservableList<String> getObsList() {
    	return namesAndSongs;
    }
    
    public static void setObsList(ObservableList<String> n) {
    	namesAndSongs = n;
    }
    
    public boolean contains(ArrayList<Song> songs, Song song) {
    	for(Song s: songs) {
    		if(s.equals(song)) return true;
    	}
    	return false;
    }
    
    public void initialize() {
    	// load in the data from a text file here
        listView.setItems(namesAndSongs);
        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
        	
        	@Override
        	public void handle(MouseEvent event) {
        		int choice = listView.getSelectionModel().getSelectedIndex();
        		Song clickedSong = songs.get(choice);
        		SongViewController.setSong(clickedSong);
        		Stage curr = (Stage) listView.getScene().getWindow();
        		try {
        			Parent root = FXMLLoader.load(getClass().getResource("/fx/view/SongView.fxml"));
        			Scene newScene = new Scene(root);
        			curr.setScene(newScene);
        			curr.show();
        		} catch (Exception e) {}
        	}
        	
        });
        if(namesAndSongs.size()>0) {
            listView.getSelectionModel().select(0);
        }
    }
    
    /**
     * @param event
     * 
     * Handles adding and deleting songs
     * 
     */
    @FXML
    public void handleAddSong(ActionEvent event) {
    	Song newSong;
    	try {
        	newSong = new Song(songField.getText(), artistField.getText(), Integer.parseInt(yearField.getText()), albumField.getText());
    	} catch(Exception e) {
    		Alert alert = new Alert(AlertType.ERROR, 
                    "All fields must not be empty and the year is a number.", 
                    ButtonType.OK);
        	alert.showAndWait();
    		return; 
    	}
    	
    	
    	if(contains(songs, newSong)) {
    		Alert alert = new Alert(AlertType.ERROR, 
                    "Song already exists in playlist", 
                    ButtonType.OK);
        	alert.showAndWait();
    	}
    	else {
    		songs.add(newSong);
    		// add to observable list so the GUI refreshes
    		namesAndSongs.add(newSong.toString());
        	// sort in alphabetical order
        	Collections.sort(namesAndSongs);
        	Collections.sort(songs);
        	
        	if(namesAndSongs.size()>0) {
                listView.getSelectionModel().select(0);
            }
    	}
    }
    
    
    @FXML
    public void handleEdit(ActionEvent event) {
    	for(Song s: songs) {
    		System.out.println(s);
    	}
    }
}