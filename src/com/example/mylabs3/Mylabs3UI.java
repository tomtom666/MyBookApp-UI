package com.example.mylabs3;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("mylabs3")
public class Mylabs3UI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = Mylabs3UI.class)
	public static class Servlet extends VaadinServlet {
	}

	// @EJB

	@Override
	protected void init(VaadinRequest request) {
		final VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setMargin(true);
		setContent(mainLayout);

		BooksComponent booksComponent = new BooksComponent();

		mainLayout.addComponent(booksComponent);

	}

}