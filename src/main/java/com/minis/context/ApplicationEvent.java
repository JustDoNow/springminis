package com.minis.context;

import java.util.EventObject;

/**
 * ApplicationEvent
 *
 * @author qizhi
 * @date 2023/06/01
 */
public class ApplicationEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	protected String msg = null;
	public ApplicationEvent(Object arg0) {
		super(arg0);
		this.msg = arg0.toString();
	}
}