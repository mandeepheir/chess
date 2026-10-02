public class QueenMovement {

    public static boolean isValidMove(int oldRow, int oldCol,
                                      int newRow, int newCol) {

        return oldRow == newRow
                || oldCol == newCol
                || Math.abs(newRow - oldRow)
                   == Math.abs(newCol - oldCol);
    }
}