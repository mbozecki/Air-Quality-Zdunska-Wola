package pl.edu.uwr.pum.air_app;

//klasa, z ktorej metod korzytaja inne klasy
public class Utility {

    public static String[] getAdvices() {
        return new String[]{"Czyste powietrze!", "Dobre powietrze", "Słaba jakość powietrza", "Zła jakość powietrza", "Nie wychodź z domu"};
    }

    public static int convertPM25ToCAQI(double num) {
        if (num < 100)
            return (int) (62.6533 * Math.log(0.0642832 * num + 1.5178) - 27.5731);
        else
            return (int) num;
    }

    public static int convertPM10ToCAQI(double num) {
        if (num <= 50)
            return (int) num;
        else if (num> 50 || num <75)
        {
            return (int) (0.624896 *num + 18.7989);

        }
        else if (num > 75 || num < 180)
            return (int) (Math.pow(0.000147168 * num, 2.36093)+68.8515);
        else
            return (int) (Math.pow(1.27998 * num, 0.873652) - 19.7017);
    }

    public static int getCAQI(double PM10, double PM25) {
        int a = convertPM10ToCAQI(PM10);
        int b = convertPM25ToCAQI(PM25);
        return Math.max(a, b);
    }

    public static int percentageNormPM10(double ug){ return (int) Math.round((ug/50) *100); }


    public static int percentageNormPM25(double ug){ return (int) Math.round((ug/25)*100); }
}
