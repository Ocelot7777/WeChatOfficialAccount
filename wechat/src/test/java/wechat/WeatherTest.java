package wechat;

import com.ocelot.service.WeatherService;
import com.ocelot.service.WeatherServiceImpl;

public class WeatherTest {
	public static void main(String[] args) {
		WeatherService weather = new WeatherServiceImpl();
		System.out.println(weather.queryWeather("上海"));
	}
}
