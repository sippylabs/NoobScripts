package oldschool.scripts.NoobCrabs.Enums;

public enum Target {
    CRAB(
            new int[]{100, 102}
    ),
    ROCK(
            new int[]{101, 103}
    );

    private int[] ids = {-1};

    Target(final int[] ids) {
        this.ids = ids;
    }

    public int[] ids() {
        return this.ids;
    }
}
