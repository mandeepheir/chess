import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

public class Chess {

    private static JPanel[][] squares = new JPanel[8][8];
    private static JLabel[][] pieces = new JLabel[8][8];

    private static int selectedRow = -1;
    private static int selectedCol = -1;

    private static boolean whiteTurn = true;

    public static void main(String[] args) {

        JFrame frame = new JFrame("Chess");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);

        JPanel panel = new JPanel(new GridLayout(8, 8));

        Color light = new Color(240, 217, 181);
        Color dark = new Color(181, 136, 99);

        ImageIcon WP = new ImageIcon("Resources/WP.png");
        ImageIcon BP = new ImageIcon("Resources/BP.png");
        ImageIcon WR = new ImageIcon("Resources/WR.png");
        ImageIcon BR = new ImageIcon("Resources/BR.png");
        ImageIcon WB = new ImageIcon("Resources/WB.png");
        ImageIcon BB = new ImageIcon("Resources/BB.png");
        ImageIcon WQ = new ImageIcon("Resources/WQ.png");
        ImageIcon BQ = new ImageIcon("Resources/BQ.png");
        ImageIcon WkN = new ImageIcon("Resources/WkN.png");
        ImageIcon BkN = new ImageIcon("Resources/BkN.png");
        ImageIcon WK = new ImageIcon("Resources/WK.png");
        ImageIcon BK = new ImageIcon("Resources/BK.png");

