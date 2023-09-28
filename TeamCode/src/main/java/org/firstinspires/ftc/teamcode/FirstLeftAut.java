package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@Autonomous
public class FirstLeftAut extends OpMode {
    Stage_Directions board = new Stage_Directions();

    @Override
    public void init() {
        board.getHW(hardwareMap);
        board.changeToPos();
    }

    @Override
    public void loop() {
        board.posRunSide(-1528);
    }
}
