package fx.view;

import java.util.ArrayList;
import java.util.Collections;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

public class SongViewController {

	/*
	 * 
	 * Variables, getters, setters
	 * 
	 */

	@FXML
	private TextArea name;
	@FXML
	private TextArea artist;
	@FXML
	private TextArea album;
	@FXML
	private TextArea year;
	@FXML
	private TextField newName;
	@FXML
	private TextField newArtist;
	@FXML
	private TextField newAlbum;
	@FXML
	private TextField newYear;
	@FXML
	private Button edit;
	@FXML
	private Button confirm;
	@FXML
	private Button cancel;
	@FXML
	private Button back;
	@FXML
	private Button deleteButton;

	private static Song selectedSong;

	public static void setSong(Song s) {
		selectedSong = s;
	}

	public static Song getSong() {
		return selectedSong;
	}

	/*
	 * 
	 * Startup
	 * 
	 */

	public void initialize() {
		// Use the selected song to fill in the initial text areas
		name.setText(selectedSong.getName());
		artist.setText(selectedSong.getArtist());
		album.setText(selectedSong.getAlbum());
		year.setText(Integer.toString(selectedSong.getYear()));
	}

	/*
	 * 
	 * Event handling
	 * 
	 */

	@FXML
	public void handleBack(ActionEvent event) {
		Stage curr = (Stage) back.getScene().getWindow();
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/fx/view/sample.fxml"));
			Scene newScene = new Scene(root);
			curr.setScene(newScene);
			curr.show();
		} catch (Exception e) {
		}
	}

	@FXML
	public void handleEdit(ActionEvent event) {
		// Change the scene to edit mode
		name.setVisible(false);
		artist.setVisible(false);
		album.setVisible(false);
		year.setVisible(false);
		newName.setVisible(true);
		newArtist.setVisible(true);
		newAlbum.setVisible(true);
		newYear.setVisible(true);
		edit.setVisible(false);
		confirm.setVisible(true);
		cancel.setVisible(true);
		back.setVisible(false);

		// default edit values will be the existing name, artist, year, album
		newName.setText(name.getText());
		newArtist.setText(artist.getText());
		newYear.setText(year.getText());
		newAlbum.setText(album.getText());

	}

	@FXML
	public void handleCancel(ActionEvent event) {
		// Change scene back to presentation mode
		newName.clear();
		newArtist.clear();
		newYear.clear();
		newAlbum.clear();
		name.setVisible(true);
		artist.setVisible(true);
		album.setVisible(true);
		year.setVisible(true);
		newName.setVisible(false);
		newArtist.setVisible(false);
		newAlbum.setVisible(false);
		newYear.setVisible(false);
		edit.setVisible(true);
		confirm.setVisible(false);
		cancel.setVisible(false);
		back.setVisible(true);
	}

	@FXML
	public void handleConfirm(ActionEvent event) {
		/*
		 * 
		 * bug: if you press edit, and then confirm without making changes, it doesn't
		 * work
		 * 
		 */

		// Grab song list and changed values, set error booleans
		ArrayList<Song> songs = Controller.getSongs();
		ObservableList<String> songsAndNames = Controller.getObsList();
		String changedName = newName.getText();
		String changedArtist = newArtist.getText();
		String changedAlbum = newAlbum.getText();
		String changedYear = newYear.getText();
		boolean nameError = false;
		boolean yearError = false;
		// Check to see if song already exists
		if (!(changedName.isEmpty() && changedArtist.isEmpty())) {
			if (changedName.isEmpty()) {
				changedName = selectedSong.getName();
			}
			if (changedArtist.isEmpty()) {
				changedArtist = selectedSong.getArtist();
			}
			// FIX: check to see that there is only one occurrence of name,artist in the
			// song arraylist

			for (Song curr : songs) {
				if (curr.getName().equals(changedName) && curr.getArtist().equals(changedArtist)) {
					nameError = true;
				}
			}
		}
		// Check if valid year
		if (!(changedYear.isEmpty())) {
			if (changedYear.length() == 4) {
				for (int i = 0; i < 4; i++) {
					if (!(changedYear.charAt(i) >= 48 && changedYear.charAt(i) <= 57)) {
						yearError = true;
					}
				}
			} else {
				yearError = true;
			}
		}
		// Send appropriate error or update
		if (nameError && yearError) {
			Alert alert = new Alert(AlertType.ERROR, "Song already exists in playlist, year must be 4 digit number",
					ButtonType.OK);
			alert.showAndWait();
		} else if (nameError) {
			Alert alert = new Alert(AlertType.ERROR, "Song already exists in playlist", ButtonType.OK);
			alert.showAndWait();
		} else if (yearError) {
			Alert alert = new Alert(AlertType.ERROR, "Year must be 4 digit number", ButtonType.OK);
			alert.showAndWait();
		} else {
			if (changedName.isEmpty()) {
				changedName = selectedSong.getName();
			}
			if (changedArtist.isEmpty()) {
				changedArtist = selectedSong.getArtist();
			}
			int changedYear2;
			if (changedYear.isEmpty()) {
				changedYear2 = selectedSong.getYear();
			} else {
				changedYear2 = Integer.parseInt(changedYear);
			}
			if (changedAlbum.isEmpty()) {
				changedAlbum = selectedSong.getAlbum();
			}
			Song newSong = new Song(changedName, changedArtist, changedYear2, changedAlbum);
			songs.remove(selectedSong);
			songs.add(newSong);
			songsAndNames.remove(selectedSong.toString());
			songsAndNames.add(newSong.toString());
			Collections.sort(songs);
			Collections.sort(songsAndNames);
			setSong(newSong);
			Controller.setSongs(songs);
			Controller.setObsList(songsAndNames);
		}
		// Reconfigure scene
		name.setText(selectedSong.getName());
		artist.setText(selectedSong.getArtist());
		album.setText(selectedSong.getAlbum());
		year.setText(Integer.toString(selectedSong.getYear()));
		newName.clear();
		newArtist.clear();
		newYear.clear();
		newAlbum.clear();
		name.setVisible(true);
		artist.setVisible(true);
		album.setVisible(true);
		year.setVisible(true);
		newName.setVisible(false);
		newArtist.setVisible(false);
		newAlbum.setVisible(false);
		newYear.setVisible(false);
		edit.setVisible(true);
		confirm.setVisible(false);
		cancel.setVisible(false);
		back.setVisible(true);
	}

	@FXML
	public void handleDelete(ActionEvent event) {
		// Needs to be tested, but this probably works

		ArrayList<Song> songs = Controller.getSongs();
		ObservableList<String> songsAndNames = Controller.getObsList();
		songsAndNames.remove(selectedSong.toString());
		songs.remove(selectedSong);

		Stage curr = (Stage) back.getScene().getWindow();
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/fx/view/sample.fxml"));
			Scene newScene = new Scene(root);
			curr.setScene(newScene);
			curr.show();
		} catch (Exception e) {
		}
	}

}