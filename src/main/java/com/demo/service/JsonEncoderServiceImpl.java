package com.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.demo.util.StringUtil;

@Service
public class JsonEncoderServiceImpl implements JsonEncoderService {

	@Override
	public Object createJson() {

		 List<String> input = new ArrayList<>();
			//input.add("DGBDGZJ4HKTBSVHY.title='Inferno: (Robert Langdon Book 4)'");
			 input.add("DGBDGZJ4HKTBSVHY.price=399");
			 input.add("DGBDGZJ4HKTBSVHY.price=4299");
			 input.add("DGBDGZJ4HKTBSVHY.sdfs='sdfdss'");
			 input.add("DGBDGZJ4HKTBSVHY2.image");
			 input.add("DGBDGZJ4HKTBSVHY2.image.sfcas.ssdvsfs.ssvsvghrgfs=true");
			 input.add("DGBDGZJ4HKTBSVHY.avc.1='sdfs'");
			// input.add("DGBDGZJ4HKTBSVHY.400x400.images.location=http://static03.digital.flipkart.com/cds/400/1");

			 Map<String, Object> map = new HashMap<>();
				Map<String, Object> map2 = map;
				// map.put("key", "value");
				for (String line : input) {
					String[] parts = line.split("=");
					String key = parts[0];
					String value=null;
					//System.out.println(
							//"va"+parts.length);
					if(parts.length>1) {
					 value = parts[1];
					}
					String[] keys = key.split("\\.");
					int keySize = keys.length;
					//System.out.println(key);
					// System.out.println("ketSize"+keySize);
					for (int i = 0; i < keySize; i++) {
						
						if (i < (keySize - 1)) {
							Map<String, Object> newMap = new HashMap<>();

							if (i > 0 ) {
								if(i>1) {
									map = (Map) map.get(keys[i - 2]);
								}
								newMap = (Map) map.get(keys[i - 1]);
								System.out.println("looking for key "+keys[i - 1] + "in map " + map);
								if(newMap==null) {
									newMap = new HashMap<>();
								}
								buildJson(keys[i], newMap, value);

							}
							else {
								buildJson(keys[i], map, value);
							}

						}

						else {
							Map<String, Object> newMap = new HashMap<>();
							if (i > 0) {
								if(i>1) {
									map = (Map) map.get(keys[i - 2]);
								}
								newMap = (Map) map.get(keys[i - 1]);
								System.out.println("looking for key "+keys[i - 1] + "in map " + map);
								if(newMap==null) {
									newMap = new HashMap<>();
								}
							}
							//System.out.println("null point" + newMap);
							if(newMap.get(keys[i])!=null) {
								List<Object> arr = new ArrayList<>();
								arr.add(newMap.get(keys[i]));
								arr.add(StringUtil.getFieldValue(value));
								newMap.put(keys[i],arr);
							}
							else
							newMap.put(keys[i], StringUtil.getFieldValue(value));
						}

					}
				}
		return map2;

	}

	private static void buildJson(String string, Map<String, Object> map, String value) {
		System.out.println(string +map + value );
		//System.out.println("there"+map.get(string) );
		if(map.get(string)==null) {
		map.put(string, new HashMap<>());
		}

	}
	
	 public static void main(String[] args) {
//		 List<String> input = new ArrayList<>();
//			//input.add("DGBDGZJ4HKTBSVHY.title='Inferno: (Robert Langdon Book 4)'");
//			 input.add("DGBDGZJ4HKTBSVHY.price=399");
//			 input.add("DGBDGZJ4HKTBSVHY.price=4299");
//			 input.add("DGBDGZJ4HKTBSVHY2.image");
//			 input.add("DGBDGZJ4HKTBSVHY2.image.sfs.sfs.sfs=true");
//			 input.add("DGBDGZJ4HKTBSVHY.2='Inferno: (Robert Langdon Book 4)'");
//			// input.add("DGBDGZJ4HKTBSVHY.400x400.images.location=http://static03.digital.flipkart.com/cds/400/1");
//
//			Map<String, Object> map = new HashMap<>();
//			Map<String, Object> map2 = map;
//			// map.put("key", "value");
//			for (String line : input) {
//				String[] parts = line.split("=");
//				String key = parts[0];
//				String value=null;
//				//System.out.println(
//						//"va"+parts.length);
//				if(parts.length>1) {
//				 value = parts[1];
//				}
//				String[] keys = key.split("\\.");
//				int keySize = keys.length;
//				//System.out.println(key);
//				// System.out.println("ketSize"+keySize);
//				for (int i = 0; i < keySize; i++) {
//					
//					if (i < (keySize - 1)) {
//						Map<String, Object> newMap = new HashMap<>();
//
//						if (i > 0 ) {
//							if(i>1) {
//								map = (Map) map.get(keys[i - 2]);
//							}
//							newMap = (Map) map.get(keys[i - 1]);
//							System.out.println("looking for key "+keys[i - 1] + "in map " + map);
//							if(newMap==null) {
//								newMap = new HashMap<>();
//							}
//							buildJson(keys[i], newMap, value);
//
//						}
//						else {
//							buildJson(keys[i], map, value);
//						}
//
//					}
//
//					else {
//						Map<String, Object> newMap = new HashMap<>();
//						if (i > 0) {
//							if(i>1) {
//								map = (Map) map.get(keys[i - 2]);
//							}
//							newMap = (Map) map.get(keys[i - 1]);
//							System.out.println("looking for key "+keys[i - 1] + "in map " + map);
//							if(newMap==null) {
//								newMap = new HashMap<>();
//							}
//						}
//						//System.out.println("null point" + newMap);
//						if(newMap.get(keys[i])!=null) {
//							List<Object> arr = new ArrayList<>();
//							arr.add(newMap.get(keys[i]));
//							arr.add(StringUtil.getFieldValue(value));
//							newMap.put(keys[i],arr);
//						}
//						else
//						newMap.put(keys[i], StringUtil.getFieldValue(value));
//						
//						
//					}
//
//				}
//			}
//			System.out.println(map2.toString());
	 }

}
