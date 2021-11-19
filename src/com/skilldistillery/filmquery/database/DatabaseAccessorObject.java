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
			String sql = "";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet filmResult = stmt.executeQuery();
			if (filmResult.next()) {

			}
			filmResult.close();
			stmt.close();
			conn.close();
			return film;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
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
				actor.setId(actorResult.getInt(1));
				actor.setFirstName(actorResult.getString(2));
				actor.setLastName(actorResult.getString(3));

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

		List<Actor> films = new ArrayList<>();
		try {
	  Connection conn = DriverManager.getConnection(URL, USER, PASS);
    String sql = "SELECT actor.id, actor.first_name, actor.last_name ";
                sql += " rental_rate, length, replacement_cost, rating, special_features "
               +  " FROM film JOIN film_actor ON film.id = film_actor.film_id "
               + " WHERE film_id = ?";
    PreparedStatement stmt = conn.prepareStatement(sql);
    stmt.setInt(1, filmId);
    ResultSet rs = stmt.executeQuery();
    while (rs.next()) {
      int id = rs.getInt("id");
      String title = rs.getString("title");
      String desc = rs.getString("description");
      String releaseYear = rs.getString("release_year");
      int langId = rs.getInt("language_id");
      int rentDur = rs.getInt("rental_duration");
      double rate = rs.getDouble("rental_rate");
      int length = rs.getInt("length");
      double repCost = rs.getDouble("replacement_cost");
      String rating = rs.getString("rating");
      String features = rs.getString("special_features");
      Film film = new Film(id, title, desc, releaseYear, langId,
                           rentDur, rate, length, repCost, rating, features);
      films.add(film);
    }
    rs.close();
    stmt.close();
    conn.close();
  } catch (SQLException e) {
    e.printStackTrace();
  }
  return films;


	return null;
}

}
