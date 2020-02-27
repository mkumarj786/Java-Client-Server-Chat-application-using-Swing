import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.awt.Color;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JOptionPane;


public class Server extends javax.swing.JFrame{

//variables
private JTextField msg;
private JButton send;
private JTextArea msg_area;
private JLabel img;

//server variables
private ServerSocket ss;
private Socket s;
private JLabel l;
private ObjectInputStream in;
private ObjectOutputStream out;
private int port = 5000;
private int clients = 50;

 //main function
public static void main(String[] args) {
    
    Server server = new Server();
    server.run();
    
}



public Server(){

Component();
this.setVisible(true);
this.setTitle("Server");

}


//component
private void Component(){

    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setResizable(false);

    img = new javax.swing.JLabel();
    msg = new javax.swing.JTextField();
    send = new javax.swing.JButton("Send");
    msg_area = new javax.swing.JTextArea();
    l = new javax.swing.JLabel("Waiting for client to connect. PORT : 5000");


    img.setIcon(new javax.swing.ImageIcon(getClass().getResource("/background.png")));
    l.setForeground(Color.WHITE);
   
    img.setBounds(0, 0, 500, 500);
    l.setBounds(30,20,370,30);
    msg.setBounds(30,50,270,30);
    send.setBounds(310, 50, 80, 30);
    msg_area.setBounds(30,150,270,270);
    msg_area.setEditable(false);

    
    send.addActionListener(new java.awt.event.ActionListener(){
        public void actionPerformed(java.awt.event.ActionEvent evt){

            sendaction(evt);
        }
    });

    add(send);add(msg);add(msg_area);add(l);
    add(img);
    setSize(500,500);
    setLayout(null);


}

//actions
public void sendaction(java.awt.event.ActionEvent evt){

    SendMessage(msg.getText());
    msg.setText("");
    
}

//run function (server accept)
public void run(){
    try{

        

            ss= new ServerSocket(port,clients);
            while(true){
                try{
                    s = ss.accept();
                    l.setText("connected to Client on port '5000'");
                    out = new ObjectOutputStream(s.getOutputStream());
                    out.flush();
                    in = new ObjectInputStream(s.getInputStream());

                    chat();
                }catch(EOFException eofException)
                {
                }

            }


        

    }catch(IOException ioException)
    {
            ioException.printStackTrace();
    }


}


//chat function
private void chat()throws IOException
{

    String message = "";
    do{

        try{
                message = (String) in.readObject();
                msg_area.append("\n"+message);

        }catch(ClassNotFoundException classNotFoundException){}

    }while(!message.equals("Client : end"));

}


//send message
private void SendMessage(String message){

        try{
                out.writeObject("Server : " + message);
                out.flush();
                msg_area.append("\nServer : " + message);

      }catch(IOException ioException){
            msg_area.append("\n client not connected");

      }

}




}
