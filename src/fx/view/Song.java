package fx.view;

public class Song implements Comparable<Song> {

	private String name;
	private String artist;
	private int year;
	private String album;

	/**
	 * @param name
	 * @param artist
	 * @param year
	 * @param album
	 */
	public Song(String name, String artist, int year, String album) {

		if (name.length() == 0 || artist.length() == 0 || album.length() == 0 || (year < 1000 || year > 9999)) {
			throw new IllegalArgumentException();
		}

		this.name = name.trim();
		this.artist = artist.trim();
		this.year = year;
		this.album = album;
	}

	@Override
	public int compareTo(Song other) {
		if (name.toLowerCase().compareTo(other.getName().toLowerCase()) == 0) {
			return artist.toLowerCase().compareTo(other.getArtist().toLowerCase());
		}
		return name.toLowerCase().compareTo(other.getName().toLowerCase());
	}

	public boolean equals(Song other) {
		return this.compareTo(other) == 0;
	}

	public String toString() {
		return String.format("%-30s %-30s", name, artist);
	}

	public String toFileString() {
		return String.format("%s\n%s\n%s\n%s\n\n", name, artist, year, album);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}
}
