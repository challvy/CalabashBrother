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
 * Object�ඨ���˻�����ά���������<br>
 * ʵ�ֻ�ͼ������ս���߼�<br>
 * ���౻Ground�����<br>
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
     * @return ͼƬ��������x
     */
    public int x() {
        return x;
    }

    /**
     * @return ͼƬ��������x
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
     * ͼƬ��������x
     * @param url ͨ������ͼƬ·������ͼƬ
     */
    public void setImageByUrl(String url){
        URL loc = this.getClass().getClassLoader().getResource(url);
        ImageIcon icon = new ImageIcon(loc);
        Image image = icon.getImage();
        this.setImage(image);
    }
}
