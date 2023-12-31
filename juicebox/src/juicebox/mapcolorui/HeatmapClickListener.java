/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2011-2021 Broad Institute, Aiden Lab, Rice University, Baylor College of Medicine
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

package juicebox.mapcolorui;

import juicebox.HiC;
import juicebox.HiCGlobals;
import juicebox.MainWindow;
import juicebox.assembly.AssemblyOperationExecutor;
import juicebox.data.basics.Chromosome;
import juicebox.gui.SuperAdapter;
import juicebox.track.feature.Feature2D;
import juicebox.track.feature.Feature2DGuiContainer;
import juicebox.windowui.HiCZoom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Created by muhammadsaadshamim on 8/9/17.
 */
public class HeatmapClickListener extends MouseAdapter implements ActionListener {
    private static final int clickDelay = 400;
    private final HeatmapPanel heatmapPanel;
    private final Timer clickTimer;
    private MouseEvent lastMouseEvent;
    private Feature2DGuiContainer currentUpstreamFeature = null;
    private Feature2DGuiContainer currentDownstreamFeature = null;

    HeatmapClickListener(HeatmapPanel heatmapPanel) {
        clickTimer = new Timer(clickDelay, this);
        this.heatmapPanel = heatmapPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        clickTimer.stop();
        singleClick(lastMouseEvent);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (heatmapPanel.getHiC() == null) return;
        safeMouseClicked(e);
    }

    private void unsafeMouseClickSubActionA(final MouseEvent eF) {
        HiC hic = heatmapPanel.getHiC();
        long[] chromosomeBoundaries = heatmapPanel.getChromosomeBoundaries();

        double binX = hic.getXContext().getBinOrigin() + (eF.getX() / hic.getScaleFactor());
        double binY = hic.getYContext().getBinOrigin() + (eF.getY() / hic.getScaleFactor());

        Chromosome xChrom = null;
        Chromosome yChrom = null;

        try {
            long xGenome = hic.getZd().getXGridAxis().getGenomicMid(binX);
            long yGenome = hic.getZd().getYGridAxis().getGenomicMid(binY);
            for (int i = 0; i < chromosomeBoundaries.length; i++) {
                if (xChrom == null && chromosomeBoundaries[i] > xGenome) {
                    xChrom = hic.getChromosomeHandler().getChromosomeFromIndex(i + 1);
                }
                if (yChrom == null && chromosomeBoundaries[i] > yGenome) {
                    yChrom = hic.getChromosomeHandler().getChromosomeFromIndex(i + 1);
                }
            }
        } catch (Exception ex) {
            // do nothing, leave chromosomes null
        }
        if (xChrom != null && yChrom != null) {
            heatmapPanel.unsafeSetSelectedChromosomes(xChrom, yChrom);
        } else {
            System.err.println("null chromosome " + xChrom + " - " + yChrom);
        }

        //Only if zoom is changed All->Chr:
        heatmapPanel.updateThumbnail();
    }

