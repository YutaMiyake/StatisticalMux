/// Import external libraries
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.*;
import javax.swing.Timer;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.lang.*;

/// WindowApplication class create a GUI window for visualizing
/// statistical multiplexer
public class WindowApplication extends JFrame
{
    /// Data field of class WindowApplication

    /// Visualized objects and data
    protected DrawingTester drawTest;

    /// Label and value field for loss, throughput and delay
    protected JLabel lossLabel;
    protected JTextField lossVal;
    protected JLabel delayLabel;
    protected JTextField delayVal;
    protected JLabel throuLabel;
    protected JTextField throuVal;

    /// Label, button, value field for routers
    protected JLabel changePrior;

    protected JLabel audio;
    protected JTextField audioVal;
    protected JButton aPlus;
    protected JButton aMinus;

    protected JLabel video;
    protected JTextField videoVal;
    protected JButton vPlus;
    protected JButton vMinus;

    protected JLabel text;
    protected JTextField textVal;
    protected JButton tPlus;
    protected JButton tMinus;

    /// List for selecting source and destination
    protected JLabel src;
    protected JLabel dst;
    protected JLabel rter;
    protected JList sourceList;
    protected JList destList;
    protected JList routerList;
    protected JScrollPane Source;
    protected JScrollPane Dest;
    protected JScrollPane Router;

    /// List for specifying packet type
    protected JLabel packTypeLabel;
    protected JList packTypeList;
    protected JScrollPane packType;

    /// Data variable for calculating optimized path
    protected StatMux statMux = new StatMux();
    protected PacketType type = PacketType.AUDIO;

    /// Data variable for setting font display
    private static Font serifFont = new Font("Serif", Font.BOLD, 14);

    /// Data variable for calculating loss, delay, and throughput
    private int []path;
    private int indexR = 0;
    private int srcID = 1, dstID = 17;
    private double numPack = 50.0;
    private int[] packThrough = new int[]{0,0,0};
    private double lossRate = 0.0;
    private double loss[] = {0.0,0.0,0.0};
    private double delayTime = 0.0;
    private int throughPut = 0;
    private int numTransfer = 20;

