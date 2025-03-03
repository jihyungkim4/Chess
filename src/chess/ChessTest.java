package chess;

import org.junit.Test;
import java.io.*;
import java.util.*;
import static org.junit.Assert.*;

public class ChessTest {

    @Test
    public void testPawnPromotion() throws IOException {
        runScenarioTest("testfiles/test_pawn_promotion.txt");
    }

    @Test
    public void testPawnBasic() throws IOException {
        runScenarioTest("testfiles/test_pawn_basic.txt");
    }

    @Test
    public void testPawnEnPassant() throws IOException {
        runScenarioTest("testfiles/test_pawn_en_passant.txt");
    }

    @Test
    public void testPawnEnPassant2() throws IOException {
        runScenarioTest("testfiles/test_pawn_en_passant2.txt");
    }

    @Test
    public void testPawnCaptureWhite() throws IOException {
        runScenarioTest("testfiles/test_pawn_capture_white.txt");
    }

    @Test
    public void testPawnCaptureBlack() throws IOException {
        runScenarioTest("testfiles/test_pawn_capture_black.txt");
    }

    @Test
    public void testBasicCheckmate() throws IOException {
        runScenarioTest("testfiles/test_basic_checkmate.txt");
    }

    @Test
    public void testCastlingWhite() throws IOException {
        runScenarioTest("testfiles/test_castling1.txt");
    }

    @Test
    public void testBishopBasic() throws IOException {
        runScenarioTest("testfiles/test_bishop_basic_moves.txt");
    }

    @Test
    public void testRookBasic() throws IOException {
        runScenarioTest("testfiles/rook_basic_move_test.txt");
    }

    @Test
    public void testKnightBasic() throws IOException {
        runScenarioTest("testfiles/test_knight_basic_moves.txt");
    }

    @Test
    public void testQueenBasic() throws IOException {
        runScenarioTest("testfiles/test_queen_basic_moves.txt");
    }
    
    @Test
    public void testFullGame() throws IOException {
        runScenarioTest("testfiles/test_full_game1.txt");
    }

    // Move class to store each move read from file
    static class Move {
        String player;
        String from;
        String to;
        boolean drawRequested;
        String expectedMessage;
        List<String> expectedPieces;

        Move(String player, String from, String to, boolean drawRequested,
                String expectedMessage, List<String> expectedPieces) {
            this.player = player;
            this.from = from;
            this.to = to;
            this.drawRequested = drawRequested;
            this.expectedMessage = expectedMessage;
            this.expectedPieces = expectedPieces;
        }
    }

    private static class Scenario {
        List<String> initialPieces;
        String startingPlayer;
        List<Move> moves;

        Scenario(List<String> initialPieces, String startingPlayer, List<Move> moves) {
            this.initialPieces = initialPieces;
            this.startingPlayer = startingPlayer;
            this.moves = moves;
        }
    }

    private Scenario parseScenarioFile(String filename) throws IOException {
        List<String> initialPieces = null;
        String startingPlayer = null;
        List<Move> moves = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("#") || line.isEmpty())
                    continue; // Skip comments and blank lines

