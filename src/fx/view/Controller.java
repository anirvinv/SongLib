package fx.view;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

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
    
    @FXML
    private ListView<String> listView;    

    @FXML
    private TextField songName;
    @FXML
    private TextField artistName;    

    private ObservableList<String> namesAndSongs = FXCollections.observableArrayList();
    
    private HashMap<String, ArrayList<String>> artistToSong = new HashMap<>();
    
    private boolean containsArtistAndSong(String artist, String song) {
    	return artistToSong.containsKey(artistName.getText()) && artistToSong.get(artistName.getText()).contains(songName.getText());
    }
    
    public void initialize() {
    	// load in the data from a text file here
        listView.setItems(namesAndSongs);
        if(namesAndSongs.size()>0) {
            listView.getSelectionModel().select(0);
        }
    }
    
    public static String formatEntry(String song, String artist) {
    	return String.format("%-30s %-30s", song, artist);
    }
    
    /**
     * @param event
     * 
     * Handles adding and deleting songs
     * 
     */
    @FXML
    public void handleAddSong(ActionEvent event) {
//    	System.out.println(event.getSource());
//    	System.out.println(artistName.getText());
//    	System.out.println(songName.getText());
    	if(containsArtistAndSong(artistName.getText(), songName.getText())) {
    		Alert alert = new Alert(AlertType.ERROR, 
                    "Song already exists in playlist", 
                    ButtonType.OK);
        	alert.showAndWait();
    	}
    	else {
    		ArrayList<String> songs = artistToSong.get(artistName.getText())==null?new ArrayList<>():artistToSong.get(artistName.getText());
    		songs.add(songName.getText());
    		artistToSong.put(artistName.getText(), songs);
    		// add to observable list so the GUI refreshes
    		namesAndSongs.add(formatEntry(songName.getText(), artistName.getText()));
        	// sort in alphabetical order
        	Collections.sort(namesAndSongs);
        	
        	if(namesAndSongs.size()>0) {
                listView.getSelectionModel().select(0);
            }
    	}
    }
    
   
    
    
    @FXML
    public void handleEdit(ActionEvent event) {
    	int selected = listView.getSelectionModel().getSelectedIndex();
//    	System.out.println(selected);
    	if(selected == -1) {
    		Alert alert = new Alert(AlertType.WARNING, "Please select a song to edit", ButtonType.OK);
    		alert.showAndWait();
    	}else {
    		if(containsArtistAndSong(artistName.getText(), songName.getText())) {
        		// display alert
            	Alert error = new Alert(AlertType.ERROR, 
                        "Song already exists in playlist", 
                        ButtonType.OK);
            	error.showAndWait();
        	
        	}
    		else {
//    			names.set(selected, artistName.getText());
//            	songs.set(selected, songName.getText());
    			
    			ArrayList<String> songs = artistToSong.get(artistName.getText())==null?new ArrayList<>():artistToSong.get(artistName.getText());
    	    	songs.add(songName.getText());
    			artistToSong.put(artistName.getText(), songs);
    			namesAndSongs.set(selected, formatEntry(songName.getText(), artistName.getText()));
            	Collections.sort(namesAndSongs);
            	if(namesAndSongs.size()>0) {
                    listView.getSelectionModel().select(0);
                }
    		}
    	}
    }
    
}