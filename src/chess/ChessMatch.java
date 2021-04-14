package chess;

import boardGame.Board;
import boardGame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {
	
	private Board board;

	public ChessMatch() {
		board = new Board(8, 8);
		initialSetup();
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
	
	private void placeNewPiece(char column, int row, ChessPiece piece ) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
	}
	
	
	
	private void initialSetup() {
		placeNewPiece('b', 6, new Rook(board, Color.WHITE));
		placeNewPiece('e', 8, new King(board, Color.BLACK));
		placeNewPiece('e', 1, new King(board, Color.WHITE));
	}
}
