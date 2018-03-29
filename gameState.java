import java.util.*;
import java.io.File;

public class gameState{
	
	/*****************************
	* ENUM for direction to move *
	*****************************/
	private enum Direction{
			UP,
			DOWN,
			LEFT,
			RIGHT,
			NONE;
	}

	public int width, height;
	public int[][] board;
	public ArrayList<move> avail;
	public boolean solved;
	private point p1, p2;
	public ArrayList<move> moves;
	public int mDist;

	/***************
	* Constructors *
	***************/
	public gameState(){
		solved	=	false;
		width	=	0;
		height	=	0;
		board	=	null;
		avail	=	new ArrayList<move>();
		moves	=	new ArrayList<move>();
		mDist	=	0;
	}

	public gameState(int w, int h, int[][] b){
		solved	=	false;
		width	=	w;
		height	=	h;
		board	=	new int[h][w];
		for( int i = 0; i < h; i++){
			for(int j = 0; j < w; j++){
				board[i][j] = 0;
				int curr = new Integer(b[i][j]);
				board[i][j] = board[i][j] + curr;
			}
		}
		moves	=	new ArrayList<move>();
		mDist	=	0;
	}

	/**********************
	* End of constructors *
	**********************/
	public boolean getSolved(){
		return solved;
	}

	public int[][] getBoard(){
		return board;
	}

	public int getH(){
		return height;
	}

	public int getW(){
		return width;
	}

	public ArrayList<move> howDidIGetHere(){
		for( int i = 0; i < moves.size(); i++ ){
			moves.get(i).print();
		}
		return new ArrayList<move>();
	}



	/************************************************
	* Print out the game board in its current state *
	************************************************/
	public void printBoard(){
		System.out.print( width + "," + height + ",\n" );
		for( int i = 0; i < height; i++ ){
			for( int j = 0; j < width; j++ ){
				System.out.print( board[i][j] + "," );
			}
			System.out.println();
		}
	}

	/*************************************************
	* Check if the puzzle is currently solved or not *
	*************************************************/
	public boolean checkComplete(){
		for( int i = 0; i < height; i++ ){
			for( int j = 0; j < width; j++ ){
				if( board[i][j] == -1 ){
					return solved;
				}
			}
		}
		solved = true;
		return solved;	
	}
	
	/*********************************************************
	* Return the number at a specific location in the board. *
	*********************************************************/
	public int getIndex( int i , int j ){
		return board[i][j];
	}
	
	/********************************************************
	* Find all the moves that correspond with a given value *
	********************************************************/
	public ArrayList<move> getMoves( int num ){
		ArrayList<move> moves	=	new ArrayList<move>();
		ArrayList<point> points	=	findPoints( num );
		if( checkU( points ) == true ){
			moves.add( new move( 'u' , num ) );
		}
		if( checkD( points ) == true ){
			moves.add( new move( 'd' , num ) );
		}
		if( checkL( points ) == true ){
			moves.add( new move( 'l' , num ) );
		}
		if( checkR( points ) == true ){
			moves.add( new move( 'r' , num ) );
		}
		
		return moves;
	}
	
	/*********************************************************
	* Find all the points that correspond with a given value *
	*********************************************************/
	public ArrayList<point> findPoints( int num ){
		ArrayList<point> points = new ArrayList<point>();
		for( int i = 0; i < height; i++ ){
			for( int j = 0; j < width; j++){
				if( board[i][j] == num ){
					points.add( new point( i , j , num ) );
				}
			}
		}
		return points;
	}

	public int getAround( int num ){
		ArrayList<point> block = findPoints( num );
		int count = 0;
		for( int i = 0; i < block.size(); i++ ){
			int x = block.get(i).getX();
			int y = block.get(i).getY();
			if( board[y+1][x] > 2 ){
				count++;
			}
			if( board[y-1][x] > 2 ){
				count++;
			}
			if( board[y][x+1] > 2 ){
				count++;
			}
			if( board[y][x-1] > 2 ){
				count++;
			}
		}
		return count;
	}
	
