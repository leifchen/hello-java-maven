package com.chen.zookeeper.util;

import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

/**
 * ACL工具类
 * <p>
 * @Author LeifChen
 * @Date 2019-06-03
 */
public class AclUtils {
	
	public static String getDigestUserPwd(String id) throws Exception {
		return DigestAuthenticationProvider.generateDigest(id);
	}
}
