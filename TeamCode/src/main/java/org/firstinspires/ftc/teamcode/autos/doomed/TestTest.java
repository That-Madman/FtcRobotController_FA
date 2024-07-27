package org.firstinspires.ftc.teamcode.autos.doomed;

import com.qualcomm.hardware.digitalchickenlabs.OctoQuad;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class TestTest extends LinearOpMode {
    //@Override
    private final int ODO_LEFT  = 0; // Facing forward direction on left side of robot (Axial motion)
    private final int ODO_RIGHT = 1; // Facing forward direction on right side or robot (Axial motion )
    private final int ODO_PERP  = 2; // Facing perpendicular direction at the center of the robot (Lateral motion)

    // Declare the OctoQuad object and members to store encoder positions and velocities
    private OctoQuad octoquad;

    private int         posLeft;
    private int         posRight;
    private int         posPerp;

    public void runOpMode() throws InterruptedException {
        octoquad = hardwareMap.get(OctoQuad.class, "octoquad");

        // Read the Firmware Revision number from the OctoQuad and display it as telemetry.
        telemetry.addData("OctoQuad Firmware Version ", octoquad.getFirmwareVersion());

        // Reverse the count-direction of any encoder that is not what you require.
        // Eg: if you push the robot forward and the left encoder counts down, then reverse it so it counts up.
        octoquad.setSingleEncoderDirection(ODO_LEFT,  OctoQuad.EncoderDirection.REVERSE);
        octoquad.setSingleEncoderDirection(ODO_RIGHT, OctoQuad.EncoderDirection.FORWARD);
        octoquad.setSingleEncoderDirection(ODO_PERP,  OctoQuad.EncoderDirection.FORWARD);

        // Any changes that are made should be saved in FLASH just in case there is a sensor power glitch.
        octoquad.saveParametersToFlash();

        telemetry.addLine("\nPress Play to read encoder values");
        telemetry.update();

        waitForStart();
        waitForStart();}}

    /*while (opModeIsActive()) {
        odom.update();
        telemetry.addData("x", odom.getX());
        telemetry.addData("x", odom.getY());
        telemetry.addData("Pos", odom.x + ", " + odom.y + ", " + odom.globalAngle);
        telemetry.update();
    }/*
}
*/