	/**************************************
	* Methods to check if moves are valid *
	***************************************/
	private boolean checkU( ArrayList<point> p ){
		boolean valid	=	false;
		if( p.get(0).val == 2 ){
			for( int i = 0; i < p.size(); i++ ){
				if( board[p.get(i).y - 1][p.get(i).x] != 0 && board[p.get(i).y - 1][p.get(i).x] != p.get(i).val && board[p.get(i).y - 1][p.get(i).x] != -1 ){
					return valid;
				}
			}
		}
		else{
			for( int i = 0; i < p.size(); i++ ){
				if( board[p.get(i).y - 1][p.get(i).x] != 0 && board[p.get(i).y - 1][p.get(i).x] != p.get(i).val){
					return valid;
				}
			}
		}

		valid = true;
		return	valid;
	}
	private boolean checkD( ArrayList<point> p ){
		boolean valid	=	false;
		if( p.get(0).val == 2 ){
			for( int i = 0; i < p.size(); i++ ){
				if( board[p.get(i).y + 1][p.get(i).x] != 0 && board[p.get(i).y + 1][p.get(i).x] != p.get(i).val && board[p.get(i).y + 1][p.get(i).x] != -1 ){
					return valid;
				}
			}
		}
		else{
			for( int i = 0; i < p.size(); i++ ){
				if( board[p.get(i).y + 1][p.get(i).x] != 0 && board[p.get(i).y + 1][p.get(i).x] != p.get(i).val){
					return valid;
				}
			}
		}

		valid = true;

		return	valid;
	}
	private boolean checkL( ArrayList<point> p ){
		boolean valid	=	false;
		
		if( p.get(0).val == 2 ){
			for( int i = 0; i < p.size(); i++ ){
				if( board[p.get(i).y][p.get(i).x - 1] != 0 && board[p.get(i).y][p.get(i).x - 1] != p.get(i).val && board[p.get(i).y][p.get(i).x - 1] != -1 ){
					return valid;
				}
			}
		}
		else{
			for( int i = 0; i < p.size(); i++ ){
				if( board[p.get(i).y][p.get(i).x - 1] != 0 && board[p.get(i).y][p.get(i).x - 1] != p.get(i).val){
					return valid;
				}
			}
		}
		valid = true;
		return	valid;
	}
	private boolean checkR( ArrayList<point> p ){
		boolean valid	=	false;
		if( p.get(0).val == 2 ){
			for( int i = 0; i < p.size(); i++ ){
				if( board[p.get(i).y][p.get(i).x + 1] != 0 && board[p.get(i).y][p.get(i).x + 1] != p.get(i).val && board[p.get(i).y][p.get(i).x + 1] != -1 ){
					return valid;
				}
			}
		}
		else{
			for( int i = 0; i < p.size(); i++ ){
				if( board[p.get(i).y][p.get(i).x + 1] != 0 && board[p.get(i).y][p.get(i).x + 1] != p.get(i).val){
					return valid;
				}
			}
		}
		valid = true;

		return	valid;
	}

	/********************************
	* Find the highest number piece *
	********************************/
	public int getHighNum(){
		int high = 0;
		for( int i = 0; i < height; i++ ){
			for( int j = 0; j < width; j++ ){
				if( board[i][j] > high ){
					high = board[i][j];
				}
			}
		}
		return high;
	}
	
