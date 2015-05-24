package com.example.mylabs3;

import java.util.List;
import java.util.ResourceBundle;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import be.mylabs.ejb.BookEJB;
import be.mylabs2.entities.Book;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class BooksComponent extends HorizontalLayout {

	private static final long serialVersionUID = -7657988410761017223L;

	private BookEJB bookEJB;

	private Table booksTable = new Table();

	private BeanItemContainer<Book> booksContainer;

	private VerticalLayout editionVerticalLayout = new VerticalLayout();

	private ResourceBundle bundle = ResourceBundle.getBundle("myLabs3", UI.getCurrent().getLocale());

	public BooksComponent() {
		bookEJB = null;
		try {
			InitialContext ctx = new InitialContext();
			bookEJB = (BookEJB) ctx.lookup("java:module/BookEJB");
		} catch (NamingException e) {
			e.printStackTrace();
		}

		List<Book> books = bookEJB.findAllBooks();
		booksContainer = new BeanItemContainer<Book>(Book.class, books);
		booksTable.setContainerDataSource(booksContainer);
		booksTable.setColumnHeader("title", bundle.getString("book.title"));
		booksTable.setColumnHeader("isbn", bundle.getString("book.isbn"));
		booksTable.setColumnHeader("price", bundle.getString("book.price"));
		booksTable.setColumnHeader("releaseDate", bundle.getString("book.releaseDate"));
		booksTable.setVisibleColumns(new Object[] { "title", "isbn", "price", "releaseDate" });
		booksTable.setImmediate(true);
		booksTable.setSelectable(true);
		booksTable.addItemClickListener(new ItemClickListener() {

			private static final long serialVersionUID = 8352660061828168070L;

			@Override
			public void itemClick(ItemClickEvent event) {
				Item item = event.getItem();
				createEditionForm(item);
			}
		});
		addComponent(booksTable);
		addComponent(editionVerticalLayout);
	}

	private void createEditionForm(Item item) {
		editionVerticalLayout.removeAllComponents();
		FormLayout formLayout = new FormLayout();
		formLayout.setSpacing(true);
		formLayout.setMargin(true);
		final BeanFieldGroup<Book> editFieldGroup = new BeanFieldGroup<Book>(Book.class);
		editFieldGroup.setItemDataSource(item);
		for (Object propertyId : editFieldGroup.getUnboundPropertyIds()) {
			formLayout.addComponent(editFieldGroup.buildAndBind(propertyId));
		}
		editionVerticalLayout.addComponent(formLayout);
		Button editButton = new Button("Save modification");
		editButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -2707823412678814175L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					editFieldGroup.commit();
					Book bean = editFieldGroup.getItemDataSource().getBean();
					bookEJB.modifyBook(bean);
				} catch (CommitException e) {
					e.printStackTrace();
				}

			}
		});
		Button addButton = new Button("Add book");
		addButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -2045320561053863693L;

			@Override
			public void buttonClick(ClickEvent event) {
				createAddForm();
			}
		});
		editionVerticalLayout.addComponent(editButton);
		editionVerticalLayout.addComponent(addButton);
	}

	private void createAddForm() {
		final Window window = new Window();
		window.setSizeUndefined();
		final FormLayout formLayout = new FormLayout();
		formLayout.setImmediate(true);
		final BeanFieldGroup<Book> addfieldGroup = new BeanFieldGroup<Book>(Book.class);
		addfieldGroup.setItemDataSource(new Book());
		formLayout.setSpacing(true);
		formLayout.setMargin(true);
		for (Object propertyId : addfieldGroup.getUnboundPropertyIds()) {
			formLayout.addComponent(addfieldGroup.buildAndBind(propertyId));
		}
		Button saveButton = new Button("save");
		saveButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -4319350045721818351L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					addfieldGroup.commit();
					Book book = addfieldGroup.getItemDataSource().getBean();
					bookEJB.createBook(book);
					booksContainer.addBean(book);
					window.close();
				} catch (CommitException e) {
					e.printStackTrace();
				}
			}
		});
		formLayout.addComponent(saveButton);
		window.setContent(formLayout);
		window.setModal(true);
		window.center();
		UI.getCurrent().addWindow(window);
	}
}
