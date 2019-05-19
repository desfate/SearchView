package com.defate.searchview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

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
    List<ItemEntity> dateList = new ArrayList<>();

    List<ItemEntity> valueList = new ArrayList<>();

    DefaultFuzzySearchRule mIFuzzySearchRule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edit_view = findViewById(R.id.edit_view);
        dateList = fillData(getResources().getStringArray(R.array.region));
        mIFuzzySearchRule = new DefaultFuzzySearchRule();


        RxTextView.textChanges(edit_view)
                .debounce (1, TimeUnit.SECONDS).skip(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CharSequence>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CharSequence charSequence) {
                        if (TextUtils.isEmpty(charSequence)) {

                        } else {
                            for (ItemEntity item : dateList) {
                                if (mIFuzzySearchRule.accept(charSequence, item.getSourceKey(), item.getFuzzyKey())) {
                                    valueList.add(item);
                                }
                            }
                        }
                        System.out.println(valueList);
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
}
