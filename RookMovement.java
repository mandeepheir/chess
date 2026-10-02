public class RookMovement {

    public static boolean isValidMove(int oldRow, int oldCol,
                                      int newRow, int newCol) {

        return oldRow == newRow || oldCol == newCol;
    }
}