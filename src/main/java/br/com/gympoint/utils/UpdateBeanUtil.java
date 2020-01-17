package br.com.gympoint.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class UpdateBeanUtil {
	
	private static final String SET = "set";
	private static final String GET = "get";

	/**
	 * Atualiza qualquer bean baseado em seus getters e setters,
	 *  setando os valores alterados da classe a para a classe b
	 * @param <T> Type to be updated
	 * @param classOne Class to be updated
	 * @param classTwo Class containing the fields to updated
	 * @return class updated
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 */
	public static <T> T updateFields (T classOne, T classTwo) 
		throws NoSuchMethodException, 
			SecurityException, 
			IllegalAccessException, 
			IllegalArgumentException, 
			InvocationTargetException 
		{
		
		for (Method method : classTwo.getClass().getDeclaredMethods()) {
			
			if (method.getName().startsWith(GET) && method.invoke(classTwo) != null) {
				
				Object invokeMeth = method.invoke(classTwo);

				classOne
					.getClass()
					.getMethod(method.getName().replace(GET, SET), invokeMeth.getClass())
					.invoke(classOne, invokeMeth);
			}
		}
		
		return classOne;
	}
	
}
