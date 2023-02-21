// Sean Read and Anirvin Vaddiyar

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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
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
	@FXML
	private Label nameLabel;
	@FXML
	private Label artistLabel;
	@FXML
	private Label yearLabel;
	@FXML
	private Label albumLabel;
	@FXML
	private Button editButton;
	@FXML
	private Button deleteButton;

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

	public static String displayYear(int year) {
		if (year == -1)
			return "";
		else
			return year + "";
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
				nameLabel.setText("Name: " + clickedSong.getName());
				artistLabel.setText("Artist: " + clickedSong.getArtist());
				albumLabel.setText("Album: " + clickedSong.getAlbum());
				yearLabel.setText("Year: " + displayYear(clickedSong.getYear()));
			}

		});
		if (!songs.isEmpty()) {
			SongViewController.setSong(songs.get(0));
			nameLabel.setText("Name: " + songs.get(0).getName());
			artistLabel.setText("Artist: " + songs.get(0).getArtist());
			albumLabel.setText("Album: " + songs.get(0).getAlbum());
			yearLabel.setText("Year: " + displayYear(songs.get(0).getYear()));
			editButton.setVisible(true);
			deleteButton.setVisible(true);
		}
		if (namesAndSongs.size() > 0) {
			listView.getSelectionModel().select(0);
		}
	}

	/**
	 * @param event
	 * 
	 *              Handles adding, editing, and deleting songs
	 * 
	 */
	@FXML
	public void handleAddSong(ActionEvent event) {
		Song newSong;
		try {
			newSong = new Song(songField.getText(), artistField.getText(),
					yearField.getText().equals("") ? -1 : Integer.parseInt(yearField.getText()),
					albumField.getText());
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR,
					"Name and artist are required. If specified, year must be a 4 digit number",
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
			} else {
				return;
			}
		}
		if (songs.size() == 1) {
			SongViewController.setSong(songs.get(0));
			nameLabel.setText("Name: " + songs.get(0).getName());
			artistLabel.setText("Artist: " + songs.get(0).getArtist());
			albumLabel.setText("Album: " + songs.get(0).getAlbum());
			yearLabel.setText("Year: " + displayYear(songs.get(0).getYear()));
			editButton.setVisible(true);
			deleteButton.setVisible(true);
		} else if (songs.size() > 1) {
			SongViewController.setSong(newSong);
			nameLabel.setText("Name: " + newSong.getName());
			artistLabel.setText("Artist: " + newSong.getArtist());
			albumLabel.setText("Album: " + newSong.getAlbum());
			yearLabel.setText("Year: " + displayYear(newSong.getYear()));
		}
	}

	@FXML
	public void handleEdit(ActionEvent event) {
		Stage curr = (Stage) listView.getScene().getWindow();
		Controller.selectedSongIndex = listView.getSelectionModel().getSelectedIndex();
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/fx/view/SongView.fxml"));
			Scene newScene = new Scene(root);
			curr.setScene(newScene);
			curr.show();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@FXML
	public void handleDelete(ActionEvent event) {
		// Needs to be tested, but this probably works
		Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure?",
				ButtonType.YES, ButtonType.CANCEL);
		alert.showAndWait();
		int songIndex = 0;
		if (alert.getResult() == ButtonType.YES) {
			Song selectedSong = SongViewController.getSong();
			songIndex = songs.lastIndexOf(selectedSong);
			namesAndSongs.remove(selectedSong.toString());
			songs.remove(selectedSong);
			if (songs.isEmpty()) {
				nameLabel.setText("Name: ");
				artistLabel.setText("Artist: ");
				albumLabel.setText("Album: ");
				yearLabel.setText("Year: ");
				editButton.setVisible(false);
				deleteButton.setVisible(false);
			} else {
				if (songIndex == songs.size()) {
					songIndex--;
					selectedSong = songs.get(songIndex);
				} else {
					selectedSong = songs.get(songIndex);
				}
				SongViewController.setSong(selectedSong);
				listView.getSelectionModel().select(songIndex);

				nameLabel.setText("Name: " + selectedSong.getName());
				artistLabel.setText("Artist: " + selectedSong.getArtist());
				albumLabel.setText("Album: " + selectedSong.getAlbum());
				yearLabel.setText("Year: " + displayYear(selectedSong.getYear()));
			}
		}

	}
}