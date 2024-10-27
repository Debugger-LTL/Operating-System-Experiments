
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Frame06 extends JFrame implements ActionListener
{

    JButton jbt1,jbt2;
    JPanel panel;
    public Frame06()
    {
        setTitle("页面置换算法");
        setLayout(null);
        setVisible(true);
        setBounds(600,200,300,400);
        setResizable(false);// 设置窗体不能改变尺寸

        panel=new JPanel();
        panel.setLayout(null);
        panel.setBounds(10,10,265,340);
        panel.setBorder(BorderFactory.createTitledBorder("选择页面置换算法"));
        jbt1=new JButton("FIFO置换算法");
        jbt2=new JButton("LRU置换算法");
        jbt1.setBounds(55,100,160,35);
        jbt2.setBounds(55,160,160,35);
        jbt1.addActionListener(this);
        jbt2.addActionListener(this);
        panel.add(jbt1);
        panel.add(jbt2);
        add(panel);
        validate();
    }

    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()==jbt1)
        {
            new Frame06_a();
        }
        if(e.getSource()==jbt2)
        {
            new Frame06_b();
        }
    }
}
