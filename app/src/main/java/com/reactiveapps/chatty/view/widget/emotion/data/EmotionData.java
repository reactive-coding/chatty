package com.reactiveapps.chatty.view.widget.emotion.data;

import java.util.List;

/**
 * Created by Administrator on 2015/11/14.
 */
public class EmotionData{

    public String getStickerIcon() {
        return stickerIcon;
    }

    public enum EmotionCategory {
        emoji, image
    }

    private List<Emoticon> emotionList;
    private String stickerIcon;
    private EmotionCategory category;
    private int row;
    private int column;
    private Emoticon uniqueItem;

    /**
     * EmotionView所展示的数据结构
     * @param emotionList emotionView中显示的图片资源或路径
     * @param stickerIcon 在emotionView下发显示的该表情组的icon
     * @param category emotionView的类别，现在暂时有 emoji 和 image 两种
     * @param row 需要显示的行
     * @param column 需要显示的列
     */
    public EmotionData(List<Emoticon> emotionList, String stickerIcon, EmotionCategory category, int row, int column) {
        this.emotionList = emotionList;
        this.stickerIcon = stickerIcon;
        this.category = category;
        this.row = row;
        this.column = column;
    }

    /**
     * EmotionView所展示的数据结构
     * @param emotionList emotionView中显示的图片资源或路径
     * @param stickerIcon 在emotionView下发显示的该表情组的icon
     * @param category emotionView的类别，现在暂时有 emoji 和 image 两种
     * @param uniqueItem 在这组表情中特有的表情 EmotionAdapter对应为 删除，CustomAdapter对应为添加
     * @param row 需要显示的行
     * @param column 需要显示的列
     */
    public EmotionData(List<Emoticon> emotionList, String stickerIcon, EmotionCategory category, Emoticon uniqueItem, int row, int column) {
        this(emotionList, stickerIcon, category, row, column);
        this.uniqueItem = uniqueItem;
    }

    public Emoticon getUniqueItem() {
        return uniqueItem;
    }

    public List<Emoticon> getEmotionList() {
        return emotionList;
    }

    public void setEmotionList(List<Emoticon> emotionList) {
        this.emotionList = emotionList;
    }

    public EmotionCategory getCategory() {
        return category;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
