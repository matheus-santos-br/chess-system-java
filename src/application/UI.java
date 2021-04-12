package application;

import chess.ChessPiece;

public class UI {

	public static void printBoard(ChessPiece[][] pieces) {
		for (int c = 0; c < pieces.length ; c += 1) {
			System.out.print((8 - c) + " ");
			for (int cc = 0; cc < pieces.length ; cc += 1) {
				printPiece(pieces[c][cc]);
			}
			System.out.println();
		}
		System.out.println("  a b c d e f g h");
	}
	
	private static void printPiece(ChessPiece piece) {
		if ( piece == null) {
			System.out.print("-");
		}
		else {
			System.out.print(piece);
		}
		System.out.print(" ");
	}
}
