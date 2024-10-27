
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Frame07_b extends JFrame implements ActionListener
{
    JButton button1,button2;
    JLabel label1,label2,label3;
    JTextField textField;
    JTextArea area1,area2;
    FileDialog open;//打开的窗口
    FileReader fr;
    BufferedReader br;
    int TrackNum=0;
    int Length=0;
    double AverageLength;
    int StartTrack;
    int[] Track = new int[999];
    public Frame07_b()
    {
        setTitle("SCAN调度算法");
        setBounds(100, 100, 704, 471);
        setVisible(true);
        setLayout(null);
        setResizable(false);// 设置窗体不能改变尺寸
        open = new FileDialog(this,"打开",FileDialog.LOAD);

        button1 = new JButton("打开文件");
        button1.setBounds(77, 94, 114, 32);
        button1.addActionListener(this);
        add(button1);

        button2 = new JButton("开始调度");
        button2.setBounds(250, 94, 114, 32);
        button2.addActionListener(this);
        add(button2);

        label1 = new JLabel("初始位置:");
        label1.setBounds(435, 105, 68, 17);
        add(label1);

        label2 = new JLabel("信息:");
        label2.setBounds(44, 194, 58, 15);
        add(label2);

        label3 = new JLabel("运行情况:");
        label3.setBounds(381, 194, 58, 15);
        add(label3);

        textField = new JTextField();
        textField.setBounds(490, 98, 91, 26);
        add(textField);
        textField.setColumns(10);

        area1 = new JTextArea();
        area1.setBounds(44, 219, 268, 189);
        area1.setEditable(false);
        add(area1);
        JScrollPane scroll1 = new JScrollPane(area1);//添加滚动条
        scroll1.setBounds(44, 219, 268, 189);
        add(scroll1);

        area2 = new JTextArea();
        area2.setBounds(381, 219, 268, 189);
        area2.setEditable(false);
        add(area2);
        JScrollPane scroll2 = new JScrollPane(area2);//添加滚动条
        scroll2.setBounds(381, 219, 268, 189);
        add(scroll2);

        validate();
    }

    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource().equals(button1))
        {
            String s;
            open.setVisible(true);
            if(open.getFile()!=null)
            {
                File file=new File(open.getDirectory()+open.getFile());
                try
                {
                    fr=new FileReader(file);
                    br=new BufferedReader(fr);
                    s=br.readLine();
                    String c[]=new String[999];
                    c=s.trim().split(" ");
                    TrackNum=c.length;
                    area1.append("磁道数为："+TrackNum+"\n");
                    area1.append("磁道号为：");
                    for (int i=0;i<TrackNum;i++)
                    {
                        Track[i] = Integer.parseInt(c[i]);
                        area1.append(Track[i]+" ");
                    }
                    fr.close();
                    br.close();
                }
                catch (Exception e1)
                {
                    e1.printStackTrace();
                }
            }
        }
        if(e.getSource().equals(button2))
        {
            if((textField.getText().equals("")))//未输入初始位置
            {
                JOptionPane.showMessageDialog(null, "请输入初始位置", "运行失败", JOptionPane.INFORMATION_MESSAGE);//跳出错误提示框
            }
            else
            {
                StartTrack = Integer.parseInt(textField.getText());
                area2.append("初始位置磁道号为："+StartTrack+"\n");
                SCAN();
            }
        }
    }

    public void SCAN()
    {
        Sort();
        if(StartTrack<=Track[0])
        {
            Length=Length+Track[TrackNum-1]-StartTrack;
            area2.append("寻道长度为："+Length+"\n");
            AverageLength=Length*1.0/TrackNum;
            area2.append("平均寻道长度为："+AverageLength+"\n");
            area2.append("磁道响应的次序为：");
            for(int i=0;i<TrackNum;i++)
                area2.append(Track[i]+" ");
        }
        else if(StartTrack>=Track[TrackNum-1])
        {
            Length=Length+StartTrack-Track[0];
            area2.append("寻道长度为："+Length+"\n");
            AverageLength=Length*1.0/TrackNum;
            area2.append("平均寻道长度为："+AverageLength+"\n");
            area2.append("磁道响应的次序为：");
            for(int i=TrackNum-1;i>=0;i--)
                area2.append(Track[i]+" ");
        }
        else
        {
            int k=0;
            for(int i=0;i<TrackNum;i++)
            {
                if(Track[i]==StartTrack)
                {
                    Length=Length+Track[TrackNum-1]-StartTrack+Track[TrackNum-1]-Track[0];
                    area2.append("寻道长度为："+Length+"\n");
                    AverageLength=Length*1.0/TrackNum;
                    area2.append("平均寻道长度为："+AverageLength+"\n");
                    area2.append("磁道响应的次序为：");
                    for(int l=i;l<TrackNum;l++)
                        area2.append(Track[l]+" ");
                    for(int m=i-1;m>=0;m--)
                        area2.append(Track[m]+" ");
                    break;
                }
                else if(Track[i]>StartTrack)
                {
                    k=i;
                    Length=Length+Track[TrackNum-1]-StartTrack+Track[TrackNum-1]-Track[0];
                    area2.append("寻道长度为："+Length+"\n");
                    AverageLength=Length*1.0/TrackNum;
                    area2.append("平均寻道长度为："+AverageLength+"\n");
                    area2.append("磁道响应的次序为：");
                    for(int m=k;m<TrackNum;m++)
                        area2.append(Track[m]+" ");
                    for(int l=k-1;l>=0;l--)
                        area2.append(Track[l]+" ");
                    break;
                }
            }
        }
    }

    public void Sort()
    {
        for (int i=0;i<TrackNum;i++)
        {
            for (int j=i+1;j<TrackNum;j++)
            {
                if (Track[j]<Track[i])
                {
                    int k=Track[j];
                    Track[j]=Track[i];
                    Track[i]=k;
                }
            }
        }
    }
}