        WP.setDescription("WP");
        BP.setDescription("BP");
        WR.setDescription("WR");
        BR.setDescription("BR");
        WB.setDescription("WB");
        BB.setDescription("BB");
        WQ.setDescription("WQ");
        BQ.setDescription("BQ");
        WkN.setDescription("WN");
        BkN.setDescription("BN");
        WK.setDescription("WK");
        BK.setDescription("BK");

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {

                JPanel square = new JPanel(new BorderLayout());
                square.setBackground((r + c) % 2 == 0 ? light : dark);

                JLabel piece = new JLabel();
                piece.setHorizontalAlignment(JLabel.CENTER);
                piece.setVerticalAlignment(JLabel.CENTER);

                // PAWNS
                if (r == 1) piece.setIcon(BP);
                if (r == 6) piece.setIcon(WP);

                // ROOKS
                if (r == 0 && (c == 0 || c == 7)) piece.setIcon(BR);
                if (r == 7 && (c == 0 || c == 7)) piece.setIcon(WR);

                // KNIGHTS
                if (r == 0 && (c == 1 || c == 6)) piece.setIcon(BkN);
                if (r == 7 && (c == 1 || c == 6)) piece.setIcon(WkN);

                // BISHOPS
                if (r == 0 && (c == 2 || c == 5)) piece.setIcon(BB);
                if (r == 7 && (c == 2 || c == 5)) piece.setIcon(WB);

                // QUEENS
                if (r == 0 && c == 3) piece.setIcon(BQ);
                if (r == 7 && c == 3) piece.setIcon(WQ);

                // KINGS
                if (r == 0 && c == 4) piece.setIcon(BK);
                if (r == 7 && c == 4) piece.setIcon(WK);

                squares[r][c] = square;
                pieces[r][c] = piece;

                square.add(piece);

                int rr = r;
                int cc = c;

                square.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        handleClick(rr, cc);
                    }
                });

                panel.add(square);
            }
        }

        frame.add(panel);
        frame.setVisible(true);
    }

    // CLICK HANDLER
    private static void handleClick(int r, int c) {

        if (!whiteTurn) return; // AI turn lock

        if (selectedRow == -1) {

            if (pieces[r][c].getIcon() != null) {

                String type = ((ImageIcon) pieces[r][c].getIcon()).getDescription();

                if (!type.startsWith("W")) return;

                selectedRow = r;
                selectedCol = c;

                highlight(r, c);
            }

        } else {

            ImageIcon from = (ImageIcon) pieces[selectedRow][selectedCol].getIcon();
            ImageIcon to = (ImageIcon) pieces[r][c].getIcon();

            String type = from.getDescription();
            boolean isCapture = (to != null);

            if (to != null) {
                String t = to.getDescription();
                if (t.charAt(0) == type.charAt(0)) {
                    reset();
                    selectedRow = -1;
                    selectedCol = -1;
                    return;
                }
            }

            if (isValid(selectedRow, selectedCol, r, c, isCapture)) {

                pieces[r][c].setIcon(from);
                pieces[selectedRow][selectedCol].setIcon(null);

                whiteTurn = false;

                reset();

                selectedRow = -1;
                selectedCol = -1;

                // AI MOVE
                makeAIMove();
            }

            reset();
            selectedRow = -1;
            selectedCol = -1;
        }
    }

    // AI OPPONENT (BLACK)
    private static void makeAIMove() {

        ArrayList<int[]> moves = new ArrayList<>();
        Random rand = new Random();

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {

                if (pieces[r][c].getIcon() == null) continue;

                String type = ((ImageIcon) pieces[r][c].getIcon()).getDescription();

                if (!type.startsWith("B")) continue;

                for (int nr = 0; nr < 8; nr++) {
                    for (int nc = 0; nc < 8; nc++) {

                        boolean isCapture = pieces[nr][nc].getIcon() != null;

                        if (isValid(r, c, nr, nc, isCapture)) {

                            ImageIcon from = (ImageIcon) pieces[r][c].getIcon();
                            ImageIcon to = (ImageIcon) pieces[nr][nc].getIcon();

                            pieces[nr][nc].setIcon(from);
                            pieces[r][c].setIcon(null);

                            boolean inCheck = isKingInCheck(false);

                            pieces[r][c].setIcon(from);
                            pieces[nr][nc].setIcon(to);

                            if (!inCheck) {
                                moves.add(new int[]{r, c, nr, nc});
                            }
                        }
                    }
                }
            }
        }

        if (moves.isEmpty()) return;

        int[] move = moves.get(rand.nextInt(moves.size()));

        pieces[move[2]][move[3]].setIcon(pieces[move[0]][move[1]].getIcon());
        pieces[move[0]][move[1]].setIcon(null);

        whiteTurn = true;
    }

    // VALIDATION (your movement files)
    private static boolean isValid(int oldR, int oldC,
                                   int newR, int newC,
                                   boolean isCapture) {

        if (pieces[oldR][oldC].getIcon() == null) return false;

        String type = ((ImageIcon) pieces[oldR][oldC].getIcon()).getDescription();

        switch (type) {

            case "WR":
            case "BR":
                return RookMovement.isValidMove(oldR, oldC, newR, newC);

            case "WB":
            case "BB":
                return BishopMovement.isValidMove(oldR, oldC, newR, newC);

            case "WQ":
            case "BQ":
                return QueenMovement.isValidMove(oldR, oldC, newR, newC);

            case "WN":
            case "BN":
                return KnightMovement.isValidMove(oldR, oldC, newR, newC);

            case "WK":
            case "BK":
                return KingMovement.isValidMove(oldR, oldC, newR, newC);

            case "WP":
                return WhitePawnMovement.isValidMove(oldR, oldC, newR, newC, isCapture);

            case "BP":
                return BlackPawnMovement.isValidMove(oldR, oldC, newR, newC, isCapture);
        }

        return false;
    }

    // CHECK SYSTEM (ASSUMED ALREADY PRESENT)
    private static boolean isKingInCheck(boolean whiteKing) {

        String king = whiteKing ? "WK" : "BK";

        int kr = -1, kc = -1;

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {

                if (pieces[r][c].getIcon() != null) {

                    String t = ((ImageIcon) pieces[r][c].getIcon()).getDescription();

                    if (t.equals(king)) {
                        kr = r;
                        kc = c;
                    }
                }
            }
        }

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {

                if (pieces[r][c].getIcon() != null) {

                    String t = ((ImageIcon) pieces[r][c].getIcon()).getDescription();

                    boolean isWhite = t.startsWith("W");

                    if (isWhite == whiteKing) continue;

                    if (isValid(r, c, kr, kc, true)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    // HIGHLIGHT
    private static void highlight(int r, int c) {

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                if (isValid(r, c, i, j, pieces[i][j].getIcon() != null)) {
                    squares[i][j].setBackground(new Color(130, 151, 105));
                }
            }
        }
    }

    // RESET
    private static void reset() {

        Color light = new Color(240, 217, 181);
        Color dark = new Color(181, 136, 99);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                squares[i][j].setBackground((i + j) % 2 == 0 ? light : dark);
            }
        }
    }
}