    /// Constructor of WindowApplication class
    public WindowApplication(String title) throws IOException
    {
        /// Add title and specify size of window
        super(title);
        setSize(850, 700);
        addWindowListener(new WindowDestroyer());

        /// Initialize list and data
        String S[] = {"Source 1","Source 2","Source 3"};
        String D[] = {"Dest. 1","Dest. 2","Dest. 3","Dest. 4"};
        String R[] = new String [13];
        String P[] = {"Audio","Video","Text"};

        for (int i = 0; i <13; i++){
            R[i] = "Router" + String.valueOf(i+1);
        }

        /// Initialize variables for displaying data on window
        src = new JLabel("Select Source");
        dst = new JLabel("Select Destination");
        rter = new JLabel("Select Router");

        /// List display
        sourceList = new JList ();
        routerList = new JList ();
        destList = new JList ();
        Source = new JScrollPane(sourceList);
        Dest = new JScrollPane(destList);
        Router = new JScrollPane(routerList);

        sourceList.setListData(S);
        destList.setListData(D);
        routerList.setListData(R);

        /// Router info display
        changePrior = new JLabel ("Change Priority (0: Highest - 2: Lowest)");
        audio = new JLabel ("AUDIO");
        audioVal = new JTextField ("");
        aPlus = new JButton("+");
        aMinus = new JButton("-");
        video = new JLabel ("VIDEO");
        videoVal = new JTextField ("");
        vPlus = new JButton("+");
        vMinus = new JButton("-");
        text = new JLabel ("TEXT");
        textVal = new JTextField ("");
        tPlus = new JButton("+");
        tMinus = new JButton("-");

        /// Packet type display
        packTypeLabel = new JLabel("Packet Type");
        packTypeList = new JList();
        packType = new JScrollPane(packTypeList);
        packTypeList.setListData(P);

        /// Loss, delay, throughput display
        lossLabel = new JLabel("Loss");
        lossVal = new JTextField("");
        delayLabel = new JLabel("Delay (ms)");
        delayVal = new JTextField("");
        throuLabel = new JLabel("Throughput (Mbps)");
        throuVal = new JTextField("");

        /// Initialize data variable for manipulating lists
        sourceList.addListSelectionListener(new ListHandler());
        destList.addListSelectionListener(new ListHandler());
        routerList.addListSelectionListener(new ListHandler());
        packTypeList.addListSelectionListener(new ListHandler());

        /// Initialize drawing field
        drawTest = new DrawingTester(this);

        /// Initialize data variable for manipulating buttons
        aPlus.addActionListener(new ActionHandler());
        aMinus.addActionListener(new ActionHandler());
        vPlus.addActionListener(new ActionHandler());
        vMinus.addActionListener(new ActionHandler());
        tPlus.addActionListener(new ActionHandler());
        tMinus.addActionListener(new ActionHandler());

        /// Define layout display
        getContentPane().setLayout(null);
        getContentPane().add(drawTest);

        getContentPane().add(src);
        getContentPane().add(dst);
        getContentPane().add(rter);
        getContentPane().add(Source);
        getContentPane().add(Dest);
        getContentPane().add(Router);

        getContentPane().add(changePrior);
        getContentPane().add(audio);
        getContentPane().add(audioVal);
        getContentPane().add(aPlus);
        getContentPane().add(aMinus);
        getContentPane().add(video);
        getContentPane().add(videoVal);
        getContentPane().add(vPlus);
        getContentPane().add(vMinus);
        getContentPane().add(text);
        getContentPane().add(textVal);
        getContentPane().add(tPlus);
        getContentPane().add(tMinus);

        getContentPane().add(packTypeLabel);
        getContentPane().add(packType);

        getContentPane().add(lossLabel);
        getContentPane().add(lossVal);
        getContentPane().add(delayLabel);
        getContentPane().add(delayVal);
        getContentPane().add(throuLabel);
        getContentPane().add(throuVal);

        /// Set bound for display components
        drawTest.setBounds(10, 10, 650, 500);

        src.setBounds(700,10,150,30);
        Source.setBounds(700,40,80,60);

        dst.setBounds(700,110,150,30);
        Dest.setBounds(700,140,80,80);

        rter.setBounds(100,520,150,30);
        Router.setBounds(100,550,80,100);

        changePrior.setBounds(320,520,260,30);
        audio.setBounds(320,550,80,25);
        aPlus.setBounds(380,550,45,25);
        audioVal.setBounds(430,550,50,25);
        aMinus.setBounds(485,550,45,25);

        video.setBounds(320,585,80,25);
        vPlus.setBounds(380,585,45,25);
        videoVal.setBounds(430,585,50,25);
        vMinus.setBounds(485,585,45,25);

        text.setBounds(320,620,80,25);
        tPlus.setBounds(380,620,45,25);
        textVal.setBounds(430,620,50,25);
        tMinus.setBounds(485,620,45,25);


        lossLabel.setBounds(700,250,80,30);
        lossVal.setBounds(700,280,100,30);
        delayLabel.setBounds(700,320,80,30);
        delayVal.setBounds(700,350,80,30);
        throuLabel.setBounds(700,390,120,30);
        throuVal.setBounds(700,420,80,30);

        packTypeLabel.setBounds(620, 520, 80, 30);
        packType.setBounds(620,550, 60, 60);


        /// Display all the components
        setVisible(true);
    }

    /// getMin function return the smaller value
    public int getMin (int a, int b){
        if(a<b)
            return a;
        else
            return b;
    }

    // getDelay function
    // It takes in an optimized path and packet type as input.
    // Based on input, the function calculates delay occured when traveling
    // through the optimized path
    public double getDelay (int[] opath, PacketType type){
        double delay = 0;

        for(int i = 1; i < 4; i++){
            Router temp = statMux.getRouter(opath[i]-4);
            int flow = temp.getFlowLevel();
            int delayR = temp.getDelay();
            int priority = temp.getPriority(type);
            int thr = temp.getThroughput();
            packThrough[i-1]=thr;

            if(priority==1){
                loss[i-1]= thr/10.0;
            }
            if(priority==2){
                loss[i-1]= thr/5.0;
            }

            delay += numPack/thr + delayR + priority*flow;
        }

        return delay;
    }

    // getThroughput function
    // It takes optimized path as input
    // It returns the throughput of the travelled paths.
    public int getThroughput (int[] path){
        int min = 20;
        for(int i = 0; i<3; i++){
            min = getMin(min,packThrough[i]);
        }
        return min;
    }

    // getLossRate function
    // It takes number of packet transfered as in put.
    // Based on delay at routers and throughput of routers, it calculates
    // loss rate occured when packets traveling through optimized path
    public int getLossRate (int numTrans){
        int packLost = 0;

        for(int i = 0; i<3; i++){
            packLost+= loss[i];
        }
        return (packLost*numTrans);
    }

    // Data bound checking and correcting
    // Priority of router cannot get above 2 or below 0
    public int dataBound(int val)
    {
        if(val<0)
            return 0;
        if(val>2)
            return 2;
        return val;
    }

    // Define window adapter for closing window
    private class WindowDestroyer extends WindowAdapter
    {
        public void windowClosing(WindowEvent e)
        {
            /// Exit the program
            System.exit(0);
        }
    }

