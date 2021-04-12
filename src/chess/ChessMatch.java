package chess;

import boardGame.Board;

public class ChessMatch {
	
	private Board board;

	public ChessMatch() {
		board = new Board(8, 8);
	}
	
	public ChessPiece[][] getPieces(){
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		for (int c = 0; c < board.getRows(); c += 1) {
			for (int cc = 0; cc < board.getColumns(); cc += 1) {
				mat[c][cc] = (ChessPiece) board.piece(c, cc);
			}
		}
		return mat;		
	}
}
