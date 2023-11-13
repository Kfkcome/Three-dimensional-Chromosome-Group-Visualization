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

import juicebox.MainWindow;
import juicebox.data.ChromosomeHandler;
import juicebox.data.basics.Chromosome;
import juicebox.gui.SuperAdapter;
import juicebox.windowui.MatrixType;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GenerateHeatmap {
    SuperAdapter superAdapter;
    String current_path;
    String current_chromosome;

    public GenerateHeatmap() {
        System.setProperty("java.awt.headless", "false");
        MainWindow.initApplication();//初始化程序
        MainWindow.getInstance();
        superAdapter = MainWindow.superAdapter;
    }

    /**
     * 功能描述：
     *
     * @param path             文件路径
     * @param chromosome1_name 染色体名字
     * @return boolean
     * @author new
     * @date 2023/11/13
     */
    private boolean setSpeciesCultivarChromosome(String path, String chromosome1_name) throws IOException {
        if (path.equals(current_path) && chromosome1_name.equals(current_chromosome)) {
            return false;
        }
        current_path = path;
        current_chromosome = chromosome1_name;
        List<String> fileNames = getFileNames(path);
        try {
            superAdapter.unsafeLoad(fileNames, false, false);
        } catch (IOException e) {
            throw new IOException(e);
        }
        ChromosomeHandler chromosomeHandler = superAdapter.getHiC().getChromosomeHandler();
        Chromosome chr1 = chromosomeHandler.getChromosomeFromName(chromosome1_name);
        superAdapter.unsafeSetSelectedChromosomes(chr1, chr1);
        return true;
    }

    public BufferedImage generateFullHeatMap(String path, String chromosome1_name, String displayOption, String normalizationType) throws IOException {
        superAdapter = MainWindow.superAdapter;
        BufferedImage temp = new BufferedImage(1057, 1057, BufferedImage.TYPE_INT_ARGB);
        setSpeciesCultivarChromosome(path, chromosome1_name);
        setNormalizationType(normalizationType);
        setDisplayOption(displayOption);
        superAdapter.getHeatmapPanel().setBounds(0, 0, 1057, 1057);
        superAdapter.getHeatmapPanel().paint(temp.getGraphics());
        String selectedItem = (String) superAdapter.getMainViewPanel().getObservedNormalizationComboBox().getSelectedItem();
        String selectedItem1 = (String) superAdapter.getMainViewPanel().getDisplayOptionComboBox().getSelectedItem().toString();
        System.out.println("当前选择的标准化类型为：" + selectedItem + "\n" +
                "当前选择的显示类型是：" + selectedItem1);
        return temp;
    }

//    public static void main(String[] args) {
//        BufferedImage image = new GenerateHeatmap().generateFullHeatMap("/home/new/fsdownload/Glycine-max_SoyC02_Leaf/Glycine-max_SoyC02_Leaf.hic", "SoyC02.Chr01");

//        long startTime = System.currentTimeMillis();
//        MainWindow.initApplication();//初始化程序
//        MainWindow.getInstance();
//        GenerateHeatmap generateHeatmap = new GenerateHeatmap();
//        generateHeatmap.superAdapter = MainWindow.superAdapter;
//        BufferedImage temp = new BufferedImage(1057, 578, BufferedImage.TYPE_INT_ARGB);
//        List<String> fileNames = getFileNames("/home/new/fsdownload/Glycine-max_SoyC02_Leaf/Glycine-max_SoyC02_Leaf.hic");
//        try {
//            generateHeatmap.superAdapter.unsafeLoad(fileNames, false, false);//加载hic文件
//            for (int i = 1; i < 10; i++) {
//                SuperAdapter superAdapter1 = generateHeatmap.superAdapter;
//                ChromosomeHandler chromosomeHandler = superAdapter1.getHiC().getChromosomeHandler();
//                Chromosome chr1 = chromosomeHandler.getChromosomeFromIndex(i);
//                Chromosome chr2 = chr1;
//                superAdapter1.unsafeSetSelectedChromosomes(chr1, chr2);
//                generateHeatmap.superAdapter.getHeatmapPanel().setBounds(0, 0, 1057, 578);//设置窗口大小，设置图片的大小
//                HeatmapClickListener clickListener = generateHeatmap.superAdapter.getMainViewPanel().getHeatmapPanel().getClickListener();
//                superAdapter1.getMainViewPanel().getResolutionSlider().setResolutionLocked(true);//锁定分辨率
//                //模拟鼠标双击
//                clickListener.mouseClicked(new MouseEvent(superAdapter1.getHeatmapPanel(), 1, (long) 1, 1, 1, 1, 10, false, 1));
//                generateHeatmap.superAdapter.getHeatmapPanel().paint(temp.getGraphics());
//                File outputFile = new File("/home/new/test" + i + ".png");
//                ImageIO.write(temp, "png", outputFile);
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
////        MainWindow.getInstance().dispose();
//        long endTime = System.currentTimeMillis(); //获取结束时间
//        System.out.println("程序运行时间： " + (endTime - startTime) + "ms");
//        System.exit(0);
//    }

    @NotNull
    private static List<String> getFileNames(String path) {
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
     * 功能描述：获取点的数据
     *
     * @param x 横坐标
     * @param y 纵坐标
     * @return {@link String }
     * @author new
     * @date 2023/11/09
     */
    public String getPointData(String path, String chromosome1_name, int x, int y) throws IOException {
        setSpeciesCultivarChromosome(path, chromosome1_name);//检测时候是当前的图片，如果不是的话就更新
        if (superAdapter == null) {
            return "";//没有传入文件是无法获取数据的
        }
        return superAdapter.getHeatmapPanel().getMouseHandler().toolTipText(x, y);
    }

    /**
     * 功能描述：设置标准化类型
     *
     * @param type 标准化类型
     * @author new
     * @date 2023/11/09
     */
    private void setNormalizationType(String type) {
        JComboBox<String> observedNormalizationComboBox = superAdapter.getMainViewPanel().getObservedNormalizationComboBox();
        observedNormalizationComboBox.setSelectedItem(type);
    }

    /**
     * 功能描述：获取显示类型的选项
     *
     * @param type 显示类型
     * @author new
     * @date 2023/11/13
     */
    public void setDisplayOption(String type) {
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
    public ArrayList<String> getNormalizationType(String path, String chromosome1_name) throws IOException {
        ArrayList<String> strings = new ArrayList<>();
        setSpeciesCultivarChromosome(path, chromosome1_name);
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
    public ArrayList<String> getDisplayOption(String path, String chromosome1_name) throws IOException {
        ArrayList<String> strings = new ArrayList<>();
        setSpeciesCultivarChromosome(path, chromosome1_name);
        JComboBox<MatrixType> displayOptionComboBox = superAdapter.getMainViewPanel().getDisplayOptionComboBox();
        for (int i = 0; i < displayOptionComboBox.getItemCount(); i++) {
            strings.add(displayOptionComboBox.getItemAt(i).toString());
        }
        return strings;
    }

}
