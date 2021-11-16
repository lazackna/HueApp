package com.lazackna.hueapp.Util;

import android.graphics.Color;

import androidx.annotation.IntRange;

import java.util.ArrayList;
import java.util.List;

public class ColorHelper {

    private static final int CPT_RED = 0;
    private static final int CPT_GREEN = 1;
    private static final int CPT_BLUE = 2;

    public static int xyToColor(double x, double y, int bri) {
        int[] rgb = xyToRGB(x,y,bri);
        float[] hsv = new float[3];
        Color.RGBToHSV(rgb[0],rgb[1],rgb[2],hsv);
        int color = Color.HSVToColor(hsv);
        return color;
    }

    public static int[] xyToRGB(double x, double y, @IntRange(from = 1, to = 254) int bri) {
        double z = 1.0 - x - y;
        double Y = bri / 255.0; // Brightness of lamp
        double X = (Y / y) * x;
        double Z = (Y / y) * z;
        double r = X * 1.612 - Y * 0.203 - Z * 0.302;
        double g = -X * 0.509 + Y * 1.412 + Z * 0.066;
        double b = X * 0.026 - Y * 0.072 + Z * 0.962;
        r = r <= 0.0031308 ? 12.92 * r : (1.0 + 0.055) * Math.pow(r, (1.0 / 2.4)) - 0.055;
        g = g <= 0.0031308 ? 12.92 * g : (1.0 + 0.055) * Math.pow(g, (1.0 / 2.4)) - 0.055;
        b = b <= 0.0031308 ? 12.92 * b : (1.0 + 0.055) * Math.pow(b, (1.0 / 2.4)) - 0.055;
        double maxValue = Double.max(r,g);
        maxValue = Double.max(maxValue, b);
        r /= maxValue;
        g /= maxValue;
        b /= maxValue;
        r = r * 255;   if (r < 0) { r = 255;}
        g = g * 255;   if (g < 0) { g = 25;}
        b = b * 255;   if (b < 0) { b = 255;}
        @IntRange(from = 0, to = 255) int red = (int) r;
        @IntRange(from = 0, to = 255) int green = (int) g;
        @IntRange(from = 0, to = 255) int blue = (int) b;
        return new int[] {red, green ,blue};
    }
    public static int colorFromXY(float[] points, String model) {
        PointF xy = new PointF(points[0], points[1]);

        xy = fixIfOutOfRange(xy, model);

        float x = xy.x;
        float y = xy.y;
        float z = 1.0f - x - y;
        float y2 = 1.0f;
        float x2 = (y2 / y) * x;
        float z2 = (y2 / y) * z;

        // sRGB D65 conversion
        float r = x2 * 3.2406f - y2 * 1.5372f - z2 * 0.4986f;
        float g = -x2 * 0.9689f + y2 * 1.8758f + z2 * 0.0415f;
        float b = x2 * 0.0557f - y2 * 0.2040f + z2 * 1.0570f;

            if (r > b && r > g && r > 1.0f) {
            // red is too big
            g = g / r;
            b = b / r;
            r = 1.0f;
        } else if (g > b && g > r && g > 1.0f) {
            // green is too big
            r = r / g;
            b = b / g;
            g = 1.0f;
        } else if (b > r && b > g && b > 1.0f) {
            // blue is too big
            r = r / b;
            g = g / b;
            b = 1.0f;
        }
        // Apply gamma correction
        r = r <= 0.0031308f ? 12.92f * r : (1.0f + 0.055f)
                * (float) Math.pow(r, (1.0f / 2.4f)) - 0.055f;
        g = g <= 0.0031308f ? 12.92f * g : (1.0f + 0.055f)
                * (float) Math.pow(g, (1.0f / 2.4f)) - 0.055f;
        b = b <= 0.0031308f ? 12.92f * b : (1.0f + 0.055f)
                * (float) Math.pow(b, (1.0f / 2.4f)) - 0.055f;

            if (r > b && r > g) {
            // red is biggest
            if (r > 1.0f) {
                g = g / r;
                b = b / r;
                r = 1.0f;
            }
        } else if (g > b && g > r) {
            // green is biggest
            if (g > 1.0f) {
                r = r / g;
                b = b / g;
                g = 1.0f;
            }
        } else if (b > r && b > g && b > 1.0f) {
            r = r / b;
            g = g / b;
            b = 1.0f;
        }

        // neglecting if the value is negative.
            if (r < 0.0f) {
            r = 0.0f;
        }
            if (g < 0.0f) {
            g = 0.0f;
        }
            if (b < 0.0f) {
            b = 0.0f;
        }

        // Converting float components to int components.
        int r1 = (int) (r * 255.0f);
        int g1 = (int) (g * 255.0f);
        int b1 = (int) (b * 255.0f);
        return rgb(r1, g1, b1);
    }

    public static int rgb(int red, int green, int blue) {
        return (0xFF << 24) | (red << 16) | (green << 8) | blue;
    }

