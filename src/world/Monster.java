package world;

import java.awt.Color;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import asciiPanel.AsciiPanel;


public class Monster extends Creature implements Runnable {

    public Monster(World world, char glyph, Color color, int maxHP, int attack, int defense, int visionRadius) {
        super(world, glyph, color, maxHP, attack, defense, visionRadius);
        //TODO Auto-generated constructor stub
    }
    Random rand = new Random();
    int direction;

    

    @Override
    public void attack(Creature other) {
        int damage = Math.max(0, this.attackValue() - other.defenseValue());
        damage = (int) (Math.random() * damage) + 1;

        other.modifyHP(-damage);

        this.notify("'%s' attack the '%s' for %d damage.",this.glyph(), other.glyph(), damage);
        //other.notify("The '%s' attacks you for %d damage.", glyph, damage);
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while(true) {
            direction = rand.nextInt(4)+1;
            switch(direction) {
                case 1:
                    if(this.x()>0){
                        this.setDirection(1);
                        new Thread(this.bulletFactory.newBullet((char) 250,this.color())).start();
                        this.moveBy(-1, 0);
                    //this.moveBy(-1, 0);
                    }
                    break;
                case 2:
                    if(this.x()<this.world.width()-1)
                    {
                        this.setDirection(2);
                        new Thread(this.bulletFactory.newBullet((char) 250,this.color())).start();
                        this.moveBy(1,0);
                    //this.moveBy(1,0);
                    }
                    break;
                case 3:
                    if(this.y()>0){
                        this.setDirection(3);
                        new Thread(this.bulletFactory.newBullet((char) 250,this.color())).start();
                        this.moveBy(0,-1);
                    //this.moveBy(0,-1);
                    }
                    break;
                case 4:
                    if(this.y()<this.world.height()-1)
                    {
                        this.setDirection(4);
                        new Thread(this.bulletFactory.newBullet((char) 250,this.color())).start();
                        this.moveBy(0,1);
                    //this.moveBy(0,1);
                    }
                    break;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(400);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
    
}
