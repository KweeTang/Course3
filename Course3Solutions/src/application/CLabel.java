package application;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;

public class CLabel<T> extends Label {
	private ObjectProperty<T> item = new SimpleObjectProperty<T>(this, "item");

	public CLabel() {
		super();
        // what to do about null items?
	}

	public CLabel(String text, T item) {
		super(text);

        if(item != null)
            setItem(item);
        else
        	setItem(null);
	}

	public CLabel(String text, Node graphic, T item) {
		super(text, graphic);
        if(item != null)
            setItem(item);
        else
        	setItem(null);
	}

	protected void updateView(T item, boolean empty) {
		System.out.println("In updateView!!");

        if(item != null) {
    		setText(item.toString());
        }
        else {
        	setText("Choose Point");
        }
	}

    public final ObjectProperty<T> itemProperty(){ return item; }


	public T getItem(){
		return item.get();
	}

	public void setItem(T newItem) {
        System.out.println("old item : "  + item.get());
		item.set(newItem);
        updateView(item.get(), true);
        System.out.println("new item : "  + item.get());
	}
}
