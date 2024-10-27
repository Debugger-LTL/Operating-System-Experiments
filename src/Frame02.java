import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Area;
import java.util.Random;
import java.util.concurrent.Semaphore;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Frame02 extends JFrame implements ActionListener
{
    JTextArea area;//文本区
    JLabel label1,label2,label3;//标签
    JButton button,button1;//按钮
    JTextField textField1,textField2,textField3;
    Random rame = new Random();
    int producerCount,consumerCount,size,maxsize1,maxsize2,cur=1;//生产者数量  消费者数量  缓冲区容量
    public Frame02()
    {
        setTitle("生产者-消费者");
        setVisible(true);
        setLayout(null);
        setBounds(200,70,400,700);
        setResizable(false);// 设置窗体不能改变尺寸

        //设计Panel1
        JPanel panel1=new JPanel();
        panel1.setBounds(10,10,365,200);
        panel1.setLayout(null);
        panel1.setBorder(BorderFactory.createTitledBorder("添加信息"));
        label1=new JLabel("生产者个数:");
        label2=new JLabel("消费者个数:");
        label3=new JLabel("缓冲区容量:");
        textField1=new JTextField();
        textField2=new JTextField();
        textField3=new JTextField();
        button=new JButton("开始运行");
        button1=new JButton("随机添加");

        label1.setBounds(50,40,80,30);
        label2.setBounds(50,80,80,30);
        label3.setBounds(50,120,80,30);
        textField1.setBounds(150,40,150,30);
        textField2.setBounds(150,80,150,30);
        textField3.setBounds(150,120,150,30);
        button.setBounds(230,160,90,30);
        button1.setBounds(30,160,90,30);

        panel1.add(label1);
        panel1.add(label2);
        panel1.add(label3);
        panel1.add(textField1);
        panel1.add(textField2);
        panel1.add(textField3);
        panel1.add(button1);
        panel1.add(button);
        button.addActionListener(this);
        button1.addActionListener(this);

        //设计Panel2
        JPanel panel2=new JPanel();
        panel2.setBounds(10,220,365,450);
        panel2.setLayout(null);
        panel2.setBorder(BorderFactory.createTitledBorder("运行情况"));
        area = new JTextArea();
        area.setBounds(5,15,355,430);
        area.setEditable(false);
        panel2.add(area);
        JScrollPane jscroll = new JScrollPane(area);//添加滚动条
        jscroll.setBounds(5,15,355,430);
        panel2.add(jscroll);

        add(panel1);
        add(panel2);
        validate();
    }
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource()==button1){
            textField1.setText(Integer.toString(rame.nextInt(9)+1));
            textField2.setText(Integer.toString(rame.nextInt(9)+1));
            textField3.setText(Integer.toString(rame.nextInt(9)+1));
        }
        if(e.getSource()==button)
        {
            if((textField1.getText().equals(""))||(textField2.getText().equals(""))||(textField3.getText().equals("")))//信息不完整
            {
                JOptionPane.showMessageDialog(null, "请完善信息", "运行失败", JOptionPane.INFORMATION_MESSAGE);//跳出错误提示框
            }
            else
            {
                maxsize1=Integer.parseInt(textField1.getText());
                maxsize2=Integer.parseInt(textField2.getText());
                producerCount = Integer.parseInt(textField1.getText());
                consumerCount = Integer.parseInt(textField2.getText());
                size = Integer.parseInt(textField3.getText());
                Cache cache = new Cache(size);
                Producer p = new Producer(cache);
                Consumer c = new Consumer(cache);

                for (int i = 0; i < producerCount; i++)
                {
                    Thread tp = new Thread(p);
                    tp.start();
                }
                for (int i = 0; i < consumerCount; i++)
                {
                    Thread cp = new Thread(c);
                    cp.start();
                }
            }
        }
    }

    class Cache
    {
        int cacheSize = 0;
        Semaphore mutex;
        Semaphore empty;//保证了容器空的时候(empty的信号量<=0)消费者等待
        Semaphore full;  //保证了容器满的时候(full的信号量 <=0)生产者等待
        public Cache(int size)
        {
            mutex = new Semaphore(1);   //二进制信号量，表示互斥锁
            empty = new Semaphore(size);
            full = new Semaphore(0);
        }
        public void produce() throws InterruptedException
        {

            empty.acquire();// 消耗一个空位
            mutex.acquire();
            if (cacheSize==size) cur=0;
            cacheSize++;

                if (cur==1)
            area.append("生产者"+(rame.nextInt(100)%maxsize1+1)+"生产了一个产品， 当前仓库里的产品数为" + cacheSize+"\n");
                if (cur==0)
                    area.append("仓库空，消费者阻塞中"+"\n");
            mutex.release();
            full.release();// 增加了一个产品
            cur=1;
        }
        public void consume() throws InterruptedException
        {
            if (cacheSize==0) cur=0;
            full.acquire();// 消耗了一个产品
            mutex.acquire();
            cacheSize--;

            if (cur==0)
            area.append("仓库满，生产者" +
                    "阻塞中"+"\n");
            else
            area.append( "消费者"+(rame.nextInt(100)%maxsize2+1)+"消费了一个产品， 当前仓库里的产品数为" + cacheSize+"\n");

            mutex.release();
            empty.release();// 增加了一个空位
            cur=1;
        }
    }

    class Producer implements Runnable
    {
        private Cache cache;
        public Producer(Cache cache)
        {
            this.cache = cache;
        }
        public void run()
        {
            while(true)
            {
                try
                {
                    cache.produce();
                    Thread.sleep(2000);
//                    cache.wait();
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    class Consumer implements Runnable
    {
        private Cache cache;
        public Consumer(Cache cache)
        {
            this.cache = cache;
        }
        public void run()
        {
            while(true)
            {
                try
                {
                    cache.consume();
                    Thread.sleep(2000);
//                    cache.wait();
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}

