public class Date {

    private final int day;
    private final int year;
    private final int month;


    /**
     * Constructor for Date.
     * @param day of the date
     * @param month of the date
     * @param year of the date
     */
    public Date(int day, int month, int year) {
        this.day = day;
        this.year = year;
        this.month = month;
    }

    /**
     * Parses a date with this format 01*01*2001
     * @param s the input string
     * @return the parsed date
     */
    public static Date parseDate(String s){
        return new Date(Integer.parseInt(s.substring(0,1)), Integer.parseInt(s.substring(3,4)),
                Integer.parseInt(s.substring(6,9)));
    }

    /**
     * ToString method
     * @return a human-readable string
     */
    @Override
    public String toString() {
        if(day < 10){
            return "0" + day + "/" + month + "/" + year;
        }
        return day + "/" + month + "/" + year;
    }
}
