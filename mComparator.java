import java.util.Comparator;
import java.lang.Math;

public class mComparator implements Comparator<gameState>{

	@Override
	public int compare( gameState g1 , gameState g2 ){
		int mDist1 , mDist2;
		mDist1 = g1.mDist + g1.moves.size();
//		mDist1 = manhattanDistance( g1 ) + g1.moves.size();
		mDist2 = g2.mDist + g2.moves.size();
		if( mDist1 > mDist2 ){
			return 1;
		}
		if( mDist1 < mDist2 ){
			return -1;
		}
		return 0;
	}

	private static int manhattanDistance( gameState g ){
		
		int goal = -1;
		int master = 2;
		int distX = Math.abs( findPointX( goal , g ) - findPointX( master , g ) );
		int distY = Math.abs( findPointY( goal , g ) - findPointY( master , g ) );

		int mDistance = distX + distY;
		g.mDist = mDistance;
		return mDistance;
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
