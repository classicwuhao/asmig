package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

public final class MainWindow extends JApplet{
	MainPanel panel = new MainPanel();	

	public MainWindow(){}

	
	public void show(){
		panel.setVisible(true);
	}

}
