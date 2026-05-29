package command;

public class TurnOnLightCommand implements ICommand {
    private Light light;
    public TurnOnLightCommand(Light light) { this.light = light; }
    @Override public void execute() { light.turnOn(); }
}
