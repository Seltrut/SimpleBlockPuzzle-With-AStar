import java.util.*;

public class node{

	public gameState g;
	public ArrayList<gameState.move> history;

	public node(){
		g = new gameState();
		history = new ArrayList<gameState.move>();
	}	

	public node(gameState game , ArrayList<gameState.move> h ){
		g = game;
		history = h;
	}

	public void addMove( gameState.move m ){
		history.add( m );
	}

	public ArrayList<gameState.move> howDidIGetHere(){
		return history;
	}
}
