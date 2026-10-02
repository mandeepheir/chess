public class KingMovement {

    public static boolean isValidMove(int oldRow, int oldCol,
                                      int newRow, int newCol) {

        return Math.abs(newRow - oldRow) <= 1
                && Math.abs(newCol - oldCol) <= 1;
    }
}