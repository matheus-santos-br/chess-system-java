package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardGame.Board;
import boardGame.Piece;
import boardGame.Position;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;

public class ChessMatch {
	
	private int turn;
	private Color currentPlayer;
	private Board board;
	private boolean check;
	private boolean checkMate;
	private ChessPiece enPassantVulnerable;
	
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();

	public ChessMatch() {
		board = new Board(8, 8);
		turn = 1;
		currentPlayer = Color.WHITE; 
		initialSetup();
	}
	
	public int getTurn() {
		return turn;
	}
	
	public Color getCurrentPlayer() {
		return currentPlayer;
	}
	
	public boolean getCheck() {
		return check;
	}
	
	public boolean getCheckMate() {
		return checkMate;
	}
	
	public ChessPiece getEnPassantVulnerable() {
		return enPassantVulnerable;
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
	
	public boolean[][] possibleMoves(ChessPosition sourcePosition){
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();
	}
	
	
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);
		validateTargetPosition(source, target);
		Piece capturedPiece = makeMove(source, target);
		
		if (testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessException("You can't put yourself in check!");
		}
		
		ChessPiece movedPiece = (ChessPiece)board.piece(target);
		
		
		check = (testCheck(opponent(currentPlayer))) ? true : false;
		
		if (testCheckMate(opponent(currentPlayer))) {
			checkMate = true;
		}
		else {
			nextTurn();
		}
		
		// #specialmove en passant
		if (movedPiece instanceof Pawn && (target.getRow() == source.getRow() - 2 || target.getRow() == source.getRow() + 2 )){
			enPassantVulnerable = movedPiece;
		}
		else {
			enPassantVulnerable = null;
		}
			
