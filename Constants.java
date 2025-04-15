package com.sixstringmarket.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * Class with constants and styles for the application design
 */
public class Constants {

	// Application title
	public static final String APP_TITLE = "SixStringMarket - Guitar Marketplace";

	// Application version
	public static final String APP_VERSION = "1.0.0";

	// Initialize StyleManager when Constants class is loaded
	static {
		StyleManager.initialize();
	}

	// Color scheme - modern and elegant
	public static final Color PRIMARY_COLOR = StyleManager.PRIMARY_COLOR;
	public static final Color SECONDARY_COLOR = StyleManager.SECONDARY_COLOR;
	public static final Color ACCENT_COLOR = StyleManager.SECONDARY_COLOR;
	public static final Color BACKGROUND_COLOR = StyleManager.BACKGROUND_COLOR;
	public static final Color PANEL_COLOR = StyleManager.CARD_BG_COLOR;
	public static final Color TEXT_COLOR = StyleManager.TEXT_COLOR;
	public static final Color TEXT_SECONDARY_COLOR = StyleManager.TEXT_SECONDARY_COLOR;

	// Functional colors
	public static final Color SUCCESS_COLOR = StyleManager.SUCCESS_COLOR;
	public static final Color WARNING_COLOR = StyleManager.WARNING_COLOR;
	public static final Color ERROR_COLOR = StyleManager.ERROR_COLOR;
	public static final Color INFO_COLOR = StyleManager.INFO_COLOR;

	// Padding and spacing
	public static final int PADDING_SMALL = StyleManager.PADDING_SMALL;
	public static final int PADDING_MEDIUM = StyleManager.PADDING_MEDIUM;
	public static final int PADDING_LARGE = StyleManager.PADDING_LARGE;
	public static final int ROUNDED_CORNER_RADIUS = StyleManager.BORDER_RADIUS;

	// Fonts
	public static Font DEFAULT_FONT = StyleManager.DEFAULT_FONT;
	public static Font BOLD_FONT = new Font(StyleManager.DEFAULT_FONT.getName(), Font.BOLD,
			StyleManager.DEFAULT_FONT.getSize());
	public static Font TITLE_FONT = StyleManager.TITLE_FONT;
	public static final Font SUBTITLE_FONT = StyleManager.SUBTITLE_FONT;
	public static final Font SMALL_FONT = StyleManager.SMALL_FONT;

	// Borders
	public static final Border PANEL_BORDER = new LineBorder(StyleManager.darken(StyleManager.CARD_BG_COLOR, 0.1f), 1,
			true);

	public static final Border CARD_BORDER = new CompoundBorder(
			new LineBorder(StyleManager.darken(StyleManager.CARD_BG_COLOR, 0.1f), 1, true),
			new EmptyBorder(PADDING_MEDIUM, PADDING_MEDIUM, PADDING_MEDIUM, PADDING_MEDIUM));

	public static final Border TEXT_FIELD_BORDER = new CompoundBorder(
			new LineBorder(StyleManager.FIELD_BORDER_COLOR, 1, true), new EmptyBorder(5, 8, 5, 8));

	public static final Border BUTTON_BORDER = new LineBorder(StyleManager.FIELD_BORDER_COLOR, 1, true);

	// Standard dimensions
	public static final Dimension BUTTON_DIMENSION = new Dimension(120, 36);
	public static final Dimension SMALL_BUTTON_DIMENSION = new Dimension(90, 32);
	public static final Dimension TEXT_FIELD_DIMENSION = new Dimension(200, 36);

	// Icon paths
	public static final String ICON_PATH = "resources/icons/";
	public static final String LOGO_PATH = ICON_PATH + "logo.png";
	public static final String USER_ICON = ICON_PATH + "user.png";
	public static final String GUITAR_ICON = ICON_PATH + "guitar.png";
	public static final String CART_ICON = ICON_PATH + "cart.png";
	public static final String SAVE_ICON = ICON_PATH + "save.png";
	public static final String SEARCH_ICON = ICON_PATH + "search.png";

	// Guitar brands for dropdown
	public static final String[] GUITAR_BRANDS = { "Fender", "Gibson", "Ibanez", "Jackson", "ESP", "PRS", "Yamaha",
			"Epiphone", "Schecter", "Dean", "Gretsch", "Martin", "Taylor", "Washburn", "Cort", "B.C. Rich", "Squier",
			"Charvel", "Takamine" };

	// Resource paths
	public static final String RESOURCES_PATH = "resources/";
	public static final String IMAGES_PATH = RESOURCES_PATH + "images/";
	public static final String CONFIG_PATH = RESOURCES_PATH + "config/";
	public static final String CSS_PATH = RESOURCES_PATH + "css/";
}