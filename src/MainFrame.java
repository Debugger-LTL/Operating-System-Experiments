import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;
public class MainFrame extends JFrame implements ActionListener
{
    JLabel label;//标签
    JTextArea area;//文本区
    JPanel panel;//内容面板
    JMenuBar jm;//菜单条
    JMenu m1,m2,m3,m4,m5;//菜单
    JMenuItem item1,item2,item3,item4,item5,item6,item7,item8,item9,item10,item11,item12;//子菜单
    JToolBar toolbar;//工具栏
    JButton Jb1,Jb2,Jb3;//按钮
    FileDialog save, open; //保存、打开的窗口
    FileWriter fw;
    BufferedWriter bw;
    FileReader fr;
    BufferedReader br;
    public MainFrame()
    {
        //窗口的各属性设置
        setTitle("操作系统辅助教学系统");
        setVisible(true);
        setBounds(260,100,700,400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        jm=new JMenuBar();
        setJMenuBar(jm);//向窗口中添加菜单条
        panel=new JPanel(new BorderLayout());//创建内容面板  使用边界布局
        //菜单名称设置
        m1=new JMenu("文件(F)");
        m2=new JMenu("编辑(E)");
        m3=new JMenu("进程管理(P)");
        m4=new JMenu("存储管理(M)");
        m5=new JMenu("设备管理(D)");
        //菜单快捷键设置
        m1.setMnemonic(KeyEvent.VK_F);//设置快捷键ALT+F
        m2.setMnemonic(KeyEvent.VK_E);//设置快捷键ALT+E
        m3.setMnemonic(KeyEvent.VK_P);//设置快捷键ALT+P
        m4.setMnemonic(KeyEvent.VK_M);//设置快捷键ALT+M
        m5.setMnemonic(KeyEvent.VK_D);//设置快捷键ALT+D
        //向菜单条中添加菜单
        jm.add(m1);
        jm.add(m2);
        jm.add(m3);
        jm.add(m4);
        jm.add(m5);
        //子菜单名称设置
        item1=new JMenuItem("打开(O)     ");
        item1.addActionListener(this);//绑定监听器
        item1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,ActionEvent.CTRL_MASK));//设置快捷键Ctrl+O
        item2=new JMenuItem("保存(S)     ");
        item2.addActionListener(this);//绑定监听器
        item2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));//设置快捷键Ctrl+S
        item3=new JMenuItem("退出(X)     ");
        item3.addActionListener(this);//绑定监听器
        item3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,ActionEvent.CTRL_MASK));//设置快捷键Ctrl+X
        item4=new JMenuItem("允许编辑");
        item4.addActionListener(this);//绑定监听器
        item5=new JMenuItem("禁止编辑");
        item5.addActionListener(this);//绑定监听器
        item6=new JMenuItem("进程状态转换");
        item6.addActionListener(this);
        item7=new JMenuItem("生产者-消费者");
        item7.addActionListener(this);
        item8=new JMenuItem("进程调度");
        item8.addActionListener(this);
        item9=new JMenuItem("银行家算法");
        item9.addActionListener(this);
        item10=new JMenuItem("动态分区分配算法");
        item10.addActionListener(this);
        item11=new JMenuItem("页面置换算法");
        item11.addActionListener(this);
        item12=new JMenuItem("磁盘调度算法");
        item12.addActionListener(this);
        //向菜单中添加子菜单
        m1.add(item1);
        m1.add(item2);
        m1.addSeparator();//在子菜单之间增加分隔线
        m1.add(item3);
        m2.add(item4);
        m2.add(item5);
        m3.add(item6);
        m3.add(item7);
        m3.add(item8);
        m3.add(item9);
        m4.add(item10);
        m4.add(item11);
        m5.add(item12);
        save = new FileDialog(this,"保存",FileDialog.SAVE);
        open = new FileDialog(this,"打开",FileDialog.LOAD);
        area=new JTextArea();
        //按钮名称设置
        Jb1=new JButton("打开");
        Jb1.addActionListener(this);//绑定监听器
        Jb2=new JButton("保存");
        Jb2.addActionListener(this);//绑定监听器
        Jb3=new JButton("退出");
        Jb3.addActionListener(this);//绑定监听器
        toolbar=new JToolBar();
        //向工具栏中添加按钮
        toolbar.add(Jb1);
        toolbar.add(Jb2);
        toolbar.add(Jb3);
        add(toolbar,BorderLayout.NORTH);//添加工具栏到内容面板
        label=new JLabel("实验内容");
        panel.add(area,BorderLayout.CENTER);//添加文本区到内容面板
        JScrollPane jscroll=new JScrollPane(area);
        panel.add(jscroll);//给文本框添加滚动条
        panel.add(label,BorderLayout.NORTH);//添加标签到内容面板
        add(panel);
        validate();
    }

    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource().equals(item1)||e.getSource().equals(Jb1))//打开
        {
            open();
        }
        if(e.getSource().equals(item2)||e.getSource().equals(Jb2))//保存
        {
            save();
        }
        if(e.getSource().equals(item3)||e.getSource().equals(Jb3))//退出
        {
            exit();
        }
        if(e.getSource().equals(item4))//允许编辑
        {
            CanEdit();
        }
        if(e.getSource().equals(item5))//禁止编辑
        {
            CanNotEdit();
        }
        if (e.getSource().equals(item6))//进程状态转换
            new Frame01();

        if (e.getSource().equals(item7))
            new Frame02();

        if (e.getSource().equals(item8))
            new Frame03();
        if(e.getSource().equals(item9))
            new Frame04();
        if(e.getSource().equals(item10))
            new Frame05();
        if(e.getSource().equals(item11))
        new Frame06();
        if(e.getSource().equals(item12))
        new Frame07();
    }

    void CanNotEdit()//禁止编辑
    {
        area.setEditable(false);
    }

    void CanEdit()//允许编辑
    {
        area.setEditable(true);
    }

    void open()//打开
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
                area.setText("");
                while((s=br.readLine())!=null)
                {
                    area.append(s+'\n');
                }
                fr.close();
                br.close();
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    void save()//保存
    {
        save.setVisible(true);
        if(save.getFile()!=null)
        {
            File file=new File(save.getDirectory()+save.getFile());
            try
            {
                fw=new FileWriter(file);
                bw=new BufferedWriter(fw);
                bw.write(area.getText(),0,(area.getText().length()));
                bw.close();
                fw.close();
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    void exit()//退出
    {
        System.exit(0);
    }

    public static void main(String[] args)
    {
        new MainFrame();
    }
}

