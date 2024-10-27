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

public class Frame06_b extends JFrame implements ActionListener
{
    JButton button1,button2;
    JLabel label1,label2,label3;
    JTextField textField;
    JTextArea area1,area2;
    FileDialog open;//打开的窗口
    FileReader fr;
    BufferedReader br;
    int[] Page = new int[999];
    int[][] Block = new int[999][999];
    int[] PageCount = new int[999];
    int PageNum;
    int MissingPageNum=0;
    int Num=0;
    double MissingPageRate;
    int BlockNum;
    public Frame06_b()
    {
        setTitle("LRU置换算法");
        setBounds(100, 100, 704, 471);
        setVisible(true);
        setLayout(null);
        setResizable(false);// 设置窗体不能改变尺寸
        open = new FileDialog(this,"打开",FileDialog.LOAD);

        button1 = new JButton("打开文件");
        button1.setBounds(77, 94, 114, 32);
        button1.addActionListener(this);
        add(button1);

        button2 = new JButton("开始运行");
        button2.setBounds(250, 94, 114, 32);
        button2.addActionListener(this);
        add(button2);

        label1 = new JLabel("物理块数:");
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
                    PageNum=c.length;
                    area1.append("页面数为："+PageNum+"\n");
                    area1.append("页面序列为：");
                    for (int i=0;i<PageNum;i++)
                    {
                        Page[i] = Integer.parseInt(c[i]);
                        area1.append(Page[i]+" ");
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
            if((textField.getText().equals("")))//未输入物理块数
            {
                JOptionPane.showMessageDialog(null, "请输入物理块数", "运行失败", JOptionPane.INFORMATION_MESSAGE);//跳出错误提示框
            }
            else
            {
                BlockNum=Integer.parseInt(textField.getText());
                for (int i = 0; i<PageNum; i++)
                {
                    for (int j = 0; j<BlockNum; j++)
                        Block[i][j] = -1;
                }
                LRU();
                Show();
            }
        }
    }

    public void LRU()
    {
        int temp,flag = 0;
        int i,j,k;
        PageCount[0] = 0;
        for(i = 0; i < PageNum; i++)
        {
            for(j = 0; j < BlockNum; j++)
            {
                if (Page[i] == Block[flag][j])
                {
                    PageCount[j] = i;
                    break;
                }
            }
            if(j == BlockNum)
            {
                for(j = 0; j < BlockNum; j++)
                {
                    if(Block[flag][j] != -1)
                        Block[i][j] = Block[flag][j];
                }
                for(j = 0; j < BlockNum; j++)
                {
                    if(Block[i][j] == -1)
                    {
                        Block[i][j] = Page[i];
                        PageCount[j] = i;
                        MissingPageNum++;
                        flag = i;
                        break;
                    }
                }
                if(j == BlockNum)
                {
                    temp = 0;
                    for(j = 0;j < BlockNum; j++)
                    {
                        if(PageCount[temp] > PageCount[j])
                        {
                            temp = j;
                        }
                    }
                    Block[i][temp] = Page[i];
                    PageCount[temp] = i;
                    MissingPageNum++;
                    Num++;//置换次数加一
                    flag = i;
                }
            }
        }
    }

    public void Show()
    {
        MissingPageRate = (double) MissingPageNum / PageNum;
        int i,j;
        area2.append("访问页面："+"\t");
        for(i = 0; i < PageNum; i++)
        {
            area2.append(Page[i] + " ");
        }
        area2.append("\n");
        for(j = 0; j < BlockNum; j++)
        {
            area2.append("内存块"+(j+1)+"："+"\t");
            for(i = 0; i < PageNum; i++)
            {
                if(Block[i][j] == -1)
                    area2.append("   ");
                else
                    area2.append(Block[i][j] + " ");
            }
            area2.append("\n");
        }
        area2.append("缺页次数为：" + MissingPageNum + "\n" + "缺页率为：" + MissingPageRate * 100 + "%\n" + "置换次数为：" + Num);
    }
}
