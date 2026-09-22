package hardwareParts;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class absoluteEncoder {

    private final AnalogInput encoder;
    public double voltage;
    public double angle;

    public absoluteEncoder(HardwareMap hardwareMap) {
        encoder = hardwareMap.get(AnalogInput.class, "encoder");
    }

    public void update() {
        voltage = encoder.getVoltage();
        angle = voltage / 3.3 * 360.0;
    }

    public double getVoltage() { return voltage; }
    public double getAngle()   { return angle; }
}