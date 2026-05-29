package command;

public class Demo {
    public static void main(String[] args) {
        Light livingRoomLight = new Light();
        Fan   livingRoomFan   = new Fan();
        Light bedRoomLight    = new Light();
        Fan   bedRoomFan      = new Fan();

        HomeAutomationRemote remote = new HomeAutomationRemote();

        remote.setCommand(new TurnOnLightCommand(livingRoomLight));
        remote.buttonPressed();

        remote.setCommand(new TurnOnLightCommand(bedRoomLight));
        remote.buttonPressed();

        remote.setCommand(new StartFanCommand(livingRoomFan));
        remote.buttonPressed();

        remote.setCommand(new StopFanCommand(livingRoomFan));
        remote.buttonPressed();

        remote.setCommand(new StartFanCommand(bedRoomFan));
        remote.buttonPressed();

        remote.setCommand(new StopFanCommand(bedRoomFan));
        remote.buttonPressed();
    }
}
