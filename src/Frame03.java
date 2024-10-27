import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public class Frame03 extends JFrame implements ActionListener{
    int mn=0;
    public ArrayList<Process> processList;
    JTextArea area;//文本区
    JLabel label1,label2,label3;//标签
    JButton button,button1;//按钮
    JTextField textField1,textField2,textField3;
    public Frame03(){
        setTitle("进程调度");
        setVisible(true);
        setLayout(null);
        setBounds(200,70,1000,700);
        setResizable(false);// 设置窗体不能改变尺寸

        //设计Panel1
        JPanel panel1=new JPanel();
        panel1.setBounds(10,10,965,100);
        panel1.setLayout(null);
        panel1.setBorder(BorderFactory.createTitledBorder("运行"));
        button=new JButton("开始运行");

        button.setBounds(450,40,90,30);

        panel1.add(button);
        button.addActionListener(this);

        //设计Panel2
        JPanel panel2=new JPanel();
        panel2.setBounds(10,120,965,550);
        panel2.setLayout(null);
        panel2.setBorder(BorderFactory.createTitledBorder("运行情况"));
        area = new JTextArea();
        area.setBounds(5,15,955,530);
        area.setEditable(false);
        panel2.add(area);
        JScrollPane jscroll = new JScrollPane(area);//添加滚动条
        jscroll.setBounds(5,15,955,530);
        panel2.add(jscroll);

        add(panel1);
        add(panel2);
        validate();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==button)
            new FCFS();
    }


    class FCFS {
        private int processNumber;
        Random ran = new Random();

        public FCFS() {
            init();
            calc();
            printResult(processList);
            calc1();
            printResult(processList);
            calc2();
            printResult(processList);
        }

        private void init() {
            Scanner sc = new Scanner(System.in);

            processNumber = ran.nextInt(7)+2;

            processList = new ArrayList<Process>();
            for (int i = 0; i < processNumber; i++) {
                processList.add(new Process());
            }

            processList.get(0).setArrivalTime(0);
            for (int i = 1; i < processNumber; i++) {
                processList.get(i).setArrivalTime(processList.get(i-1).getArrivalTime()+ran.nextInt(100)%10+1);
            }

            for (int i = 0; i < processNumber; i++) {
                processList.get(i).setServicesTime(ran.nextInt(100)%20+1);
            }
        }

        private void calc() {  //先来先服务
            int timeNow = 0;
            Process pro;

            for (int i = 0; i < processNumber; i++) {
                pro = processList.get(i);

                if (timeNow<pro.getArrivalTime())  timeNow=pro.getArrivalTime();

                int waitTime = timeNow - pro.getArrivalTime();
                int completionTime = timeNow + pro.getServicesTime();
                int turnAroundTime = completionTime
                        - pro.getArrivalTime();
                double turnAroundTimeWithWeight = (double) turnAroundTime
                        / pro.getServicesTime();

                pro.setStartTime(timeNow);
                pro.setWaitTime(waitTime);
                pro.setCompletionTime(completionTime);
                pro.setTurnAroundTime(turnAroundTime);
                pro.setTurnAroundTimeWithWeight(
                        turnAroundTimeWithWeight);

                timeNow += pro.getServicesTime();
            }
        }

        private void calc1() {  //时间片轮转
            int timeNow = 0;
            int flag=processNumber;
            int timepiece = ran.nextInt(10)+1;
            Process pro;
            int[] a = new int[1001];
            for (int i=0;i<processNumber;i++){
                pro = processList.get(i);
                pro.setStartTime(timepiece*(i+1));
            }
            while (flag>0){
                for (int i=0;i<processNumber;i++){
                    pro = processList.get(i);
                    if (timeNow<pro.getArrivalTime()&&a[i]==0) timeNow=pro.getArrivalTime();
                    if (timepiece<pro.getServicesTime()&&a[i]==0){
                        pro.setServicesTime(pro.getServicesTime()-timepiece);
                        timeNow+=timepiece;
                    }
                    if (a[i]==0&&timepiece>=pro.getServicesTime()){
                        timeNow+= pro.getServicesTime();
                        pro.setCompletionTime(timeNow);
                        pro.setTurnAroundTime(timeNow- pro.getArrivalTime());
                        pro.setTurnAroundTimeWithWeight((double)(timeNow- pro.getArrivalTime())/ pro.getServicesTime() );
                        a[i]=1;
                        flag--;
                    }
                }
            }
        }

        private void calc2() {   //高相应比
            int timeNow = 0;
            Process pro;
            int[] a = new int[100];
            double max=-1;
            int record=0;

            for (int i = 0; i < processNumber; i++) {
                if (i>0)
                for (int j=0;j<processNumber;j++){
                    double te=1.0*(processList.get(j).getServicesTime())/(processList.get(j).getServicesTime()+timeNow-processList.get(j).getArrivalTime());
                    if (a[j]==0&&te>max){
                        max=te;
                        record=j;
                    }
                }
                pro = processList.get(record);

                if (timeNow<pro.getArrivalTime())  timeNow=pro.getArrivalTime();

                int waitTime = timeNow - pro.getArrivalTime();
                int completionTime = timeNow + pro.getServicesTime();
                int turnAroundTime = completionTime
                        - pro.getArrivalTime();
                double turnAroundTimeWithWeight = (double) turnAroundTime
                        / pro.getServicesTime();

                pro.setStartTime(timeNow);
                pro.setWaitTime(waitTime);
                pro.setCompletionTime(completionTime);
                pro.setTurnAroundTime(turnAroundTime);
                pro.setTurnAroundTimeWithWeight(
                        turnAroundTimeWithWeight);

                timeNow += pro.getServicesTime();
            }
        }

        public void main(String [] args) {
            new FCFS();
        }
    }


    String printResult(ArrayList<Process> processList) {
        if (mn==0) {
            area.append("\n           FCFS\n");
            mn++;
        }
        else if (mn==1){
            area.append("\n           时间片调度算法\n");
            mn++;
        }
        else if (mn==2){
            area.append("\n           高相应比调度算法\n");
            mn=0;
        }

        area.append("\tArrive:\t\t");
        for (int i = 0; i < processList.size(); i++) {
            area.append(processList.get(i).getArrivalTime() + "\t");
        }
        area.append("\n");

        area.append("\tService:\t\t");
        for (int i = 0; i < processList.size(); i++) {
            area.append(processList.get(i).getServicesTime() + "\t");
        }
        area.append("\n");

        area.append("\tStart:\t\t");
        for (int i = 0; i < processList.size(); i++) {
            area.append(processList.get(i).getStartTime() + "\t");
        }
        area.append("\n");

        area.append("\tWait:\t\t");
        for (int i = 0; i < processList.size(); i++) {
            area.append(processList.get(i).getWaitTime() + "\t");
        }
        area.append("\n");

        area.append("\tFinish:\t\t");
        for (int i = 0; i < processList.size(); i++) {
            area.append(processList.get(i).getCompletionTime() + "\t");
        }
        area.append("\n");

        area.append("\t周转时间:\t\t");
        for (int i = 0; i < processList.size(); i++) {
            area.append(processList.get(i).getTurnAroundTime() + "\t");
        }
        area.append("\n");

        area.append("\t权时间比:\t\t");
        for (int i = 0; i < processList.size(); i++) {
            area.append(Math.round(processList.get(i)
                    .getTurnAroundTimeWithWeight() * 100) / 100.0 + "\t");
        }
        area.append("\n");

        area.append("\t平均运行时间:"
                + Tools.calcAverageTurnAroundTime(processList) + "\t");
        area.append("\t平均带权周转比:"
                + Tools.calcAverageTurnAroundTimeWithWeight(processList));

        area.append("\n");
        return null;
    }



    static class Tools {

        public static double calcAverageTurnAroundTime(
                ArrayList<Process> processList) {
            double sum = 0;
            for (int i = 0; i < processList.size(); i++) {
                sum += processList.get(i).getTurnAroundTime();
            }
            return Math.round(sum / processList.size() * 100) / 100.0;
        }

        public static double calcAverageTurnAroundTimeWithWeight(
                ArrayList<Process> processList) {
            double sum = 0;
            for (int i = 0; i < processList.size(); i++) {
                sum += processList.get(i).getTurnAroundTimeWithWeight();
            }
            return Math.round(sum / processList.size() * 100) / 100.0;
        }

    }



    class Process {
        private int arrivalTime;
        private int servicesTime;
        private int remainServiceTime;
        private int startTime;
        private int waitTime;
        private int completionTime;

        /**
         * turnAroundTime = completionTime - arrivalTime
         */
        private int turnAroundTime;

        /**
         * turnAroundTimeWithWeight = turnAroundTime / servicesTime
         */
        private double turnAroundTimeWithWeight;

        public Process() {
            ;
        }

        public int getArrivalTime() {
            return arrivalTime;
        }

        public void setArrivalTime(int arrivalTime) {
            this.arrivalTime = arrivalTime;
        }

        public int getServicesTime() {
            return servicesTime;
        }

        public void setServicesTime(int servicesTime) {
            this.servicesTime = servicesTime;
        }

        public int getRemainServiceTime() {
            return remainServiceTime;
        }

        public void setRemainServiceTime(int remainServiceTime) {
            this.remainServiceTime = remainServiceTime;
        }

        public int getStartTime() {
            return startTime;
        }

        public void setStartTime(int startTime) {
            this.startTime = startTime;
        }

        public int getWaitTime() {
            return waitTime;
        }

        public void setWaitTime(int waitTime) {
            this.waitTime = waitTime;
        }

        public int getCompletionTime() {
            return completionTime;
        }

        public void setCompletionTime(int completionTime) {
            this.completionTime = completionTime;
        }

        public int getTurnAroundTime() {
            return turnAroundTime;
        }

        public void setTurnAroundTime(int turnAroundTime) {
            this.turnAroundTime = turnAroundTime;
        }

        public double getTurnAroundTimeWithWeight() {
            return turnAroundTimeWithWeight;
        }

        public void setTurnAroundTimeWithWeight(double turnAroundTimeWithWeight) {
            this.turnAroundTimeWithWeight = turnAroundTimeWithWeight;
        }

        @Override
        public String toString() {
            return "Process [arrivalTime=" + arrivalTime + ", servicesTime="
                    + servicesTime + ", remainServiceTime=" + remainServiceTime
                    + ", startTime=" + startTime + ", waitTime=" + waitTime
                    + ", completionTime=" + completionTime + ", turnAroundTime="
                    + turnAroundTime + ", turnAroundTimeWithWeight="
                    + turnAroundTimeWithWeight + "]";
        }
    }
}





