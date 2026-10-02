public class WhitePawnMovement {

    public static boolean isValidMove(int oldRow, int oldCol,
                                      int newRow, int newCol,
                                      boolean isCapture) {

        // forward move
        if (oldCol == newCol) {

            // 1 step forward
            if (newRow == oldRow - 1 && !isCapture) {
                return true;
            }

            // 2 step forward (first move only - optional simple rule)
            if (oldRow == 6 && newRow == oldRow - 2 && !isCapture) {
                return true;
            }
        }

        // diagonal capture
        if (isCapture &&
                newRow == oldRow - 1 &&
                Math.abs(newCol - oldCol) == 1) {
            return true;
        }

        return false;
    }
}