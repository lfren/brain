package util.processing;

/**
 * Created by lilia on 3/31/2017.
 */
import controlP5.ControlP5;
import processing.core.PApplet;
import processing.serial.Serial;
import util.Row;

import java.awt.*;
import java.io.PrintWriter;
import java.util.*;

public class BrainProcessing extends PApplet{
    public ControlP5 controlP5;

    public Serial serial;

    public Channel[] channels = new Channel[11];
    public Monitor[] monitors = new Monitor[10];
    public Graph graph;
    public ConnectionLight connectionLight;
    public PrintWriter output;

    public int packetCount = 0;
    public int globalMax = 0;
    public String scaleMode;
    private long startTime;
    final private Command command = new Command();
    final private HashMap<Integer, ArrayList<Double>> averages = new HashMap<>();
    final private HashSet<Row> rowData = new HashSet<>();

    public void setup() {
        // Set up window
        //    size(1024, 768);
        frameRate(60);
        smooth();
        surface.setTitle("Processing Brain Grapher");
        frame.setVisible(false);

        // Set up serial connection
        println("Find your Arduino in the list below, note its [index]:\n");
        Toolkit.getDefaultToolkit().beep();
        System.err.println(System.getProperty("java.library.path"));

        for (int i = 0; i < Serial.list().length; i++) {
            println("[" + i + "] " + Serial.list()[i]);
        }
        Toolkit.getDefaultToolkit().beep();
        // Put the index found above here:
        //   serial = new Serial(this, Serial.list()[0], 9600);
        //    serial.bufferUntil(10);
        try {
            serial = new Serial(this, Serial.list()[0], 9600);
            serial.bufferUntil('\n');
        }
        catch(Exception e) {
            println("check settings above and usb cable, something went wrong:");
            e.printStackTrace();
        }



        output = createWriter("C:\\Projects\\brain\\data\\results.csv");
        Timer commandTimer = new Timer();
        TimerTask commmandTask = new TimerTask() {
            @Override
            public void run() {
                java.awt.Toolkit.getDefaultToolkit().beep();
                // average result and  output in average.csv
                command.toggleCommand(0,1);
                System.out.println ("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println ("!                                             !");
                System.out.println ("!        "  + command.getCurrentCommandDescription() + "                         !");
                System.out.println ("!                                             !");
                System.out.println ("!                                             !");
                System.out.println ("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            }
        };
        commandTimer.schedule(commmandTask, 0, 20000);
                // average result and  output in average.csv

    }

    public void serialEvent(Serial p) {
        // Split incoming packet on commas
        // See https://github.com/kitschpatrol/Arduino-Brain-Library/blob/master/README for information on the CSV packet format
        String incomingString = "";
        try {
            incomingString = p.readString();
            if (incomingString.contains(",")) {
                // list of commands
                // -1 - do nothing
                // 1 - close eyes
                // 2 - focus on red circle
                // 3 - focus on black circle
                // 4 - focus on white circle
                String incomingStringWithCommand = command.getCurrentCommand() + "," + incomingString;
                System.out.println(incomingStringWithCommand);
                output.println(incomingStringWithCommand);
                output.flush();
            }

        } catch(Exception e) {
            System.out.println(" incomingString error " + incomingString);
            e.printStackTrace();
        }
    }

    public void settings(){
        size(1024, 768);
    }

    public void draw(){
        /*background(0);
        ellipse(mouseX, mouseY, 20, 20);*/
    }

    public ArrayList<Integer> calculateAverage(ArrayList<ArrayList<Integer>> round) {

        return null;
    }

    public static void main(String... args){
        BrainProcessing pt = new BrainProcessing();
        PApplet.main(pt.getClass().getCanonicalName());

    }
}