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
	private void initialSetup() {
		board.placePiece(new Rook(board, Color.WHITE), new Position (2, 1));
		board.placePiece(new King(board, Color.BLACK), new Position (0, 4));
		board.placePiece(new King(board, Color.WHITE), new Position (7, 4));
	}
}
