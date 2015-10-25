package oldschool.scripts.noobcrabs;

public class Startup {
    public long startTime;
    public double eatAtPercentage;
    public int atkxp;
    public int strxp;
    public int defxp;
    public int hpxp;
    public boolean killSteal;
    public boolean pure;

    public boolean hoppingEnabled;
    public int maxPlayersInArea;
    public int maxWorldsToHop;

    public Startup() {
        startTime = 0;
        atkxp = 0;
        strxp = 0;
        defxp = 0;
        hpxp = 0;

        eatAtPercentage = 0.5;
        killSteal = false;
        pure = false;

        hoppingEnabled = false;
        maxPlayersInArea = 8;
        maxWorldsToHop = 10;
    }
}
