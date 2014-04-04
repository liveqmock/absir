import com.absir.zxing.support.BufferedImageLuminanceSource;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-8-13 上午10:18:09
 */

/**
 * @author absir
 * 
 */
public class ReadLogoTest {

	public void test() throws NotFoundException, ChecksumException, FormatException {
		LuminanceSource luminanceSource = new BufferedImageLuminanceSource(null);
		QRCodeReader qrCodeReader = new QRCodeReader();
		qrCodeReader.decode(new BinaryBitmap(new HybridBinarizer(luminanceSource)));
	}
}
