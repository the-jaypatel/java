interface Switchable {
    void on();
    void off();

    default void toggle() {
        on();
    }
}

class Fan implements Switchable {
    public void on() {
        System.out.println("Fan is ON");
    }
    public void off() {
        System.out.println("Fan is OFF");
    }
}

class Light implements Switchable {
    public void on() {
        System.out.println("Light is ON");
    }
    public void off() {
        System.out.println("Light is OFF");
    }
}

@FunctionalInterface
interface SwitchPermission {
    boolean maySwitchOn(Switchable device, int hour);
}

public class Main {
    public static void main(String[] args) {
        Switchable[] devices = {
            new Fan(),
            new Light()
        };

        System.out.println("Toggling Devices:");
        for (Switchable device : devices) {
            device.toggle();
        }

        SwitchPermission permission1 = new SwitchPermission() {
            public boolean maySwitchOn(Switchable device, int hour) {
                return hour >= 6 && hour <= 22;
            }
        };

        SwitchPermission permission2 =
                (device, hour) -> hour >= 6 && hour <= 22;

        int hour = 10;

        System.out.println("\nAnonymous Class:");
        System.out.println(permission1.maySwitchOn(devices[0], hour));

        System.out.println("\nLambda:");
        System.out.println(permission2.maySwitchOn(devices[1], hour));
    }
}
