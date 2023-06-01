package com.minis.core;


/**
 * ApplicationEventPublisher
 *
 * @author qizhi
 * @date 2023/06/01
 */
public interface ApplicationEventPublisher {
	void publishEvent(ApplicationEvent event);
}