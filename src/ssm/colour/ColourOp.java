package ssm.colour;

public class ColourOp {
    public static int sum(int a, int b) {
        int aA = a >> 24 & 255;
        int aR = a >> 16 & 255;
        int aG = a >> 8 & 255;
        int aB = a & 255;

        int bA = b >> 24 & 255;
        int bR = b >> 16 & 255;
        int bG = b >> 8 & 255;
        int bB = b & 255;

        int result = (aB + bB) | (aG + bG) << 8 | (aR + bR) << 16 | (aA + bA) << 24;
        return result;
    }

    public static int minus(int a, int b) {
        int aA = a >> 24 & 255;
        int aR = a >> 16 & 255;
        int aG = a >> 8 & 255;
        int aB = a & 255;

        int bA = b >> 24 & 255;
        int bR = b >> 16 & 255;
        int bG = b >> 8 & 255;
        int bB = b & 255;

        int result = (aB - bB) | (aG - bG) << 8 | (aR - bR) << 16 | (aA - bA) << 24;
        return result;
    }

    public static int divide(int a, int b) {
        int aA = a >> 24 & 255;
        int aR = a >> 16 & 255;
        int aG = a >> 8 & 255;
        int aB = a & 255;

        int bA = b >> 24 & 255;
        int bR = b >> 16 & 255;
        int bG = b >> 8 & 255;
        int bB = b & 255;

        int result = (aB / bB) | (aG / bG) << 8 | (aR / bR) << 16 | (aA / bA) << 24;
        return result;
    }
}
