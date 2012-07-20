package monsterbreeder;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * <p>A color mixer filter can be used to mix a solid color to an image. The
 * result is an image tinted by the specified color. The force of the effect
 * can be controlled with the <code>mixValue</code>, a number between  0.0 and
 * 1.0 that can be seen as the percentage of the mix (0.0 does not affect the
 * source image and 1.0 replaces all the pixels by the solid color).</p>
 * <p>The color of the pixels in the resulting image is computed as follows:</p>
 * <pre>
 * cR = cS * (1 - mixValue) + cM * mixValue
 * </pre>
 * <p>Definition of the parameters:</p>
 * <ul>
 *   <li><code>cR</code>: color of the resulting pixel</li>
 *   <li><code>cS</code>: color of the source pixel</li>
 *   <li><code>cM</code>: the solid color to mix with the source image</li>
 *   <li><code>mixValue</code>: strength of the mix, a value between 0.0 and 1.0</li>
 * </ul>
 *
 * @author Romain Guy <romain.guy@mac.com>
 */

public class ColorTintFilter extends AbstractFilter {
    private final Color mixColor;
    private final float mixValue;

    /**
     * <p>Creates a new color mixer filter. The specified color will be used
     * to tint the source image, with a mixing strength defined by
     * <code>mixValue</code>.</p>
     *
     * @param mixColor the solid color to mix with the source image
     * @param mixValue the strength of the mix, between 0.0 and 1.0; if the
     *   specified value lies outside this range, it is clamped
     * @throws IllegalArgumentException if <code>mixColor</code> is null
     */
    public ColorTintFilter(Color mixColor, float mixValue) {
        if (mixColor == null) {
            throw new IllegalArgumentException("mixColor cannot be null");
        }

        this.mixColor = mixColor;
        if (mixValue < 0.0f) {
            mixValue = 0.0f;
        } else if (mixValue > 1.0f) {
            mixValue = 1.0f;
        }
        this.mixValue = mixValue;
    }

    /**
     * <p>Returns the mix value of this filter.</p>
     *
     * @return the mix value, between 0.0 and 1.0
     */
    public float getMixValue() {
        return mixValue;
    }

    /**
     * <p>Returns the solid mix color of this filter.</p> 
     *
     * @return the solid color used for mixing
     */
    public Color getMixColor() {
        return mixColor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        if (dst == null) {
            dst = createCompatibleDestImage(src, null);
        }

        int width = src.getWidth();
        int height = src.getHeight();

        int[] pixels = new int[width * height];
        src.getRGB( 0, 0, width, height, pixels, 0, width );
        mixColor(pixels);
        dst.setRGB( 0, 0, width, height, pixels, 0, width );
        
        return dst;
    }

    private void mixColor(int[] pixels) {
        int mix_a = mixColor.getAlpha();
        int mix_r = mixColor.getRed();
        int mix_g = mixColor.getGreen();
        int mix_b = mixColor.getBlue();

        for (int i = 0; i < pixels.length; i++) {
            int argb = pixels[i];

            int a = (argb >> 24) & 0xFF;
            int r = (argb >> 16) & 0xFF;
            int g = (argb >>  8) & 0xFF;
            int b = (argb      ) & 0xFF;
            
           // a = (int) (a * (1.0f - mixValue) + mix_a * mixValue);
            r = (int) (r * (1.0f - mixValue) + mix_r * mixValue);
            g = (int) (g * (1.0f - mixValue) + mix_g * mixValue);
            b = (int) (b * (1.0f - mixValue) + mix_b * mixValue);

            pixels[i] = a << 24 | r << 16 | g << 8 | b;
        }
    }
}