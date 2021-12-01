package world;

import asciiPanel.AsciiPanel;

import java.awt.Color;

public class BulletFactory { //每一个creature拥有一个子弹生成的工厂

    private World world;
    private Creature owner;

    public BulletFactory(World world, Creature owner)
    {
        this.world = world;
        this.owner = owner;
    }

    public synchronized Bullet newBullet(char style,Color color)
    {
        Bullet bullet = new Bullet(this.owner,this.world,style,color, 2,this.owner.direction());
        world.addBulletAt(bullet);
        return bullet;
    }
    
}
