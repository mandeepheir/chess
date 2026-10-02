public class BlackPawnMovement {

    public static boolean isValidMove(int oldRow, int oldCol,
                                      int newRow, int newCol,
                                      boolean isCapture) {

        // forward move
        if (oldCol == newCol) {

            // 1 step
            if (newRow == oldRow + 1 && !isCapture) {
                return true;
            }

            // 2 step first move
            if (oldRow == 1 && newRow == oldRow + 2 && !isCapture) {
                return true;
            }
        }

        // diagonal capture
        if (isCapture &&
                newRow == oldRow + 1 &&
                Math.abs(newCol - oldCol) == 1) {
            return true;
        }

        return false;
    }
}