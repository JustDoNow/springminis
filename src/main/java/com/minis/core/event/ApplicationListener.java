package com.minis.core.event;

import java.util.EventListener;

/**
 * ApplicationEventListener
 *
 * @author qizhi
 * @date 2023/06/02
 */
public class ApplicationListener implements EventListener {
	void onApplicationEvent(ApplicationEvent event) {
		System.out.println(event.toString());
	}
}


