package ir.mosobhani.calendar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * <strong> Persian(Shamsi) calendar </strong>
 * <p>
 * </p>
 * <p>
 * The calendar consists of 12 months, the first six of which are 31 days, the
 * next five 30 days, and the final month 29 days in a normal year and 30 days
 * in a leap year.
 * </p>
 * <p>
 * As one of the few calendars designed in the era of accurate positional
 * astronomy, the Persian calendar uses a very complex leap year structure which
 * makes it the most accurate solar calendar in use today. Years are grouped
 * into cycles which begin with four normal years after which every fourth
 * subsequent year in the cycle is a leap year. Cycles are grouped into grand
 * cycles of either 128 years (composed of cycles of 29, 33, 33, and 33 years)
 * or 132 years, containing cycles of of 29, 33, 33, and 37 years. A great grand
 * cycle is composed of 21 consecutive 128 year grand cycles and a final 132
 * grand cycle, for a total of 2820 years. The pattern of normal and leap years
 * which began in 1925 will not repeat until the year 4745!
 * </p>
 * </p> Each 2820 year great grand cycle contains 2137 normal years of 365 days
 * and 683 leap years of 366 days, with the average year length over the great
 * grand cycle of 365.24219852. So close is this to the actual solar tropical
 * year of 365.24219878 days that the Persian calendar accumulates an error of
 * one day only every 3.8 million years. As a purely solar calendar, months are
 * not synchronized with the phases of the Moon. </p>
 * <p>
 * </p>
 * <p/>
 * <p>
 * <strong>PersianCalendar</strong> by extending Default GregorianCalendar
 * provides capabilities such as:
 * </p>
 * <p>
 * </p>
 * <p/>
 * <li>you can set the date in Persian by setPersianDate(persianYear,
 * persianMonth, persianDay) and get the Gregorian date or vice versa</li>
 * <p>
 * </p>
 * <li>determine is the current date is Leap year in persian calendar or not by
 * IsPersianLeapYear()</li>
 * <p>
 * </p>
 * <li>getPersian short and long Date String getPersianShortDate() and
 * getPersianLongDate you also can set dateDelimiter to assign dateDelimiter of returned
 * dateString</li>
 * <p>
 * </p>
 * <li>Parse string based on assigned dateDelimiter</li>
 * <p>
 * </p>
 * <p>
 * </p>
 * <p>
 * </p>
 * <p>
 * <strong> Example </strong>
 * </p>
 * <p>
 * </p>
 * <p/>
 * <pre>
 * {@code
 *       PersianCalendar persianCal = new PersianCalendar();
 *       System.out.println(persianCal.getPersianShortDate());
 *
 *       persianCal.set(1982, Calendar.MAY, 22);
 *       System.out.println(persianCal.getPersianShortDate());
 *
 *       persianCal.setDateDelimiter(" , ");
 *       persianCal.parse("1361 , 03 , 01");
 *       System.out.println(persianCal.getPersianShortDate());
 *
 *       persianCal.setPersianDate(1361, 3, 1);
 *       System.out.println(persianCal.getPersianLongDate());
 *       System.out.println(persianCal.getTime());
 *
 *       persianCal.addPersianDate(Calendar.MONTH, 33);
 *       persianCal.addPersianDate(Calendar.YEAR, 5);
 *       persianCal.addPersianDate(Calendar.DATE, 50);
 *
 * }
 *
 * <pre>
 *
 * @author Morteza  contact: <a href="mailto:Mortezaadi@gmail.com">Mortezaadi@gmail.com</a>
 * @version 1.1
 */
public class PersianCalendar extends GregorianCalendar {

    public static final long MILLIS_JULIAN_EPOCH = -210866803200000L;
    public static final long MILLIS_OF_A_DAY = 86400000L;
    public static final long PERSIAN_EPOCH = 1948321;
    private static final long serialVersionUID = 5541422440580682494L;
    private static String[] MONTH_NAME = new String[]{"فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور", "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"};
    private int persianYear;
    private int persianMonth;
    private int persianDay;
    private String dateDelimiter = "-";
    private String timeDelimiter = ":";

