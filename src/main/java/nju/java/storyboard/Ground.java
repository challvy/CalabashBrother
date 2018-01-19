package nju.java.storyboard;


import javax.swing.*;

/***
 * @author challvy
 * <br>
 * https://github.com/challvy
 * <br>
 * Ground类继承自JFrame，被Main类调用<br>
 * 窗口设置为居中显示<br>
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
