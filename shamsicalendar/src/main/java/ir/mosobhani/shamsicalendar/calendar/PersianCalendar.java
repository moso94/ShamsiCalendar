package ir.mosobhani.shamsicalendar.calendar;

import java.util.GregorianCalendar;
import java.util.TimeZone;

public class PersianCalendar extends GregorianCalendar {

    private static final long MILLIS_JULIAN_EPOCH = -210866803200000L;
    private static final long MILLIS_OF_A_DAY = 86400000L;
    private static final long PERSIAN_EPOCH = 1948321;
    private static final long serialVersionUID = 5541422440580682494L;
    private static String[] persianNumbers = new String[]{"۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹"};
    private static String[] MONTH_NAME = new String[]{"فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور", "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"};
    private int persianYear;
    private int persianMonth;
    private int persianDay;
    private String dateDelimiter = "-";
    private String timeDelimiter = ":";

    public PersianCalendar(PersianCalendar calendar) {
        setTimeZone(TimeZone.getTimeZone("GMT"));
        setPersianDate(calendar.getPersianYear(), calendar.getPersianMonth(), calendar.getPersianDay());
    }

    public PersianCalendar(long millis) {
        setTimeZone(TimeZone.getTimeZone("GMT"));
        setTimeInMillis(millis);
    }

    public PersianCalendar() {
        setTimeZone(TimeZone.getTimeZone("GMT"));
        set(HOUR_OF_DAY, 0);
        set(MINUTE, 0);
        set(SECOND, 0);
        set(MILLISECOND, 0);
    }

    public PersianCalendar(int persianYear, int persianMonth, int persianDay) {
        setTimeZone(TimeZone.getTimeZone("GMT"));
        setPersianDate(persianYear, persianMonth, persianDay);
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
        this.persianMonth = month + 1;
        this.persianDay = day;
    }

    public void SetDayOfMonth(int day) {
        setPersianDate(persianYear, persianMonth, day);
    }

    public boolean isPersianLeapYear() {
        return isPersianLeapYear(this.persianYear);
    }

    public void setPersianDate(int persianYear, int persianMonth, int persianDay) {
        setTimeInMillis(convertToMilis(persianToJulian(persianYear > 0 ? persianYear : persianYear + 1, persianMonth - 1, persianDay)));
    }

    public int getPersianYear() {
        return this.persianYear;
    }

    public int getPersianMonth() {
        return this.persianMonth;
    }

    public String getPersianMonthName() {
        return MONTH_NAME[persianMonth - 1] + " ماه " + toPersianNumber(String.valueOf(persianYear));
    }

    public int getPersianDay() {
        return this.persianDay;
    }

    public String getPersianDayStr() {
        return toPersianNumber(String.valueOf(this.persianDay));
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

    public static String toPersianNumber(String text) {
        if (text.isEmpty())
            return "";
        String out = "";
        int length = text.length();
        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);
            if ('0' <= c && c <= '9') {
                int number = Integer.parseInt(String.valueOf(c));
                out += persianNumbers[number];
            } else if (c == '٫') {
                out += '،';
            } else {
                out += c;
            }

        }
        return out;
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
                    && cal.getPersianMonth() == persianMonth
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

