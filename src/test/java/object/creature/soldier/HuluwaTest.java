package object.creature.soldier;

import nju.java.object.creature.Role;
import nju.java.object.creature.soldier.Huluwa;
import org.junit.Test;

import javax.swing.*;
import java.net.URL;

public class HuluwaTest {

    @Test
    public void testPosition(){
        Huluwa dawa = new Huluwa("Dawa.png",0, 0, Role.Dawa,0, 0,null,1);
        assert (dawa.x()==0);
        assert (dawa.y()==0);
    }

    @Test
    public void testLive() throws Exception{
        Huluwa dawa = new Huluwa("Dawa.png",0, 0, Role.Dawa,0, 0,null,1);
        assert(dawa.isLive());
        dawa.setDie();
        assert(!dawa.isLive());
    }

    @Test
    public void testRole(){
        Huluwa dawa = new Huluwa("Dawa.png",0, 0, Role.Dawa,0, 0,null,1);
        assert (dawa.isRightRole());
    }

    @Test
    public void testImage() throws Exception{
        Huluwa dawa = new Huluwa("Dawa.png",0, 0, Role.Dawa,0, 0,null,1);
        URL url = this.getClass().getClassLoader().getResource("Dawa.png");
        ImageIcon imageIcon = new ImageIcon(url);
        dawa.setImage(imageIcon.getImage());
        assert (dawa.getImage()==imageIcon.getImage());
    }

    @Test
    public void testAttack(){
        Huluwa dawa = new Huluwa("Dawa.png",0, 0, Role.Dawa,0, 0,null,1);
        dawa.setAttack(true);
        assert(dawa.isAttacking());
        dawa.setAttack(false);
        assert(!dawa.isAttacking());
    }

}