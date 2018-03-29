import java.util.*;
import java.io.File;

public class BlockPuzzleDriver{
	
	private static Scanner scan;
//	public ArrayList<gameState> closed;
	/*******************
	* Driving function *
	*******************/
	public static void main( String[] args ){

		int width, height;
		gameState game = null;

		if( args.length < 1 ){
			System.out.println( "Please specify game state file" );
			return;
		}
		
		game = readBoard( args[0] );

/*		long bfsStart = System.currentTimeMillis();
		gameState solvedBFS = bfs( game );
		long bfsEnd = System.currentTimeMillis();
		gameState solvedDFS = dfs( game );
		long dfsEnd = System.currentTimeMillis();
		gameState solvedIDS = ids( game );
		long idsEnd = System.currentTimeMillis();
/*
		System.out.println("******BREADTH FIRST SEARCH******");
		solvedBFS.howDidIGetHere();
		solvedBFS.printBoard();
		System.out.println("Number of nodes in the solution: " + solvedBFS.moves.size() );
		System.out.println("BFS TIME: " + (bfsEnd - bfsStart) + "ms" );
		System.out.println("******DEPTH FIRST SEARCH******");
		solvedDFS.howDidIGetHere();
		solvedDFS.printBoard();
		System.out.println("Number of nodes in the solution: " + solvedDFS.moves.size() );
		System.out.println("DFS TIME: " + (dfsEnd - bfsEnd) + "ms" );
		System.out.println("******ITERATIVE DEEPENING******");
		solvedIDS.howDidIGetHere();
		solvedIDS.printBoard();
		System.out.println("Number of nodes in the solution: " + solvedIDS.moves.size() );
		System.out.println("IDS TIME: " + (idsEnd - dfsEnd) +"ms" );
*/

		long amsStart	=	System.currentTimeMillis();
		gameState solvedAMan = aManStar( game );
		long amsEnd	=	System.currentTimeMillis();
		System.out.println( "****** MANHATTAN A* ******");
		solvedAMan.howDidIGetHere();
		solvedAMan.printBoard();
		System.out.println("Number of nodes in the solution: " + solvedAMan.moves.size() );
		System.out.println( "Time: " + (amsEnd - amsStart) + "ms" );

		long aosStart	=	System.currentTimeMillis();
		gameState solvedAOther = aOtherStar( game );
		long aosEnd	=	System.currentTimeMillis();
		System.out.println( "****** SURROUNDING PIECES A* ******");
		solvedAOther.howDidIGetHere();
		solvedAOther.printBoard();
		System.out.println("Number of nodes in the solution: " + solvedAOther.moves.size() );
		System.out.println( "Time: " + (aosEnd - aosStart) + "ms" );

//		randomWalks( game , 100 );		

	}

	/***********************************************************************
	* Logistics of reading the board from a file into an array of integers *
	***********************************************************************/
	private static gameState readBoard( String fileName ){
		gameState game;

		File file = new File( fileName );
		String b = "";
		int count, width, height;
		try{
			scan 	=	new Scanner( file );
		}
		catch( Exception e ){
			e.printStackTrace();
		}
		
		while( scan.hasNextLine() ){
			b = b + scan.nextLine();
		}

		String[] str = b.split( "," );
		int[] tok = new int[str.length];

		for( int i = 0; i < str.length; i++ ){
			tok[i] 	=	Integer.parseInt( str[i] );
		}

		count	=	2;
		width	= 	tok[0];
		height	=	tok[1];

		int[][] board = new int[height][width];
		
		/********************************************
		* print out the board as read from the file *
		********************************************/
		for( int i = 0; i < height; i++ ){
			for( int j = 0; j < width; j++ ){
				board[i][j]	=	tok[count] ;
				count++;
			}
		}

		game = new gameState( width , height , board );
		game.printBoard();
		System.out.println("Game Solved: " + game.checkComplete() );
		return game;
	}

	/******************************************
	* Clone a game state and return the clone *
	******************************************/
	public static gameState cloneState( gameState g ){
		int h = g.height;
		int w = g.width;
		int[][] nBoard = new int[h][w];
		int curr;
		for( int i = 0; i < h; i++) {
			for( int j = 0; j < w; j++){
				nBoard[i][j] = 0;
				curr = new Integer(g.board[i][j]);
				nBoard[i][j] = new Integer(nBoard[i][j]+curr);
			}
		}

		gameState clone = new gameState( w , h , nBoard );
		for( int i = 0; i < g.moves.size(); i++ ){
			clone.moves.add( clone.new move( g.moves.get(i).d , g.moves.get(i).piece ) );
		}
		return clone;
	}
	
