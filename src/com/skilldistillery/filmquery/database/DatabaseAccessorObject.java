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
			String sql = "SELECT DISTINCT film.id, film.title"
					+ " FROM film JOIN film_actor ON film.id = film_actor.film_id WHERE film_id = ?;";
			PreparedStatement stmt = conn.prepareStatement(sql);
//			System.out.println(stmt);
			stmt.setInt(1, filmId);
			ResultSet filmResult = stmt.executeQuery();
			if (filmResult.next()) {
				film = new Film();
				film.setId(filmResult.getInt("film.id"));
				film.setTitle(filmResult.getString("film.title"));
				film.setActors(findActorsByFilmId(filmId));

			}
			filmResult.close();
			stmt.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return film;
	}

	@Override
	public Actor findActorById(int actorId) {
		Actor actor = null;
		try {
			Connection conn = DriverManager.getConnection(URL, USER, PASS);
			String sql = "SELECT id, first_name, last_name FROM actor WHERE id = ?;";
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
		try {
			Connection conn = DriverManager.getConnection(URL, USER, PASS);
			String sql = "SELECT actor.first_name, actor.last_name ";
			sql += " FROM actor JOIN film_actor ON actor.id = film_actor.actor_id "
					+ "JOIN film ON film_actor.film_id = film.id " + " WHERE film.id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String firstName = rs.getString("actor.first_name");
				String lastName = rs.getString("actor.last_name");

				Actor actor = new Actor(firstName, lastName);
				actors.add(actor);
			}
			rs.close();
			stmt.close();
			conn.close();
			return actors;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

}
