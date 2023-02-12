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
		
		if(name.length()==0||artist.length()==0 || album.length()==0) {
			throw new IllegalArgumentException();
		}
		
		this.name = name;
		this.artist = artist;
		this.year = year;
		this.album = album;
	}

	@Override
	public int compareTo(Song other) {
		if(name.compareTo(other.getName()) == 0) {
			return artist.compareTo(other.getArtist());
		}
		return name.compareTo(other.getName());
	}
	
	public boolean equals(Song other) {
		return this.compareTo(other)==0;
	}
	
	public String toString() {
		return String.format("%-30s %-30s %-30d %-30s", name, artist, year, album);
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
