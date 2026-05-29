package command;

public class TurnOffLightCommand implements ICommand {
    private Light light;
    public TurnOffLightCommand(Light light) { this.light = light; }
    @Override public void execute() { light.turnOff(); }
}
