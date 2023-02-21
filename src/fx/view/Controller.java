package fx.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

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

	@FXML
	private ListView<String> listView;
	@FXML
	private TextField songField;
	@FXML
	private TextField artistField;
	@FXML
	private TextField yearField;
	@FXML
	private TextField albumField;

	private static ObservableList<String> namesAndSongs = FXCollections.observableArrayList();

	private static ArrayList<Song> songs = new ArrayList<Song>();

	private static int selectedSongIndex = -1;

	private static boolean ran = false;

	public static void setSelectedSongIndex(int selectedSongIndex) {
		Controller.selectedSongIndex = selectedSongIndex;
	}

	public static int getSelectedSongIndex() {
		return selectedSongIndex;
	}

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
		for (Song s : songs) {
			if (s.equals(song))
				return true;
		}
		return false;
	}

	public void initialize() throws Exception {
		if (!ran) {
			boolean dataCorrupted = false;
			try {
				File input = new File("src/Songs.txt");
				if (input.exists()) {
					Scanner fileScan = new Scanner(input);

					ArrayList<String> lines = new ArrayList<>();

					while (fileScan.hasNextLine()) {
						String currLine = fileScan.nextLine();
						lines.add(currLine);
					}
					fileScan.close();
					dataCorrupted = lines.size() % 5 != 0;
					if (!dataCorrupted) {
						for (int i = 0; i < lines.size(); i += 5) {
							Song newSong = new Song(lines.get(i), lines.get(i + 1), Integer.parseInt(lines.get(i + 2)),
									lines.get(i + 3));
							namesAndSongs.add(newSong.toString());
							songs.add(newSong);
						}
					}
				}
			} catch (Exception e) {
				System.out.println(e);
			}
			if (dataCorrupted)
				throw new Exception("data corrupted");

			ran = true;
		}
		listView.setItems(namesAndSongs);
		listView.getSelectionModel().select(Controller.selectedSongIndex != -1 ? selectedSongIndex : 0);

		listView.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				int choice = listView.getSelectionModel().getSelectedIndex();
				if (choice == -1)
					return;
				Song clickedSong = songs.get(choice);
				SongViewController.setSong(clickedSong);
				Stage curr = (Stage) listView.getScene().getWindow();
				try {
					Parent root = FXMLLoader.load(getClass().getResource("/fx/view/SongView.fxml"));
					Scene newScene = new Scene(root);
					curr.setScene(newScene);
					curr.show();
				} catch (Exception e) {
				}
			}

		});
	}

	/**
	 * @param event
	 * 
	 *              Handles adding and deleting songs
	 * 
	 */
	@FXML
	public void handleAddSong(ActionEvent event) {
		Song newSong;
		try {
			newSong = new Song(songField.getText(), artistField.getText(), Integer.parseInt(yearField.getText()),
					albumField.getText());
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR,
					"All fields must not be empty and the year is a 4 digit number.",
					ButtonType.OK);
			alert.showAndWait();
			return;
		}

		if (contains(songs, newSong)) {
			Alert alert = new Alert(AlertType.ERROR,
					"Song already exists in playlist",
					ButtonType.OK);
			alert.showAndWait();
		} else {
			Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure?",
					ButtonType.YES, ButtonType.CANCEL);
			alert.showAndWait();

			if (alert.getResult() == ButtonType.YES) {
				// add to observable list so the GUI refreshes
				namesAndSongs.add(newSong.toString());
				songs.add(newSong);
				// sort in alphabetical order
				Collections.sort(namesAndSongs);
				Collections.sort(songs);

				listView.getSelectionModel().select(namesAndSongs.indexOf(newSong.toString()));

				int choice = namesAndSongs.indexOf(newSong.toString());
				Song clickedSong = songs.get(choice);
				SongViewController.setSong(clickedSong);
				Stage curr = (Stage) listView.getScene().getWindow();
				try {
					Parent root = FXMLLoader.load(getClass().getResource("/fx/view/SongView.fxml"));
					Scene newScene = new Scene(root);
					curr.setScene(newScene);
					curr.show();
				} catch (Exception e) {
				}

			} else {
				return;
			}
		}
	}
}