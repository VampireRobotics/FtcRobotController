package teleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import hardwareParts.absoluteEncoder;

@TeleOp(name = "Test Absolute Encoder")
public class testAbsoluteEncoder extends LinearOpMode {

    @Override
    public void runOpMode() {
        absoluteEncoder encoder = new absoluteEncoder(hardwareMap);

        telemetry.addLine("Ready");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            encoder.update();
            telemetry.addData("Voltage", "%.3f V", encoder.getVoltage());
            telemetry.addData("Angle",   "%.1f deg", encoder.getAngle());
            telemetry.update();
        }
    }
}
