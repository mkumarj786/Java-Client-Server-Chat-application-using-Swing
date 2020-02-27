import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.awt.Color;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.lang.Thread;
import java.util.concurrent.*;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;





public class Client extends javax.swing.JFrame {

    //variables
    String server; 
    private javax.swing.JTextArea msg_area;
    private javax.swing.JButton send;
    public javax.swing.JLabel status;
    private javax.swing.JLabel img;
    private javax.swing.JTextField msg;
    private javax.swing.JLabel po;
    private javax.swing.JTextField port_number;
    private javax.swing.JButton connect;
    
    //socket vari
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String message="";
    public Socket s;
    //public boolean on = false;


    //main function
public static void main(String[] args) {
    Client c = new Client("127.0.0.1");
    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
 
        Runnable task = new Runnable() {
            public void run() {
                
                c.start();
            }
        };
 // delay time is 10. so make sure you open the server and press the connect button in client part within 10 seconds. 
 //after 10 seconds you need to restart the client.
        int delay = 10;
        scheduler.schedule(task, delay, TimeUnit.SECONDS);
        scheduler.shutdown();
    
}
   


    public Client(String s){

        component();
        
        this.setTitle("Client");
        this.setVisible(true);
        server = s;
    }

   //component 
    private void component() {

         po = new javax.swing.JLabel("Enter port number");
         port_number = new javax.swing.JTextField();
         connect = new javax.swing.JButton("Connect");
         msg = new javax.swing.JTextField();
         status = new javax.swing.JLabel("waiting for connection");
         img = new javax.swing.JLabel();
         send = new javax.swing.JButton("send");
         msg_area = new javax.swing.JTextArea();

         img.setIcon(new javax.swing.ImageIcon(getClass().getResource("/background.png")));
        status.setForeground(Color.WHITE);
        po.setForeground(Color.WHITE);
        status.setForeground(Color.WHITE);
       
        


        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        img.setBounds(0, 0, 500, 500);
        po.setBounds(30,20,270,25);
        port_number.setBounds(30,50,80,30);
        connect.setBounds(130,50,120,30);
        msg.setBounds(30,100,270,30);
        send.setBounds(310, 100, 80, 30);
        status.setBounds(30,130,270,25);
        msg_area.setBounds(30,170,270,270);
        msg_area.setEditable(false);
        msg_area.setColumns(20);
        msg_area.setRows(5);


        send.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent evt){
                sendaction(evt);
            }
       });

        connect.addActionListener(new java.awt.event.ActionListener(){
           public void actionPerformed(java.awt.event.ActionEvent e){
               connectaction(e);
           }
       });

       

        add(msg);add(po);
       
        add(connect);
        add(port_number);
        add(msg_area);add(send);add(status);
        setLayout(null);
        setSize(500,500);
        add(img);


    }

    //actions

public void connectaction(java.awt.event.ActionEvent e){
    
    status.setText("trying to connect");
    //on = true;   
     port_number.setEditable(false);  
    

}

private void sendaction(java.awt.event.ActionEvent evt){
    SendMessage(msg.getText());
    msg.setText("");
    
}


//start function (connect part)
    public void start()
    {             
        if(port_number.getText().equals("")){
            port_number.setText("5000");
            status.setText("Trying to connect to default port : 5000");
        }
        try{ 
           
            try{
            s = new Socket(InetAddress.getByName(server),Integer.parseInt(port_number.getText()));
            
            }catch(IOException ioException){
            status.setText("sever is not online");
            JOptionPane.showMessageDialog(null, "sever is not online", "warning", JOptionPane.WARNING_MESSAGE);
            dispose();    
        }
    
            status.setText("connected to I.P :"+InetAddress.getByName(server) );   

            out = new ObjectOutputStream(s.getOutputStream());
            out.flush();
            in = new ObjectInputStream(s.getInputStream());

            chat();
        
            
        }catch(IOException ioException)
        {   
             ioException.printStackTrace();
        }}
        

        
    
//chat function
    private void chat() throws IOException
    {
        do{

                try{

                    message = (String) in.readObject();
                    msg_area.append("\n"+message);    
                }catch(ClassNotFoundException classNotFoundException){}

        }
        while(!message.equals("Client : end"));
    }

    //send message function 

    private void SendMessage(String mes){

        try{
            out.writeObject("Client : " + mes);
            out.flush();
            msg_area.append("\nClient : " + mes);
        }catch(IOException ioException)
        {
            msg_area.append("\n Unable to Send Message");
        }
    }



    




}