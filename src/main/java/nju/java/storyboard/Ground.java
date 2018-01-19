package nju.java.storyboard;


import javax.swing.*;

/***
 * @author challvy
 * <br>
 * https://github.com/challvy
 * <br>
 * Ground��̳���JFrame����Main�����<br>
 * ��������Ϊ������ʾ<br>
 * @see nju.java.storyboard.Ground
 * @see nju.java.Main
 */
public final class Ground extends JFrame {
    public Ground(){
        Battle battle = new Battle();
        add(battle);
        setSize(battle.getBoardWidth(), battle.getBoardHeight());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);
    }
}
