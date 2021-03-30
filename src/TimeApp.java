
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Date;
import java.util.Formatter;

public class TimeApp {

    private long start;
    private long stop;
    private Date startTime;
    private Date stopTime;
    private double duration;
    private int network;

    private JFrame frame;
    private JButton startBtn;
    private JButton stopBtn;
    private JLabel networkLabel;
    private JTextField networkTextField;
    private JTextArea callDetails;

    private TimeApp() {
        frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        startBtn = new JButton("Begin");
        startBtn.setEnabled(false);
        stopBtn = new JButton("End");
        stopBtn.setEnabled(false);
        networkLabel = new JLabel("Network(0 is current Network, 1 is for other networks)");
        networkTextField = new JTextField(10);
        callDetails = new JTextArea();
        callDetails.setEditable(false);

        networkTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                enableBtns();
            }

            @Override
            public void keyPressed(KeyEvent e) {
                enableBtns();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                enableBtns();
            }
        });

        startBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                start = System.currentTimeMillis();
                startTime = new Date();
                stopBtn.setEnabled(true);
                startBtn.setEnabled(false);
                networkTextField.setEnabled(false);
                callDetails.setText("");
            }
        });

        stopBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                stop = System.currentTimeMillis();

                stopTime = new Date();
                double durationS = (stop - start) / 1000;
                duration = durationS / 60;
                try {
                    network = Integer.parseInt(networkTextField.getText());
                    if (isNetworkValid(network)) {
                        outputCallDetails(startTime, stopTime, durationS, duration, network);
                    } else {
                        throw new IllegalArgumentException("Invalid Network");
                    }
                } catch (Exception exc) {
                    callDetails.setText("Please enter a valid network.");
                }

                stopBtn.setEnabled(false);
                startBtn.setEnabled(true);
                networkTextField.setEnabled(true);
            }
        });

        frame.add(networkLabel);
        frame.add(networkTextField);
        frame.add(startBtn);
        frame.add(stopBtn);
        frame.add(callDetails);

        frame.setVisible(true);

    }

    private double calculateBill(double minutes, int network) {
        double bill;
        if (network == 1) {
            if (isDayTime()) {
                bill = minutes * 4.0;
            } else {
                bill = minutes * 3.0;
            }
        } else {
            bill = minutes * 5.0;
        }

        if (minutes > 2) {
            bill = bill + (bill * 16 / 100);
        }
        return bill;
    }

    private void outputCallDetails(Date startTime, Date stopTime, double seconds, double minutes, int network) {
        String output = "\tDetails of your Call: \nTime Started:\t " + startTime + ".\n Time Ended:\t " + stopTime + ".\n Time taken in seconds:\t "
                + seconds + ".\n Time taken in minutes:\t " + formatDoubles(minutes) + ".\n ";
        if (network == 1) {
            output += "Network:\t Other Network(Ksh 5 per minute). \n";
        } else if (network == 0) {
            output += "Network:\t This Network. \n";
            if (isDayTime()) {
                output += "Time:\t Day(Between 6am and 6pm)(Ksh 4 per minute). \n";
            } else {
                output += "Time:\t Night(Between 6pm and 6am)(Ksh 3 per minute). \n";
            }
        }
        if (minutes > 2) {
            output += "More than 2 minute talk:\t True (16% VAT). \n";
        } else {
            output += "VAT:\t 0% \n";
        }
        output += "\nTotal Amount to be paid : KSH " + formatDoubles(calculateBill(minutes, network)) + " \n";
        callDetails.setText(output);
    }

    private String formatDoubles(Double aDouble) {
        try (Formatter formatter = new Formatter()) {
            return formatter.format("%.2f", aDouble).toString();
        }
    }

    private boolean isDayTime() {

        Date currDate = new Date();
        return currDate.getHours() > 5 && currDate.getHours() < 17;
    }

    private boolean isNetworkValid(int network) {
        return (network == 1 || network == 0);
    }

    private void enableBtns() {
        if (!networkTextField.getText().isEmpty() && !(networkTextField.getText().equals(""))) {
            startBtn.setEnabled(true);
            stopBtn.setEnabled(true);
        } else {
            startBtn.setEnabled(false);
            stopBtn.setEnabled(false);
        }
        try {
            network = Integer.parseInt(networkTextField.getText());
            if (isNetworkValid(network)) {
                startBtn.setEnabled(true);
                stopBtn.setEnabled(true);
                callDetails.setText("");
            } else {
                throw new IllegalArgumentException("Invalid Network");
            }
        } catch (Exception exc) {
            startBtn.setEnabled(false);
            stopBtn.setEnabled(false);
            callDetails.setText("Please enter valid network.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new TimeApp();
            }
        });
    }

}
