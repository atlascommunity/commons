package ru.mail.jira.plugins.commons;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@SuppressWarnings("UnusedDeclaration")
public class LocalUtils {
  public static final String[] MONTH_NAMES_NOMINATIVE =
      new String[] {
        "Январь",
        "Февраль",
        "Март",
        "Апрель",
        "Май",
        "Июнь",
        "Июль",
        "Август",
        "Сентябрь",
        "Октябрь",
        "Ноябрь",
        "Декабрь"
      };
  public static final String[] MONTH_NAMES_GENITIVE =
      new String[] {
        "января",
        "февраля",
        "марта",
        "апреля",
        "мая",
        "июня",
        "июля",
        "августа",
        "сентября",
        "октября",
        "ноября",
        "декабря"
      };
  private static final String ZERO_CAPTION = "ноль";
  private static final String MINUS_CAPTION = "минус";
  private static final String[][] MAGNITUDE_CAPTIONS =
      new String[][] {
        {null, null, null, null},
        {"FEMALE", "тысяча", "тысячи", "тысяч"},
        {null, "миллион", "миллиона", "миллионов"},
        {null, "миллиард", "миллиарда", "миллиардов"}
      };
  private static final int SEX_MAGNITUDE = 0;
  private static final int ONE_MAGNITUDE = 1;
  private static final int FOUR_MAGNITUDE = 2;
  private static final int MANY_MAGNITUDE = 3;
  private static final String[][] DIGIT_CAPTIONS =
      new String[][] {
        {null, null, "десять", null, null},
        {"один", "одна", "одиннадцать", "десять", "сто"},
        {"два", "две", "двенадцать", "двадцать", "двести"},
        {"три", "три", "тринадцать", "тридцать", "триста"},
        {"четыре", "четыре", "четырнадцать", "сорок", "четыреста"},
        {"пять", "пять", "пятнадцать", "пятьдесят", "пятьсот"},
        {"шесть", "шесть", "шестнадцать", "шестьдесят", "шестьсот"},
        {"семь", "семь", "семнадцать", "семьдесят", "семьсот"},
        {"восемь", "восемь", "восемнадцать", "восемьдесят", "восемьсот"},
        {"девять", "девять", "девятнадцать", "девяносто", "девятьсот"}
      };
  private static final int MALE_DIGIT = 0;
  private static final int FEMALE_DIGIT = 1;
  private static final int TEEN_DIGIT = 2;
  private static final int DECADE_DIGIT = 3;
  private static final int HUNDRED_DIGIT = 4;

  public static String numberToCaption(int number) {
    if (number == 0) return ZERO_CAPTION;

    List<String> result = new ArrayList<String>();

    if (number < 0) {
      result.add(MINUS_CAPTION);
      number = -number;
    }

    for (int magnitude = MAGNITUDE_CAPTIONS.length - 1; magnitude >= 0; magnitude--) {
      int magnitudeMultiplier = (int) Math.pow(1000, magnitude);
      int part = number / magnitudeMultiplier;
      number %= magnitudeMultiplier;

      if (part > 0) {
        if (part >= 100) {
          result.add(DIGIT_CAPTIONS[part / 100][HUNDRED_DIGIT]);
          part %= 100;
        }
        if (part >= 10 && part < 20) {
          result.add(DIGIT_CAPTIONS[part - 10][TEEN_DIGIT]);
          part = 0;
        }
        if (part >= 20) {
          result.add(DIGIT_CAPTIONS[part / 10][DECADE_DIGIT]);
          part %= 10;
        }
        if (part > 0) {
          boolean female = MAGNITUDE_CAPTIONS[magnitude][SEX_MAGNITUDE] != null;
          result.add(DIGIT_CAPTIONS[part][female ? FEMALE_DIGIT : MALE_DIGIT]);
        }

        if (magnitude > 0)
          switch (part) {
            case 1:
              result.add(MAGNITUDE_CAPTIONS[magnitude][ONE_MAGNITUDE]);
              break;
            case 2:
            case 3:
            case 4:
              result.add(MAGNITUDE_CAPTIONS[magnitude][FOUR_MAGNITUDE]);
              break;
            default:
              result.add(MAGNITUDE_CAPTIONS[magnitude][MANY_MAGNITUDE]);
              break;
          }
      }
    }

    return StringUtils.join(result, " ");
  }

  public static SimpleDateFormat updateMonthNames(
      SimpleDateFormat simpleDateFormat, String[] monthNames) {
    DateFormatSymbols dateFormatSymbols = simpleDateFormat.getDateFormatSymbols();
    dateFormatSymbols.setMonths(monthNames);
    simpleDateFormat.setDateFormatSymbols(dateFormatSymbols);
    return simpleDateFormat;
  }

  public static boolean isWeekend(Calendar calendar) {
    return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
        || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
  }

  public static boolean isHoliday(Calendar calendar) {
    int month = calendar.get(Calendar.MONTH);
    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    if (month == Calendar.JANUARY && dayOfMonth < 9) return true;
    if (month == Calendar.FEBRUARY && dayOfMonth == 23) return true;
    if (month == Calendar.MARCH && dayOfMonth == 8) return true;
    if (month == Calendar.MAY && (dayOfMonth == 1 || dayOfMonth == 9)) return true;
    if (month == Calendar.JUNE && dayOfMonth == 12) return true;
    return month == Calendar.NOVEMBER && dayOfMonth == 4;
  }
}
