package com.starocean.fileCopy.test;

import com.starocean.fileCopy.Util.WeDrivePathUtil;

public class TestDemo {
	public static void main(String[] args) {
//		LocalDateTime now = LocalDateTime.now();
//		System.out.println(now);
//		LocalDateTime truncatedTo = now.minusDays(7).truncatedTo(ChronoUnit.HOURS);
//		System.out.println(truncatedTo);
//		String str = "/Doc/2026/270745694d54406b9c7699c41afcf819.pdf";
//		String[] split = str.split("/");
//		if (split.length >= 1) {
//			String fileName = split[split.length - 1]; 
//			System.out.println(fileName);
//		}
//		System.out.println(truncatedTo);
//		Map<String,String> of = Map.of("key1", "value1", "key2", "value2","key3", "value3");
//		System.out.println(of);
//		LocalDateTime createdAt = LocalDateTime.now();
//		String targetMoveFolderName = String.valueOf(createdAt.getYear())+ String.valueOf(createdAt.getMonthValue() < 10 ? "0" + createdAt.getMonthValue() : createdAt.getMonthValue()) +
//				String.valueOf(createdAt.getDayOfMonth() < 10 ? "0" + createdAt.getDayOfMonth() : createdAt.getDayOfMonth());
//		System.out.println(targetMoveFolderName);
		String relativePath = "Doc/2026/aaa.pdf";
		String normalizeFullPath = WeDrivePathUtil.normalizeFullPathWithoutFile(relativePath);
		String normalizeRelativePath = WeDrivePathUtil.normalizeRelativePathContainFile(relativePath);
		String fileNameByPath = WeDrivePathUtil.getFileNameByPath(relativePath);
		System.out.println(normalizeFullPath);
		System.out.println(normalizeRelativePath);
		System.out.println(fileNameByPath);
	}
}
