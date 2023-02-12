package fx.view;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class Controller {
    
    @FXML private ListView<String> listView;    
    @FXML private TextField songField;
    @FXML private TextField artistField;
    @FXML private TextField yearField;
    @FXML private TextField albumField;
    private ObservableList<String> namesAndSongs = FXCollections.observableArrayList();
    
    ArrayList<Song> songs = new ArrayList<>();
    
    public boolean contains(ArrayList<Song> songs, Song song) {
    	for(Song s: songs) {
    		if(s.equals(song)) return true;
    	}
    	return false;
    }
    
    public void initialize() {
    	// load in the data from a text file here
        listView.setItems(namesAndSongs);
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