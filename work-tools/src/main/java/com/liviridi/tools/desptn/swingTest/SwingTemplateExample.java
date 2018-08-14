package com.liviridi.tools.desptn.swingTest;

import java.awt.Graphics;

import javax.swing.JFrame;

/**
 * 模板方法 java api 中的实例(Swing)
 * @author liyun
 *
 */
public class SwingTemplateExample extends JFrame {
	private static final long serialVersionUID = 1L;

	public SwingTemplateExample(String tittle) {
		super(tittle);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(400,500);
		this.setVisible(true);
	}

	@Override
	public void paint(Graphics graphics) {
		super.paint(graphics);
		String msg = "what is this!";
		graphics.drawString(msg, 100, 100);
		graphics.drawString("nimei", 100, 120);
	}

	public static void main(String[] args) {
		new SwingTemplateExample("dandan");
	}
}