class FCFS {
    private int processNumber;
    private ArrayList<Process> processList;
    Random ran = new Random();

    public FCFS() {
        init();
        calc();
        Tools.printResult(processList);
        calc1();
        Tools.printResult(processList);
    }

    private void init() {
        Scanner sc = new Scanner(System.in);
        processNumber = 4;//sc.nextInt();

        processList = new ArrayList<Process>();
        for (int i = 0; i < processNumber; i++) {
            processList.add(new Process());
        }

        for (int i = 0; i < processNumber; i++) {
            processList.get(i).setArrivalTime(ran.nextInt(100)%10+1);//(sc.nextInt());
        }

        for (int i = 0; i < processNumber; i++) {
            processList.get(i).setServicesTime(ran.nextInt(100)%20+1);//(sc.nextInt());
        }
    }

    private void calc() {
        int timeNow = 0;
        Process pro;

        for (int i = 0; i < processNumber; i++) {
            pro = processList.get(i);
            if (timeNow<pro.getArrivalTime()) timeNow=pro.getArrivalTime();
            int waitTime = timeNow - pro.getArrivalTime();
            int completionTime = timeNow + pro.getServicesTime();
            int turnAroundTime = completionTime
                    - pro.getArrivalTime();
            double turnAroundTimeWithWeight = (double) turnAroundTime
                    / pro.getServicesTime();

            pro.setStartTime(timeNow);
            pro.setWaitTime(waitTime);
            pro.setCompletionTime(completionTime);
            pro.setTurnAroundTime(turnAroundTime);
            pro.setTurnAroundTimeWithWeight(
                    turnAroundTimeWithWeight);

            timeNow += pro.getServicesTime();
        }
    }