		return (ChessPiece)capturedPiece;
	}
	
	private Piece makeMove(Position source, Position target) {
		ChessPiece p = (ChessPiece)board.removePiece(source);
		p.increaseMoveCount();
		Piece capturedPiece = board.removePiece(target);
		board.placePiece(p, target);
		
		if(capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		
		//#specialmove castling kingside rock
		if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
			Position targetT = new Position(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece)board.removePiece(sourceT);
			board.placePiece(rook, targetT);
			rook.increaseMoveCount();
		}
		
		//#specialmove castling queenside rock
		if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
			Position targetT = new Position(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece)board.removePiece(sourceT);
			board.placePiece(rook, targetT);
			rook.increaseMoveCount();
		}
		
		
		return capturedPiece;
	}
	
	private void undoMove(Position source, Position target, Piece capturedPiece) {
		ChessPiece p = (ChessPiece)board.removePiece(target);
		p.decreaseMoveCount();
		board.placePiece(p, source);
		
		if (capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}
	
		//#specialmove castling kingside rock
		if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
			Position targetT = new Position(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece)board.removePiece(targetT);
			board.placePiece(rook, sourceT);
			rook.decreaseMoveCount();
		}
		
		//#specialmove castling queenside rock
		if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
			Position targetT = new Position(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece)board.removePiece(targetT);
			board.placePiece(rook, sourceT);
			rook.decreaseMoveCount();
		}

	}
	
	
	private void validateSourcePosition(Position position) {
		if (!board.thereIsAPiece(position)) {
			throw new ChessException("There is no piece on source position!");
		}
		if(currentPlayer != ((ChessPiece)board.piece(position)).getColor()){
			throw new ChessException("The chosen piece isn't yours!");
		}
		
		if(!board.piece(position).isThereAnyPossibleMove()) {
			throw new ChessException("There is no possible moves for the chosen piece!");
		}
	}
	
	private void validateTargetPosition(Position source, Position target) {
		if(!board.piece(source).possibleMove(target)) {
		throw new ChessException("The chosen piece cannot move to target position!");
		}
	}
	
	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	
	private Color opponent(Color color) {
		return(color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}

	private ChessPiece king(Color color) {
		List<Piece> list = piecesOnTheBoard.stream().filter( aux -> ((ChessPiece)aux).getColor() == color).collect(Collectors.toList());
		for (Piece p : list) {
			if (p instanceof King) {
				return (ChessPiece)p;
			}
		}
		throw new IllegalStateException("There is no " + color + " king on the board!");
	}
	
	private boolean testCheck(Color color) {
		Position kingPosition = king(color).getChessPosition().toPosition();
		List<Piece> opponentPieces = piecesOnTheBoard.stream().filter( aux -> ((ChessPiece)aux).getColor() == opponent(color)).collect(Collectors.toList());
		for (Piece p : opponentPieces) {
			boolean[][] mat = p.possibleMoves();
			if (mat[kingPosition.getRow()][kingPosition.getColumn()] ) {
				return true;
			}
		}
		return false;
	}
	
	private boolean testCheckMate(Color color) {
		if (!testCheck(color)) {
			return false;
		}
		
		List<Piece> list = piecesOnTheBoard.stream().filter( aux -> ((ChessPiece)aux).getColor() == color).collect(Collectors.toList());
		for (Piece p : list) {
			boolean[][] mat = p.possibleMoves();
			for( int c = 0 ; c < board.getRows() ; c += 1 ) {
				for ( int cc = 0 ; cc < board.getColumns() ; cc += 1) {
					if (mat[c][cc]) {
						Position source = ((ChessPiece)p).getChessPosition().toPosition();
						Position target = new Position(c, cc);
						Piece capturedPiece = makeMove(source, target);
						boolean testCheck = testCheck(color);
						undoMove(source, target, capturedPiece);
						if (!testCheck) {
							return false;
						}
					}
				}
			}
		}
		
		return true;
	}
	
	private void placeNewPiece(char column, int row, ChessPiece piece ) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece);
	}
	
	private void initialSetup() {
		 	placeNewPiece('a', 1, new Rook(board, Color.WHITE));
		 	placeNewPiece('b', 1, new Knight(board, Color.WHITE));
		 	placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
		 	placeNewPiece('d', 1, new Queen(board, Color.WHITE));
		 	placeNewPiece('e', 1, new King(board, Color.WHITE, this));
		 	placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
		 	placeNewPiece('g', 1, new Knight(board, Color.WHITE));
		 	placeNewPiece('h', 1, new Rook(board, Color.WHITE));
	        placeNewPiece('a', 2, new Pawn(board, Color.WHITE, this));
	        placeNewPiece('b', 2, new Pawn(board, Color.WHITE, this));
	        placeNewPiece('c', 2, new Pawn(board, Color.WHITE, this));
	        placeNewPiece('d', 2, new Pawn(board, Color.WHITE, this));
	        placeNewPiece('e', 2, new Pawn(board, Color.WHITE, this));
	        placeNewPiece('f', 2, new Pawn(board, Color.WHITE, this));
	        placeNewPiece('g', 2, new Pawn(board, Color.WHITE, this));
	        placeNewPiece('h', 2, new Pawn(board, Color.WHITE, this));
	        
	        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
	        placeNewPiece('b', 8, new Knight(board, Color.BLACK));
	        placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
	        placeNewPiece('d', 8, new Queen(board, Color.BLACK));
	        placeNewPiece('e', 8, new King(board, Color.BLACK, this));
	        placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
	        placeNewPiece('g', 8, new Knight(board, Color.BLACK));
	        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
	        placeNewPiece('a', 7, new Pawn(board, Color.BLACK, this));
	        placeNewPiece('b', 7, new Pawn(board, Color.BLACK, this));
	        placeNewPiece('c', 7, new Pawn(board, Color.BLACK, this));
	        placeNewPiece('d', 7, new Pawn(board, Color.BLACK, this));
	        placeNewPiece('e', 7, new Pawn(board, Color.BLACK, this));
	        placeNewPiece('f', 7, new Pawn(board, Color.BLACK, this));
	        placeNewPiece('g', 7, new Pawn(board, Color.BLACK, this));
	        placeNewPiece('h', 7, new Pawn(board, Color.BLACK, this));
	}
}
