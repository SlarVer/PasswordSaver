package by.bsu.kas.PasswordSaver.crypto.asymmetric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static by.bsu.kas.PasswordSaver.crypto.asymmetric.Constants.*;

public class SupportAlgo {
    public String BigIntegerToString(BigInteger bigInteger) {
        return Base64.getEncoder().encodeToString(bigInteger.toByteArray());
    }

    public static BigInteger stringToBigInteger(String string) {
        return new BigInteger(Base64.getDecoder().decode(string));
    }

    public static List<Integer> bigIntegerToBinary(BigInteger bigInteger) {
        List<Integer> binary = new ArrayList<>();
        while (bigInteger.compareTo(ZERO) != 0) {
            binary.add(bigInteger.mod(TWO).intValue());
            bigInteger = bigInteger.divide(TWO);
        }
        return binary;
    }

    //Extended Euclidean algorithm
    public static List<BigInteger> extendedEuclidean(BigInteger a, BigInteger b) {
        List<BigInteger> r = new ArrayList<>(3);
        List<BigInteger> x = new ArrayList<>(3);
        List<BigInteger> y = new ArrayList<>(3);
        BigInteger q;
        r.add(a);
        r.add(b);
        x.add(ONE);
        x.add(ZERO);
        y.add(ZERO);
        y.add(ONE);
        int i = 1;
        while (r.get(i).compareTo(ZERO) != 0) {
            i++;
            q = r.get(i - 2).divide(r.get(i - 1));
            r.add(r.get(i - 2).subtract(q.multiply(r.get(i - 1))));
            x.add(x.get(i - 2).subtract(q.multiply(x.get(i - 1))));
            y.add(y.get(i - 2).subtract(q.multiply(y.get(i - 1))));
        }
        if (x.get(i - 1).compareTo(ZERO) < 0) {
            x.set(i - 1, x.get(i - 1).add(b));
        }
        List<BigInteger> resultList = new ArrayList<>(3);
        resultList.add(r.get(i - 1));
        resultList.add(x.get(i - 1));
        resultList.add(y.get(i - 1));
        return resultList;
    }

    //Raising a to power b modulo n (number)
    public static BigInteger moduloPower(BigInteger a, BigInteger b, BigInteger number) {
        List<Integer> binary = bigIntegerToBinary(b);
        BigInteger u = ONE;
        BigInteger v = a;
        for (Integer num : binary) {
            if (num != 0) {
                u = u.multiply(v).mod(number);
            }
            v = v.multiply(v).mod(number);
        }
        return u;
    }
}
