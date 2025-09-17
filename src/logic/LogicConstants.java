package logic;

public final class LogicConstants {

    // Schedule CSV column indices
    public static final int CSV_TICK_COL = 0;
    public static final int CSV_RECV_COL = 1;
    public static final int CSV_PAYLOAD_COL = 2;

    // Physics constants
    public static final double EDGE_PROBABILITY_THRESHOLD = 0.9; // TODO: should this be very low or very high?

    public static final double TRANSMITTER_MAX_RANGE = 1000.0; // in world units
    public static final double CLOSE_RANGE_FRAC = 0.3; // fraction of TRANSMITTER_RANGE where prob is 1.0
    public static final double TRANSMITTER_CLOSE_RANGE = CLOSE_RANGE_FRAC * TRANSMITTER_MAX_RANGE;


}