    public static PointF fixIfOutOfRange(PointF xy, String model) {
        List<PointF> colorPoints = colorPointsForModel(model);
        boolean inReachOfLamps = checkPointInLampsReach(xy, colorPoints);
        if (!inReachOfLamps) {
            // It seems the colour is out of reach
            // let's find the closest colour we can produce with our lamp and
            // send this XY value out.
            // Find the closest point on each line in the triangle.
            PointF pAB = getClosestPointToPoints(colorPoints.get(CPT_RED),
                    colorPoints.get(CPT_GREEN), xy);
            PointF pAC = getClosestPointToPoints(colorPoints.get(CPT_BLUE),
                    colorPoints.get(CPT_RED), xy);

            PointF pBC = getClosestPointToPoints(colorPoints.get(CPT_GREEN),
                    colorPoints.get(CPT_BLUE), xy);

            // Get the distances per point and see which point is closer to our point.

            float dAB = getDistanceBetweenTwoPoints(xy, pAB);
            float dAC = getDistanceBetweenTwoPoints(xy, pAC);
            float dBC = getDistanceBetweenTwoPoints(xy, pBC);
            float lowest = dAB;
            PointF closestPoint = pAB;
            if (dAC < lowest) {
                lowest = dAC;
                closestPoint = pAC;
            }
            if (dBC < lowest) {
                lowest = dBC;
                closestPoint = pBC;
            }
            // Change the xy value to a value which is within the reach of the lamp.
            xy.x = closestPoint.x;
            xy.y = closestPoint.y;
        }
        return new PointF(xy.x, xy.y);
    }

    private static PointF getClosestPointToPoints(PointF pointA, PointF pointB,
                                                  PointF pointP) {
        if (pointA == null || pointB == null || pointP == null) {
            return null;
        }
        PointF pointAP = new PointF(pointP.x - pointA.x, pointP.y - pointA.y);
        PointF pointAB = new PointF(pointB.x - pointA.x, pointB.y - pointA.y);
        float ab2 = pointAB.x * pointAB.x + pointAB.y * pointAB.y;
        float apAb = pointAP.x * pointAB.x + pointAP.y * pointAB.y;
        float t = apAb / ab2;
        if (t < 0.0f) {
            t = 0.0f;
        }
        else if (t > 1.0f) {
            t = 1.0f;
        }
        PointF newPoint = new PointF(pointA.x + pointAB.x * t, pointA.y
                + pointAB.y * t);
        return newPoint;
    }

    private static float getDistanceBetweenTwoPoints(PointF one, PointF two) {
        float dx = one.x - two.x; // horizontal difference
        float dy = one.y - two.y; // vertical difference
        float dist = (float) Math.sqrt(dx * dx + dy * dy);

        return dist;
    }

    private static List<PointF> colorPointsForModel(String model) {
        // LLC001, // LedStrip // LWB001, // LivingWhite
        if (model == null) { // if model is not known go for the default choice
            model = " ";
        }
        ArrayList<PointF> colorPoints = new ArrayList<PointF>();

        ArrayList<String> hueBulbs = new ArrayList<String>();
        hueBulbs.add("LCT001"); /* Hue A19 */
        hueBulbs.add("LCT002"); /* Hue BR30 */
        hueBulbs.add("LCT003"); /* Hue GU10 */

        ArrayList<String> livingColors = new ArrayList<String>();
        livingColors.add("LLC001"); /* Monet, Renoir, Mondriaan (gen II) */
        livingColors.add("LLC005"); /* Bloom (gen II) */
        livingColors.add("LLC006"); /* Iris (gen III) */
        livingColors.add("LLC007"); /* Bloom, Aura (gen III) */
        livingColors.add("LLC011"); /* Hue Bloom */
        livingColors.add("LLC013"); /* Disney Story Teller */
        livingColors.add("LST001"); /* Light Strips */

        if (hueBulbs.contains(model)) {
            // Hue bulbs color gamut triangle
            colorPoints.add(new PointF(.674F, 0.322F)); // Red
            colorPoints.add(new PointF(0.408F, 0.517F)); // Green
            colorPoints.add(new PointF(0.168F, 0.041F)); // Blue
        } else if (livingColors.contains(model)) {
            // LivingColors color gamut triangle
            colorPoints.add(new PointF(0.703F, 0.296F)); // Red
            colorPoints.add(new PointF(0.214F, 0.709F)); // Green
            colorPoints.add(new PointF(0.139F, 0.081F)); // Blue
        } else {
            // Default construct triangle which contains all values
            colorPoints.add(new PointF(1.0F, 0.0F));// Red
            colorPoints.add(new PointF(0.0F, 1.0F)); // Green
            colorPoints.add(new PointF(0.0F, 0.0F));// Blue
        }
        return colorPoints;
    }

    private static boolean checkPointInLampsReach(PointF point, List<PointF> colorPoints) {
        if (point == null || colorPoints == null) {
            return false;
        }
        PointF red = colorPoints.get(CPT_RED);
        PointF green = colorPoints.get(CPT_GREEN);
        PointF blue = colorPoints.get(CPT_BLUE);
        PointF v1 = new PointF(green.x - red.x, green.y - red.y);
        PointF v2 = new PointF(blue.x - red.x, blue.y - red.y);
        PointF q = new PointF(point.x - red.x, point.y - red.y);
        float s = crossProduct(q, v2) / crossProduct(v1, v2);
        float t = crossProduct(v1, q) / crossProduct(v1, v2);
        if ((s >= 0.0f) && (t >= 0.0f) && (s + t <= 1.0f)) {
            return true;
        }

        return false;

    }

    private static float crossProduct(PointF point1, PointF point2) {

        return (point1.x * point2.y - point1.y * point2.x);
    }
}
