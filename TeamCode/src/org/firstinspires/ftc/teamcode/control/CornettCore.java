package org.firstinspires.ftc.teamcode.control;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.hardware.Robot;
import org.firstinspires.ftc.teamcode.math.Curve;
import org.firstinspires.ftc.teamcode.math.Point;
import org.firstinspires.ftc.teamcode.util.AngleUtil;
import org.firstinspires.ftc.teamcode.util.MiniPID;

public class CornettCore extends OpMode {

    private boolean ranOnInit = false;
    private Robot robot;

    private double zero = 1e-9;
    private boolean end = false;
    boolean done = false;

    public double output = 0, direction = 0, pidOutput = 0;

    public CornettCore(Robot robot) {
        this.robot = robot;
    }

    public void rotate(double heading, double anglePrecision) {  // Note this is currently Async and does not wait for other functions to be called
        MiniPID turnPID = new MiniPID(1, 0, 0);         // This will cancelled and run the later iteration of this function without wait
                                                                //  Angle Precision/Tolerance Has yet to be setup | PID has to be defined somewhere else, DT maybe?
        turnPID.setSetpoint(heading);
        turnPID.setOutputLimits(-1, 1);

        turnPID.setError(Curve.getShortestDistance(robot.pos.getHeading(), heading));

        direction = Curve.getDirection(robot.pos.getHeading(), heading);
        pidOutput = turnPID.getOutput(AngleUtil.deNormalizeAngle(robot.pos.getHeading()));
        output = direction * pidOutput;

        robot.DriveTrain.setMotorPowers(0, 0, output);
    }

    public void runToPosition(double x, double y, double heading) {
        MiniPID xPID = new MiniPID(1, 0, 0);
        MiniPID yPID = new MiniPID(1, 0, 0);
        MiniPID headingPID = new MiniPID(3, 0, 0);

        robot.updateOdometry();

        xPID.setSetpoint(x);
        yPID.setSetpoint(y);
        headingPID.setSetpoint(heading);

        xPID.setOutputLimits(-1, 1);
        yPID.setOutputLimits(-1, 1);
        headingPID.setOutputLimits(-1, 1);

        xPID.setError(Math.abs(x-robot.pos.x));
        yPID.setError(Math.abs(y-robot.pos.y));
        headingPID.setError(Curve.getShortestDistance(robot.pos.getHeading(), heading));  // Only for Heading

        double xPIDOutput = xPID.getOutput(robot.pos.x);
        double yPIDOutput = yPID.getOutput(robot.pos.y);
        double headingPIDOutput = headingPID.getOutput(AngleUtil.deNormalizeAngle(robot.pos.getHeading()));

        double theta = Curve.getAngle(robot.pos.toPoint(), new Point(x, y));

        output = Math.toDegrees(theta);

        double xRawPower = Math.cos(theta);
        double yRawPower = Math.sin(theta);

        double xControlPoint = xRawPower * xPIDOutput;
        double yControlPoint = yRawPower * yPIDOutput;
        double headingControlPoint = 1 * headingPIDOutput;

        robot.DriveTrain.setMotorPowers(-xRawPower, -yRawPower, 0);
    }

    @Override
    public void init() {

    }

    @Override
    public void loop() {

    }
}
