package org.firstinspires.ftc.teamcode.opmode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "AutoDaniel Red", group = "Experimental")
public class AutoDanielRed extends AutoDaniel {
    @Override
    protected boolean isMirrored() {
        return true;
    }
}
