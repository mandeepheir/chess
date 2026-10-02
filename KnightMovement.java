public class KnightMovement {

    public static boolean isValidMove(int oldRow, int oldCol,
                                      int newRow, int newCol) {

        int rowDiff = Math.abs(newRow - oldRow);
        int colDiff = Math.abs(newCol - oldCol);

        return (rowDiff == 2 && colDiff == 1)
                || (rowDiff == 1 && colDiff == 2);
    }
}