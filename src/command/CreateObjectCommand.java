package command;

import shapes.model.AbstractGraphicObject;
import shapes.controller.GraphicObjectController;

public class CreateObjectCommand implements Command {
    private final AbstractGraphicObject object;
    private final GraphicObjectController controller;

    public CreateObjectCommand(AbstractGraphicObject object, GraphicObjectController controller) {
        this.object = object;
        this.controller = controller;
    }

    @Override
    public void execute() {
        controller.addGraphicObject(object);
    }

    @Override
    public void undo() {
        controller.removeGraphicObject(object);
    }

    @Override
    public boolean doIt() {
        return false;
    }

    @Override
    public void undoIt() {
    }
}
