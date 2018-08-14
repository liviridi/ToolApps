package com.liviridi.tools.desptn.swingTest;

import javax.swing.*;

import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
//import java.awt.event.*;

public class JTextFTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame frame=new JFrame();
		final JTextField tf=new JTextField(15);
		tf.addKeyListener(new KeyListener(){

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				int charstr=e.getKeyChar();
				//take care the str don't contain this one
				String str=tf.getText();
				//ignore the first zero
				if(str.length()==0) {
					if(charstr==KeyEvent.VK_0){
						e.consume();
					}

				}
				//control the value between 0 and 9
				if(charstr<KeyEvent.VK_0||charstr>KeyEvent.VK_9){
					e.consume();
				}
				//control the value below 120 or eqal 120
				if(str.length()>0){
					byte[] bt=str.getBytes();
					if(str.length()==2){
						if(bt[0]>KeyEvent.VK_1){
							e.consume();
						}else if(bt[1]>KeyEvent.VK_2){

							e.consume();
						}else if(bt[1]==KeyEvent.VK_2){
							if(charstr!=KeyEvent.VK_0){
								e.consume();
							}
						}
					}
					if(str.length()>2) e.consume();
				}

			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				System.out.println("press" + e.getKeyCode());
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				System.out.println("release" + e.getKeyCode());

			}

		});
		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(tf);
		frame.setSize(300,200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

}
