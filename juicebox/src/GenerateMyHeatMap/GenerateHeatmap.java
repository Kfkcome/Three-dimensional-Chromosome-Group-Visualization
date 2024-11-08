/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2011-2023 Broad Institute, Aiden Lab, Rice University, Baylor College of Medicine
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package GenerateMyHeatMap;

import juicebox.Context;
import juicebox.HiC;
import juicebox.MainWindow;
import juicebox.data.ChromosomeHandler;
import juicebox.data.basics.Chromosome;
import juicebox.gui.SuperAdapter;
import juicebox.mapcolorui.JColorRangePanel;
import juicebox.mapcolorui.RangeSlider;
import juicebox.track.HiCTrack;
import juicebox.track.ResourceTree;
import juicebox.track.TrackPanel;
import juicebox.track.feature.Feature2DGuiContainer;
import juicebox.windowui.HiCZoom;
import juicebox.windowui.MatrixType;
import juicebox.windowui.layers.Load2DAnnotationsDialog;
import org.broad.igv.util.Pair;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class GenerateHeatmap {

    public static SuperAdapter superAdapter;
    public static String current_hic_path;
    public static String current_gene_path;//基因结构注释文件
    private static GenerateHeatmap instance;
    String current_chromosome;

    private GenerateHeatmap() {
        System.setProperty("java.awt.headless", "false");
        MainWindow.initApplication();//初始化程序
        MainWindow.getInstance();
        superAdapter = MainWindow.superAdapter;
    }


    /**
     * 功能描述：公共静态方法，提供全局访问点
     *
     * @return {@link GenerateHeatmap }
     * @author new
     * @date 2023/11/30
     */
    public synchronized static GenerateHeatmap getInstance() {
        if (instance == null) {
            instance = new GenerateHeatmap();
        }
        return instance;
    }

    @NotNull
    private synchronized static List<String> getFileNames(String path) {
//        File hicFile=new File("/home/new/fsdownload/Arabidopsis-thaliana_Col-0_Root/Arabidopsis-thaliana_Col-0_Root.hic");
        File hicFile = new File(path);
        List<File> files = new ArrayList<>();
        List<String> fileNames = new ArrayList<>();
        files.add(hicFile);
        for (File f : files) {
            fileNames.add(f.getAbsolutePath());
        }
        return fileNames;
    }

    /**
     * 功能描述：
     *
     * @param path 文件路径
     * @return boolean
     * @author new
     * @date 2023/11/13
     */
    private synchronized boolean loadHicFile(String path) throws IOException {
//        if (path.equals(current_hic_path)) {
//            return false;
//        }
        current_hic_path = path;
        List<String> fileNames = getFileNames(path);
        try {
            //重置之前加载的文件
            superAdapter.getHeatmapPanel().disableAssemblyEditing();
            superAdapter.resetAnnotationLayers();
            //加载hic文件
            superAdapter.unsafeLoad(fileNames, false, false);
        } catch (IOException e) {
            throw new IOException(e);
        }
        return true;
    }

    /**
     * 功能描述：选择染色体
     *
     * @param chromosome1_name
     * @return boolean
     * @author new
     * @date 2023/11/28
     */
    private boolean setChromosome(String chromosome1_name) {
//        if (chromosome1_name.equals(current_chromosome)) {
//            return false;
//        }
        current_chromosome = chromosome1_name;
        ChromosomeHandler chromosomeHandler = superAdapter.getHiC().getChromosomeHandler();
        Chromosome chr1 = chromosomeHandler.getChromosomeFromName(current_chromosome);
        if (chr1 != null) {
            //TODO: 处理找不到染色体的异常
            superAdapter.unsafeSetSelectedChromosomes(chr1, chr1);
        } else {
            System.out.println("找不到这个染色体");
            return false;
        }
        return true;
    }

    private boolean setHeatMapColor(double min, double max) {
        HiC hic = superAdapter.getHiC();
        JColorRangePanel colorRangePanel = superAdapter.getMainViewPanel().getColorRangePanel();


        RangeSlider colorRangeSlider = colorRangePanel.getColorRangeSlider();
        double min_slider = min * colorRangePanel.getColorRangeScaleFactor();
        double max_slider = max * colorRangePanel.getColorRangeScaleFactor();
        colorRangeSlider.setLowerValue((int) min_slider);
        colorRangeSlider.setUpperValue((int) max_slider);
        ChangeListener[] changeListeners = colorRangeSlider.getChangeListeners();
        for (ChangeListener changeListener : changeListeners) {
            changeListener.stateChanged(null);
        }


//        String key = "";
//        try {
//            if (hic != null && hic.getZd() != null && hic.getDisplayOption() != null) {
//                key = HeatmapRenderer.getColorScaleCacheKey(hic.getZd(), hic.getDisplayOption(), hic.getObsNormalizationType(), hic.getControlNormalizationType());
//            }
//        } catch (Exception e2) {
//            if (HiCGlobals.printVerboseComments) {
//                e2.printStackTrace();
//            }
//        }
//
//        superAdapter.getHeatmapPanel().setNewDisplayRange(hic.getDisplayOption(), min, max, key);
        return true;
    }

    /**
     * 功能描述：生成完整的热图
     *
     * @param path
     * @param chromosome1_name
     * @param displayOption
     * @param normalizationType
     * @return {@link BufferedImage }
     * @author new
     * @date 2023/11/27
     */
    public synchronized BufferedImage generateFullHeatMap(int type, String path, String chromosome1_name, String displayOption, String normalizationType, double minColor, double maxColor, int clarity, int resolution) throws IOException {
        BufferedImage temp = new BufferedImage(1502 * clarity, 1502 * clarity, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = temp.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (type == 1) {
            loadHicFile(path);//加载hic文件
            setChromosome(chromosome1_name);//选择染色体
            setNormalizationType(normalizationType);//设置标准化类型
            setDisplayOption(displayOption);//设置显示选项
        }
        //设置颜色
        setHeatMapColor(minColor, maxColor);


        //设置resolution
        Map<Integer, HiCZoom> idxZoomMap = superAdapter.getMainViewPanel().getResolutionSlider().getIdxZoomMap();
        Integer targetKey = -1;
        for (Map.Entry<Integer, HiCZoom> entry : idxZoomMap.entrySet()) {
            if (entry.getValue().getBinSize() == resolution) {
                targetKey = entry.getKey();
                break;
            }
        }
        if (targetKey == -1) {
            targetKey = 0;
        }
        superAdapter.getMainViewPanel().getResolutionSlider().getSlider().setValue(targetKey);

        superAdapter.getHiC().setScaleFactor(clarity);

//        superAdapter.getMainWindow().setSize(3000 * clarity, 3000 * clarity);
//        superAdapter.getMainWindow().setMinimumSize(new Dimension(1502 * clarity, 1502 * clarity));
//        superAdapter.getHeatmapPanel().setSize(1502 * clarity, 1502 * clarity);
        superAdapter.getHeatmapPanel().setMinimumSize(new Dimension(1502 * clarity * clarity, 1502 * clarity * clarity));
        superAdapter.getHeatmapPanel().setPreferredSize(new Dimension(1502 * clarity * clarity, 1502 * clarity * clarity));
        superAdapter.getHeatmapPanel().setSize(1502 * clarity * clarity, 1502 * clarity * clarity);

        //设置颜色
        System.out.println("修改颜色成功: minColor=" + minColor + ", maxColor=" + maxColor);
        setHeatMapColor(minColor, maxColor);

        superAdapter.getHeatmapPanel().paint(g2d);
        String selectedItem = (String) superAdapter.getMainViewPanel().getObservedNormalizationComboBox().getSelectedItem();
        String selectedItem1 = superAdapter.getMainViewPanel().getDisplayOptionComboBox().getSelectedItem().toString();

        System.out.println("当前选择的标准化类型为：" + selectedItem + "\n" +
                "当前选择的显示类型是：" + selectedItem1);
//        superAdapter.getHeatmapPanel().reset();
        return temp;
    }

    public synchronized long getChromosomeLength(String path, String chromosome1_name) throws IOException {
        loadHicFile(path);
        setChromosome(chromosome1_name);
        Context xContext = superAdapter.getHiC().getXContext();
        return xContext.getChromosome().getLength();
    }

    /**
     * 功能描述：生成基因结构注释的图
     *
     * @param path
     * @param annotation_path
     * @param chromosome1_name
     * @return {@link BufferedImage }
     * @author new
     * @date 2023/11/27
     */
    public synchronized BufferedImage generateAnnotation1D(String path, String annotation_path, String chromosome1_name, int clarity) throws IOException {
        superAdapter = MainWindow.superAdapter;
        //TODO:动态生成图片大小
        BufferedImage temp = new BufferedImage(3000 * clarity, 25, BufferedImage.TYPE_INT_ARGB);
        //加载hic文件
        loadHicFile(path);

        //加载染色体注释文件
        setAnnotation1D(annotation_path);

        //选择染色体
        if (!setChromosome(chromosome1_name)) {
            return null;
            //TODO:添加找不到染色体异常
        }
        //TODO:动态生成图片大小
        superAdapter.getHiC().setScaleFactor(clarity);
        superAdapter.getHeatmapPanel().setSize(3000 * clarity, 3000 * clarity);
        superAdapter.getMainWindow().setSize(3100 * clarity, 3100 * clarity);

        superAdapter.getMainViewPanel().getTrackPanelX().paint(temp.getGraphics());
        //画制基因结构图
        Collection<Pair<Rectangle, HiCTrack>> trackRectangles = superAdapter.getMainViewPanel().getTrackPanelX().getTrackRectangles();
        for (Pair<Rectangle, HiCTrack> p : trackRectangles) {
            p.getSecond().setPosColor(new Color(255, 0, 0));
        }
        superAdapter.updateTrackPanel();
        superAdapter.getMainViewPanel().getTrackPanelX().paint(temp.getGraphics());

        return temp;
    }

    /**
     * 功能描述：生成带2D注释的热图
     *
     * @param path              hic文件路径
     * @param annotation_path   注释路径
     * @param chromosome1_name  染色体名字
     * @param displayOption     展示选项
     * @param normalizationType 正则化的选项
     * @param maxColor          颜色最大值
     * @param minColor          颜色最小值
     * @param clarity           清晰度
     * @return {@link BufferedImage }
     * @author new
     * {@code @date} 2024/5/20
     */
    public synchronized BufferedImage generateAnnotation2D(String path, ArrayList<String> annotation_path, String chromosome1_name, String displayOption, String normalizationType, double minColor, double maxColor, int clarity, int resolution) throws IOException {
        BufferedImage temp = new BufferedImage(1502, 1502, BufferedImage.TYPE_INT_ARGB);
        loadHicFile(path);//加载hic文件
        setChromosome(chromosome1_name);//选择染色体
        setNormalizationType(normalizationType);//设置标准化类型
        setDisplayOption(displayOption);//设置显示选项


        //寻找2D注释文件
        File[] annotation_file = new File[3];
        for (int i = 0; i < annotation_path.size(); i++) {
            annotation_file[i] = new File(annotation_path.get(i));
        }
        //加载2D注释文件
        actionPerformed2(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "myCommand"), annotation_file);
        JTree tree = superAdapter.getLayersPanel().getLoad2DAnnotationsDialog().getTree();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();

        //打印当前加载的文件
        DefaultMutableTreeNode tempNode = (DefaultMutableTreeNode) root.getChildAt(1);
        for (int i = 0; i < tempNode.getChildCount(); i++) {
            DefaultMutableTreeNode childAt1 = (DefaultMutableTreeNode) tempNode.getChildAt(i);
            System.out.println(childAt1.toString());
        }
        System.out.println("________________________");
        if (annotation_path.size() > 1) {
            //模拟鼠标确认二维注释
            TreeNode childAt = root.getChildAt(1);
            TreeNode childAt1 = childAt.getChildAt(0);
            TreeNode[] nodes = {root, childAt, childAt1};
            TreePath treePath = new TreePath(nodes);
            tree.setSelectionPath(treePath);
            superAdapter.getLayersPanel().getLoad2DAnnotationsDialog().getOpenButton().doClick();

            TreeNode childAt2 = childAt.getChildAt(1);
            TreeNode[] nodes2 = {root, childAt, childAt2};
            TreePath treePath1 = new TreePath(nodes2);
            tree.setSelectionPath(treePath1);
            superAdapter.getLayersPanel().getLoad2DAnnotationsDialog().getOpenButton().doClick();
        } else if (annotation_path.size() == 1) {
            TreeNode childAt = root.getChildAt(1);
            TreeNode childAt1 = childAt.getChildAt(0);
            TreeNode[] nodes = {root, childAt, childAt1};
            TreePath treePath = new TreePath(nodes);
            tree.setSelectionPath(treePath);
            superAdapter.getLayersPanel().getLoad2DAnnotationsDialog().getOpenButton().doClick();
        } else {
            System.out.println("没有找到2D注释文件");
            throw new IOException("没有找到文件");
        }
        //绘制
//        superAdapter.getHeatmapPanel().paint(temp.getGraphics());

        temp = generateFullHeatMap(0, path, chromosome1_name, displayOption, normalizationType, minColor, maxColor, clarity, resolution);
        //删除已添加的注释文件
        root.remove(1);
        superAdapter.getLayersPanel().getLoad2DAnnotationsDialog().setCustomAddedFeatures(null);
        //恢复环境
        Load2DAnnotationsDialog load2DAnnotationsDialog = superAdapter.getLayersPanel().getLoad2DAnnotationsDialog();
        Map<String, MutableTreeNode> loadedAnnotationsMap = load2DAnnotationsDialog.getLoadedAnnotationsMap();
        for (File file : annotation_file) {
            if (file != null) {
                String absolutePath = file.getAbsolutePath();
                superAdapter.getLayersPanel().getLoad2DAnnotationsDialog().getLoadedAnnotationsMap().remove(loadedAnnotationsMap.get(path));
                loadedAnnotationsMap.remove(absolutePath);
            }
        }
        return temp;
    }

    /**
     * 功能：描述加载基因结构的注释
     *
     * @param path
     * @author new
     * &#064;date  2023/11/28
     */
    public synchronized void setAnnotation1D(String path) {

//        if (path.equals(current_gene_path)) {
//            return;
//        }
        current_gene_path = path;
        //加载基因结构图文件
        superAdapter.getEncodeAction();
        HiC hiC = superAdapter.getHiC();
        if (hiC.getResourceTree() == null) {
            ResourceTree resourceTree = new ResourceTree(superAdapter.getHiC(), null);
            hiC.setResourceTree(resourceTree);
        }
        boolean loadSuccessful = superAdapter.getHiC().getResourceTree().addLocalButtonActionPerformed(superAdapter, new File[]{new File(path)});
        if (loadSuccessful) {
            superAdapter.getLayersPanel().getTrackLoadAction().actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "myCommand"));
        }

    }

    /**
     * 功能描述：获取点的数据
     *
     * @param x 横坐标
     * @param y 纵坐标
     * @return {@link String }
     * @author new
     * @date 2023/11/09
     */
    public synchronized String getPointData(String path, String chromosome1_name, int x, int y) throws IOException {
        loadHicFile(path);
        setChromosome(chromosome1_name);//检测时候是当前的图片，如果不是的话就更新
        superAdapter.getHeatmapPanel().setSize(1502, 1502);
//        superAdapter.getMainWindow().setSize(3000, 3000);
        //FIXME：修复相同坐标查询到数据不同
        if (superAdapter == null) {
            return "";//没有传入文件是无法获取数据的
        }
        return superAdapter.getHeatmapPanel().getMouseHandler().toolTipText((int) (x / 40000 * superAdapter.getHiC().getScaleFactor()), (int) (y / 40000 * superAdapter.getHiC().getScaleFactor()));
    }

    public ArrayList<String> getAnnotationPoint(String path, ArrayList<String> annotation_path, String chromosome1_name, int x, int y) throws IOException {
        BufferedImage temp = new BufferedImage(1502, 1502, BufferedImage.TYPE_INT_ARGB);
        loadHicFile(path);//加载hic文件
        setChromosome(chromosome1_name);//选择染色体


        //寻找2D注释文件
        File[] annotation_file = new File[3];
        for (int i = 0; i < annotation_path.size(); i++) {
            annotation_file[i] = new File(annotation_path.get(i));
        }
        //加载2D注释文件
        actionPerformed2(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, "myCommand"), annotation_file);
        JTree tree = superAdapter.getLayersPanel().getLoad2DAnnotationsDialog().getTree();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();

        //打印当前加载的文件
        DefaultMutableTreeNode tempNode = (DefaultMutableTreeNode) root.getChildAt(1);
        for (int i = 0; i < tempNode.getChildCount(); i++) {
            DefaultMutableTreeNode childAt1 = (DefaultMutableTreeNode) tempNode.getChildAt(i);
            System.out.println(childAt1.toString());
        }
        System.out.println("________________________");
        if (annotation_path.size() > 1) {
            //模拟鼠标确认二维注释
            TreeNode childAt = root.getChildAt(1);
            TreeNode childAt1 = childAt.getChildAt(0);
            TreeNode[] nodes = {root, childAt, childAt1};
            TreePath treePath = new TreePath(nodes);
            tree.setSelectionPath(treePath);
            superAdapter.getLayersPanel().getLoad2DAnnotationsDialog().getOpenButton().doClick();

            TreeNode childAt2 = childAt.getChildAt(1);
            TreeNode[] nodes2 = {root, childAt, childAt2};
            TreePath treePath1 = new TreePath(nodes2);
            tree.setSelectionPath(treePath1);
            superAdapter.getLayersPanel().getLoad2DAnnotationsDialog().getOpenButton().doClick();
        } else if (annotation_path.size() == 1) {
            TreeNode childAt = root.getChildAt(1);
            TreeNode childAt1 = childAt.getChildAt(0);
            TreeNode[] nodes = {root, childAt, childAt1};
            TreePath treePath = new TreePath(nodes);
            tree.setSelectionPath(treePath);
            superAdapter.getLayersPanel().getLoad2DAnnotationsDialog().getOpenButton().doClick();
        } else {
            System.out.println("没有找到2D注释文件");
            throw new IOException("没有找到文件");
        }
        temp = generateFullHeatMap(0, path, chromosome1_name, "", "", 1, 100, 1, 400000);


        Point currMouse = new Point((int) (x), (int) (y));
        double minDistance = Double.POSITIVE_INFINITY;

        ArrayList<String> txt = new ArrayList<>();
        int numLayers = superAdapter.getAllLayers().size();
        int globalPriority = numLayers;
        List<Feature2DGuiContainer> allFeaturePairs = superAdapter.getHeatmapPanel().getMouseHandler().getAllFeaturePairs();
        for (Feature2DGuiContainer loop : allFeaturePairs) {
            Long s1 = loop.getFeature2D().getStart1(), s2 = loop.getFeature2D().getStart2(), e1 = loop.getFeature2D().getEnd1(), e2 = loop.getFeature2D().getEnd2();
            if (currMouse.x >= s1 && currMouse.x <= e1 && currMouse.y >= s2 && currMouse.y <= e2) {
                // TODO - why is this code duplicated in this file?
                String s = loop.getFeature2D().tooltipText();
                boolean flag = true;
                for (String string : txt) {
                    if (string.equals(s)) {
                        flag = false;
                    }
                }
                if (flag) txt.add(s);
                int layerNum = superAdapter.getAllLayers().indexOf(loop.getAnnotationLayerHandler());
                int loopPriority = numLayers - layerNum;
                double distance = currMouse.distance(loop.getRectangle().getX(), loop.getRectangle().getY());
                if (distance < minDistance && loopPriority <= globalPriority) {
                    minDistance = distance;
                    globalPriority = loopPriority;
                }
            }
        }

        root.remove(1);
        superAdapter.getLayersPanel().getLoad2DAnnotationsDialog().setCustomAddedFeatures(null);
        //恢复环境
        Load2DAnnotationsDialog load2DAnnotationsDialog = superAdapter.getLayersPanel().getLoad2DAnnotationsDialog();
        Map<String, MutableTreeNode> loadedAnnotationsMap = load2DAnnotationsDialog.getLoadedAnnotationsMap();
        for (File file : annotation_file) {
            if (file != null) {
                String absolutePath = file.getAbsolutePath();
                superAdapter.getLayersPanel().getLoad2DAnnotationsDialog().getLoadedAnnotationsMap().remove(loadedAnnotationsMap.get(path));
                loadedAnnotationsMap.remove(absolutePath);
            }
        }

        return txt;
    }

    /**
     * 功能描述：设置标准化类型
     *
     * @param type 标准化类型
     * @author new
     * @date 2023/11/09
     */
    private synchronized void setNormalizationType(String type) {
        if (type.isEmpty()) {
            type = "None";
        }
        JComboBox<String> observedNormalizationComboBox = superAdapter.getMainViewPanel().getObservedNormalizationComboBox();
        for (int i = 0; i < observedNormalizationComboBox.getItemCount(); i++) {
            if (observedNormalizationComboBox.getItemAt(i).toString().equals(type)) {
                observedNormalizationComboBox.setSelectedIndex(i);
                return;
            }
        }
        observedNormalizationComboBox.setSelectedItem("None");
    }

    /**
     * 功能描述：设置显示类型的选项
     *
     * @param type 显示类型
     * @author new
     * @date 2023/11/13
     */
    public synchronized void setDisplayOption(String type) {
        if (type.isEmpty()) {
            type = "Observed";
        }
        JComboBox<MatrixType> displayOptionComboBox = superAdapter.getMainViewPanel().getDisplayOptionComboBox();
        for (int i = 0; i < displayOptionComboBox.getItemCount(); i++) {
            if (displayOptionComboBox.getItemAt(i).toString().equals(type)) {
                displayOptionComboBox.setSelectedIndex(i);
                break;
            }
        }
    }

    /**
     * 功能描述：获取所有标准化类型
     *
     * @return {@link ArrayList }<{@link String }>
     * @author new
     * @date 2023/11/09
     */
    public synchronized ArrayList<String> getNormalizationType(String path, String chromosome1_name) throws IOException {
        ArrayList<String> strings = new ArrayList<>();
        loadHicFile(path);
        setChromosome(chromosome1_name);
        JComboBox<String> observedNormalizationComboBox = superAdapter.getMainViewPanel().getObservedNormalizationComboBox();
        for (int i = 0; i < observedNormalizationComboBox.getItemCount(); i++) {
            strings.add(observedNormalizationComboBox.getItemAt(i));
        }
        return strings;
    }

    /**
     * 功能描述：获取显示选项
     *
     * @param path             路径
     * @param chromosome1_name 染色体名字
     * @return {@link ArrayList }<{@link String }>
     * @author new
     * @date 2023/11/13
     */
    public synchronized ArrayList<String> getDisplayOption(String path, String chromosome1_name) throws IOException {
        ArrayList<String> strings = new ArrayList<>();
        loadHicFile(path);
        setChromosome(chromosome1_name);
        JComboBox<MatrixType> displayOptionComboBox = superAdapter.getMainViewPanel().getDisplayOptionComboBox();
        for (int i = 0; i < displayOptionComboBox.getItemCount(); i++) {
            strings.add(displayOptionComboBox.getItemAt(i).toString());
        }
        return strings;
    }

    /**
     * 功能描述：获取基因结构图的点
     *
     * @param path
     * @param gene_path
     * @param chromosome1_name
     * @param x
     * @param y
     * @return {@link String }
     * @author new
     * @date 2023/11/29
     */
    public synchronized String getAnnotation1DData(String path, String gene_path, String chromosome1_name, int x, int y) throws IOException {
        //FIXME:修复只能在画图后才能获取点的数
        BufferedImage temp = new BufferedImage(1502, 25, BufferedImage.TYPE_INT_ARGB);
        //加载hic文件
        loadHicFile(path);

        //加载染色体注释文件
        setAnnotation1D(gene_path);

        //选择染色体
        if (!setChromosome(chromosome1_name)) {
            return null;
            //TODO:添加找不到染色体异常
        }

        superAdapter.getHeatmapPanel().setSize(1502, 1502);
//        superAdapter.getMainWindow().setSize(3000, 3000);
        //画制基因结构图
        //TODO:修复不用画图也能获取到点的数据
        superAdapter.getMainViewPanel().getTrackPanelX().paint(temp.getGraphics());

        Collection<Pair<Rectangle, HiCTrack>> trackRectangles = superAdapter.getMainViewPanel().getTrackPanelX().getTrackRectangles();
        String toolTipText = null;
        for (Pair<Rectangle, HiCTrack> trackRectangle : trackRectangles) {
            HiCTrack second = trackRectangle.getSecond();
            toolTipText = second.getToolTipText((int) (x / 40000 * superAdapter.getHiC().getScaleFactor()), (int) (y / 40000 * superAdapter.getHiC().getScaleFactor()), TrackPanel.Orientation.X);
        }

        return toolTipText;

    }

    public void actionPerformed2(ActionEvent e, File[] annotation_path) {
        superAdapter.getEncodeAction();
        Load2DAnnotationsDialog load2DAnnotationsDialog = superAdapter.getLayersPanel().getLoad2DAnnotationsDialog();

        load2DAnnotationsDialog = new Load2DAnnotationsDialog(superAdapter.getLayersPanel(), superAdapter);
        superAdapter.getLayersPanel().setLoad2DAnnotationsDialog(load2DAnnotationsDialog);

        load2DAnnotationsDialog.addLocalButtonActionPerformed(superAdapter.getLayersPanel(), annotation_path);
    }

    public void actionPerformed(ActionEvent e, String path) {
        superAdapter.getEncodeAction();
        HiC hiC = superAdapter.getHiC();
        if (hiC.getResourceTree() == null) {
            ResourceTree resourceTree = new ResourceTree(superAdapter.getHiC(), null);
            hiC.setResourceTree(resourceTree);
        }
        boolean loadSuccessful = superAdapter.getHiC().getResourceTree().addLocalButtonActionPerformed(superAdapter, new File[]{new File(path)});
        if (loadSuccessful) {
            superAdapter.getLayersPanel().getTrackLoadAction().actionPerformed(e);
        }
    }
}
