package hardwareParts;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@TeleOp(name="limelightBioBuzz")
public class limelightBioBuzz {

    // ------------ Hardware ------------
    private DcMotor followMotor;
    private Limelight3A limelight;

    // ------------ Pipelines ------------
    private static final int PIPE_BLUE = 0;  // taguri 38-45 (blue)
    private static final int PIPE_RED  = 1;  // taguri 30-37 (red)

    private static final Set<Integer> BLUE_TAGS = new HashSet<>(Arrays.asList(
            38, 39, 40, 41, 42, 43, 44, 45
    ));
    private static final Set<Integer> RED_TAGS = new HashSet<>(Arrays.asList(
            30, 31, 32, 33, 34, 35, 36, 37
    ));

    private int currentPipeline = PIPE_BLUE;

    // ------------ Debounce buton cross ------------
    private boolean prevCross = false;

    // ------------ Ultimul tag văzut ------------
    private int lastSeenTagId  = -1;
    private double lastSeenTx  = 0.0;
    private double lastSeenTy  = 0.0;

    // ============================================================

    public void init() {
        followMotor = hardwareMap.get(DcMotor.class, "followMotor");
        followMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        followMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        followMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        followMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(currentPipeline);

        telemetry.addData("Limelight init", "Pipeline %d (%s)",
                currentPipeline, pipelineName(currentPipeline));
        telemetry.update();
    }

    public void start() {
        limelight.start();
        limelight.pipelineSwitch(currentPipeline);
        prevCross    = false;
        lastSeenTagId = -1;
    }

    /**
     * Apelat din OpMode în fiecare loop.
     * @param crossPressed  gamepad1.cross (sau gamepad1.a pe controlere Xbox)
     */
    public void loop(boolean crossPressed) {
        // --- Switch pipeline pe front pozitiv al butonului ---
        if (crossPressed && !prevCross) {
            currentPipeline = (currentPipeline == PIPE_BLUE) ? PIPE_RED : PIPE_BLUE;
            limelight.pipelineSwitch(currentPipeline);
        }
        prevCross = crossPressed;

        // --- Citire rezultate ---
        Set<Integer> validIds = (currentPipeline == PIPE_BLUE) ? BLUE_TAGS : RED_TAGS;
        boolean found = false;

        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid()) {
            List<LLResultTypes.FiducialResult> fids = result.getFiducialResults();
            if (fids != null) {
                // Cel mai centrat tag valid (cel cu |tx| minim)
                LLResultTypes.FiducialResult best = null;
                for (LLResultTypes.FiducialResult fr : fids) {
                    if (validIds.contains(fr.getFiducialId())) {
                        if (best == null ||
                                Math.abs(fr.getTargetXDegrees()) < Math.abs(best.getTargetXDegrees())) {
                            best = fr;
                        }
                    }
                }
                if (best != null) {
                    found = true;
                    lastSeenTagId = best.getFiducialId();
                    lastSeenTx    = best.getTargetXDegrees();
                    lastSeenTy    = best.getTargetYDegrees();
                }
            }
        }

        // --- Telemetry ---
        telemetry.addData("Pipeline", "%d  (%s)  [CROSS = switch]",
                currentPipeline, pipelineName(currentPipeline));
        if (found) {
            telemetry.addData("Tag văzut", "ID=%d  tx=%.2f°  ty=%.2f°",
                    lastSeenTagId, lastSeenTx, lastSeenTy);
        } else {
            telemetry.addData("Tag văzut", "niciun tag %s detectat",
                    pipelineName(currentPipeline));
        }
        telemetry.update();
    }

    // ============================================================
    // Getteri utili pentru alte clase

    public int  getCurrentPipeline() { return currentPipeline; }
    public int  getLastSeenTagId()   { return lastSeenTagId; }
    public double getLastSeenTx()    { return lastSeenTx; }
    public boolean isBlue()          { return currentPipeline == PIPE_BLUE; }
    public boolean isRed()           { return currentPipeline == PIPE_RED; }

    // ============================================================

    private String pipelineName(int pipe) {
        return (pipe == PIPE_BLUE) ? "BLUE 38-45" : "RED 30-37";
    }
}
