package hardwareParts;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Sasiu {

    private double speed = 1;// 1.75
    public Telemetry telemetry;
    public HardwareMap hardwaremap;

    public DcMotor rightFront;
    public DcMotor rightRear;
    public DcMotor leftFront;
    public DcMotor leftRear;

    //   public Servo pinionLeft;
    //   public Servo pinionRight;

    public Sasiu(HardwareMap hardwaremap, Telemetry telemetry) {
        this.hardwaremap = hardwaremap;
        this.telemetry = telemetry;

        rightFront = hardwaremap.get(DcMotor.class,"rightFront");
        rightRear  = hardwaremap.get(DcMotor.class,"rightRear");
        leftFront  = hardwaremap.get(DcMotor.class,"leftFront");
        leftRear   = hardwaremap.get(DcMotor.class,"leftRear");


        rightRear.setDirection(DcMotor.Direction.REVERSE);
        rightFront.setDirection(DcMotor.Direction.REVERSE);

        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

       /*pinionLeft = hardwaremap.get(Servo.class, "pinionLeft");
       pinionRight = hardwaremap.get(Servo.class, "pinionRight");*/
    }

    public void Drive(double forward, double strafe, double turn) {

        double denominator = Math.max(1,
                Math.abs(forward) + Math.abs(strafe) + Math.abs(turn)
        );

        double lf = (forward + strafe + turn) / denominator * speed;
        double lr = (forward - strafe + turn) / denominator * speed;
        double rf = (forward - strafe - turn) / denominator * speed;
        double rr = (forward + strafe - turn) / denominator * speed;

        leftFront.setPower(lf);
        leftRear.setPower(lr);
        rightFront.setPower(rf);
        rightRear.setPower(rr);
    }

}
