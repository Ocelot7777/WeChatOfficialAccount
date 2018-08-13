package com.ocelot.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

import com.google.gson.Gson;
import com.ocelot.po.poi.Detail_info;
import com.ocelot.po.poi.PoiBaidu;
import com.ocelot.po.poi.Result;

public class PoiServiceImpl implements PoiService {
	private static String api = "http://api.map.baidu.com/place/v2/search?";
	private static String ak = "3qAI4GHwkGqOYSGXN0zGGWZ856BeFHVX";
	private static String page_size = "5";
	private static String scope = "2";
	private static String output = "json";
	
	@Override
	public String searchPoi(String query,String region) {
		//转码，防止请求出现乱码导致查询失败
		String query_url = null;
		String region_url = null;
		try {
			query_url = URLEncoder.encode(query, "utf-8");
			region_url = URLEncoder.encode(region, "utf-8");
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//拼接url参数
		StringBuffer sb = new StringBuffer();
		sb.append("ak=" + ak);
		sb.append("&page_size=" + page_size);
		sb.append("&scope=" + scope);
		sb.append("&output=" + output);
		sb.append("&query=" + query_url);
		sb.append("&region=" + region_url);
		String param = sb.toString();
		
		String url = api + param;
		URL realUrl = null;
		System.out.println(url);
		StringBuffer json = new StringBuffer();
		try {
			realUrl = new URL(url);
			URLConnection conn = realUrl.openConnection();
			
			//获取输入流
			InputStream input = conn.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(input, "utf-8"));
			
			String line = null;
			while((line = br.readLine())!=null) {
				json.append(line);
			}
			br.close();
			
//			System.out.println(json.toString());
			
			//使用Gson将获取的json信息存入相应的po类中
			Gson gson = new Gson();
			PoiBaidu poiBaidu = gson.fromJson(json.toString(), PoiBaidu.class);
			
			//查询失败
			if(poiBaidu.getStatus()!=0) {
				System.out.println(poiBaidu.getMessage());
				return "查询失败，请核对输入的信息。\n建议格式【地区 关键词】如【上海 星巴克】\n";
			}else if(poiBaidu.getTotal()==0){
				return "很抱歉，未能查询到相关信息，请核对输入的信息是否有误。\n建议格式【地区 关键词】如【上海 星巴克】\n";
			}else {
				List<Result> results = poiBaidu.getResults();
				StringBuffer content = new StringBuffer();
				
				//返回结果太多时进行处理，返回提示
				if(results.get(0).getUid()==null) {
					content.append("查询到的条目过多，请尝试加入以下地区限定词以获得精确结果。\n建议格式【地区 关键词】如【上海 星巴克】\n");
					List<Result> newResults = results.subList(0, 5);
					for (Result result : newResults) {
						content.append(result.getName() + "：" + result.getNum() +"条信息\n");
					}
				}else {
					for (Result result : results) {
						content.append("【名称】" + result.getName() + "\n");
						
						//添加地区信息
						String province = result.getProvince();
						String city = result.getCity();
						String area = result.getArea();
						if(province!=null && city!=null && area!=null) {
							String addr = (province.equals(city)?"":province) + " " + city + " " + area;
							content.append("【地区】" + addr + "\n");
						}
						
						//添加详细地址
						String address = result.getAddress();
						if(address!=null)
							content.append("【详细地址】" + address + "\n");
						//添加电话信息
						String tel = result.getTelephone();
						if(tel!=null)
							content.append("【联系电话】" + tel + "\n");
						//添加详情
						if("1".equals(result.getDetail())) {
							Detail_info info = result.getDetail_info();
							String tag = info.getTag();
							String price = info.getPrice();
							String rating = info.getOverall_rating();
							
							if(tag!=null)
								content.append("【标签】" + tag + "\n");
							if(price!=null)
								content.append("【参考价格】" + price + "\n");
							if(rating!=null)
								content.append("【总体评分】" + rating + "\n");
						}
						content.append("\n");
					}
				}
				return content.toString();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "搜索poi出错！！";
		}
	}

}
