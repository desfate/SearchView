package com.defate.searchview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.jakewharton.rxbinding3.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class MainActivity extends AppCompatActivity {
    EditText edit_view;
    ListView mListView;
    List<ItemEntity> dateList = new ArrayList<>();
    List<ItemEntity> valueList = new ArrayList<>();
    DefaultFuzzySearchRule mIFuzzySearchRule;
    SearchAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edit_view = findViewById(R.id.edit_view);
        dateList = fillData(getResources().getStringArray(R.array.region));
        mIFuzzySearchRule = new DefaultFuzzySearchRule();
        mListView = findViewById(R.id.mListView);
        searchAdapter = new SearchAdapter(valueList, this);
        mListView.setAdapter(searchAdapter);
        RxTextView.textChanges(edit_view)
                .debounce (1, TimeUnit.SECONDS).skip(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CharSequence>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CharSequence charSequence) {
                        valueList.clear();
                        if (TextUtils.isEmpty(charSequence)) {

                        } else {
                            for (ItemEntity item : dateList) {
                                if (mIFuzzySearchRule.accept(charSequence, item.getSourceKey(), item.getFuzzyKey())) {
                                    valueList.add(item);
                                }
                            }
                        }
//                        for(int i = 0; i < valueList.size(); i--){
                            System.out.println("搜索到了数据 ="+valueList.size());
//                        }
//                        for (ItemEntity item : valueList) {
//                            System.out.println(item.getValue());
//                        }
                        searchAdapter.notifyDataSetChanged();
//                        System.out.println(valueList);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private List<ItemEntity> fillData(String[] date) {
        List<ItemEntity> sortList = new ArrayList<>();
        for (String item : date) {
            String letter;
            //汉字转换成拼音
            List<String> pinyinList = PinYinUtils.getPinYinList(item);
            if (pinyinList != null && !pinyinList.isEmpty()) {
                // A-Z导航
                String letters = pinyinList.get(0).substring(0, 1).toUpperCase();
                // 正则表达式，判断首字母是否是英文字母
                if (letters.matches("[A-Z]")) {
                    letter = letters.toUpperCase();
                } else {
                    letter = "#";
                }
            } else {
                letter = "#";
            }
            sortList.add(new ItemEntity(item, letter, pinyinList));
        }
        return sortList;
    }

    class SearchAdapter extends BaseAdapter{
        List<ItemEntity> valueList = new ArrayList<>();
        Context context;
        SearchAdapter( List<ItemEntity> valueList, Context context){
            this.valueList = valueList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return valueList.size();
        }

        @Override
        public Object getItem(int position) {
            return valueList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HolderView holderView;
            if (convertView == null) {
                holderView = new HolderView();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_view, null);
                convertView.setTag(holderView);
            } else {
                holderView = (HolderView) convertView.getTag();
            }
            holderView.mytest = convertView.findViewById(R.id.item_txt);
            ItemEntity data = valueList.get(position);
            if (data != null) {
                holderView.mytest.setText(data.getValue());
            }
            return convertView;
        }

        class HolderView{
            TextView mytest;
        }
    }
}
