package command;

public interface Command {
	void execute();

	void undo();

	boolean doIt();

	void undoIt();
}
