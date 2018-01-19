package nju.java.object;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.net.URL;

/***
 * @author challvy
 * <br>
 * https://github.com/challvy
 * <br>
 * Object类定义了基本二维物体的属性<br>
 * 实现绘图功能与战斗逻辑<br>
 * 该类被Ground类调用<br>
 *
 * @see nju.java.storyboard.Ground
 * @see nju.java.Main
 */
public class Object {

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected Image image;

    public Object(String url, int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        URL loc = this.getClass().getClassLoader().getResource(url);
        ImageIcon icon = new ImageIcon(loc);
        Image image = icon.getImage();
        this.setImage(image);
    }

    /**
     * @return 图片定点坐标x
     */
    public int x() {
        return x;
    }

    /**
     * @return 图片定点坐标x
     */
    public int y() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * 图片定点坐标x
     * @param url 通过传入图片路径设置图片
     */
    public void setImageByUrl(String url){
        URL loc = this.getClass().getClassLoader().getResource(url);
        ImageIcon icon = new ImageIcon(loc);
        Image image = icon.getImage();
        this.setImage(image);
    }
}
