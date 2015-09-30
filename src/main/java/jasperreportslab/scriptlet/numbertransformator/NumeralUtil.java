package jasperreportslab.scriptlet.numbertransformator;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Класс содержит вспомогательные функции для класса RussianNumber
 *
 * @see RussianNumber
 *
 * @author Peter Bukhal <bukhal as i-teco.ru> on 21.09.15.
 */
public final class NumeralUtil {

    /**
     * Разбивает строку на группы заданой величины, помещает их
     * в двумерный массив байт, где массивы байт это группы символов,
     * а элементы массивов это символы исходной строки.
     *
     * @param txt исходная строка для разбиения на группы
     * @param groupSize размер групп для разбиения строки
     * @return массив байт с разбитыми на группы символами строки
     */
    public static byte[][] groups(String txt, int groupSize) {
    	
        int length = txt.length();
        int groupCount = length / groupSize;
        int remainder = length % groupSize;

        int j = 0, k = 0;
        if (remainder > 0) {
            groupCount++;
            k = groupSize - remainder;
        }
        byte nn[][] = new byte[groupCount][groupSize];
        // 1234567 - 1,2,3,4,5,6,7 -- [0,0,1],[2,3,4],[5,6,7]
        for (int i = 0; i < txt.length(); ++i) {
            byte x = (byte) (txt.charAt(i) - '0');
            if (x < 0 || x > 9) {
                throw new IllegalArgumentException("Wrong string:" + txt);
            }
            nn[j][k] = x;
            if (k == groupSize - 1) {
                k = 0;
                j++;
            } else {
                k++;
            }
        }
        return nn;
    }

    /**
     * Производит поиск в массиве строк, если какой либо элемент массива
     * содержит искомую строку то возвращает индекс первого элемента строкового
     * массива для этого элемента. Если искомая строка не содержиться в массиве,
     * то возвращает -1
     *
     * @param array массив строк для поиска
     * @param token искомая строка для поиска
     * @return индекс первого элемента массива с искомой строкой
     */
    public static int search(String[] array, String token) {
        int x = -1;
        for (int i = 0; i < array.length; ++i) {
            if (array[i].equals(token)) {
                x = i;
                break;
            }
        }
        return x;
    }

    /**
     * Преобразует первый символ в строке к верхнему регистру
     *
     * @param sb
     *
     * @see StringBuilder
     */
    public static void toUpperCaseFirstLetter(StringBuilder sb) {
        // to upper case
        char first = sb.charAt(0);
        first = Character.toUpperCase(first);
        sb.setCharAt(0, first);
    }

    /**
     * Преобразует экземпляр класса Number к экземпляру класса BigDecimal
     *
     * @param n число для преобразования
     * @return экземпляр класса BigDecimal
     *
     * @see Number
     * @see BigDecimal
     */
    public static BigDecimal toBigDecimal(Number n) {
        BigDecimal bd;
        if (n == null) {
            throw new IllegalArgumentException("Your number is null");
        } else if (n instanceof BigDecimal) {
            bd = (BigDecimal) n;
        } else if (n instanceof BigInteger) {
            bd = new BigDecimal((BigInteger) n);
        } else if (n instanceof Short
                || n instanceof Long
                || n instanceof Integer) {
            bd = BigDecimal.valueOf(n.longValue());
        } else if (n instanceof Float
                || n instanceof Double) {
            bd = BigDecimal.valueOf(n.doubleValue());
        } else {
            throw new IllegalArgumentException("Unsupported type:" + n);
        }
        return bd;
    }

}
