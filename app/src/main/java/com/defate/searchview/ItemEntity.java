package com.defate.searchview;


import java.util.List;

public class ItemEntity implements IAZItem, IFuzzySearchItem {

	private String mValue;
	private String mSortLetters;
	private List<String> mFuzzySearchKey;

	public ItemEntity(String value, String sortLetters, List<String> fuzzySearchKey) {
		mValue = value;
		mSortLetters = sortLetters;
		mFuzzySearchKey = fuzzySearchKey;
	}

	public String getValue() {
		return mValue;
	}

	@Override
	public String getSortLetters() {
		return mSortLetters;
	}

	@Override
	public String getSourceKey() {
		return mValue;
	}

	@Override
	public List<String> getFuzzyKey() {
		return mFuzzySearchKey;
	}
}
