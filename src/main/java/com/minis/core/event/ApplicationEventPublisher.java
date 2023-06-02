package com.minis.core.event;


/**
 * ApplicationEventPublisher
 *
 * @author qizhi
 * @date 2023/06/01
 */
public interface ApplicationEventPublisher {
	void publishEvent(ApplicationEvent event);
	void addApplicationListener(ApplicationListener listener);
}