    /// findOptimizedPath function
    /// It finds an optimized path for transfering data by continuously
    ///     call StatMux class findNextNode method
    private int[] findOptimizedPath(int src, int dst, PacketType typeP){
        int i = 1;
        int []a = new int [5];

        a[0]=src;

        for(int j = 1; j < 5; j++){
            a[j]= statMux.findNextNode(a[j-1],dst,typeP);
        }

        return a;
    }

    // Define drawing component
    private class DrawingTester extends JComponent
    {
        WindowApplication app;

        public DrawingTester(WindowApplication a)
	{
		app = a;
	}

        public void paint(Graphics g)
        {
            path = findOptimizedPath(srcID,dstID,type);

            /// Create the drawing board
            Dimension d = getSize();
            g.setColor(Color.white);
            g.fillRect(1, 1, d.width-2, d.height-2);

            g.setColor(Color.black);
            g.drawRect(1, 1, d.width-2, d.height-2);
            g.setFont(serifFont);

            /// Draw the whole network, including all routers with
            ///     delay and flow level, sources and destinations.
            int numR = 1;
            int w = 95;
            int h = d.height/5;
            int pos = -1;

            for(int i = 0; i < 3; i++)
            {
                g.drawOval(w, h+100*i, 40, 40);
                g.drawString("S" + String.valueOf(i+1),w+13,h+100*i-5);
            }

            for(int i = 0; i < 3; i++)
            {
                pos++;
                Router temp = statMux.getRouter(pos);
                g.drawOval(w+110, h+100*i, 40, 40);
                g.drawString("R" + String.valueOf(numR++),w+123,h+100*i-5);
                g.drawString(String.valueOf(temp.getDelay()*temp.getPriority(type)+temp.getFlowLevel()),w+125,h+100*i+15);
                g.drawString(String.valueOf(temp.getFlowLevel()),w+125,h+100*i+35);
            }

            h = d.height/11;
            for(int i = 0; i < 4; i++)
            {
                pos++;
                Router temp = statMux.getRouter(pos);
                g.drawOval(w+210, h+100*i, 40, 40);
                g.drawString("R" + String.valueOf(numR++),w+223,h+100*i-5);
                g.drawString(String.valueOf(temp.getDelay()*temp.getPriority(type)+temp.getFlowLevel()),w+225,h+100*i+15);
                g.drawString(String.valueOf(temp.getFlowLevel()),w+225,h+100*i+35);

            }

            h = 20;
            for(int i = 0; i < 6; i++)
            {
                pos++;
                Router temp = statMux.getRouter(pos);
                g.drawOval(w+310, h+80*i, 40, 40);
                g.drawString("R" + String.valueOf(numR++),w+320,h+80*i-5);
                g.drawString(String.valueOf(temp.getDelay()*temp.getPriority(type)+temp.getFlowLevel()),w+325,h+80*i+15);
                g.drawString(String.valueOf(temp.getFlowLevel()),w+325,h+80*i+35);
            }

            for(int i = 0; i < 4; i++)
            {
                g.drawOval(w+410, d.height/11+100*i, 40, 40);
                g.drawString("D" + String.valueOf(i+1),w+423,d.height/11+100*i-5);
            }

            g.setColor(Color.black);
            int[][] connection = statMux.getConnections();

            /// Check buffer for connections at each step and draw links at layer1
            for(int i = 0; i < connection[path[0]-1].length; i++){
                int temp = connection[path[0]-1][i]-3;
                g.drawLine(w+40,(path[0])*d.height/5+20, w+110, temp*d.height/5+20);
            }

            /// Check buffer for connections at each step and draw links at layer2
            for(int i = 0; i < connection[path[1]-1].length; i++){
                int temp = connection[path[1]-1][i]-7;
                g.drawLine(w+150,(path[1]-3)*d.height/5+20, w+210, (d.height/11)+100*temp+20);
            }

            /// Check buffer for connections at each step and draw links at layer3
            for(int i = 0; i < connection[path[2]-1].length; i++){
                int temp = connection[path[2]-1][i]-11;
                g.drawLine(w+250, (d.height/11)+100*(path[2]-7)+20, w+310, 80*temp+40);
            }

            /// Draw optimized path for packets traveling between source
            /// and destination
            h = d.height/5;
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.red);

            g2.drawLine(w+40, h*(path[0])+20, w+110, h*(path[1]-3)+20);
            g2.drawLine(w+150, h*(path[1]-3)+20, w+210, (d.height/11)+100*(path[2]-7)+20);
            g2.drawLine(w+250, (d.height/11)+100*(path[2]-7)+20, w+310, 80*(path[3]-11)+40);
            g2.drawLine(w+350, 80*(path[3]-11)+40, w+410, (d.height/11)+100*(path[4]-17)+20);

           
            /// Calculate and display loss, delay, and throughput
            delayTime = getDelay(path,type);
            throughPut = getThroughput(path);

