/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-23 下午8:46:00
 */
package com.absir.core.util;

/**
 * @author absir
 * 
 */
public class UtilNode<T> {

	/** element */
	private T element;

	/** previous */
	private UtilNode<T> previous;

	/** next */
	private UtilNode<T> next;

	/**
	 * 
	 */
	public UtilNode() {
	}

	/**
	 * @param element
	 */
	public UtilNode(T element) {
		this.element = element;
	}

	/**
	 * @return the element
	 */
	public T getElement() {
		return element;
	}

	/**
	 * @param element
	 *            the element to set
	 */
	public void setElement(T element) {
		this.element = element;
	}

	/**
	 * @return the previous
	 */
	public UtilNode<T> getPrevious() {
		return previous;
	}

	/**
	 * @return the next
	 */
	public UtilNode<T> getNext() {
		return next;
	}

	/**
	 * @param node
	 */
	public void beforeAdd(UtilNode<T> node) {
		if (previous != null) {
			previous.next = node;
		}

		node.previous = previous;
		node.next = this;
		previous = node;
	}

	/**
	 * @param node
	 */
	public void afterAdd(UtilNode<T> node) {
		if (next != null) {
			next.previous = node;
		}

		node.previous = this;
		node.next = next;
		next = node;
	}

	/**
	 * 
	 */
	public void remove() {
		if (previous != null) {
			previous.next = next;
		}

		if (next != null) {
			next.previous = previous;
		}
	}

	/**
	 * @param node
	 */
	public void beforeInsert(UtilNode<T> node) {
		node.remove();
		beforeAdd(node);
	}

	/**
	 * @param node
	 */
	public void afterInsert(UtilNode<T> node) {
		node.remove();
		afterAdd(node);
	}
}
