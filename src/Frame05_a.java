
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

class Block_01
{
    int address; //分区首地址
    int size;    //分区大小
    int id;      //进程id
}
public class Frame05_a extends JFrame implements ActionListener
{
    JLabel label1,label2,label3,label4,label5;
    JPanel panel1,panel2;
    JTextField textField1,textField2,textField3,textField4;
    JButton button1,button2,button3;
    JTextArea textArea;
    LinkedList<Block_01> blockList =new LinkedList<Block_01>();
    public Frame05_a()
    {
        setTitle("首次适应算法");
        setBounds(100, 100, 704, 471);
        setVisible(true);
        setResizable(false);// 设置窗体不能改变尺寸
        setLayout(null);

        label1 = new JLabel("内存空间:");
        label1.setBounds(52, 61, 58, 15);
        add(label1);

        textField1 = new JTextField();
        textField1.setBounds(125, 55, 66, 21);
        add(textField1);
        textField1.setColumns(10);

        button1 = new JButton("确认");
        button1.setBounds(219, 53, 97, 23);
        button1.addActionListener(this);
        add(button1);

        panel1 = new JPanel();
        panel1.setBounds(39, 117, 291, 113);
        panel1.setBorder(BorderFactory.createTitledBorder("分配内存"));
        add(panel1);
        panel1.setLayout(null);

        label2 = new JLabel("作业id:");
        label2.setBounds(15, 24, 58, 15);
        panel1.add(label2);

        textField2 = new JTextField();
        textField2.setBounds(86, 18, 66, 21);
        panel1.add(textField2);
        textField2.setColumns(10);

        label3 = new JLabel("分配空间:");
        label3.setBounds(15, 71, 58, 15);
        panel1.add(label3);

        textField3 = new JTextField();
        textField3.setBounds(86, 65, 66, 21);
        panel1.add(textField3);
        textField3.setColumns(10);

        button2 = new JButton("确认");
        button2.setBounds(180, 40, 97, 23);
        button2.addActionListener(this);
        panel1.add(button2);

        panel2 = new JPanel();
        panel2.setLayout(null);
        panel2.setBorder(BorderFactory.createTitledBorder("释放内存"));
        panel2.setBounds(39, 257, 291, 113);
        add(panel2);

        label4 = new JLabel("作业id:");
        label4.setBounds(15, 50, 58, 15);
        panel2.add(label4);

        textField4 = new JTextField();
        textField4.setColumns(10);
        textField4.setBounds(86, 44, 66, 21);
        panel2.add(textField4);

        button3 = new JButton("确认");
        button3.setBounds(180, 43, 97, 23);
        button3.addActionListener(this);
        panel2.add(button3);

        textArea = new JTextArea();
        textArea.setBounds(380, 43, 300, 381);
        textArea.setFocusable(false);
        add(textArea);
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setBounds(380, 43, 300, 381);
        add(scroll);

        label5 = new JLabel("运行情况:");
        label5.setBounds(379, 18, 58, 15);
        add(label5);

        validate();
    }
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource().equals(button1))
        {
            initBlock();
        }
        if(e.getSource().equals(button2))
        {
            int id;
            int size;
            id=Integer.parseInt(textField2.getText());
            size=Integer.parseInt(textField3.getText());
            if(allocation(id, size))
                textArea.append("内存分配成功\n"+"作业id："+id+"\n申请空间大小："+size+"K\n");
            else
                textArea.append("内存分配失败\n");
            show(blockList);
        }
        if(e.getSource().equals(button3))
        {
            int id;
            id=Integer.parseInt(textField4.getText());
            if(release(id))
                textArea.append("内存释放成功\n"+"作业id："+id+"\n");
            else
                textArea.append("内存释放失败\n");
            show(blockList);
        }
    }

    public void initBlock()
    {
        Block_01 block = new Block_01();
        block.size = Integer.parseInt(textField1.getText());
        block.address = 0;
        block.id = -1;
        textArea.append("内存空间大小为:"+block.size+"K\n");
        blockList.add(block);
    }

    public boolean allocation(int id, int size)//分配
    {
        boolean flag = false;
        int blockAddress;
        int blockSize;
        int blockId;
        for(int i=0;i<blockList.size();i++)//顺序遍历所有分区
        {
            blockAddress = blockList.get(i).address;
            blockSize = blockList.get(i).size;
            blockId = blockList.get(i).id;
            if(blockId == -1 && blockSize >= size)//若存在空闲且内存合适的分区
            {
                blockList.get(i).id = id;
                blockList.get(i).size = size;
                if (blockSize > size)//若当前分区重新分配后有剩余内存，则将其作为新的分区并设置新的address和size
                {
                    Block_01 newBlock = new Block_01();
                    newBlock.id = -1;
                    newBlock.address = blockAddress + size;
                    newBlock.size = blockSize - size;
                    blockList.add(i + 1, newBlock);
                }
                flag = true;
                break;
            }
        }
        return flag;
    }

    public boolean release(int id)//释放内存并合并空闲分区
    {
        boolean bool= true ;

        boolean flag = false;
        int blockId;
        for(int i=0;i<blockList.size();i++)
        {
            blockId = blockList.get(i).id;
            if(blockId == id)
            {
                blockList.get(i).id = -1;
                if(i == 0)//当前分区为第一个
                {
                    if(blockList.get(i+1).id == -1)//若与之相邻下一个分区空闲，则更新本分区的size并删除下一分区
                    {
                        blockList.get(i).size += blockList.get(i+1).size;
                        blockList.remove(i+1);
                    }
                }
                else if(i < blockList.size()-1)//当分区不是第一个也不是最后一个
                {
                    if(blockList.get(i-1).id == -1 && blockList.get(i+1).id != -1)//若与之相邻上一个分区空闲且下一分区不空闲，更新上一分区的size并删除本分区
                    {
                        blockList.get(i-1).size += blockList.get(i).size;
                        blockList.remove(i);
                    }
                    else if(blockList.get(i-1).id != -1 && blockList.get(i+1).id == -1)//若与之相邻下一个分区空闲且上一分区不空闲，更新本分区的size并删除下一分区
                    {
                        blockList.get(i).size += blockList.get(i+1).size;
                        blockList.remove(i+1);
                    }
                    else if(blockList.get(i-1).id == -1 && blockList.get(i+1).id == -1)//若与之相邻上下分区都空闲，更新上一分区的size并删除本分区和下一分区
                    {
                        blockList.get(i-1).size = blockList.get(i-1).size + blockList.get(i).size + blockList.get(i+1).size;
                        blockList.remove(i);
                        blockList.remove(i);
                    }
                }
                else if(i == blockList.size()-1)//当分区为最后一个分区时
                {
                    if(blockList.get(i-1).id == -1)//若与之相邻上一个分区空闲，更新上一分区的size并删除本分区
                    {
                        blockList.get(i-1).size += blockList.get(i).size;
                        blockList.remove(i);
                    }
                }
                flag = true;
                break;
            }
        }
        return flag;
    }

    public void show(LinkedList<Block_01> blockList)
    {
        textArea.append("\t首地址"+"\t分区大小"+"\t作业id\n");
        for(int i=0;i<blockList.size();i++)
        {
            textArea.append("分区"+i+":\t"+blockList.get(i).address+"\t"+blockList.get(i).size+"\t"+blockList.get(i).id+"\n");
        }
    }

}
