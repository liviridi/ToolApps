package com.liviridi.tools.desptn.swingTest;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * 观察者模式  java api 中的实例(Swing)
 * @author ly
 *
 */
public class SwingObserverExample {
	JFrame frame;

	public static void main(String[] args) {
		SwingObserverExample expl = new SwingObserverExample();
		expl.go();
	}

	public void go() {
		frame = new JFrame("tttt");

		JButton button = new JButton("do it ?");
		button.addActionListener(new AngelListener());
		button.addActionListener(new DevilListener());

		frame.setJMenuBar(null);
		frame.getContentPane().add(BorderLayout.CENTER, button);
		frame.setSize(200, 180);
		frame.setVisible(true);
	}

	class AngelListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			System.out.println("do your sister.");
		}
	}

	class DevilListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			System.out.println(event.getModifiers());
			System.out.println("just do " +
					"you idiot");
		}
	}
}
