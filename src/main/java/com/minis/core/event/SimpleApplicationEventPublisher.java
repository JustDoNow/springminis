package com.minis.core.event;

import java.util.ArrayList;
import java.util.List;

/**
 * SimpleApplicationEventPublisher
 *
 * @author qizhi
 * @date 2023/06/02
 */
public class SimpleApplicationEventPublisher implements
		ApplicationEventPublisher{
	List<ApplicationListener> listeners = new ArrayList<>();
	@Override
	public void publishEvent(ApplicationEvent event) {
		for (ApplicationListener listener : listeners) {
			listener.onApplicationEvent(event);
		}
	}
	@Override
	public void addApplicationListener(ApplicationListener listener) {
		this.listeners.add(listener);
	}
}