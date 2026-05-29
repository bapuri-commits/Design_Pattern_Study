package command;

public class StartFanCommand implements ICommand {
    private Fan fan;
    public StartFanCommand(Fan fan) { this.fan = fan; }
    @Override public void execute() { fan.start(); }
}
