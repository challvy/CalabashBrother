package nju.java;

import nju.java.storyboard.Ground;

import java.io.IOException;


/***
 * @author challvy
 * <br>
 * https://github.com/challvy
 * <br>
 * 程序入口类<br>
 * 使用Ground类加载窗口<br>
 * @see nju.java.storyboard.Ground
 */
public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Ground ground = new Ground();
        ground.setVisible(true);
    }
}
