package command;

public class HomeAutomationRemote {
    private ICommand command;
    public void setCommand(ICommand command) { this.command = command; }
    public void buttonPressed() { command.execute(); }
}