    private void calc1() {
        int timeNow = 0;
        int flag=processNumber;
        Scanner in = new Scanner(System.in);
        int timepiece = ran.nextInt(10)+1,timeto=0;
        Process pro;
        int[] a = new int[1001];
        for (int i=0;i<processNumber;i++){
            pro = processList.get(i);
            pro.setStartTime(pro.getArrivalTime()+timeto);
            timeto+=pro.getArrivalTime();
        }
        while (flag>=0){
            for (int i=0;i<processNumber;i++){
                pro = processList.get(i);
                if (timeNow<pro.getArrivalTime()&a[i]==0) timeNow=pro.getArrivalTime();
                if (timepiece<pro.getServicesTime()&&a[i]==0){
                    pro.setServicesTime(pro.getServicesTime()-timepiece);
                    timeNow+=timepiece;
                }
                if (a[i]==0&&timepiece>=pro.getServicesTime()){
                    timeNow+= pro.getServicesTime();
                    pro.setCompletionTime(timeNow);
                    pro.setTurnAroundTime(timeNow- pro.getArrivalTime());
                    pro.setTurnAroundTimeWithWeight((double)(timeNow- pro.getArrivalTime())/ pro.getServicesTime() );
                    a[i]=1;
                    flag--;
                }

            }
        }
    }

