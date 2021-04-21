package application;

import java.util.InputMismatchException;
import java.util.Scanner;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class Program {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner (System.in); 
		ChessMatch chessMatch = new ChessMatch();
		
		while(true) {
			try {
				UI.clear_Screen();
				UI.printBoard(chessMatch.getPieces());
				System.out.println();
				System.out.println("Source: ");
				ChessPosition source = UI.readChessPosition(sc);
				
				System.out.println();
				System.out.println("Target: ");
				ChessPosition target = UI.readChessPosition(sc);
				
				ChessPiece capturedPiece = chessMatch.performChessMove(source, target);
			}
			catch (ChessException aux) {
				System.out.println(aux.getMessage());
				sc.nextLine();
			}
			catch (InputMismatchException aux) {
				System.out.println(aux.getMessage());
				sc.nextLine();
			}
		}
	}

}
