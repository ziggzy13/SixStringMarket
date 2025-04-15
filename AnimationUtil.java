package com.sixstringmarket.util;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

/**
 * Utility class for creating animations in the Swing UI
 */
public class AnimationUtil {
    
    /**
     * Creates a smooth animation for any numeric property
     * 
     * @param startValue Starting value
     * @param endValue Ending value
     * @param durationMs Animation duration in milliseconds
     * @param updateCallback Callback that receives the current value during animation
     * @param completeCallback Optional callback when animation completes
     * @return The timer controlling the animation
     */
    public static Timer animate(double startValue, double endValue, int durationMs, 
                                Consumer<Double> updateCallback, Runnable completeCallback) {
        final int fps = 60;
        final int totalFrames = durationMs * fps / 1000;
        final double valueChange = endValue - startValue;
        
        final int[] currentFrame = {0};
        
        Timer timer = new Timer(1000 / fps, e -> {
            currentFrame[0]++;
            
            if (currentFrame[0] >= totalFrames) {
                // End of animation
                updateCallback.accept(endValue);
                ((Timer)e.getSource()).stop();
                
                if (completeCallback != null) {
                    completeCallback.run();
                }
                return;
            }
            
            // Calculate current value using easing function
            double progress = (double) currentFrame[0] / totalFrames;
            double easedProgress = easeInOutQuad(progress);
            double currentValue = startValue + valueChange * easedProgress;
            
            // Update the component
            updateCallback.accept(currentValue);
        });
        
        timer.start();
        return timer;
    }
    
    /**
     * Animate a color change
     * 
     * @param startColor Starting color
     * @param endColor Ending color
     * @param durationMs Animation duration in milliseconds
     * @param updateCallback Callback that receives the current color during animation
     * @param completeCallback Optional callback when animation completes
     * @return The timer controlling the animation
     */
    public static Timer animateColor(Color startColor, Color endColor, int durationMs,
                                     Consumer<Color> updateCallback, Runnable completeCallback) {
        final int fps = 60;
        final int totalFrames = durationMs * fps / 1000;
        
        // Color component changes
        final int redDiff = endColor.getRed() - startColor.getRed();
        final int greenDiff = endColor.getGreen() - startColor.getGreen();
        final int blueDiff = endColor.getBlue() - startColor.getBlue();
        final int alphaDiff = endColor.getAlpha() - startColor.getAlpha();
        
        final int[] currentFrame = {0};
        
        Timer timer = new Timer(1000 / fps, e -> {
            currentFrame[0]++;
            
            if (currentFrame[0] >= totalFrames) {
                // End of animation
                updateCallback.accept(endColor);
                ((Timer)e.getSource()).stop();
                
                if (completeCallback != null) {
                    completeCallback.run();
                }
                return;
            }
            
            // Calculate current color using easing function
            double progress = (double) currentFrame[0] / totalFrames;
            double easedProgress = easeInOutQuad(progress);
            
            Color currentColor = new Color(
                clamp(startColor.getRed() + (int)(redDiff * easedProgress), 0, 255),
                clamp(startColor.getGreen() + (int)(greenDiff * easedProgress), 0, 255),
                clamp(startColor.getBlue() + (int)(blueDiff * easedProgress), 0, 255),
                clamp(startColor.getAlpha() + (int)(alphaDiff * easedProgress), 0, 255)
            );
            
            // Update the component
            updateCallback.accept(currentColor);
        });
        
        timer.start();
        return timer;
    }
    
    /**
     * Animate component size change
     * 
     * @param component The component to resize
     * @param targetWidth Target width
     * @param targetHeight Target height
     * @param durationMs Animation duration in milliseconds
     * @param completeCallback Optional callback when animation completes
     * @return The timer controlling the animation
     */
    public static Timer animateResize(JComponent component, int targetWidth, int targetHeight, 
                                      int durationMs, Runnable completeCallback) {
        int startWidth = component.getWidth();
        int startHeight = component.getHeight();
        
        return animate(0, 1, durationMs, progress -> {
            int currentWidth = startWidth + (int)((targetWidth - startWidth) * progress);
            int currentHeight = startHeight + (int)((targetHeight - startHeight) * progress);
            
            component.setPreferredSize(new Dimension(currentWidth, currentHeight));
            component.revalidate();
            component.repaint();
        }, completeCallback);
    }
    
    /**
     * Animate the width of a component (useful for sidebar collapsing)
     * 
     * @param component The component to resize
     * @param targetWidth Target width
     * @param durationMs Animation duration in milliseconds
     * @param completeCallback Optional callback when animation completes
     * @return The timer controlling the animation
     */
    public static Timer animateWidth(JComponent component, int targetWidth, 
                                    int durationMs, Runnable completeCallback) {
        int startWidth = component.getWidth();
        
        return animate(0, 1, durationMs, progress -> {
            int currentWidth = startWidth + (int)((targetWidth - startWidth) * progress);
            
            component.setPreferredSize(new Dimension(currentWidth, component.getHeight()));
            component.revalidate();
            component.repaint();
        }, completeCallback);
    }
    
    /**
     * Create a shake animation (for error feedback)
     * 
     * @param component The component to shake
     * @param amplitude Maximum shake distance in pixels
     * @param durationMs Animation duration in milliseconds
     */
    public static void shake(JComponent component, int amplitude, int durationMs) {
        int originalX = component.getX();
        final int cycles = 5; // Number of shakes
        
        Timer timer = new Timer(durationMs / (cycles * 2), null);
        final int[] step = {0};
        final int totalSteps = cycles * 2;
        
        timer.addActionListener(e -> {
            step[0]++;
            
            if (step[0] >= totalSteps) {
                component.setLocation(originalX, component.getY());
                timer.stop();
                return;
            }
            
            // Calculate the shake offset using sine wave
            double progress = (double) step[0] / totalSteps;
            double shakeAmount = Math.sin(progress * Math.PI * cycles) * amplitude;
            
            // Apply the shake
            component.setLocation(originalX + (int)shakeAmount, component.getY());
        });
        
        timer.start();
    }
    
    /**
     * Create a fade-in effect for a label or message
     * 
     * @param component The component to fade in
     * @param durationMs Animation duration in milliseconds
     */
    public static void fadeIn(JLabel component, int durationMs) {
        component.setForeground(new Color(
                component.getForeground().getRed(),
                component.getForeground().getGreen(),
                component.getForeground().getBlue(),
                0
        ));
        
        animateColor(component.getForeground(), 
                new Color(
                        component.getForeground().getRed(),
                        component.getForeground().getGreen(),
                        component.getForeground().getBlue(),
                        255
                ), 
                durationMs, 
                component::setForeground,
                null);
    }
    
    /**
     * Quadratic ease-in-out function
     */
    private static double easeInOutQuad(double t) {
        return t < 0.5 ? 2 * t * t : 1 - Math.pow(-2 * t + 2, 2) / 2;
    }
    
    /**
     * Clamp a value between min and max
     */
    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}