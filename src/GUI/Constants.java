package GUI;

import logic.communication.transmitters.TransmitterType;

import java.awt.*;
import java.io.File;

public final class Constants {

    public static final int NODE_RADIUS = 25;
    public static final Color NODE_COLOR = Color.BLACK;
    public static final Color NODE_OUTLINE_COLOR = Color.BLACK;
    public static final Stroke NODE_OUTLINE_STROKE = new BasicStroke(2.0f);
    public static final Color NODE_TEXT_COLOR = Color.WHITE;
    public static final Font NODE_FONT = new Font("Arial", Font.BOLD, 17);

    public static final Color BLOCK_OUTLINE_COLOR = Color.BLACK;

    public static final Color EDGE_COLOR = Color.BLACK;
    public static final Stroke EDGE_STROKE = new BasicStroke(2.0f);

    public static final Color PREVIEW_COLOR = new Color(102, 197, 225, 242);
    public static final Stroke PREVIEW_STROKE = new BasicStroke(5, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{10f, 10f, 10f, 10f}, 0.0f);


    // Height constants for the slider
    public static final int MINIMUM_HEIGHT = -100;
    public static final int BLUE_HEIGHT = -30;
    public static final int GREEN_HEIGHT = -10;
    public static final int YELLOW_HEIGHT = 60;
    public static final int MAXIMUM_HEIGHT = 100;

    public static final TransmitterType DEFAULT_TRANSMITTER_TYPE = TransmitterType.FLOODING;

    public static final Color CHOSEN_BUTTON_COLOR = new Color(173, 216, 230);

    public static final String PRESETS_DIRECTORY = System.getProperty("user.dir")+ File.separator+"src"+File.separator+"presets";
    public static final String SCHEDULES_DIRECTORY = System.getProperty("user.dir")+ File.separator+"src"+File.separator+"schedules";


}
