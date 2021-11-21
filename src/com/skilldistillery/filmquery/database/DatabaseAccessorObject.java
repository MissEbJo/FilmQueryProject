package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class DatabaseAccessorObject implements DatabaseAccessor {
//connection object goes here
	private static final String URL = "jdbc:mysql://localhost:3306/sdvid?useSSL=false";
	private static final String USER = "student";
	private static final String PASS = "student";

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Film findFilmById(int filmId) {

		Film film = null;

		try {
			Connection conn = DriverManager.getConnection(URL, USER, PASS);
			String sql = "SELECT * FROM film JOIN language ON film.language_id = language.id WHERE film.id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet filmResult = stmt.executeQuery();

			if (filmResult.next()) {
				film = new Film();
				film.setTitle(filmResult.getString("film.title"));
				film.setReleaseYear(filmResult.getString("release_year"));
				film.setDescription(filmResult.getString("description"));
				film.setRating(filmResult.getString("rating"));
				film.setLanguageId(filmResult.getString("language.name"));
				film.setActors(findActorsByFilmId(filmId));

			}
			filmResult.close();
			stmt.close();
			conn.close();

		} catch (SQLException e) {
			System.out.println("Invalid input. Please try again.");
			e.printStackTrace();
		}
		return film;
	}

	@Override
	public Actor findActorById(int actorId) {
		Actor actor = null;
		try {
			Connection conn = DriverManager.getConnection(URL, USER, PASS);
			String sql = "SELECT id, first_name, last_name FROM actor WHERE id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, actorId);
			ResultSet actorResult = stmt.executeQuery();
			if (actorResult.next()) {
				actor = new Actor(); // Create the object
				// Here is our mapping of query columns to our object fields:
				actor.setId(actorResult.getInt("id"));
				actor.setFirstName(actorResult.getString("first_name"));
				actor.setLastName(actorResult.getString("last_name"));

			}
			actorResult.close();
			stmt.close();
			conn.close();
			return actor;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public List<Actor> findActorsByFilmId(int filmId) {

		List<Actor> actors = new ArrayList<>();
		Actor actor = null;
		String sql = "SELECT actor.first_name, actor.last_name ";
		sql += " FROM actor JOIN film_actor ON actor.id = film_actor.actor_id "
				+ "JOIN film ON film_actor.film_id = film.id " + " WHERE film.id = ?";
		try {
			Connection conn = DriverManager.getConnection(URL, USER, PASS);
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				actor = new Actor();
				actor.setFirstName(rs.getString("actor.first_name"));
				actor.setLastName(rs.getString("actor.last_name"));

				actors.add(actor);
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return actors;
	}

	public List<Film> findFilmsByKeyword(String theWordToSearchFor) {
		List<Film> films = new ArrayList<>();
		Film film = null;
		String sql = "SELECT * FROM film JOIN language ON film.language_id = language.id WHERE title LIKE ? OR description LIKE ?";

		try {
			Connection conn = DriverManager.getConnection(URL, USER, PASS);
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, "%" + theWordToSearchFor + "%");
			stmt.setString(2, "%" + theWordToSearchFor + "%");
			ResultSet filmResult = stmt.executeQuery();

			while (filmResult.next()) {
				film = new Film();
				film.setId(filmResult.getInt("film.id"));
				film.setTitle(filmResult.getString("film.title"));
				film.setReleaseYear(filmResult.getString("release_year"));
				film.setDescription(filmResult.getString("description"));
				film.setRating(filmResult.getString("rating"));
				film.setLanguageId(filmResult.getString("language.name"));
				film.setActors(findActorsByFilmId(film.getId()));

				films.add(film);

			}

			filmResult.close();
			stmt.close();
			conn.close();

		} catch (SQLException e) {
			System.out.println("Invalid input. Please try again.");
			e.printStackTrace();
		}
		return films;

	}

}