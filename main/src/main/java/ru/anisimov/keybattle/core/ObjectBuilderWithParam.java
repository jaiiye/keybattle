package ru.anisimov.keybattle.core;

/**
 * @author Ivan Anisimov
 *         valter@yandex-team.ru
 *         10/22/14
 */
public abstract class ObjectBuilderWithParam implements ObjectBuilder, Cloneable {
	protected Object param;

	public ObjectBuilder cloneWithParam(Object param) {
		ObjectBuilderWithParam result = null;
		try {
			result = (ObjectBuilderWithParam) this.clone();
		} catch (CloneNotSupportedException e) {
			// do nothing
		}
		result.setParam(param);
		return result;
	}

	public Object getParam() {
		return param;
	}

	public void setParam(Object param) {
		this.param = param;
	}
}
