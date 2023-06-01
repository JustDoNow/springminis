package com.minis.core;

import java.util.EventObject;

/**
 * ApplicationEvent
 *
 * @author qizhi
 * @date 2023/06/01
 */

public class ApplicationEvent  extends EventObject {
	private static final long serialVersionUID = 1L;
	public ApplicationEvent(Object arg0) {
		super(arg0);
	}
}