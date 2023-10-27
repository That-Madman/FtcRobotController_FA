package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class hardware {
    private ColorSensor colorSensor;

    public void init(HardwareMap hwMap) {
        colorSensor = hwMap.get(ColorSensor.class, "col");
    }

    int getAmountRed(){
        return colorSensor.red();
    }
}
