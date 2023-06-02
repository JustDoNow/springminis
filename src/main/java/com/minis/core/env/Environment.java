package com.minis.core.env;

/**
 * Environment
 *
 * @author qizhi
 * @date 2023/06/02
 */
public interface Environment extends PropertyResolver {
	String[] getActiveProfiles();
	String[] getDefaultProfiles();
	boolean acceptsProfiles(String... profiles);
}
