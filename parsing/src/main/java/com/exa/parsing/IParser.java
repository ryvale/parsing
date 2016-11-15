package com.exa.parsing;

import com.exa.utils.ManagedException;

public interface IParser<T> {

	T parse(String str) throws ManagedException;

	boolean validates(String exp) throws ManagedException;

}