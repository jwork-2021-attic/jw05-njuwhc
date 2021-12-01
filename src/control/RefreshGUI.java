package control;

import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

public class RefreshGUI implements Runnable {

    JFrame app;

    public RefreshGUI(JFrame app) {this.app = app;}

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while(true) 
        {
            app.repaint();
            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
    }
    
}
