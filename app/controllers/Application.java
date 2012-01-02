package controllers;

import models.Grid;
import play.mvc.Controller;

public class Application extends Controller {

	public static void index() {

		Grid grid = new Grid(15, 11);
		grid.generateRandomBlackCells(20);

		render(grid);
	}
}