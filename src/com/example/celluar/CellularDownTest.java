/**
 * 
 */
package com.example.celluar;

import org.junit.Assert;

import android.test.AndroidTestCase;

import com.example.Integrity.IntegrityCheck;

/**
 * @author zxyqwe
 * 
 */
public class CellularDownTest extends AndroidTestCase {

	/**
	 * @throws java.lang.Exception
	 */
	@Override
	public void setUp() throws Exception {
	}

	/**
	 * Test method for
	 * {@link com.example.celluar.CellularDown#queryFragment(java.lang.String)}.
	 */
	public final void testQueryFragment() {
		IntegrityCheck iTC = IntegrityCheck.getInstance();
		String tmpp = IntegrityCheck.URI_TAG + "1.mp4";
		byte[] tmp = iTC.getSegments(tmpp);
		Assert.assertNotNull(tmp);
	}
}
