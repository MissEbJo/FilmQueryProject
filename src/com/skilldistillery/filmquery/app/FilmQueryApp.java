package com.skilldistillery.filmquery.app;

import java.util.List;
import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {
  
  DatabaseAccessor db = new DatabaseAccessorObject();

  public static void main(String[] args) {
    FilmQueryApp app = new FilmQueryApp();
//    app.test();
    app.launch();
  }

  private void test() {
    Film film = db.findFilmById(115);
    System.out.println(film);
//    Actor actor = db.findActorById(115);
//    System.out.println(actor);
    List<Actor> actors = db.findActorsByFilmId(127);
    System.out.println(actors);
  }

  private void launch() {
    Scanner input = new Scanner(System.in);
    
    startUserInterface(input);
    
    input.close();
  }

  private void startUserInterface(Scanner input) {
	  
	  int choice = 0;
	  boolean keepGoing = true;
	  while(keepGoing) {
	  printMenu();
	  choice =input.nextInt();
	  switch (choice) {
	case 1:
		
		System.out.println("Enter the film number: ");
		int number = input.nextInt();
		Film film = db.findFilmById(number);
		if(film == null) {
			System.out.println("There is not title matching your request! Please try with a different film number!");
		}else {
	    System.out.println(film);
		}
		break;
	case 2:
		System.out.println("Enter a word to search for your film: ");
		String theWordToSearchFor = input.next();
		List<Film> film2 = db.findFilmsByKeyword(theWordToSearchFor);
		if(film2 == null) {
			System.out.println("There is not title matching your request! Please try with a different film number!");
		}else {
	    System.out.println(film2);
		}
		
		break;
	case 3:
		System.out.println("Thank you for choosing FilmIt! Goodbye!");
		keepGoing = false;
		break;

	default:
		break;
	}
	  
	 
	  }
  }
  
  private void printMenu(){
	  System.out.println("************************************");
	  System.out.println("         Welcome to FilmIt          ");
	  System.out.println("************************************");
	  System.out.println("Please select from the following options(1-3):");
	  System.out.println("1) Search Film By Id");
	  System.out.println("2) Search Film By Keyword");
	  System.out.println("3) Exit");
	  System.out.println();
	  System.out.println("Enter selection: ");
	  
  }

}