	/********************************
	* Apply a move to a board state *
	*********************************/
	public void applyMove( move m ){
		moves.add( m );
		int tmp = 0;
//		m.print();
		switch( m.dir ){
			case UP:
				for(int i = 0; i < this.height; i++){
					for(int j = 0; j < this.width; j++){
						if(this.board[i][j] == m.piece){
							tmp = this.board[i - 1][j];
							if(tmp == -1){
								tmp = 0;
							}
							this.board[i-1][j] = this.board[i][j];
							this.board[i][j] = tmp;
						}
					}
				}
				break;
			case DOWN:
				for(int i = height-1; i >= 0; i-- ){
					for(int j = width-1; j >= 0; j--){
						if(this.board[i][j] == m.piece){
							tmp = this.board[i+1][j];
							if( tmp == -1 ){
								tmp = 0;
							}
							this.board[i+1][j] = this.board[i][j];
							this.board[i][j] = tmp;
						}
					}
				}
				break;
			case LEFT:
				for(int i = 0; i < this.height; i++){
					for(int j = 0; j < this.width; j++){
						if(this.board[i][j] == m.piece){
							tmp = this.board[i][j-1];
							if( tmp == -1 ){
								tmp = 0;
							}
							this.board[i][j-1] = this.board[i][j];
							this.board[i][j] = tmp;
						}
					}
				}
				break;
			case RIGHT:
				for(int i = height-1; i >= 0; i--){
					for(int j = width-1; j >= 0; j--){
						if(this.board[i][j] == m.piece){
							tmp = this.board[i][j+1];
							if( tmp == -1 ){
								tmp = 0;
							}
							this.board[i][j+1] = this.board[i][j];
							this.board[i][j] = tmp;
						}
					}
				}
				break;
		}
	}
	
	public void normalize(){
		int nextIdx = 3;
		for( int i = 0; i < height; i++ ) {
			for( int j = 0; j < width; j++ ) {
				if( board[i][j] == nextIdx ){
					nextIdx++;
				} else if ( board[i][j] > nextIdx ) {
					swapIdx( nextIdx , board[i][j] );
					nextIdx++;
				}
			}
		}
	}

	private void swapIdx( int one , int two ){
		for( int i = 0; i < height; i++ ){
			for( int j = 0; j < width; j++ ){
				if( board[i][j] == one ){
					board[i][j] = two;
				} else if( board[i][j] == two ){
					board[i][j] = one;
				}
			}
		}
	}
	
	public void printMoves(){
		for( int i = 0; i < moves.size(); i++ ){
			moves.get(i).print();
		}
	}

	/******************************
	* Inner class for the points. *
	******************************/
	public class point{
		int x , y, val;
		public point( int y , int x , int val ){
			this.x	=	x;
			this.y	=	y;
			this.val=	val;
		}

		public int getX(){
			return x;
		}
		
		public int getY(){
			return y;
		}

		public void print(){
			System.out.println( x + ", " + y );
		}
	}
	
	public void setMDist( int d ){
		mDist = d;
	}
	
	/********************************************************
	* Inner class for to hold any information about a move. *
	********************************************************/
	public class move{

		public Direction dir;
		public int piece;
		public String direction;
		public char d;

		/**********************************
		* Constructor for the move class. *
		**********************************/
		public move( char d, int p ){
			this.d = d;
			switch( Character.toLowerCase( d ) ){
				case 'u':
					dir = Direction.UP;
					direction = "up";
					break;
				case 'd':
					dir = Direction.DOWN;
					direction = "down";
					break;
				case 'l':
					dir = Direction.LEFT;
					direction = "left";
					break;
				case 'r':
					dir = Direction.RIGHT;
					direction = "right";
					break;
				default:
					dir = Direction.NONE;
					direction = "none";
					break;
			}
			piece = p;
		}
		
		/*****************************************
		* Get the direction of the current move. *
		*****************************************/
		public Direction getDir(){
			return dir;
		}

		/*******************************************
		* Get the number of the piece in question. *
		*******************************************/
		public int getPiece(){
			return piece;
		}
		
		/***************************************
		* Print out the logistics of the move. *
		***************************************/
		public void print(){
			System.out.println( "(" + piece + ", " + direction + ")" );
		}
	}
}