                if (line.startsWith("INITIAL")) {
                    // Example line: INITIAL [a2:WP, b2:WP, e1:WK, e8:BK] white
                    int firstBracket = line.indexOf('[');
                    int lastBracket = line.indexOf(']');

                    if (firstBracket == -1 || lastBracket == -1 || lastBracket < firstBracket) {
                        throw new IllegalArgumentException("Malformed INITIAL line in " + filename + ": " + line);
                    }

                    // Extract everything inside the brackets
                    String pieceListString = line.substring(firstBracket + 1, lastBracket);
                    initialPieces = parsePieceList(pieceListString);

                    // Extract player (should be after the closing bracket)
                    startingPlayer = line.substring(lastBracket + 2).trim(); // Skip space after the bracket

                    if (!(startingPlayer.equals("white") || startingPlayer.equals("black"))) {
                        throw new IllegalArgumentException("Invalid player in INITIAL line: " + startingPlayer);
                    }
                } else {
                    moves.add(parseMoveLine(line));
                }
            }
        }

        return new Scenario(initialPieces, startingPlayer, moves);
    }

    private void runScenarioTest(String filename) throws IOException {
        Scenario scenario = parseScenarioFile(filename);

        System.out.println();

        // Initialize Chess and load the initial board state
        Chess.start();

        // If initial position was specified reload it from the specified list otherwise
        // start with the full board position.
        if (scenario.initialPieces != null) {
            if (scenario.startingPlayer == null) {
                throw new IllegalArgumentException("File " + filename + " is missing INITIAL line.");
            }

            ArrayList<ReturnPiece> initialPieceObjects = convertToReturnPieceList(scenario.initialPieces);
            Chess.board.load(initialPieceObjects,
                    scenario.startingPlayer.equals("white") ? Chess.Player.white : Chess.Player.black);
        }

        // Execute and verify each move
        for (int i = 0; i < scenario.moves.size(); i++) {
            Move move = scenario.moves.get(i);
            String fullMove = move.from + " " + move.to;
            if (move.drawRequested) {
                fullMove += " draw?";
            }

            ReturnPlay result = Chess.play(fullMove);
            if (result.message != null) {
                System.out.println("\n" + result.message);
            }
            System.out.println();
            PlayChess.printBoard(result.piecesOnBoard);

            String actualMessage = result.message != null ? result.message.toString() : "OK";

            if (!actualMessage.equals(move.expectedMessage)) {
                fail(String.format("File: %s | Move %d by %s: Expected message %s but got %s",
                        filename, i + 1, move.player, move.expectedMessage, actualMessage));
            }

            List<String> actualPieces = piecesToStringList(result.piecesOnBoard);
            if (!new HashSet<>(actualPieces).equals(new HashSet<>(move.expectedPieces))) {
                fail(String.format("File: %s | Move %d by %s: Board state mismatch.\nExpected: %s\nActual: %s",
                        filename, i + 1, move.player, move.expectedPieces, actualPieces));
            }
        }
    }

    // Parses: [a2:WP, b2:WP, e1:WK]
    private List<String> parsePieceList(String pieceListString) {
        pieceListString = pieceListString.replaceAll("[\\[\\]]", ""); // Remove brackets
        pieceListString = pieceListString.replaceAll(" ", ""); // Remove spaces
        return Arrays.asList(pieceListString.split(","));
    }

    // Converts list like ["a2:WP", "e1:WK"] to ArrayList<ReturnPiece>
    private ArrayList<ReturnPiece> convertToReturnPieceList(List<String> pieces) {
        ArrayList<ReturnPiece> pieceObjects = new ArrayList<>();
        for (String piece : pieces) {
            pieceObjects.add(parseReturnPiece(piece.trim()));
        }
        return pieceObjects;
    }

    private ReturnPiece parseReturnPiece(String pieceStr) {
        String[] parts = pieceStr.split(":");
        if (parts.length != 2)
            throw new IllegalArgumentException("Invalid piece format: " + pieceStr);

        ReturnPiece piece = new ReturnPiece();
        piece.pieceFile = ReturnPiece.PieceFile.valueOf(parts[0].substring(0, 1));
        piece.pieceRank = Integer.parseInt(parts[0].substring(1));
        piece.pieceType = ReturnPiece.PieceType.valueOf(parts[1]);

        return piece;
    }

    private Move parseMoveLine(String line) {
        String[] parts = line.split("\\s+");
        if (parts.length < 5) {
            throw new IllegalArgumentException("Invalid move line: " + line);
        }

        String player = parts[0];
        String from = parts[1];
        String to = parts[2];
        boolean drawRequested = "draw?".equals(parts[3]);
        String expectedMessage = drawRequested ? parts[4] : parts[3];
        int piecesStartIndex = drawRequested ? 5 : 4;

        List<String> expectedPieces = new ArrayList<>();
        for (int i = piecesStartIndex; i < parts.length; i++) {
            expectedPieces.add(parts[i].replaceAll("[\\[\\],]", ""));
        }

        return new Move(player, from, to, drawRequested, expectedMessage, expectedPieces);
    }

    // Convert pieces to string for easy comparison
    private List<String> piecesToStringList(ArrayList<ReturnPiece> piecesOnBoard) {
        List<String> pieces = new ArrayList<>();
        for (ReturnPiece piece : piecesOnBoard) {
            pieces.add(piece.toString());
        }
        return pieces;
    }
}
