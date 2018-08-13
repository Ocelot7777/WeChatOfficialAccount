package com.ocelot.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

import com.google.gson.Gson;
import com.ocelot.po.weather.Casts;
import com.ocelot.po.weather.Forecast;
import com.ocelot.po.weather.Lives;
import com.ocelot.po.weather.Status;

public class WeatherServiceImpl implements WeatherService {
	//高德地图api所需参数
	private static String api = "https://restapi.amap.com/v3/weather/weatherInfo?";
	private static String extensions = "all";
	private static String output = "json";
	private static String key = "e7ed763fd10a4dfc144c34cabba9d271";
	String[] weekday = {"","星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
	
	public String queryWeather(String str) {
		String city = null;
		try {
			city = URLEncoder.encode(str, "utf-8");
		} catch (Exception e) {
			// TODO: handle exception
		}
		//拼接url参数
		StringBuffer sb = new StringBuffer();
		sb.append("city=");
		sb.append(city);
		sb.append("&output=");
		sb.append(output);
		sb.append("&key=");
		sb.append(key);
		sb.append("&extensions=");
		sb.append(extensions);
		String param = sb.toString();
		
		String url = api + param;
		URL realUrl = null;
		
		StringBuffer result = new StringBuffer();
		try {
			//建立连接
			realUrl = new URL(url);
			URLConnection conn = realUrl.openConnection();
			//获取输入流，转码
			InputStream input = conn.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(input, "utf-8"));
			
			String line = null;
			while((line = br.readLine())!=null) {
				result.append(line);				
			}
			br.close();
			
			//使用Gson将获取的json信息存入相应的po类中
			Gson gson = new Gson();
			Status status = gson.fromJson(result.toString(),Status.class);
			
			if("0".equals(status.getCount())) {
				//未查询到天气信息
				return "很抱歉，未能查询到相应城市的天气信息";
			}else {
				StringBuffer content = new StringBuffer();
				
				List<Lives> lives = status.getLives();
				if(lives!=null && lives.size()>0) {
					content.append("实时天气信息：\n");
	
					for (Lives live : lives) {
						String province = live.getProvince();
						String cityName = live.getCity();
						String weather = live.getWeather();
						String temperature = live.getTemperature();
						String winddirection = live.getWinddirection();
						String windpower = live.getWindpower();
						String humidity = live.getHumidity();
						
						content.append("【地区】" + province + " "+ cityName + "\n"
								+ "【天气】" + weather + "\n"
								+ "【气温】" + temperature +"℃\n"
								+ "【风向】" + winddirection +"风\n"
								+ "【风力】" + windpower +"级\n"
								+ "【湿度】" + humidity +"\n"
								);
						content.append("\n");
					}
				}
				
				List<Forecast> forecasts = status.getForecasts();
				if(forecasts!=null && forecasts.size()>0) {					
//					content.append("预报天气信息：\n");				
					
					for (Forecast forecast : forecasts) {
						List<Casts> casts = forecast.getCasts();
						content.append("【地区】" + forecast.getProvince() + " "+ forecast.getCity() + "\n");
						for (Casts cast : casts) {
							content.append("【日期】" + cast.getDate() + " " + weekday[Integer.parseInt(cast.getWeek())] + "\n"
									+ "【白天天气】" + cast.getDayweather() + " " + cast.getDaytemp() + "℃\n"
									+ "【夜间天气】" + cast.getNightweather() + " " + cast.getNighttemp() + "℃\n"
									+ "【风向】" + cast.getDaywind() + "风\n"
									+ "【风力】" + cast.getDaypower() + "级\n"
									+ "\n");
						}
					}
				}
				return content.toString();
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return "天气查询出错";
	}

}
