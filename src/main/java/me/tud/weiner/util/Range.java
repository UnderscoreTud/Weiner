package me.tud.weiner.util;

public class Range {

    private final Number from;
    private final Number to;

    public Range(long from, long to) {
        this.from = Math.min(from, to);
        this.to = Math.max(from, to);
    }

    public Range(double from, double to) {
        this.from = Math.min(from, to);
        this.to = Math.max(from, to);
    }

    public Number getFrom() {
        return from;
    }

    public Number getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "Range{" +
                "from=" + from +
                ", to=" + to +
                '}';
    }
}
