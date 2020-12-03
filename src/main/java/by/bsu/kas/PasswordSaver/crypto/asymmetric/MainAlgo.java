package by.bsu.kas.PasswordSaver.crypto.asymmetric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static by.bsu.kas.PasswordSaver.crypto.asymmetric.SupportAlgo.*;
import static by.bsu.kas.PasswordSaver.crypto.asymmetric.Constants.*;

public class MainAlgo {
    //Generating keys of specified length
    public static List<BigInteger> generateKeys(int length) {
        BigInteger  p, q;
        do {
            p = generateRandomPrimeNumber(length / 2);
            q = generateRandomPrimeNumber(length / 2);
        } while (p.compareTo(q) == 0
                || extendedEuclidean(E, p.subtract(ONE)).get(0).compareTo(ONE) != 0
                || extendedEuclidean(E, q.subtract(ONE)).get(0).compareTo(ONE) != 0);
        BigInteger number = q.multiply(p);
        BigInteger y = q.subtract(ONE).multiply(p.subtract(ONE));
        BigInteger d = extendedEuclidean(E, y).get(1);
        List<BigInteger> resultList = new ArrayList<>(3);
        resultList.add(E);
        resultList.add(number);
        resultList.add(d);
        return resultList;
    }

    //Encryption
    public static BigInteger encrypt(BigInteger number, BigInteger e, BigInteger message) {
        return moduloPower(message, e, number);
    }

    //Decryption
    public static BigInteger decrypt(BigInteger number, BigInteger d, BigInteger message) {
        return moduloPower(message, d, number);
    }

    //Generating a random number of length k
    public static BigInteger generateRandomPrimeNumber(int k) {
        BigInteger number;
        do {
            StringBuilder stringBuilder = new StringBuilder(k);
            stringBuilder.append(1);
            Random random = new Random();
            for (int i = 0; i < k - 2; i++) {
                int randomNumber = random.nextInt(2);
                stringBuilder.append(randomNumber);
            }
            stringBuilder.append(1);
            number = new BigInteger(stringBuilder.toString(), 2);
        } while (!isPrimeFerma(number) && !isPrimeMillerRabin(number));
        return number;
    }

    //Fermat primalty test
    public static boolean isPrimeFerma(BigInteger number) {
        BigInteger a = new BigInteger(bigIntegerToBinary(number).size(), new Random());
        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            while (a.compareTo(ZERO) == 0 || a.compareTo(number.subtract(ONE)) > 0) {
                a = new BigInteger(bigIntegerToBinary(number).size(), new Random());
            }
            if (extendedEuclidean(a, number).get(0).compareTo(ONE) != 0 ||
                    moduloPower(a, number.subtract(ONE), number).compareTo(ONE) != 0) {
                return false;
            }
        }
        return true;
    }

    //Miller-Rabin primalty test
    public static boolean isPrimeMillerRabin(BigInteger number) {
        BigInteger t = number.subtract(BigInteger.ONE);
        BigInteger s = BigInteger.ZERO;
        while (t.mod(TWO).compareTo(BigInteger.ZERO) == 0) {
            t = t.divide(TWO);
            s = s.add(BigInteger.ONE);
        }
        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            BigInteger a;
            do {
                a = new BigInteger(bigIntegerToBinary(number).size(), new Random());
            } while (a.compareTo(TWO) < 0 || a.compareTo(number.subtract(TWO)) > -1);
            BigInteger x = moduloPower(a, t, number);
            if (x.compareTo(ONE) == 0 || x.compareTo(number.subtract(ONE)) == 0) {
                continue;
            }
            for (BigInteger j = ONE; j.compareTo(s) < 0; j = j.add(ONE)) {
                x = moduloPower(x, TWO, number);
                if (x.compareTo(ONE) == 0) {
                    return false;
                }
                if (x.compareTo(number.subtract(ONE)) != 0) {
                    break;
                }
            }
            if (x.compareTo(number.subtract(ONE)) != 0) {
                return false;
            }
        }
        return true;
    }
}
