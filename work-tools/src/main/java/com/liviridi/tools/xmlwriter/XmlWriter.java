package com.liviridi.tools.xmlwriter;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.apache.crimson.tree.XmlDocument;
import org.w3c.dom.Element;

public class XmlWriter {
	private static String kokykdaicho = null;
	private static String syomtrset = null;

	private static XmlDocument doc;

	public static void WriteXml() {
		doc = new XmlDocument();
		Element replyData=null;
		Element aplhdr_area =null;

		//实例元素变量
		replyData = doc.createElement("replyData");

		aplhdr_area = doc.createElement("aplhdr-area");
		aplhdr_area.setAttribute("xmlns", "http://ogis-ri.co.jp/APLHDR-AREA");
		Element aplhdr_dci_inf = doc.createElement("aplhdr-dci-inf");
		aplhdr_dci_inf.appendChild(doc.createElement("aplhdr-menuid"));
		aplhdr_dci_inf.appendChild(doc.createElement("aplhdr-pgmid"));
		aplhdr_dci_inf.appendChild(doc.createElement("aplhdr-convsign"));
		aplhdr_dci_inf.appendChild(doc.createElement("aplhdr-dci-reserve"));

		Element aplhdr_appl_inf = doc.createElement("aplhdr-appl-inf");
		aplhdr_appl_inf = WriteChild(aplhdr_appl_inf, "aplhdr-rtncd", "00");
		aplhdr_appl_inf = WriteChild(aplhdr_appl_inf, "aplhdr-rsncd", "0000");
		aplhdr_appl_inf = WriteChild(aplhdr_appl_inf, "aplhdr-othcd", "");
		aplhdr_appl_inf = WriteChild(aplhdr_appl_inf, "aplhdr-msgid", "");
		aplhdr_appl_inf = WriteChild(aplhdr_appl_inf, "aplhdr-msgkbn", "");
		aplhdr_appl_inf = WriteChild(aplhdr_appl_inf, "aplhdr-msg", "");
		aplhdr_appl_inf = WriteChild(aplhdr_appl_inf, "aplhdr-userdata-len", "00000000");
		aplhdr_appl_inf = WriteChild(aplhdr_appl_inf, "aplhdr-appl-reserve", "");

		aplhdr_area.appendChild(aplhdr_dci_inf);
		aplhdr_area.appendChild(aplhdr_appl_inf);

		replyData.appendChild(aplhdr_area);

		Element d4400r_response = doc.createElement("d4400r-response");
		d4400r_response.setAttribute("xmlns", "http://ogis-ri.co.jp/D4400R-RESPONSE");

		d4400r_response = WriteChild(d4400r_response, "d4400r-s-kokykdaicho", kokykdaicho);
		d4400r_response = WriteChild(d4400r_response, "d4400r-c-syonyurykhoho", "Ｘ");
		d4400r_response = WriteChild(d4400r_response, "d4400r-s-syodesig", "BBBBBBBBBBBBB");
		d4400r_response = WriteChild(d4400r_response, "d4400r-s-syookyakusama", "CCCCCCCCCCCCCC");
		d4400r_response = WriteChild(d4400r_response, "d4400r-s-syomtrset", syomtrset);
		d4400r_response = WriteChild(d4400r_response, "d4400r-s-syozip", "1900155");
		d4400r_response = WriteChild(d4400r_response, "d4400r-n-todofknkj", "東京都");
		d4400r_response = WriteChild(d4400r_response, "d4400r-n-skgnchsnkj", "あきる野市");
		d4400r_response = WriteChild(d4400r_response, "d4400r-n-oazatushokj", "あきる野");
		d4400r_response = WriteChild(d4400r_response, "d4400r-n-azachomekj", "ああああああああああああ");
		d4400r_response = WriteChild(d4400r_response, "d4400r-n-syobantgsknj", "ああああああああああ");
		d4400r_response = WriteChild(d4400r_response, "d4400r-n-syosgojtkknj", "あああああああああああああああ");
		d4400r_response = WriteChild(d4400r_response, "d4400r-n-syotogosuknj", "あああああああああああああああ");
		d4400r_response = WriteChild(d4400r_response, "d4400r-n-syokokykknj", "ああああああああああああああああああああ");
		d4400r_response = WriteChild(d4400r_response, "d4400r-n-syokokykkana", "ｱｱｱｱｱｱｱｱｱｱｱｱｱｱｱｱｱｱｱｱ");
		d4400r_response = WriteChild(d4400r_response, "d4400r-s-syotel", "AAAAAAAAAAAAAA");
		d4400r_response = WriteChild(d4400r_response, "d4400r-c-chhanbaiten", "AAAAAA");
		d4400r_response = WriteChild(d4400r_response, "d4400r-s-chkeiyaku", "AAAAA");
		d4400r_response = WriteChild(d4400r_response, "d4400r-s-chshonin", "AAA");
		d4400r_response = WriteChild(d4400r_response, "d4400r-s-chsettisaki", "AAAA");
		d4400r_response = WriteChild(d4400r_response, "d4400r-s-chsubuser", "AAAAAAA");
		d4400r_response = WriteChild(d4400r_response, "d4400r-s-chkomuten", "AAAAAAA");

		Element kiki_tb = doc.createElement("d4400r-l-kiki-tb");

		d4400r_response.appendChild(kiki_tb);
		d4400r_response = WriteChild(d4400r_response, "d4400r-c-msg", "");
		replyData.appendChild(d4400r_response);

		doc.appendChild(replyData);

		String path = "D:/test/D4400R.xml";
		try {
			OutputStreamWriter out = new OutputStreamWriter(
			new BufferedOutputStream(new FileOutputStream(path)), "UTF-8");
	        System.out.println(doc.item(0));

			doc.write(out, "UTF-8");
			out.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	private static Element WriteChild(Element Parent, String NodeName, String str) {
		if (str == null) {
			str = "";
		}
		Element Child = doc.createElement(NodeName);
		Child.appendChild(doc.createTextNode(str));
		Parent.appendChild(Child);
		return Parent;
	}
	public static void setKokykdaicho(String kokykdaicho) {
		XmlWriter.kokykdaicho = kokykdaicho;
	}
	public static void setSyomtrset(String syomtrset) {
		XmlWriter.syomtrset = syomtrset;
	}
}