    public PersianCalendar(long millis) {
        setTimeInMillis(millis);
    }

    public PersianCalendar() {
        setTimeZone(TimeZone.getTimeZone("GMT"));
        set(HOUR_OF_DAY, 0);
        set(MINUTE, 0);
        set(SECOND, 0);
        set(MILLISECOND, 0);
    }

    public PersianCalendar(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        int y = jsonObject.getInt("year");
        int m = jsonObject.getInt("month");
        int d = jsonObject.getInt("day");
        setPersianDate(y, m, d);
    }

    public static long persianToJulian(long year, int month, int day) {
        return 365L * ((ceil(year - 474L, 2820D) + 474L) - 1L) + ((long) Math.floor((682L * (ceil(year - 474L, 2820D) + 474L) - 110L) / 2816D)) + (PERSIAN_EPOCH - 1L) + 1029983L
                * ((long) Math.floor((year - 474L) / 2820D)) + (month < 7 ? 31 * month : 30 * month + 6) + day;
    }

    public static boolean isPersianLeapYear(int persianYear) {
        return ceil((38D + (ceil(persianYear - 474L, 2820L) + 474L)) * 682D, 2816D) < 682L;
    }

    public static long julianToPersian(long julianDate) {
        long persianEpochInJulian = julianDate - persianToJulian(475L, 0, 1);
        long cyear = ceil(persianEpochInJulian, 1029983D);
        long ycycle = cyear != 1029982L ? ((long) Math.floor((2816D * (double) cyear + 1031337D) / 1028522D)) : 2820L;
        long year = 474L + 2820L * ((long) Math.floor(persianEpochInJulian / 1029983D)) + ycycle;
        long aux = (1L + julianDate) - persianToJulian(year, 0, 1);
        int month = (int) (aux > 186L ? Math.ceil((double) (aux - 6L) / 30D) - 1 : Math.ceil((double) aux / 31D) - 1);
        int day = (int) (julianDate - (persianToJulian(year, month, 1) - 1L));
        return (year << 16) | (month << 8) | day;
    }

    public static long ceil(double double1, double double2) {
        return (long) (double1 - double2 * Math.floor(double1 / double2));
    }

    private long convertToMilis(long julianDate) {
        return MILLIS_JULIAN_EPOCH + julianDate * MILLIS_OF_A_DAY
                + ceil(getTimeInMillis() - MILLIS_JULIAN_EPOCH, MILLIS_OF_A_DAY);
    }

    public int getDayOfWeek() {
        int dayOfWeek = getTime().getDay() + 1;
        if (dayOfWeek > 6)
            dayOfWeek -= 7;
        return dayOfWeek;
    }

    protected void calculatePersianDate() {
        long julianDate = ((long) Math.floor((getTimeInMillis() - MILLIS_JULIAN_EPOCH)) / MILLIS_OF_A_DAY);
        long PersianRowDate = julianToPersian(julianDate);
        long year = PersianRowDate >> 16;
        int month = (int) (PersianRowDate & 0xff00) >> 8;
        int day = (int) (PersianRowDate & 0xff);
        this.persianYear = (int) (year > 0 ? year : year - 1);
        this.persianMonth = month;
        this.persianDay = day;
    }

    public void SetDayOfMonth(int day) {
        setPersianDate(persianYear, persianMonth + 1, day);
    }

    public boolean isPersianLeapYear() {
        // calculatePersianDate();
        return isPersianLeapYear(this.persianYear);
    }

    public void setPersianDate(int persianYear, int persianMonth, int persianDay) {
        //persianMonth += 1; // TODO
        setTimeInMillis(convertToMilis(persianToJulian(persianYear > 0 ? persianYear : persianYear + 1, persianMonth - 1, persianDay)));
    }

    public int getPersianYear() {
        // calculatePersianDate();
        return this.persianYear;
    }

    public int getPersianMonth() {
        // calculatePersianDate();
        return this.persianMonth + 1;
    }

    public String getPersianMonthName() {
        return MONTH_NAME[persianMonth] + " ماه " + persianYear;
    }

    public int getPersianDay() {
        // calculatePersianDate();
        return this.persianDay;
    }