    public static void main(String [] args) {
        new FCFS();
    }
}







class Tools {

    public static double calcAverageTurnAroundTime(
            ArrayList<Process> processList) {
        double sum = 0;
        for (int i = 0; i < processList.size(); i++) {
            sum += processList.get(i).getTurnAroundTime();
        }
        return Math.round(sum / processList.size() * 100) / 100.0;
    }

    public static double calcAverageTurnAroundTimeWithWeight(
            ArrayList<Process> processList) {
        double sum = 0;
        for (int i = 0; i < processList.size(); i++) {
            sum += processList.get(i).getTurnAroundTimeWithWeight();
        }
        return Math.round(sum / processList.size() * 100) / 100.0;
    }

    public static void printResult(ArrayList<Process> processList) {
//        System.out.println("\n    #RESULT#");


        System.out.print("\tArrive:\t\t");
        for (int i = 0; i < processList.size(); i++) {
            System.out.print(processList.get(i).getArrivalTime() + "\t");
        }
        System.out.println();

        System.out.print("\tService:\t");
        for (int i = 0; i < processList.size(); i++) {
            System.out.print(processList.get(i).getServicesTime() + "\t");
        }
        System.out.println();

        System.out.print("\tStart:\t\t");
        for (int i = 0; i < processList.size(); i++) {
            System.out.print(processList.get(i).getStartTime() + "\t");
        }
        System.out.println();

        System.out.print("\tWait:\t\t");
        for (int i = 0; i < processList.size(); i++) {
            System.out.print(processList.get(i).getWaitTime() + "\t");
        }
        System.out.println();

        System.out.print("\tFinish:\t\t");
        for (int i = 0; i < processList.size(); i++) {
            System.out.print(processList.get(i).getCompletionTime() + "\t");
        }
        System.out.println();

        System.out.print("\t周转时间:\t");
        for (int i = 0; i < processList.size(); i++) {
            System.out.print(processList.get(i).getTurnAroundTime() + "\t");
        }
        System.out.println();

        System.out.print("\t权时间比:\t");
        for (int i = 0; i < processList.size(); i++) {
            System.out.print(Math.round(processList.get(i)
                    .getTurnAroundTimeWithWeight() * 100) / 100.0 + "\t");
        }
        System.out.println();

        System.out.println("\t平均运行时间:"
                + Tools.calcAverageTurnAroundTime(processList) + "\t");
        System.out.println("\t平均带权周转时间:"
                + Tools.calcAverageTurnAroundTimeWithWeight(processList));

        System.out.println();
    }
}




class Process {
    private int arrivalTime;
    private int servicesTime;
    private int remainServiceTime;
    private int startTime;
    private int waitTime;
    private int completionTime;

    /**
     * turnAroundTime = completionTime - arrivalTime
     */
    private int turnAroundTime;

    /**
     * turnAroundTimeWithWeight = turnAroundTime / servicesTime
     */
    private double turnAroundTimeWithWeight;

    public Process() {
        ;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getServicesTime() {
        return servicesTime;
    }

    public void setServicesTime(int servicesTime) {
        this.servicesTime = servicesTime;
    }

    public int getRemainServiceTime() {
        return remainServiceTime;
    }

    public void setRemainServiceTime(int remainServiceTime) {
        this.remainServiceTime = remainServiceTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public int getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }

    public int getTurnAroundTime() {
        return turnAroundTime;
    }

    public void setTurnAroundTime(int turnAroundTime) {
        this.turnAroundTime = turnAroundTime;
    }

    public double getTurnAroundTimeWithWeight() {
        return turnAroundTimeWithWeight;
    }

    public void setTurnAroundTimeWithWeight(double turnAroundTimeWithWeight) {
        this.turnAroundTimeWithWeight = turnAroundTimeWithWeight;
    }

    @Override
    public String toString() {
        return "Process [arrivalTime=" + arrivalTime + ", servicesTime="
                + servicesTime + ", remainServiceTime=" + remainServiceTime
                + ", startTime=" + startTime + ", waitTime=" + waitTime
                + ", completionTime=" + completionTime + ", turnAroundTime="
                + turnAroundTime + ", turnAroundTimeWithWeight="
                + turnAroundTimeWithWeight + "]";
    }
}