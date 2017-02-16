package minesweeper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("mytheme")
public class LuckyMineSweeperUI extends UI {

	private static final long serialVersionUID = -1514566519435404590L;
	private static final String SAD_SMILEY = ":-(";
	private static final String HAPPY_SMILEY = ":-)";
	private static final int WIDTH_PIXELS = 40;
	private static final int GRID_CELLS = 10;

	private static final List<Integer> CHARS = Arrays.asList(new Integer[] { 0, 1, 2, 3 });

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

		GridLayout grid = new GridLayout(GRID_CELLS, GRID_CELLS);
		Map<Button, Integer> allButtons = addButtonsToGrid(grid);

		Button resetButton = createResetButton(allButtons);
		allButtons.keySet().forEach(clickedButton -> clickedButton
				.addClickListener(createMineListener(clickedButton, allButtons, resetButton)));

		mainLayout.addComponent(new HorizontalLayout(new Label("Welcome to Vaadin demo. The Lucky MineSweeper.")));
		mainLayout.addComponent(grid);
		mainLayout.addComponent(resetButton);
		setContent(mainLayout);
	}

	private Button createResetButton(Map<Button, Integer> addedButtons) {
		Button reset = new Button(HAPPY_SMILEY);
		reset.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void buttonClick(ClickEvent event) {
				for (Entry<Button, Integer> entry : addedButtons.entrySet()) {
					reset.setCaption(HAPPY_SMILEY);
					entry.getKey().setCaption(""); 
					entry.setValue(CHARS.get(new Random().nextInt(CHARS.size())));
				}
			}
		});
		return reset;
	}

	private ClickListener createMineListener(Button clickedButton, Map<Button, Integer> allButtons,
			Button resetButton) {
		return new ClickListener() {
			private static final long serialVersionUID = -2544632506350332631L;
			@Override
			public void buttonClick(ClickEvent event) {
				setCaptionWithSleep(clickedButton, allButtons.get(clickedButton));				
				
				if (allButtons.get(clickedButton).equals(0)) {
					resetButton.setCaption(SAD_SMILEY);			
					allButtons.keySet().forEach(b -> setCaptionWithSleep(b, allButtons.get(b)));
				} 
			}
		};
	}

	private void setCaptionWithSleep(Button button, Integer caption) {
		try {
			button.setCaption(""+caption);
			Thread.sleep(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	private Map<Button, Integer> addButtonsToGrid(GridLayout grid) {
		Map<Button, Integer> buttons = new HashMap<>();
		for (int i = 0; i < GRID_CELLS; i++) {
			for (int j = 0; j < GRID_CELLS; j++) {
				Button button = new Button();
				button.setWidth(WIDTH_PIXELS, Unit.PIXELS);
				button.setHeight(WIDTH_PIXELS, Unit.PIXELS);
				grid.addComponent(button);
				buttons.put(button, new Random().nextInt(CHARS.size()));
			}
		}
		return buttons;
	}

	@WebServlet(urlPatterns = "/*", name = "ExampleServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = LuckyMineSweeperUI.class, productionMode = false)
	public static class ExampleServlet extends VaadinServlet {
		private static final long serialVersionUID = 1442012330836900031L;
	}
}