    public String getPersianWeekDayName() {
//        switch (get(DAY_OF_WEEK)) {
//            case SATURDAY:
//                return PersianCalendarConstants.persianWeekDays[0];
//            case SUNDAY:
//                return PersianCalendarConstants.persianWeekDays[1];
//            case MONDAY:
//                return PersianCalendarConstants.persianWeekDays[2];
//            case TUESDAY:
//                return PersianCalendarConstants.persianWeekDays[3];
//            case WEDNESDAY:
//                return PersianCalendarConstants.persianWeekDays[4];
//            case THURSDAY:
//                return PersianCalendarConstants.persianWeekDays[5];
//            default:
//                return PersianCalendarConstants.persianWeekDays[6];
//        }
        return "";
    }

    public String getPersianLongDate() {
        return getPersianWeekDayName() + "  " + this.persianDay + "  " + getPersianMonthName() + "  " + this.persianYear;
    }

    public String getPersianLongDateAndTime() {
        return getPersianLongDate() + " ساعت " + get(HOUR_OF_DAY) + ":" + get(MINUTE) + ":" + get(SECOND);
    }

    public String getPersianShortDate() {
        // calculatePersianDate();
        return "" + formatToMilitary(this.persianYear) + dateDelimiter + formatToMilitary(getPersianMonth()) + dateDelimiter + formatToMilitary(this.persianDay);
    }

    public String getPersianDate() {
        return persianDay + " " + MONTH_NAME[persianMonth];
    }

    public String getPersianShortDateTime() {
        return "" + formatToMilitary(this.persianYear) + dateDelimiter + formatToMilitary(getPersianMonth() + 1) + dateDelimiter + formatToMilitary(this.persianDay) + " " + formatToMilitary(this.get(HOUR_OF_DAY)) + ":" + formatToMilitary(get(MINUTE))
                + ":" + formatToMilitary(get(SECOND));
    }

    public String getShortDateTime() {
        String seprator = "#";
        return this.get(YEAR) + dateDelimiter + this.get(MONTH) + dateDelimiter + this.get(DATE) + seprator + this.get(HOUR) + timeDelimiter + this.get(MINUTE) + timeDelimiter + this.get(SECOND);
    }

    private String formatToMilitary(int i) {
        return (i < 9) ? "0" + i : String.valueOf(i);
    }

    public void addPersianDate(int field, int amount) {
        if (amount == 0) {
            return; // Do nothing!
        }

        if (field < 0 || field >= ZONE_OFFSET) {
            throw new IllegalArgumentException();
        }

        if (field == YEAR) {
            setPersianDate(this.persianYear + amount, getPersianMonth(), this.persianDay);
            return;
        } else if (field == MONTH) {
            setPersianDate(this.persianYear + ((getPersianMonth() + amount) / 12), (getPersianMonth() + amount) % 12, this.persianDay);
            return;
        }
        add(field, amount);
        calculatePersianDate();
    }

    public String getDateDelimiter() {
        return dateDelimiter;
    }

    public void setDateDelimiter(String dateDelimiter) {
        this.dateDelimiter = dateDelimiter;
    }

    public String toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("year", persianYear);
        json.put("month", persianMonth + 1);
        json.put("day", persianDay);
        return json.toString();
    }

    @Override
    public String toString() {
        String str = getPersianShortDate();// = super.toString();
        return str;//.substring(0, str.length() - 1) + ",PersianDate=" + getPersianShortDate() + "]";
    }

    @Override
    public void add(int field, int value) {
        super.add(field, value);
        calculatePersianDate();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != "") {
            PersianCalendar cal = (PersianCalendar) obj;
            return cal.getPersianYear() == persianYear
                    && cal.getPersianMonth() == persianMonth + 1
                    && cal.getPersianDay() == persianDay;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public void set(int field, int value) {
        super.set(field, value);
        calculatePersianDate();
    }

    @Override
    public void setTimeInMillis(long millis) {
        super.setTimeInMillis(millis);
        calculatePersianDate();
    }

    @Override
    public void setTimeZone(TimeZone zone) {
        super.setTimeZone(zone);
        calculatePersianDate();
    }
}

