package com.ocelot.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ocelot.service.PoiService;
import com.ocelot.service.PoiServiceImpl;
import com.ocelot.service.WeatherService;
import com.ocelot.service.WeatherServiceImpl;
import com.ocelot.util.KeywordsUtil;

import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutTextMessage;

//接收微信服务器请求
public class WxServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//微信服务器get传递过来的参数
		String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        
        //一些API和封装的操作类
        WxMpService wxMpService = new WxMpServiceImpl();
        //准备注入token参数 WxMpInMemoryConfigStorage为微信配置参数实体类
        WxMpInMemoryConfigStorage wxConfigProvider = new WxMpInMemoryConfigStorage();
        wxConfigProvider.setToken("steam");
        
        wxMpService.setWxMpConfigStorage(wxConfigProvider);
        //直接调用checkSignature进行验证
        boolean flag = wxMpService.checkSignature(timestamp, nonce, signature);
        
        PrintWriter out = response.getWriter();
        if(flag) {
        	out.print(echostr);
        }
        out.close();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		//WxMpXmlMessage为消息处理类，可以自动解析xml文件
		WxMpXmlMessage message = WxMpXmlMessage.fromXml(request.getInputStream());
		String messageType = message.getMsgType();
		String fromUser = message.getFromUser();
		String toUser = message.getToUser();
		String content = message.getContent();
		String responseText = null;
		
		//判断消息类型
		if("event".equals(messageType)) {
			String eventType = message.getEvent();
			//判断是否为订阅事件
			if("subscribe".equals(eventType)) {
				responseText = "欢迎来到灰烬湖！\n"
						+ "目前已开通以下功能：\n"
						+ "【天气查询】回复“地区+空格+天气”\n例如“上海 天气”\n\n"
						+ "【地点查询】回复“地区+空格+关键词”\n例如“上海 美食” 或者 “上海 东方明珠”\n\n"
						+ "其他功能正在陆续开发中"
						;
			}
		}
		else if("text".equals(messageType)) {
			//天气查询
			if(content.indexOf("天气")>=0) {
				String city = content.replaceAll("天气", "").trim();
				
				//调用WeatherService进行查询
				WeatherService weatherService = new WeatherServiceImpl();
				String weather = weatherService.queryWeather(city);
				responseText = weather;
			}else  {
				//处理传来的信息
				String[] keys = KeywordsUtil.getKeys(content);
				//调用WeatherService进行查询
				PoiService poiService = new PoiServiceImpl();
				String poi = poiService.searchPoi(keys[1], keys[0]);
				responseText = poi;
			}

		}
		
		if(responseText == null)
			responseText = "success";
		
		WxMpXmlOutTextMessage outMessage = WxMpXmlOutTextMessage.TEXT().fromUser(toUser).toUser(fromUser).content(responseText).build();
		String xml = outMessage.toXml();
		
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(xml);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
			out = null;
		}
		
	}
	
}
