package com.minis.core;

import java.util.ArrayList;
import java.util.List;

/**
 * ArgumentValues
 *
 * @author qizhi
 * @date 2023/06/01
 */
public class ArgumentValues {
	private final List<ArgumentValue> argumentValueList = new ArrayList<>();
	public ArgumentValues() {
	}
	public void addArgumentValue(ArgumentValue argumentValue) {
		this.argumentValueList.add(argumentValue);
	}
	public ArgumentValue getIndexedArgumentValue(int index) {
		ArgumentValue argumentValue = this.argumentValueList.get(index);
		return argumentValue;
	}
	public int getArgumentCount() {
		return (this.argumentValueList.size());
	}
	public boolean isEmpty() {
		return (this.argumentValueList.isEmpty());
	}
}
