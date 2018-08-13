package wechat;

import com.ocelot.service.PoiService;
import com.ocelot.service.PoiServiceImpl;
import com.ocelot.util.KeywordsUtil;

public class PoiTest {
	public static void main(String[] args) {
		PoiService poiService = new PoiServiceImpl();
//		System.out.println(poiService.searchPoi("必胜客","中国"));
	
		String ans[] = KeywordsUtil.getKeys("上海 星巴克");
		for (String string : ans) {
			System.out.println(string);
		}
		System.out.println(poiService.searchPoi(ans[1], ans[0]));
	}
}
