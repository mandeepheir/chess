public class BishopMovement {

    public static boolean isValidMove(int oldRow, int oldCol,
                                      int newRow, int newCol) {

        return Math.abs(newRow - oldRow)
                == Math.abs(newCol - oldCol);
    }
}