package GUI.modes;

import GUI.MainSimulationWindow;

public class ModeFactory {

    public final AddNodeMode ADD_NODE;
    public final AddBlockMode ADD_BLOCK;
    public final BlankMode BLANK;
    public final EditMode EDIT;

    public ModeFactory(MainSimulationWindow window) {
        ADD_NODE = new AddNodeMode(window);
        ADD_BLOCK = new AddBlockMode(window);
        BLANK = new BlankMode(window);
        EDIT = new EditMode(window);
    }


}
