package boardGame;

public abstract class Piece {

	protected Position position;
	private Board board;
	
	public Piece(Board board) {
		this.board = board;
		position = null;
	}

	protected Board getBoard() {
		return board;
	}

	public abstract boolean[][] possibleMoves();
		
	public boolean possibleMove(Position position) {
		return possibleMoves()[position.getRow()][position.getColumn()];
		
	}
	
	public boolean isThereAnyPossibleMove() {
		boolean[][] mat = possibleMoves();
		for ( int c = 0 ; c < mat.length ; c += 1) {
			for (int cc = 0 ; cc < mat.length ; cc += 1) {
				if(mat[c][cc]) {
					return true;
				}
				
			}
		}
		return false;
	}
}
