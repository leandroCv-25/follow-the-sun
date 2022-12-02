package app.machines.main;

import java.awt.EventQueue;

import app.machines.view.login.Login;

public class Main {
	
public static void main(String[] args) {
		
	EventQueue.invokeLater(new Runnable() {
		public void run() {
			try {
				Login frame = new Login();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	});
	}
}
