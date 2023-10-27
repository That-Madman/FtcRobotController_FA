package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class op extends OpMode {
    hardware nick = new hardware();
    @Override
    public void init() {
        nick.init(hardwareMap);
    }

    @Override
    public void loop() {
        telemetry.addData("color", nick.getAmountRed() );
    }
}
