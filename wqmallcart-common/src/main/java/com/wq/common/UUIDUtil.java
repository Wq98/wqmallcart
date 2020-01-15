package com.wq.common;

import java.util.UUID;

/**
 * @ClassName: UUIDUtil
 * @Description:
 * @author: 魏秦
 * @date: 2019/11/27  14:57
 */
public class UUIDUtil {
	public static String getUUID(){
		UUID uuid=UUID.randomUUID();
		return uuid.toString();
	}
	public static void main(String[] args) {
		System.out.println(getUUID());
	}
}