    private void unsafeMouseClickSubActionB(double centerBinX, double centerBinY, HiCZoom newZoom) {
        HiC hic = heatmapPanel.getHiC();

        try {
            final String chrXName = hic.getXContext().getChromosome().toString();
            final String chrYName = hic.getYContext().getChromosome().toString();

            final long xGenome = hic.getZd().getXGridAxis().getGenomicMid(centerBinX);
            final long yGenome = hic.getZd().getYGridAxis().getGenomicMid(centerBinY);

            hic.unsafeActuallySetZoomAndLocation(chrXName, chrYName, newZoom, xGenome, yGenome, -1, false,
                    HiC.ZoomCallType.STANDARD, true, hic.isResolutionLocked() ? 1 : 0, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void safeMouseClicked(final MouseEvent eF) {
        HiC hic = heatmapPanel.getHiC();

        if (!eF.isPopupTrigger() && eF.getButton() == MouseEvent.BUTTON1 && !eF.isControlDown()) {

            try {
                hic.getZd();
            } catch (Exception e) {
                return;
            }

            if (eF.getClickCount() > 0) {
                lastMouseEvent = eF;
                if (clickTimer.isRunning() || eF.getClickCount() > 1) {
                    clickTimer.stop();
                    doubleClick(lastMouseEvent);
                } else {
                    clickTimer.restart();
                    heatmapPanel.setPromptedAssemblyActionOnClick(heatmapPanel.getCurrentPromptedAssemblyAction());
                    currentUpstreamFeature = heatmapPanel.getCurrentUpstreamFeature();
                    currentDownstreamFeature = heatmapPanel.getCurrentDownstreamFeature();
                }
            }
        }
    }

    private void singleClick(final MouseEvent lastMouseEvent) {
        HiC hic = heatmapPanel.getHiC();
        MainWindow mainWindow = heatmapPanel.getMainWindow();
        SuperAdapter superAdapter = heatmapPanel.getSuperAdapter();

        if (hic.isWholeGenome()) {
            Runnable runnable = new Runnable() {
                public void run() {
                    unsafeMouseClickSubActionA(lastMouseEvent);
                }
            };
            mainWindow.executeLongRunningTask(runnable, "Mouse Click Set Chr");
        } else {
            if (!lastMouseEvent.isShiftDown()) {
                List<Feature2D> selectedFeatures = heatmapPanel.getSelectedFeatures();

                HeatmapMouseHandler.PromptedAssemblyAction action = heatmapPanel.getPromptedAssemblyActionOnClick();
                if (action == HeatmapMouseHandler.PromptedAssemblyAction.REGROUP) {
                    AssemblyOperationExecutor.toggleGroup(superAdapter, currentUpstreamFeature.getFeature2D(), currentDownstreamFeature.getFeature2D());
                    heatmapPanel.repaint();
                    try {
                        Robot bot = new Robot();
                        bot.mouseMove(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y);
                    } catch (AWTException e) {
                        e.printStackTrace();
                    }
                } else if (action == HeatmapMouseHandler.PromptedAssemblyAction.PASTEBOTTOM) {
                    heatmapPanel.moveSelectionToEnd(); // TODO fix this so that highlight moves with translated selection
                    heatmapPanel.repaint(); // moveSelectionToEnd already handles removeSelection
                } else if (action == HeatmapMouseHandler.PromptedAssemblyAction.PASTETOP) {
                    AssemblyOperationExecutor.moveSelection(superAdapter,
                            selectedFeatures,
                            null);
                    removeAndRepaint();  // TODO fix this so that highlight moves with translated selection
                } else if (action == HeatmapMouseHandler.PromptedAssemblyAction.PASTE) {
                    AssemblyOperationExecutor.moveSelection(superAdapter, selectedFeatures, currentUpstreamFeature.getFeature2D());
                    removeAndRepaint();  // TODO fix this so that highlight moves with translated selection
                } else if (action == HeatmapMouseHandler.PromptedAssemblyAction.INVERT) {
                    AssemblyOperationExecutor.invertSelection(superAdapter, selectedFeatures);
                    removeAndRepaint(); // TODO fix this so that highlight moves with translated selection
                }
            }
        }

        if (HiCGlobals.printVerboseComments) {
            try {
                superAdapter.getAssemblyStateTracker().getAssemblyHandler().toString();
            } catch (Exception e) {
                System.err.println("Unable to print assembly state");
            }
        }
    }

    private void removeAndRepaint() {
        heatmapPanel.removeSelection();  // TODO fix this so that highlight moves with translated selection
        heatmapPanel.repaint();
    }

    private void doubleClick(MouseEvent lastMouseEvent) {
        HiC hic = heatmapPanel.getHiC();
        MainWindow mainWindow = heatmapPanel.getMainWindow();


        // Double click, zoom and center on click location
        try {
            // in all-by-all mimic single click
            if (hic.isWholeGenome()) {
                singleClick(lastMouseEvent);
                return;
            }

            final HiCZoom currentZoom = hic.getZd().getZoom();
            final HiCZoom nextPotentialZoom = hic.getDataset().getNextZoom(currentZoom, !lastMouseEvent.isAltDown());
            final HiCZoom newZoom = hic.isResolutionLocked() ||
                    hic.isPearsonEdgeCaseEncountered(nextPotentialZoom) ? currentZoom : nextPotentialZoom;

            // If newZoom == currentZoom adjust scale factor (no change in resolution)
            final double centerBinX = hic.getXContext().getBinOrigin() + (lastMouseEvent.getX() / hic.getScaleFactor());
            final double centerBinY = hic.getYContext().getBinOrigin() + (lastMouseEvent.getY() / hic.getScaleFactor());

            // perform superzoom / normal zoom / reverse-superzoom
            if (newZoom.equals(currentZoom)) {
                double mult = lastMouseEvent.isAltDown() ? 0.5 : 2.0;
                // if newScaleFactor > 1.0, performs superzoom
                // if newScaleFactor = 1.0, performs normal zoom
                // if newScaleFactor < 1.0, performs reverse superzoom
                double newScaleFactor = Math.max(0.0, hic.getScaleFactor() * mult);

                String chrXName = hic.getXContext().getChromosome().getName();
                String chrYName = hic.getYContext().getChromosome().getName();

                int genomeX = Math.max(0, (int) (centerBinX) * newZoom.getBinSize());
                int genomeY = Math.max(0, (int) (centerBinY) * newZoom.getBinSize());

                hic.unsafeActuallySetZoomAndLocation(chrXName, chrYName, newZoom, genomeX, genomeY,
                        newScaleFactor, true, HiC.ZoomCallType.STANDARD, true, hic.isResolutionLocked() ? 1 : 0, true);

            } else {
                Runnable runnable = new Runnable() {
                    public void run() {
                        unsafeMouseClickSubActionB(centerBinX, centerBinY, newZoom);
                    }
                };
                mainWindow.executeLongRunningTask(runnable, "Mouse Click Zoom");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
