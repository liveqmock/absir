import org.junit.Test;

import com.absir.core.helper.HelperFileName;

/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-9-5 下午3:01:45
 */

/**
 * @author absir
 * 
 */
public class FilenameTest {

	@Test
	public void test() {

		System.out.println(HelperFileName.addFilenameSubExtension("1.jpg", "abc"));
		System.out.println(HelperFileName.iterateFilename("/Users/absir", "/Desktop/MyFive/gen/five/itcast/cn", "readme.txt"));
	}

}
