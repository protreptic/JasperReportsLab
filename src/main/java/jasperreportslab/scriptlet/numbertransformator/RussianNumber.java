package jasperreportslab.scriptlet.numbertransformator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Класс преобразут число/сумму в их строковое представление на русском языке
 *
 * @see Number
 * @see BigDecimal
 * @see BigInteger
 *
 * @author Peter Bukhal <bukhal as i-teco.ru> on 21.09.15.
 */
public class RussianNumber {

  private static final String UNITS[] =
      { "ноль", "один", "два", "три", "четыре",
        "пять", "шесть", "семь", "восемь", "девять" };

  private static final String DOZENS[] =
      { "десять", "одиннадцать", "двенадцать", "тринадцать", "четырнадцать",
        "пятнадцать", "шестнадцать", "семнадцать", "восемьнадцать", "девятнадцать" };

  private static final String TENS[] =
      { "", "десять", "двадцать", "тридцать",
        "сорок", "пятьдесят", "шестьдесят",
        "семьдесят", "восемьдесят", "девяносто" };

  private static final String HUNDREDS[] =
      { "", "сто", "двести", "триста", "четыреста", "пятьсот", "шестьсот",
        "семьсот", "восемьсот", "девятьсот" };

  private static final String LIONS[] =
      { "", "тысяча", "миллион", "миллиард", "триллион",
        "квадриллион", "квинтиллион", "секстиллион", "септиллион",
        "октиллион", "нониллион", "дециллион" };

  public static final BigInteger MAX_SUPPORTED = new BigInteger("999999999999999999999999999999999999");

  /**
   * Преобразует число в его строковое представление на русском языке.
   *
   * @param number число для преобразования
   *
   * @return строка с преобразованным в текст числом
   */
  public String format(Number number) {
    if (checkSupported(number)) {
      return formatImpl(number.toString());
    }

    return String.valueOf("");
  }

  /**
   * Преобразует сумму в ее строковое представление на русском языке.
   *
   * @param amount сумма для преобразования
   *
   * @return строка с преобразованной в текст суммой
   */
  public String amount(Number amount) {
    return amount(NumeralUtil.toBigDecimal(amount));
  }

  private String amount(BigDecimal bi) {
    String txt = bi.toPlainString();

    int point = txt.indexOf('.');
    StringBuilder sb = new StringBuilder();
    String rubli = txt;
    if (point > 0) {
      rubli = txt.substring(0, point);
    }
    String celaya = formatImpl(rubli);
    sb.append(celaya);
    sb.append(" ");
    String currency = rubley(bi);

    sb.append(currency);
    sb.append(" ");
    int k = roundKopeyki(bi);
    assert (k >= 0 && k < 100);

    if (k < 10) {
      sb.append("0");
      sb.append(k);
    } else {
      sb.append(k);
    }
    sb.append(" ");
    sb.append(kopeyki(k));
    // to upper case
    NumeralUtil.toUpperCaseFirstLetter(sb);
    return sb.toString();
  }

  private Boolean checkSupported(Number number) {
    Boolean result = false;

    if (number instanceof Integer
        || number instanceof Long
        || number instanceof Short
        ||number instanceof Byte) {
      result = true;
    } else if (number instanceof BigInteger) {
      BigInteger bi = (BigInteger) number;
      if (bi.abs().compareTo(MAX_SUPPORTED) > 0) {
        throw new IllegalArgumentException("Max supported number:" + MAX_SUPPORTED);
      }
    } else {
      throw new IllegalArgumentException("Support only Integer numbers: BigInteger, Integer, Long and Short."
          + "Floating-point is not supported");
    }

    return result;
  }

  private String formatImpl(String text) {
    if ("0".equals(text)) {
      return UNITS[0];
    }
    StringBuilder sb = new StringBuilder();
    if (text.startsWith("-")) {
      sb.append("минус ");
      text = text.substring(1);
    }

    byte n[][] = NumeralUtil.groups(text, 3);

    for (int i = 0; i < n.length; ++i) {
      // 1 = 1000, 2 = 1 000 000
      int k = n.length - i - 1;

      int h = n[i][0]; // сотни
      int t = n[i][1]; // десятки
      int u = n[i][2]; // единицы
      if (h == 0 && t == 0 && u == 0) {
        // этих вобще
        continue;
      }
      // есть сотенные...
      if (h > 0) {
        String sotni = HUNDREDS[h];
        sb.append(sotni);
        sb.append(" ");
      }
      // десяток нет
      if (t == 0) {
        if (u > 0) {
          String txt = UNITS[u];
          if (k == 1) {
            switch (u) {
              case 1:
                txt = "одна";
                break;
              case 2:
                txt = "две";
                break;
            }
          }
          sb.append(txt);
          sb.append(" ");
        }
      } else if (t == 1) {
        sb.append(DOZENS[u]);
        sb.append(" ");
      } else if (t > 1) {
        // 21 - двадцать один и больше
        sb.append(TENS[t]);
        if (u > 0) {
          sb.append(" ");
          String ed =  UNITS[u];
          if (k == 1) {
            switch (u) {
              case 1:
                  ed = "одна";
                  break;
              case 2:
                  ed = "две";
                  break;
              default:
            }
          }
          sb.append(ed);
      }
        sb.append(" ");
      }
      // одна две три четыре пять шесть семь восемь девять десять
      if (k > 0 && (h + t + u > 0)) {
        if (k == 1) {
            sb.append(hundreds(h, t, u));
        } else {
            sb.append(lions(h, t, u, k));
        }
        //
        sb.append(" ");
      }
    }

    return sb.toString().trim();
  }

  private String lions(int h, int t, int u, int k) {
    StringBuilder sb = new StringBuilder();
    sb.append(LIONS[k]);

    if (t == 0 || t > 1) {
      switch (u) {
        case 1:
          break;
        case 2:
        case 3:
        case 4:
          sb.append("а");
          break;
        default:
          sb.append("ов");
          break;
      }
    } else {
      sb.append("ов");
    }

    return sb.toString();
  }

  private String hundreds(int h, int t, int u) {
    String result = "тысяч";

    // от 0 до 9 или h*100
    if (t == 0 || t > 1) {
      switch (u) {
        case 1:
          result = "тысяча";
          break;
        case 2:
        case 3:
        case 4:
          result = "тысячи";
          break;
      }
    }

    return result;
  }

  private int roundKopeyki(BigDecimal b) {
    b = b.abs();
    int k = b.multiply(BigDecimal.valueOf(100)).remainder(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP).intValue();
    return k;
  }

  private String kopeyki(int k) {
    String result;

    if (k > 10 && k < 20) {
      result = "копеек";
    } else {
      int last = k % 10;
      switch (last) {
        case 1:
          result = "копейка";
          break;
        case 2:
        case 3:
        case 4:
          result = "копейки";
          break;
        default:
          result = "копеек";
      }
    }

    return result;
  }

  private String rubley(BigDecimal amount) {
    BigInteger r = amount.setScale(0, RoundingMode.DOWN).toBigInteger();
    String result;
    r = r.remainder(BigInteger.valueOf(100));
    if (r.compareTo(BigInteger.TEN) > 0 && r.compareTo(BigInteger.valueOf(20)) < 0) {
      result = "рублей";
    } else {
      int last = r.remainder(BigInteger.TEN).intValue();
      switch (last) {
        case 1:
          result = "рубль";
          break;
        case 2:
        case 3:
        case 4:
          result = "рубля";
          break;
        default:
          result = "рублей";
      }
    }

    return result;
  }

}
