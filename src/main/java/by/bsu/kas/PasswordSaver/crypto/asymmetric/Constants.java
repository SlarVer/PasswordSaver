package by.bsu.kas.PasswordSaver.crypto.asymmetric;

import java.math.BigInteger;

public class Constants {
    public static int KEY_SIZE_ASYMMETRIC = 512;
    public static final int KEY_SIZE_AES = 256;
    public static final int NUMBER_OF_ITERATIONS = 100;
    public static final BigInteger E = BigInteger.valueOf(65537);
    public static final BigInteger ZERO = BigInteger.valueOf(0);
    public static final BigInteger ONE = BigInteger.valueOf(1);
    public static final BigInteger TWO = BigInteger.valueOf(2);
}
