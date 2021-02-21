package CustomDialogs;

import aggressor.AggressorClient;
import beacon.TaskBeacon;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyTestDialog extends JDialog {
    private JPanel contentPane;
    private JButton RDIButton;
    private JButton BOFButton;
    private AggressorClient mClient;
    private String[] mBids;

    public MyTestDialog(AggressorClient client,String[] bids) {
        this.mClient = client;
        this.mBids = bids;//beaconID的数组
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(RDIButton);
        RDIButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TaskBeacon beacon = new TaskBeacon(MyTestDialog.this.mClient,MyTestDialog.this.mBids);
                beacon.RDITest();
                MyTestDialog.this.dispose();
            }
        });
        BOFButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TaskBeacon beacon = new TaskBeacon(MyTestDialog.this.mClient,MyTestDialog.this.mBids);
                beacon.MyTestBOF();
                MyTestDialog.this.dispose();
            }
        });


    }

    public  void Show(){
        this.pack();
        this.setVisible(true);
    }


}
