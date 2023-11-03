package Client;

import javax.swing.*;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Client {
    static JFrame frame;
    static JLabel labelOne;
    static JLabel labelTwo;
    static JLabel labelThree;
    static JLabel labelFour;
    static JButton connectButton;
    static JButton disconnectButton;
    static JButton setButton;
    static  JButton addButton;
    static JButton sendButton;
    static JTextField textfieldOne;
    static JTextField textfieldTwo;
    static JTextField textfieldThree;
    static JTextArea textarea;
    static int count = 0;
    static int inc = 0;
    static Socket clientSocket;
    static String numbersMessage = "";
    static String addNumber = "";
    static BufferedReader inFromServer;
    static String receivedSentence;

    public static void main(String[] args) {

        setupGUI();

        connectButton.addActionListener(new ActionListener() {

            // Set up virtual wire connection between Client and Server
            public void actionPerformed(ActionEvent e) {
                try {
                    clientSocket = new Socket("localhost", 6789);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                labelOne.setText("Connection Status: Connected");

                connectButton.setVisible(false);
                disconnectButton.setVisible(true);
                labelTwo.setVisible(true);
                textfieldOne.setVisible(true);
                setButton.setVisible(true);

                setButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        labelTwo.setEnabled(false);
                        textfieldOne.setEnabled(false);
                        setButton.setEnabled(false);

                        labelThree.setVisible(true);
                        textfieldTwo.setVisible(true);
                        addButton.setVisible(true);

                        String countNumber = textfieldOne.getText(); // Get user input of count number

                        if(!countNumber.isEmpty()) {
                            try {
                                count = Integer.parseInt(countNumber); // checks if it is a valid input
                            }catch(NumberFormatException ex){
                                textfieldOne.setText("");
                                JOptionPane.showMessageDialog(frame, "Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }

                        addButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {

                                labelFour.setVisible(true);
                                textfieldThree.setVisible(true);

                                addNumber = textfieldTwo.getText();

                                // adds numbers as a string until mac count number is reached
                                if(!addNumber.isEmpty()) {
                                    try {
                                        Double.parseDouble(addNumber);
                                        numbersMessage += addNumber + ",";
                                        textfieldThree.setText(numbersMessage);
                                        System.out.println(numbersMessage);
                                        inc++;
                                        if(inc >= count){
                                            numbersMessage = numbersMessage + "\n";
                                            labelThree.setVisible(false);
                                            textfieldTwo.setVisible(false);
                                            addButton.setVisible(false);
                                            sendButton.setVisible(true);

                                        }
                                        else{
                                            textfieldTwo.setText("");
                                        }
                                    }catch(NumberFormatException ex){
                                        textfieldTwo.setText("");
                                        JOptionPane.showMessageDialog(frame, "Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                            }
                        });
                    }
                });
            }
        });
        //Disconnected TCP conection with server when disconnect button is clicked
        disconnectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    clientSocket.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                disconnectButton.setVisible(false);
                connectButton.setVisible(true);
                setupGUI();
            }
        });
        //When send button is clicked, numbers inputted by user will be sent as a string to sendMessage function which will send data to server
        //In addition, results will be retrieved through data sent by server, and displayed
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    sendMessage(numbersMessage);
                    receiveResults();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                sendButton.setVisible(false);
            }
        });
    }
    // Function which sends input data to Server
    public static void sendMessage(String numbersMessage) throws IOException {
        DataOutputStream outToServer = new DataOutputStream (clientSocket.getOutputStream());
        outToServer.writeBytes(numbersMessage);
    }
    //Function that receives results data from server and displays result in a text area
    public static void receiveResults() throws IOException {
        inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        receivedSentence = inFromServer.readLine();
        System.out.println(receivedSentence);
        String[] results = receivedSentence.split(",");
        textarea.setText("Sum = " + results[0] + '\n');
        textarea.append("Average = " + results[1] + '\n');
        textarea.append("Max = " + results[2] + '\n');
        textarea.append("Min = " + results[3]);
        textarea.setVisible(true);
    }
    public static void setupGUI(){
        frame = new JFrame();
        frame.setBounds(100, 100, 700, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("TCP Client" );
        frame.getContentPane().setLayout(null);
        frame.setVisible(true);

        labelOne = new JLabel("");
        labelOne.setBounds(20, 20, 500, 20);
        labelOne.setFont(new Font("Times", Font.BOLD, 16));
        labelOne.setHorizontalAlignment(SwingConstants.LEFT);
        labelOne.setVerticalAlignment(SwingConstants.CENTER);
        frame.getContentPane().add(labelOne);
        labelOne.setText("Connection Status: Not Connected");

        connectButton = new JButton("Connect");
        connectButton.setBounds(500, 20, 120, 23);
        frame.getContentPane().add(connectButton);

        disconnectButton = new JButton("Disconnect");
        disconnectButton.setBounds(500, 20, 120, 23);
        frame.getContentPane().add(disconnectButton);

        labelTwo = new JLabel("");
        labelTwo.setBounds(20, 60, 500, 20);
        labelTwo.setFont(new Font("Times", Font.BOLD, 16));
        labelTwo.setHorizontalAlignment(SwingConstants.LEFT);
        labelTwo.setVerticalAlignment(SwingConstants.CENTER);
        frame.getContentPane().add(labelTwo);
        labelTwo.setText("Count of Numbers: ");
        labelTwo.setVisible(false);

        textfieldOne = new JTextField("");
        textfieldOne.setFont(new Font("Times", Font.BOLD, 14));
        textfieldOne.setBounds(300, 60, 100, 20);
        frame.getContentPane().add(textfieldOne);
        textfieldOne.setVisible(false);

        setButton = new JButton("Set");
        setButton.setBounds(500, 60, 120, 23);
        frame.getContentPane().add(setButton);
        setButton.setVisible(false);

        labelFour = new JLabel("");
        labelFour.setBounds(20, 140, 500, 20);
        labelFour.setFont(new Font("Times", Font.BOLD, 16));
        labelFour.setHorizontalAlignment(SwingConstants.LEFT);
        labelFour.setVerticalAlignment(SwingConstants.CENTER);
        frame.getContentPane().add(labelFour);
        labelFour.setText("Numbers in Message: ");
        labelFour.setVisible(false);

        textfieldThree = new JTextField("");
        textfieldThree.setFont(new Font("Times", Font.BOLD, 14));
        textfieldThree.setBounds(20, 180, 600, 20);
        frame.getContentPane().add(textfieldThree);
        textfieldThree.setVisible(false);

        labelThree = new JLabel("");
        labelThree.setBounds(20, 100, 500, 20);
        labelThree.setFont(new Font("Times", Font.BOLD, 16));
        labelThree.setHorizontalAlignment(SwingConstants.LEFT);
        labelThree.setVerticalAlignment(SwingConstants.CENTER);
        frame.getContentPane().add(labelThree);
        labelThree.setText("Add Number to Message: ");
        labelThree.setVisible(false);

        textfieldTwo = new JTextField("");
        textfieldTwo.setFont(new Font("Times", Font.BOLD, 14));
        textfieldTwo.setBounds(300, 100, 100, 20);
        frame.getContentPane().add(textfieldTwo);
        textfieldTwo.setVisible(false);

        addButton = new JButton("Add");
        addButton.setBounds(500, 100, 120, 23);
        frame.getContentPane().add(addButton);
        addButton.setVisible(false);

        sendButton = new JButton("Send");
        sendButton.setBounds(300, 220, 120, 23);
        frame.getContentPane().add(sendButton);
        sendButton.setVisible(false);

        textarea = new JTextArea();
        textarea.setBounds(20, 350, 600, 300);
        frame.getContentPane().add(textarea);
        textarea.setVisible(false);
    }
}
