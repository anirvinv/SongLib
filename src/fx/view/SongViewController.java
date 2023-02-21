// Sean Read and Anirvin Vaddiyar

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
	private TextField newName;
	@FXML
	private TextField newArtist;
	@FXML
	private TextField newAlbum;
	@FXML
	private TextField newYear;
	@FXML
	private Button confirm;
	@FXML
	private Button cancel;

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

		newName.setText(selectedSong.getName());
		newArtist.setText(selectedSong.getArtist());
		newYear.setText(selectedSong.getYear() == -1 ? "" : selectedSong.getYear() + "");
		newAlbum.setText(selectedSong.getAlbum());

		return;
	}

	/*
	 * 
	 * Event handling
	 * 
	 */

	@FXML
	public void handleCancel(ActionEvent event) {
		// Send back to main scene
		Stage curr = (Stage) cancel.getScene().getWindow();
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/fx/view/MainView.fxml"));
			Scene newScene = new Scene(root);
			curr.setScene(newScene);
			curr.show();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@FXML
	public void handleConfirm(ActionEvent event) {
		// Grab song list and changed values, set error booleans
		ArrayList<Song> songs = Controller.getSongs();
		ObservableList<String> songsAndNames = Controller.getObsList();
		String changedName = newName.getText();
		String changedArtist = newArtist.getText();
		String changedAlbum = newAlbum.getText();
		String changedYear = newYear.getText();
		boolean nameError = false;
		boolean yearError = false;
		Song selectedSongClone = new Song(selectedSong.getName(), selectedSong.getArtist(), selectedSong.getYear(),
				selectedSong.getAlbum());
		// Check to see if song already exists
		if (!(changedName.isEmpty() && changedArtist.isEmpty())) {
			if (changedName.isEmpty()) {
				changedName = selectedSong.getName();
			}
			if (changedArtist.isEmpty()) {
				changedArtist = selectedSong.getArtist();
			}
			// BUG: can't press edit and then confirm without changing the name or artist
			// FIXED: we can remove the current song before even checking to see if the new
			// song exists. Add it back if the confirmation causes an error so we don't lose
			// data

			songs.remove(selectedSong);
			songsAndNames.remove(selectedSong.toString());

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
			songs.add(selectedSongClone);
			songsAndNames.add(selectedSongClone.toString());
			Collections.sort(songs);
			Collections.sort(songsAndNames);
			Alert alert = new Alert(AlertType.ERROR, "Song already exists in playlist, year must be 4 digit number",
					ButtonType.OK);
			alert.showAndWait();
		} else if (nameError) {
			songs.add(selectedSongClone);
			songsAndNames.add(selectedSongClone.toString());
			Collections.sort(songs);
			Collections.sort(songsAndNames);
			Alert alert = new Alert(AlertType.ERROR, "Song already exists in playlist", ButtonType.OK);
			alert.showAndWait();
		} else if (yearError) {
			songs.add(selectedSongClone);
			songsAndNames.add(selectedSongClone.toString());
			Collections.sort(songs);
			Collections.sort(songsAndNames);
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

			songs.add(newSong);
			songsAndNames.add(newSong.toString());
			Collections.sort(songs);
			Collections.sort(songsAndNames);
			setSong(newSong);
			Controller.setSongs(songs);
			Controller.setObsList(songsAndNames);

			Controller.setSelectedSongIndex(songs.indexOf(newSong));
		}
		// Send back to main scene
		Stage curr = (Stage) confirm.getScene().getWindow();
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/fx/view/MainView.fxml"));
			Scene newScene = new Scene(root);
			curr.setScene(newScene);
			curr.show();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}