	/**************************************************************
	* Get all of the available moves given the current game state *
	**************************************************************/
	public static ArrayList<gameState.move> getAllMoves( gameState g){
		int high = g.getHighNum();
		ArrayList<gameState.move> allMoves = new ArrayList<gameState.move>();
		for( int i = 2; i <= high; i++ ){
			allMoves.addAll( g.getMoves( i ) );
		}
		return allMoves;
	}

	public static gameState applyMoveCloning( gameState g , gameState.move m ){
		gameState clone = new gameState();
		clone = cloneState( g );
		clone.applyMove( m );
		return clone;
	}

	public static boolean checkSameState( gameState a , gameState b ){
		if( a.height != b.height || a.width != b.width ){
			return false;
		}
		for( int i = 0; i < a.height; i++ ){
			for( int j = 0; j < a.width; j++ ){
				if(a.board[i][j] != b.board[i][j]){
					return false;
				}
			}
		}
		
		return true;
	}

	public static void randomWalks( gameState game , int num ){
		int count = 0;
		int r;
		Random rand = new Random();
		ArrayList<gameState.move> moves = new ArrayList<gameState.move>();
		moves = getAllMoves( game );
		while( count < num ){

			if( game.checkComplete() ){
				System.out.println( "Puzzle Solved" );
				break;
			}
			
			System.out.println( count );
			moves = getAllMoves( game );
			r = rand.nextInt( moves.size() );
			game = applyMoveCloning( game , moves.get(r) );

			game.normalize();
			game.printBoard();
			count++;
		}
	}

	public static gameState dfs( gameState game ){
		
		game.normalize();
		Stack<gameState> s = new Stack<gameState>();
		s.push( game );
		ArrayList<gameState> closed = new ArrayList<gameState>();
//		closed.add( game );

		while( !s.empty() ){
			gameState current = s.pop();
			if( current.checkComplete() ){
				System.out.println("DFS Visited " + closed.size() + " nodes");
				return current;
			}
			boolean discovered = false;

			for(int i = 0; i < closed.size(); i++){
				if( checkSameState( current , closed.get(i) ) ){
					discovered = true;
					break;
				}
			}
			
			if( !discovered ){
				closed.add( current );
				ArrayList<gameState.move> moves = getAllMoves( current );
				ArrayList<gameState> states = moveAll( current , moves );
				for( int j = 0; j < states.size(); j++ ){
					s.push( states.get(j) );
				}
			}
		}
		return new gameState();
	}

	/********************************************
	* Breadth First Search to find the solution *
	********************************************/
	public static gameState bfs( gameState game ){

		Queue<node> q = new LinkedList<node>();
		ArrayList<gameState> closed = new ArrayList<gameState>();
		
		ArrayList<gameState.move> history = new ArrayList<gameState.move>();
		game.normalize();
		q.add( new node( game , new ArrayList<gameState.move>() ) );
		closed.add(game);
		while( !q.isEmpty() ){

			node current = q.poll();
			if( current.g.checkComplete() ){
				System.out.println("BFS Visited " + closed.size() + " nodes");
				return current.g;
			}
			closed.add( current.g );
			ArrayList<gameState.move> moves = getAllMoves( current.g );
			ArrayList<gameState> states = moveAll( current.g, moves );
//			System.out.println("number of moves made :" + states.size() );

			for( int i = 0; i < states.size(); i++ ){
				states.get(i).normalize();
				boolean notClosed = true;
				for( int j = 0; j < closed.size(); j++ ){
					if( checkSameState( states.get(i) , closed.get(j)  )){
						notClosed = false;
						//System.out.println(notClosed);
						break;
					}
				}
//				System.out.println(notClosed);
				if( notClosed ){
					q.add( new node( states.get(i) , new ArrayList<gameState.move>() ));
					closed.add(states.get(i));
				}
			}
			
		}
		return new gameState();
	}

	public static gameState ids( gameState game ){
		
		int depth = 0;
		boolean solved = false;
		while( true ){
			gameState solution = dls( game , depth , new ArrayList<gameState>());
			if( solution != null ){
				return solution;
			}
			depth++;
		}
	}

