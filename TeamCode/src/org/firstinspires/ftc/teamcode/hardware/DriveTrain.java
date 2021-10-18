package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.util.MiniPID;


public class DriveTrain {

    private final double terminalXVelocity = 0.3;
    private final double terminalYVelocity = 0.4;
    private final double terminalThetalVelocity = 0.02;

    private final double maximumMotorSpeed = 0.85;

    private double p = 0, i = 0, d = 0, f = 0;

    public static double flbr = 0;
    public static double frbl = 0;

    double controlPointX = 0;
    double controlPointY = 0;
    double controlPointTurn = 0;

    Robot robot;
    private BNO055IMU gyro;

    public DriveTrain(Robot robot) {
        this.robot = robot;

        gyro = robot.getImu();
    }

    public void setMotorVelocitys(double x, double y, double turn) { // Where x,y,turn are Velocities

        MiniPID xPIDF = new MiniPID(0, 0, 0, 0);
        MiniPID yPIDF = new MiniPID(0, 0, 0, 0);
        MiniPID turnPIDF = new MiniPID(0, 0, 0, 0);



        setMotorPowers(controlPointX, controlPointY, controlPointTurn);
    }

    public void setMotorPowers(double x, double y, double turn) {
        double h = Math.hypot(x, y);
        double theta = Math.atan2(y, x) - Math.toRadians(45);

        double[] motorVector = new double[] {
            h * Math.cos(theta) - turn,
            h * Math.sin(theta) - turn,
            h * Math.sin(theta) + turn,
            h * Math.cos(theta) + turn
        };

        setMotorPowers(motorVector);
    }

    public void setMotorPowers(double x, double y, double turn, double xyMag, double turnMag) {
        double h = Math.hypot(x, y);
        double theta = Math.atan2(y, x) - Math.toRadians(45);

        turn *= turnMag;  // Turn Speed
        h *= xyMag;       // xy, Speed

        double[] motorVector = new double[] {
                h * Math.cos(theta) - turn,
                h * Math.sin(theta) - turn,
                h * Math.sin(theta) + turn,
                h * Math.cos(theta) + turn
        };

        setMotorPowers(motorVector);
    }

    public void setMotorPowers(double fls, double bls, double frs, double brs) {
        robot.getFrontLeft().setPower(fls);
        robot.getFrontRight().setPower(frs);
        robot.getBackLeft().setPower(bls);
        robot.getBackRight().setPower(brs);
    }
    public void setMotorPowers(double[]powers) {
        setMotorPowers(
                powers[0],
                powers[1],
                powers[2],
                powers[3]
        );
    }
    public void setMotorPowers(double left, double right) {
        robot.getFrontLeft().setPower(left);
        robot.getBackLeft().setPower(left);
        robot.getFrontRight().setPower(right);
        robot.getBackRight().setPower(right);
    }
    public void stopDrive() {
        robot.getFrontLeft().setPower(0);
        robot.getBackLeft().setPower(0);
        robot.getFrontRight().setPower(0);
        robot.getBackRight().setPower(0);
    }

    public DcMotor frontLeft() {
        return robot.getFrontLeft();
    }

    public DcMotor frontRight() {
        return robot.getFrontRight();
    }

    public DcMotor backLeft() {
        return robot.getBackLeft();
    }

    public DcMotor backRight() {
        return robot.getBackRight();
    }
}
