package com.fan.framework.imageloader;

import java.util.ArrayList;
import java.util.HashMap;

import com.fan.framework.utils.FFLogUtil;

public class FFImageRequestQueue {
	private HashMap<String, ArrayList<FFImageRequest>> map;
	public ArrayList<FFImageRequest> getQueue(String url){
		return map.get(url);
	}
	
	public FFImageRequestQueue(){
		map = new HashMap<String, ArrayList<FFImageRequest>>();
	}
	
	public void addQueue(String key, FFImageRequest request){
		ArrayList<FFImageRequest> queue = new ArrayList<FFImageRequest>();
		queue.add(request);
		map.put(key, queue);
	}

	public boolean containsKey(String imageUrl) {
		return map.containsKey(imageUrl);
	}

	public void add(String key, FFImageRequest request) {
		map.get(key).add(request);
	}

	public void remove(String imageUrl) {
		map.remove(imageUrl);
		FFLogUtil.e("图片加载", "当前队列数量"+map.size());
	}
}