	private static gameState dls( gameState game , int depth , ArrayList<gameState> closed ){
		game.normalize();
		if(game.checkComplete() ){
			System.out.println("IDS Visited " + closed.size() + " nodes");
			return game;
		}
		if( depth == 0 && game.checkComplete() ){
			return game;
		}else if( depth > 0 ){
			ArrayList<gameState.move> moves = getAllMoves( game );
			ArrayList<gameState> states = moveAll( game , moves );
	
			for( int i = 0; i < states.size(); i++){
				boolean discovered = false;
				for( int j = 0; j < closed.size(); j++){
					if( checkSameState( states.get(i) , closed.get(j) ) ){
						discovered = true;
						break;
					}
				}
				gameState solution = null;
				if( !discovered ){
					closed.add( states.get(i) );
					solution = dls( states.get(i) , depth - 1 , closed );
				}
				if( solution != null ){
					return solution;
				}
			}
		}
		
		return null;
	}

	public static gameState aManStar( gameState game ){
		Comparator<gameState> comp = new mComparator();
		PriorityQueue<gameState> pQ = new PriorityQueue<gameState>( 1 , comp );
		ArrayList<gameState> closed = new ArrayList<gameState>();

		game.normalize();
		pQ.add( game );
		closed.add( game );
		int count = 0;
		while( pQ.size() != 0 ){
			count++;
			gameState current = pQ.poll();
//			current.printBoard();
//			current.normalize();
//			closed.add( current );
			if( current.checkComplete() ){
				System.out.println("NODES: " + count );
				return current;
			}
			ArrayList<gameState.move> moves = getAllMoves( current );
			ArrayList<gameState>	  states = moveAll( current , moves );

			for( int i = 0; i < states.size(); i++ ){
//				states.get(i).normalize();
				boolean discovered = false;
				for( int j = 0; j < closed.size(); j++ ){
					if( checkSameState( states.get( i ) , closed.get(j ) ) ){
						discovered = true;
						break;
					}
				}
				if( !discovered ){
					pQ.add( states.get(i) );
					closed.add( states.get(i) );
					int distX = Math.abs( findPointX( -1 , states.get(i) ) - findPointX( 2 , states.get(i) ) )    ;
					int distY = Math.abs( findPointY( -1 , states.get(i) ) - findPointY( 2 , states.get(i) ) )    ;
				states.get(i).setMDist((distX + distY));
 
				}
			}
		}
		return new gameState();
	}

	public static gameState aOtherStar( gameState game ){
		Comparator<gameState> comp = new mComparator();
		PriorityQueue<gameState> pQ = new PriorityQueue<gameState>( 1 , comp );
		ArrayList<gameState> closed = new ArrayList<gameState>();

		game.normalize();
		pQ.add( game );
		closed.add( game );
		int count = 0;
		while( pQ.size() != 0 ){
			count++;
			gameState current = pQ.poll();
//			current.printBoard();
//			current.normalize();
//			closed.add( current );
			if( current.checkComplete() ){
				System.out.println("NODES: " + count );
				return current;
			}
			ArrayList<gameState.move> moves = getAllMoves( current );
			ArrayList<gameState>	  states = moveAll( current , moves );

			for( int i = 0; i < states.size(); i++ ){
//				states.get(i).normalize();
				boolean discovered = false;
				for( int j = 0; j < closed.size(); j++ ){
					if( checkSameState( states.get( i ) , closed.get(j ) ) ){
						discovered = true;
						break;
					}
				}
				if( !discovered ){
					pQ.add( states.get(i) );
					closed.add( states.get(i) );
					int hOfX = states.get(i).getAround(2);
					states.get(i).setMDist( hOfX );
 
				}
			}
		}
		return new gameState();
	}

	private static ArrayList<gameState> moveAll( gameState game , ArrayList<gameState.move> moves ){

		gameState clone;
		ArrayList<gameState> states = new ArrayList<gameState>();

		for( int i = 0; i < moves.size(); i++){
	//		moves.get(i).print();
			clone = applyMoveCloning( game , moves.get(i) );
			states.add( clone );
			states.get(i).normalize();
		}

		return states;
	}



	


	private static int findPointX( int piece , gameState g ){
		for( int i = 0; i < g.height; i++ ){
			for( int j = 0; j < g.width; j++ ){
                                if( g.board[i][j] == piece ){
                                        return j;
                                }
                        }
                }
                return -1;
         }

         private static int findPointY( int piece , gameState g ){
                for( int i = 0; i < g.height; i++ ){
                         for( int j = 0; j < g.width; j++ ){
                                 if( g.board[i][j] == piece ){
                                         return i;
                                 }
                         }
                 }
                 return -1;
         }
}