            int numPackLost = getLossRate(numTransfer);

            lossRate = numPackLost/100000.0 + 0.0005*delayTime;
            delayVal.setText(String.format("%.2f",delayTime));
            throuVal.setText(String.valueOf(throughPut));
            lossVal.setText(String.format("%.4f",lossRate));

        }
    }

    /// Define list handler component
    private class ListHandler implements ListSelectionListener
    {
        /// Pass user's interaction with list to appropriate handler
        public void valueChanged(ListSelectionEvent e)
        {
            if (e.getSource() == sourceList)
            {
                if (!e.getValueIsAdjusting())
                {
                    int indexS = sourceList.getSelectedIndex();
                    srcID = indexS+1;
                    drawTest.repaint();
                }
            }

            if (e.getSource() == destList)
            {
                if (!e.getValueIsAdjusting())
                {
                    int indexD = destList.getSelectedIndex();
                    dstID = indexD+17;
                    drawTest.repaint();
                }
            }

            if (e.getSource() == routerList)
            {
                if (!e.getValueIsAdjusting())
                {
                    indexR = routerList.getSelectedIndex();

                    Router temp = statMux.getRouter(indexR);
                    int []pList = temp.getPriorityList();
                    audioVal.setText(String.valueOf(pList[0]));
                    videoVal.setText(String.valueOf(pList[1]));
                    textVal.setText(String.valueOf(pList[2]));
                }
            }

            if (e.getSource() == packTypeList)
            {
                if (!e.getValueIsAdjusting())
                {
                    int temp = packTypeList.getSelectedIndex();
                    if(temp==0){
                        type=PacketType.AUDIO;
                    }
                    else if(temp==1){
                        type=PacketType.VIDEO;
                    }
                    else{
                        type=PacketType.TEXT;
                    }
                    drawTest.repaint();
                }
            }
        }
    }

    /// Define action handler component
    private class ActionHandler implements ActionListener {
        /// Check for button clicking and apply appropriate response
        public void actionPerformed(ActionEvent e){
            /// For each input, pass it to the correct handler
            /// Priority of router will be updated based on input
            /// Upon user's changes of the priority value, repaint
            /// the display component
            if (e.getSource() == aPlus)
            {
                Router temp = statMux.getRouter(indexR);
                int []pList = temp.getPriorityList();
                pList[0] = dataBound(pList[0]+1);
                temp.setPriorityList(pList);
                audioVal.setText(String.valueOf(pList[0]));
                videoVal.setText(String.valueOf(pList[1]));
                textVal.setText(String.valueOf(pList[2]));
                drawTest.repaint();
            }

            if (e.getSource() == aMinus)
            {
                Router temp = statMux.getRouter(indexR);
                int []pList = temp.getPriorityList();
                pList[0] = dataBound(pList[0]-1);
                temp.setPriorityList(pList);
                audioVal.setText(String.valueOf(pList[0]));
                videoVal.setText(String.valueOf(pList[1]));
                textVal.setText(String.valueOf(pList[2]));
                drawTest.repaint();
            }

            if (e.getSource() == vPlus)
            {
                Router temp = statMux.getRouter(indexR);
                int []pList = temp.getPriorityList();
                pList[1] = dataBound(pList[1]+1);
                temp.setPriorityList(pList);
                audioVal.setText(String.valueOf(pList[0]));
                videoVal.setText(String.valueOf(pList[1]));
                textVal.setText(String.valueOf(pList[2]));
                drawTest.repaint();
            }

            if (e.getSource() == vMinus)
            {
                Router temp = statMux.getRouter(indexR);
                int []pList = temp.getPriorityList();
                pList[1] = dataBound(pList[1]-1);
                temp.setPriorityList(pList);
                audioVal.setText(String.valueOf(pList[0]));
                videoVal.setText(String.valueOf(pList[1]));
                textVal.setText(String.valueOf(pList[2]));
                drawTest.repaint();
            }

            if (e.getSource() == tPlus)
            {
                Router temp = statMux.getRouter(indexR);
                int []pList = temp.getPriorityList();
                pList[2] = dataBound(pList[2]+1);
                temp.setPriorityList(pList);
                audioVal.setText(String.valueOf(pList[0]));
                videoVal.setText(String.valueOf(pList[1]));
                textVal.setText(String.valueOf(pList[2]));
                drawTest.repaint();
            }

            if (e.getSource() == tMinus)
            {
                Router temp = statMux.getRouter(indexR);
                int []pList = temp.getPriorityList();
                pList[2] = dataBound(pList[2]-1);
                temp.setPriorityList(pList);
                audioVal.setText(String.valueOf(pList[0]));
                videoVal.setText(String.valueOf(pList[1]));
                textVal.setText(String.valueOf(pList[2]));
                drawTest.repaint();
            }
        }
    }
}


