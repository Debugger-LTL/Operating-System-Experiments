import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.*;

class PCB
{
    String name;//进程名
    int runtime;//运行时间
    int priority;//优先级
    String status;//状态
}

public class Frame01 extends JFrame implements ActionListener
{
    JLabel p1_namel;
    JLabel p1_runtimel;
    JLabel p1_priorityl;
    JLabel p1_statusl;
    JTextField p1_name;
    JTextField p1_runtime;
    JTextField p1_priority;
    JTextField p1_status;
    JButton p1_submit;
    JButton p1_clear;
    JButton p5_clear;
    JButton p5_block;
    JButton p5_wakeup;
    JButton p5_start;
    JTextArea p3_text;
    JTextArea p4_text;
    PCB ready[] = new PCB[999];//就绪数组
    PCB finished[] = new PCB[999];//消亡数组
    PCB blocked[] = new PCB[999];//阻塞数组
    int n1=0,n2=0,n3=0;//分别为三个计数器;n1用来计数就绪数组元素个数,n2用来计数消亡数组元素个数,n3用来计数阻塞数组元素个数
    public Frame01()
    {

        setTitle("进程状态转换");
        setLayout(null);
        setVisible(true);
        setBounds(200,70,1180,700);
        Font font = new Font("宋体", Font.BOLD, 16);
        setResizable(false);// 设置窗体不能改变尺寸

        JPanel panel1=new JPanel();
        JPanel panel2=new JPanel();
        JPanel panel3=new JPanel();
        JPanel panel4=new JPanel();
        JPanel panel5=new JPanel();

        panel1.setBounds(10, 10, 350, 635);
        panel1.setLayout(null);
        panel2.setBounds(370, 10, 780, 635);
        panel2.setLayout(null);
        panel3.setBounds(10, 200, 760, 400);
        panel3.setLayout(null);
        panel4.setBounds(10, 5, 760, 310);
        panel4.setLayout(null);
        panel5.setBounds(25, 285, 300, 180);
        panel5.setLayout(null);

        //设计Panel1
        panel1.setBorder(BorderFactory.createTitledBorder("添加信息"));
        p1_namel=new JLabel("进程名:");
        p1_runtimel=new JLabel("运行时间:");
        p1_priorityl=new JLabel("优先级:");
        p1_statusl=new JLabel("状态:");
        p1_submit=new JButton("插入");
        p1_clear=new JButton("清空");

        p1_name=new JTextField();
        p1_runtime=new JTextField();
        p1_priority=new JTextField();
        p1_status=new JTextField("ready");//进程的初始状态为就绪状态
        p1_status.setFocusable(false);//设置不可编辑

        p1_namel.setFont(font);
        p1_runtimel.setFont(font);
        p1_priorityl.setFont(font);
        p1_statusl.setFont(font);
        p1_submit.setFont(font);
        p1_clear.setFont(font);

        p1_submit.setBounds(50,180,80,30);
        p1_clear.setBounds(220,180,80,30);
        p1_namel.setBounds(50,40,80,30);
        p1_runtimel.setBounds(50,80,80,30);
        p1_priorityl.setBounds(50,120,80,30);
        p1_statusl.setBounds(50,160,80,30);
        p1_name.setBounds(150,40,150,30);
        p1_runtime.setBounds(150,80,150,30);
        p1_priority.setBounds(150,120,150,30);
        p1_status.setBounds(150,160,150,30);

        panel1.add(p1_namel);
        panel1.add(p1_runtimel);
        panel1.add(p1_priorityl);
//        panel1.add(p1_statusl);
        panel1.add(p1_name);
        panel1.add(p1_runtime);
        panel1.add(p1_priority);
//        panel1.add(p1_status);
        panel1.add(p1_clear);
        panel1.add(p1_submit);

        p1_clear.addActionListener(this);
        p1_submit.addActionListener(this);
        //设计pane3
        panel3.setBorder(BorderFactory.createTitledBorder("进程调度"));
        p3_text=new JTextArea(10,50);
        p3_text.setBounds(10,20,740,380);
        p3_text.setFont(new Font("宋体", Font.BOLD, 13));
        p3_text.setEditable(false);
        panel3.add(p3_text);
        JScrollPane p3_jscroll = new JScrollPane(p3_text);//添加滚动条
        p3_jscroll.setBounds(10,20,740,380);
        panel3.add(p3_jscroll);
        //设计pane4
        panel4.setBorder(BorderFactory.createTitledBorder("进程管理"));
        p4_text=new JTextArea(10,50);
        p4_text.setBounds(10,20,740,165);
        p4_text.setFont(new Font("宋体", Font.BOLD, 13));
        p4_text.setEditable(false);
        panel4.add(p4_text);
        JScrollPane p4_jscroll = new JScrollPane(p4_text);//添加滚动条
        p4_jscroll.setBounds(10,20,740,165);
        panel4.add(p4_jscroll);
        //设计pane5
        panel5.setBorder(BorderFactory.createTitledBorder("进程管理按钮"));
        p5_clear=new JButton("清空");
        p5_block=new JButton("阻塞");
        p5_wakeup=new JButton("唤醒");
        p5_start=new JButton("调度");

        p5_clear.setBounds(270,390,70,30);
        p5_block.setBounds(30,390,70,30);
        p5_wakeup.setBounds(110,390,70,30);
        p5_start.setBounds(190,390,70,30);

        p5_clear.setFont(font);
        p5_start.setFont(font);
        p5_block.setFont(font);
        p5_wakeup.setFont(font);

        panel1.add(p5_clear);
        panel1.add(p5_block);
        panel1.add(p5_wakeup);
        panel1.add(p5_start);

        p5_clear.addActionListener(this);
        p5_block.addActionListener(this);
        p5_wakeup.addActionListener(this);
        p5_start.addActionListener(this);

        add(panel1);
        add(panel2);
//        panel1.add(panel5);
        panel2.add(panel3);
        panel2.add(panel4);
    }
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()==p1_submit)//如果点击插入按钮
        {
            if((p1_name.getText().equals(""))||(p1_runtime.getText().equals(""))||(p1_priority.getText().equals("")))//进程信息不完整
            {
                JOptionPane.showMessageDialog(null, "请完善信息", "添加失败", JOptionPane.INFORMATION_MESSAGE);//跳出错误提示框
            }
            else
            {
                //获取输入的进程信息
                ready[n1]=new PCB();
                ready[n1].name=p1_name.getText();
                ready[n1].priority=Integer.parseInt(p1_priority.getText());
                ready[n1].runtime=Integer.parseInt(p1_runtime.getText());
                ready[n1].status=p1_status.getText();
                p4_text.append("进程名:"+ready[n1].name+"  优先级:"+ready[n1].priority+"  运行时间:"+ready[n1].runtime+"  运行状态:"+ready[n1].status+"\n");
                n1++;//就绪进程数加一
            }
        }
        if(e.getSource()==p1_clear)//如果点击清空按钮
        {
            //输入框中的进程信息被清空
            p1_name.setText("");
            p1_runtime.setText("");
            p1_priority.setText("");
        }
        if(e.getSource()==p5_start)//如果点击调度按钮
        {
            if(n1!=0)//如果输入的进程数不为0
            {
                sort(ready);// 将优先级进行排序
                ready[n1-1].runtime=ready[n1-1].runtime-1;//剩余执行次数减一
                ready[n1-1].status="running";//优先级最高的就绪进程先运行
                p3_text.append("现在正在执行的进程是:\n");
                p3_text.append("进程名:"+ready[n1-1].name+"  优先级:"+ready[n1-1].priority+"  运行时间:"+ready[n1-1].runtime+"  运行状态:"+ready[n1-1].status+"\n");
                p3_text.append("就绪中的进程有:\n");
                for(int i=0;i<n1-1;i++)//除数组最后一个进程元素外的进程元素全部设置为ready态
                {
                    if(ready[i].runtime!=0)
                    {
                        ready[i].status="ready";
                        p3_text.append("进程名:"+ready[i].name+"  优先级:"+ready[i].priority+"  运行时间:"+ready[i].runtime+"  运行状态:"+ready[i].status+"\n");
                    }
                }
                if(ready[n1-1].runtime==0)//运行时间为0   进程结束
                {
                    finished[n2]=ready[n1-1];
                    finished[n2].status="finished";
                    p3_text.append("进程"+ready[n1-1].name+"消亡"+"\n");
                    n2++;
                    n1--;
                }
                if(n2!=0)//如果消亡的进程数量不为0  则显示出消亡进程信息
                {
                    p3_text.append("消亡的进程有:\n");
                    for(int i=0;i<n2;i++)
                    {
                        p3_text.append("进程名:"+finished[i].name+"  优先级:"+finished[i].priority+"  运行时间:"+finished[i].runtime+"  运行状态:"+finished[i].status+"\n");
                    }
                }
                if(n3!=0)//如果阻塞的进程数量不为0 则显示出阻塞进程信息
                {
                    p3_text.append("阻塞的进程有:\n");
                    for(int i=0;i<n3;i++)
                    {
                        p3_text.append("进程名:"+blocked[i].name+"  优先级:"+blocked[i].priority+"  运行时间:"+blocked[i].runtime+"  运行状态:"+blocked[i].status+"\n");
                    }
                }
                p3_text.append("***********************\n***********************\n");
            }
            else//如果输入的进程数为0  则跳出调度失败的错误窗口信息
            {
                JOptionPane.showMessageDialog(null, "调度失败", "错误", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        if(e.getSource()==p5_clear)//如果点击清空进程信息按钮
        {
            //显示进程运行情况文本框将被清空
            p3_text.setText("");
        }
        if(e.getSource()==p5_block)//如果点击阻塞按钮
        {
            if(n1==0||ready[n1-1].runtime==0)//如果无输入进程或者运行进程时间为0  则跳出错误阻塞失败的错误窗口信息
            {
                JOptionPane.showMessageDialog(null, "阻塞失败", "错误", JOptionPane.INFORMATION_MESSAGE);
            }
            else
            {
                blocked[n3]=ready[n1-1];//将正在运行的进程赋给阻塞进程数组中
                blocked[n3].status="blocked";
                p3_text.append("进程"+ready[n1-1].name+"被阻塞"+"\n");
                n3++;//阻塞进程数加一
                n1--;//就绪进程数减一
                p3_text.append("***********************\n***********************\n");
            }

        }
        if(e.getSource()==p5_wakeup)
        {
            if(n3==0)//如果无阻塞进程  则跳出唤醒失败的错误窗口信息
            {
                JOptionPane.showMessageDialog(null, "唤醒失败", "错误", JOptionPane.INFORMATION_MESSAGE);
            }
            else
            {
                n1++;//就绪进程数加一
                ready[n1-1]=blocked[n3-1];//将阻塞的进程赋给就绪进程数组中
                p3_text.append("进程"+blocked[n3-1].name+"被唤醒"+"\n");
                n3--;//阻塞进程数减一
                p3_text.append("***********************\n***********************\n");
            }
        }
    }
    public void sort(PCB A[])//比较优先级函数
    {
        for (int j=0;j<n1;j++)
        {
            for (int k=j+1;k<n1;k++)
            {
                if (A[k].priority<A[j].priority)
                {
                    PCB t=A[k];
                    A[k]=A[j];
                    A[j]=t;
                }
            }
        }
    }
}