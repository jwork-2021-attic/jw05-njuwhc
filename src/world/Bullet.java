package world;

import java.awt.Color;
import java.util.concurrent.TimeUnit;

public class Bullet implements Runnable {

    private Creature owner;
    private World world;
    private int flydirection;
    

    public Bullet(Creature owner,World world, char glyph, Color color, int attack,int direction) {
        
        this.owner = owner;
        this.world = world;
        this.glyph = glyph;
        this.attackValue = attack;
        this.flag=true;
        this.flydirection=direction;
        this.color= color;
    }

    private int x;

    public void setX(int x) {
        this.x = x;
    }

    public int ownerX(){return this.owner.x();}
    public int ownerY(){return this.owner.y();}
    public int ownerDirection(){return this.owner.direction();}
    public int flyDirection(){return this.flydirection;}

    public int x() {
        return x;
    }

    private int y;

    public void setY(int y) {
        this.y = y;
    }

    public int y() {
        return y;
    }

    private char glyph;

    public char glyph() {
        return this.glyph;
    }

    private Color color;

    public Color color() {
        return this.color;
    }

    private boolean flag;//用来判断是否撞击到敌人 flag==0则撞击到物体

    private int attackValue;

    public int attackValue() {
        return this.attackValue;
    }

    public Tile tile(int wx, int wy) {
        return world.tile(wx, wy);
    }

    public void dig(int wx, int wy) {
        world.dig(wx, wy);
    }

    public void moveBy(int mx, int my) {
        Creature other = world.creature(x + mx, y + my);

        if (other == null){
            this.onEnter(x + mx, y + my, world.tile(x + mx, y + my));
        } else {
            this.owner.attack(other);
            world.remove(this);
            flag=false;
        }
    }

    
    public void onEnter(int x, int y, Tile tile) {
     //   if(x>=0 && y>=0 && x<=world.width() && y<=world.height()){
            if (tile.isGround()) {
                this.setX(x);
                this.setY(y);
            } else if (tile.isDiggable()) {
                this.dig(x, y);
                world.remove(this);
                flag=false;
            }
       //     else 
        //    {
       //         world.remove(this);
        //        flag=false;
      //      }
        }
   // }

    public void attack(Creature other) {
        this.owner.attack(other);
        world.remove(this);
        flag=false;
    }



    @Override
    public void run() {
        // TODO Auto-generated method stub
        int mx=0,my=0;
        switch(this.flyDirection())
        {
            case(1):
                mx=-1;
                my=0;
                break;
            case(2):
                mx=1;
                my=0;
                break;
            case(3):
                mx=0;
                my=-1;
                break;
            case(4):
                mx=0;
                my=1;
                break;
        }

        while(flag)
        {
            if(this.x()==1 || this.x()==this.world.width()-1 || this.y()==1 || this.y==this.world.height()-1)
            {
                world.remove(this);
                break;
            }

            moveBy(mx, my);
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
    
}
