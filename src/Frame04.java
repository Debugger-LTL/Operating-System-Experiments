import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Frame04 extends JFrame implements ActionListener
{
    JTextArea textArea;
    JLabel label1,label2,label3,label4,label5;
    JButton button1,button2;
    JTextField textField1,textField2,textField3,textField4;
    int[] Available = {10,8,7};
    int[][] Max = {{8,7,5}, {5,2,5}, {6,6,2}};
    int[][] Allocation = {{3,2,0}, {2,0,2}, {1,3,2}};
    int[][] Need = {{5,5,5}, {3,2,3}, {5,3,0}};
    int[][] Request = new int[3][3];
    int[] Work = new int[3];
    int num;//进程编号
    public Frame04()
    {
        setTitle("银行家算法");
        setVisible(true);
        setLayout(null);
        setBounds(100,100,704,471);
        setResizable(true);// 设置窗体不能改变尺寸

        textArea = new JTextArea();
        textArea.setBounds(34, 195, 617, 229);
        textArea.setEditable(false);
        add(textArea);
        JScrollPane scroll = new JScrollPane(textArea);//添加滚动条
        scroll.setBounds(34, 195, 617, 229);
        add(scroll);

        label1 = new JLabel("请求的进程编号：");
        label1.setBounds(82, 50, 112, 15);
        add(label1);

        label2 = new JLabel("A类资源请求数目：");
        label2.setBounds(82, 80, 112, 15);
        add(label2);

        label3 = new JLabel("B类资源请求数目：");
        label3.setBounds(82, 110, 112, 15);
        add(label3);

        label4 = new JLabel("C类资源请求数目：");
        label4.setBounds(82, 140, 112, 15);
        add(label4);

        label5 = new JLabel("分配情况:");
        label5.setBounds(34, 175, 58, 15);
        add(label5);

        button1 = new JButton("清空");
        button1.setBounds(348, 121, 84, 23);
        button1.addActionListener(this);
        add(button1);

        button2 = new JButton("提交");
        button2.setBounds(474, 121, 84, 23);
        button2.addActionListener(this);
        add(button2);

        textField1 = new JTextField();
        textField1.setBounds(180, 47, 102, 21);
        add(textField1);
        textField1.setColumns(10);

        textField2 = new JTextField();
        textField2.setBounds(180, 75, 102, 21);
        add(textField2);
        textField2.setColumns(10);

        textField3 = new JTextField();
        textField3.setBounds(180, 105, 102, 21);
        add(textField3);
        textField3.setColumns(10);

        textField4 = new JTextField();
        textField4.setBounds(180, 135, 102, 21);
        add(textField4);
        textField4.setColumns(10);

        validate();
        for (int i = 0; i < 3; i++)//设置Available
        {
            for (int j = 0; j < 3; j++)
            {
                Available[i] = Available[i] - Allocation[j][i];
            }
        }
        print();
        SecurityAlgorithm();
    }

    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource().equals(button1))
        {
            textField1.setText("");
            textField2.setText("");
            textField3.setText("");
            textField4.setText("");
        }

        if(e.getSource().equals(button2))
        {
            num = Integer.parseInt(textField1.getText());
            Request[num][0] = Integer.parseInt(textField2.getText());
            Request[num][1] = Integer.parseInt(textField3.getText());
            Request[num][2] = Integer.parseInt(textField4.getText());
            textArea.append("即进程P" + num + "对各资源请求Request：(" + Request[num][0] + "," + Request[num][1] + "," + Request[num][2] + ")\n");
            BankerAlgorithm();
        }
    }

    public void print()//输出
    {
        textArea.append("此时资源分配量如下：\n");
        textArea.append("进程 \tMax\tAllocation\tNeed\tAvailable\n");
        textArea.append("\tA B C\tA B C\tA B C\tA B C\n");
        for(int i=0;i<3;i++)
        {
            textArea.append("P"+i+"\t");
            for(int j=0;j<3;j++)
            {
                textArea.append(Max[i][j]+" ");
            }
            textArea.append("\t");
            for(int j=0;j<3;j++)
            {
                textArea.append(Allocation[i][j]+" ");
            }
            textArea.append("\t");
            for(int j=0;j<3;j++)
            {
                textArea.append(Need[i][j]+" ");
            }
            textArea.append("\t");
            if(i==0)
            {
                for(int j=0;j<3;j++)
                {
                    textArea.append(Available[j]+" ");
                }
            }
            textArea.append("\n");
        }
    }

    public void BankerAlgorithm()//银行家算法
    {
        boolean T=true;
        if (Request[num][0] <= Need[num][0] && Request[num][1] <= Need[num][1] && Request[num][2] <= Need[num][2])//判断Request是否小于Need
        {
            if (Request[num][0] <= Available[0] && Request[num][1] <= Available[1] && Request[num][2] <= Available[2])//判断Request是否小于Available
            {
                for (int i = 0; i < 3; i++)
                {
                    Available[i] -= Request[num][i];
                    Allocation[num][i] += Request[num][i];
                    Need[num][i] -= Request[num][i];
                }
            }
            else
            {
                textArea.append("当前没有足够的资源可分配，进程P" + num + "\n");
                T=false;
            }
        }
        else
        {
            textArea.append("进程P" + num + "请求已经超出最大需求量Need\n");
            T=false;
        }
        if(T==true)
        {
            print();
            textArea.append("现在进入安全算法：\n");
            SecurityAlgorithm();
        }
    }

    public void SecurityAlgorithm()//安全算法
    {
        boolean[] Finish = {false, false, false};//初始化Finish
        int count = 0;//完成进程数
        int circle = 0;//循环圈数
        int[] S=new int[3];//安全序列
        for (int i = 0; i < 3; i++)//设置工作向量
        {
            Work[i] = Available[i];
        }
        boolean flag = true;
        while (count < 3)
        {
            if(flag)
            {
                textArea.append("进程 \tWork\tAllocation\tNeed\tWork+Alloction\n");
                textArea.append("\tA B C\tA B C\tA B C\tA B C\n");
                flag = false;
            }
            for (int i = 0; i < 3; i++)
            {
                if (Finish[i]==false&&Need[i][0]<=Work[0]&&Need[i][1]<=Work[1]&&Need[i][2]<=Work[2])//判断条件
                {
                    textArea.append("P"+i+"\t");
                    for (int k = 0; k < 3; k++)
                    {
                        textArea.append(Work[k]+" ");
                    }
                    textArea.append("\t");
                    for (int j = 0; j<3;j++)
                    {
                        Work[j]+=Allocation[i][j];
                    }
                    Finish[i]=true;//当前进程能满足时
                    S[count]=i;//设置当前序列排号

                    count++;//满足进程数加1
                    for(int j=0;j<3;j++)
                    {
                        textArea.append(Allocation[i][j]+" ");
                    }
                    textArea.append("\t");
                    for(int j=0;j<3;j++)
                    {
                        textArea.append(Need[i][j]+" ");
                    }
                    textArea.append("\t");
                    for(int j=0;j<3;j++)
                    {
                        textArea.append(Work[j]+" ");
                    }
                    textArea.append("\n");
                }
            }
            circle++;
            if(count==3)//判断是否满足所有进程需要
            {
                textArea.append("此时存在一个安全序列：");
                for (int i = 0; i<3;i++)//输出安全序列
                {
                    textArea.append("P"+S[i]+" ");
                }
                textArea.append("故当前可分配\n");
                break;//跳出循环
            }
            if(count<circle)//判断完成进程数是否小于循环圈数
            {
                textArea.append("当前系统处于不安全状态，故不存在安全序列\n");
                for (int i = 0; i < 3; i++)
                {
                    Available[i] += Request[num][i];
                    Allocation[num][i] -= Request[num][i];
                    Need[num][i] += Request[num][i];
                }
                break;//跳出循环
            }
        }
    }
}
